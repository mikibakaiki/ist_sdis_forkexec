package com.forkexec.pts.ws.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.ws.Response;

import com.forkexec.pts.ws.*;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;


/**
 * Client port wrapper.
 *
 * Adds easier end point address configuration to the Port generated by
 * wsimport.
 */

public class PointsFrontEnd {

	/** WS service */
	PointsService service = null;

	/** WS port (port type is the interface, port is the implementation) */
	PointsPortType port = null;

	private UDDINaming binder;

	private int numReplicas;
	private int minReadReplicas;
	private int minWriteReplicas;

	private List<String> listUrlReplicas;

	/** constructor with provided uddi URL */
	public PointsFrontEnd(String uddiURL)  {
		try {
			binder = new UDDINaming(uddiURL);
		} catch (UDDINamingException e) {
			System.err.println("Couldn't find the UDDI @ " + uddiURL);
		}
		this.numReplicas = getNumReplicas();
		this.listUrlReplicas = getUrlReplicas();
		this.minReadReplicas = getMinReadReplicas();
		this.minWriteReplicas = getMinWriteReplicas();
	}


	// remote invocation methods ----------------------------------------------
	
	public void activateUser(String userEmail) throws Throwable {
		checkEmailFormat(userEmail);
		List<Response<ActivateUserResponse>> listResp = new CopyOnWriteArrayList<Response<ActivateUserResponse>>();
		int numRespostas = 0;
		
		synchronized (this) {
			
			//manda pedidos assincronos para cada replica
			for(String ptsServerUrl : listUrlReplicas) {
				try {
					PointsClient ptsClt = new PointsClient(ptsServerUrl);
					Response<ActivateUserResponse> resp = ptsClt.activateUserAsync(userEmail);
					listResp.add(resp);
					//ptsClt.activateUser(userEmail);
				} catch (PointsClientException e) {
					System.err.println("Error creating PTS client.");
				}
			}
			
			//obtem respostas duma maioria das replicas
			while(numRespostas < ((numReplicas/2)+1)) {
				for(Response<ActivateUserResponse> resp : listResp) {
					if(resp.isDone()) {
						numRespostas++;
						try {
							resp.get();
						} catch (InterruptedException e) {
							System.err.println("Caught InterruptedException");
							System.err.println("Cause: " + e.getCause());
						} catch (ExecutionException e) {						
							System.err.println("Caught ExecutionException");
							System.err.println("Cause: " + e.getCause());
							if(e.getCause() instanceof EmailAlreadyExistsFault_Exception)
								throwEmailAlreadyExistsFaultException(e.getCause().getMessage());
						}
						listResp.remove(resp);
						if(numRespostas >= ((numReplicas/2)+1)) {
							break;
						}
					}
				}
			}
		}
	}
	
	public int pointsBalance(String userEmail) throws InvalidEmailFault_Exception {
		checkEmailFormat(userEmail);
		System.out.println("POINTSBALANCE ESTE E O EMAIL: "+userEmail);
		System.err.println("POINTSBALANCE ESTE E O EMAIL: "+userEmail);
		PointsTag pt;
		
		synchronized (this) {
			pt = getMaxPointsTag(userEmail);

			//WRITEBACK PHASE
			setPoints(userEmail, pt.getPoints(), pt.getTag());
		}
		
		return pt.getPoints();
	}
	
	public int addPoints(String userEmail, int pointsToAdd)
			throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
		checkPoints(pointsToAdd);
		checkEmailFormat(userEmail);
		PointsTag pt;
		
		//obter max tag
		synchronized (this) {
			pt = getMaxPointsTag(userEmail);
		
			pt.getTag().setSeq(pt.getTag().getSeq() + 1);
			setPoints(userEmail, pt.getPoints()+pointsToAdd, pt.getTag());
		}
		return pt.getPoints() + pointsToAdd;
	}

	public int spendPoints(String userEmail, int pointsToSpend)
			throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		checkPoints(pointsToSpend);
		checkEmailFormat(userEmail);
		PointsTag pt;
		
		synchronized (this) {
			//obter max tag
			pt = getMaxPointsTag(userEmail);

			if(pt.getPoints() < pointsToSpend) {
				throwNotEnoughBalanceFaultException("User " + userEmail + " doesn't have enough points.");
			}
		
			pt.getTag().setSeq(pt.getTag().getSeq() + 1);
			setPoints(userEmail, pt.getPoints()-pointsToSpend, pt.getTag());
		}
		
		return pt.getPoints() - pointsToSpend;
	}
	
	// control operations -----------------------------------------------------

	public String ctrlPing(String inputMessage) {
		return port.ctrlPing(inputMessage);
	}

	public void ctrlClear() {
		List<Response<CtrlClearResponse>> listResp = new CopyOnWriteArrayList<Response<CtrlClearResponse>>();
		int numRespostas = 0;
		//manda pedidos assincronos para cada replica
		for(String ptsServerUrl : listUrlReplicas) {
			try {
				PointsClient ptsClt = new PointsClient(ptsServerUrl);
				Response<CtrlClearResponse> resp = ptsClt.ctrlClearAsync();
				listResp.add(resp);
			} catch (PointsClientException e) {
				System.err.println("Error creating PTS client.");
			}
		}
		
		//obtem respostas duma maioria das replicas
		while(numRespostas < ((numReplicas/2)+1)) {
			for(Response<CtrlClearResponse> resp : listResp) {
				if(resp.isDone()) {
					numRespostas++;
					try {
						resp.get();
					} catch (InterruptedException e) {
						System.err.println("Caught InterruptedException");
						System.err.println("Cause: " + e.getCause());
					} catch (ExecutionException e) {						
						System.err.println("Caught ExecutionException");
						System.err.println("One server is down, but i can still run :)");					
					}
					listResp.remove(resp);
					if(numRespostas >= ((numReplicas/2)+1)) {
						break;
					}
				}
			}
		}		
		
	}

	public void ctrlInit(int startPoints) throws BadInitFault_Exception {
		List<Response<CtrlInitResponse>> listResp = new CopyOnWriteArrayList<Response<CtrlInitResponse>>();
		int numRespostas = 0;
		
		//manda pedidos assincronos para cada replica
		for(String ptsServerUrl : listUrlReplicas) {
			try {
				PointsClient ptsClt = new PointsClient(ptsServerUrl);
				Response<CtrlInitResponse> resp = ptsClt.ctrlInitAsync(startPoints);
				listResp.add(resp);
			} catch (PointsClientException e) {
				System.err.println("Error creating PTS client.");
			}
		}
		
		//obtem respostas duma maioria das replicas
		while(numRespostas < ((numReplicas/2)+1)) {
			for(Response<CtrlInitResponse> resp : listResp) {
				if(resp.isDone()) {
					numRespostas++;
					try {
						resp.get();
					} catch (InterruptedException e) {
						System.err.println("Caught InterruptedException");
						System.err.println("Cause: " + e.getCause());
					} catch (ExecutionException e) {						
						System.err.println("Caught ExecutionException");
						System.err.println("One server is down, but i can still run :)");
					}
					listResp.remove(resp);
					if(numRespostas >= ((numReplicas/2)+1)) {
						break;
					}
				}
			}
		}		
		port.ctrlInit(startPoints);
	}
	
	// auxiliary operations -----------------------------------------------------
	
	private void checkPoints(int points) throws InvalidPointsFault_Exception {
		if(points < 0) {
			throwInvalidPointsFaultException("Invalid points: " + points);
		}
	}
	
	private void checkEmailFormat(String userEmail) throws InvalidEmailFault_Exception {
		if(userEmail == null || userEmail.trim().length() == 0 || !userEmail.matches("\\w+(\\.?\\w)*@\\w+(\\.?\\w)*")) {
			throwInvalidEmailFaultException("Invalid email: " + userEmail);
		}
	}
	
	private int getNumReplicas() {
		Properties p = new Properties();
		try {
			p.load(PointsClient.class.getClassLoader().getResourceAsStream("project.properties"));			
			int n = Integer.parseInt(p.getProperty("numReplicas"));
			return n;
			
		} catch (IOException e) {
			
			System.err.println("ERROR ON THE URL");
		}
		return 0;
	}
	
	private int getMinReadReplicas() {
		if(numReplicas < 1) {
			return 0;
		}
		else if(numReplicas == 1 || numReplicas == 2) {
			return 1;
		}
		else if(numReplicas < 6) {
			return 2;
		}
		else if(numReplicas < 11) {
			return 3;
		}
		else {
			return 4;
		}
	}
	
	private int getMinWriteReplicas() {
		if(numReplicas < 1) {
			return 0;
		}
		else if(numReplicas == 1) {
			return 1;
		}
		else if(numReplicas == 2 || numReplicas == 3) {
			return 2;
		}
		else if(numReplicas == 4) {
			return 3;
		}
		else if(numReplicas < 7) {
			return 4;
		}
		else if(numReplicas < 11) {
			return numReplicas - 2;
		}
		else {
			return numReplicas - 3;
		}
	}
	
	private List<String> getUrlReplicas() {
		List<String> list = new ArrayList<String>();
		for(int i=1; i <= numReplicas ; i++) {
			String serverName = "T26_Points"+i;
			try {
				if(i==9) {
					continue;
				}
				String ptsServerUrl = binder.lookup(serverName);
				list.add(ptsServerUrl);
			} catch (UDDINamingException e1) {
				System.err.println("Error retrieving the url for the UDDI server.");
			}
		}
		return list;
	}
	
	private synchronized PointsTag getMaxPointsTag(String userEmail) throws InvalidEmailFault_Exception, NullPointerException {
		List<PointsTag> res = new ArrayList<PointsTag>();
		List<Response<ReadResponse>> listResp = new CopyOnWriteArrayList<Response<ReadResponse>>();
		int numRespostas = 0;
		
		//manda pedidos assincronos para cada replica
		for(String ptsServerUrl : listUrlReplicas) {
			try {
				PointsClient ptsClt = new PointsClient(ptsServerUrl);
				Response<ReadResponse> resp = ptsClt.pointsBalanceAsync(userEmail);
				listResp.add(resp);
			} catch (PointsClientException e) {
				System.err.println("Error creating PTS client.");
			}			
		}
		
		//obtem respostas duma maioria das replicas
		while(numRespostas < minReadReplicas) {
			for(Response<ReadResponse> resp : listResp) {
				try {
					if(resp.isDone()) {
						res.add(resp.get().getReturn());
						numRespostas++;
						listResp.remove(resp);
						if(numRespostas >= minReadReplicas) {
							break;
						}
					}
				} catch (InterruptedException e) {
					System.err.println("Caught InterruptedException");
					System.err.println("Cause: " + e.getCause());
				} catch (ExecutionException e) {
					System.err.println("Caught ExecutionException");
					System.err.println("One server is down, but i can still run :)");
				}
			}
		}
		
		//obter a maior tag (e respetivos pontos) entre as respostas obtidas
		PointsTag pt0 = res.get(0);		
		for(int j=1; j < res.size(); j++) {
			PointsTag pt = res.get(j);
			Tag tag = pt.getTag();
			if(tag.getSeq() > pt0.getTag().getSeq()) {
				pt0.getTag().setSeq(tag.getSeq());
				pt0.setPoints(pt.getPoints());
			}
			else if(tag.getSeq() == pt0.getTag().getSeq()) {
				if(pt.getPoints() != pt0.getPoints()) {
					System.err.println("Incoherent result: Seq's are equal ("
					+tag.getSeq()+","+pt0.getTag().getSeq()+") but points are different ("
					+pt.getPoints()+","+pt0.getPoints()+").");
				}
			}
		}
		return pt0;
	}

	private synchronized int setPoints(String userEmail, int pointsToSet, Tag tag) {
		List<Response<WriteResponse>> listResp = new CopyOnWriteArrayList<Response<WriteResponse>>();
		int numRespostas = 0;
		
		//manda pedidos assincronos para cada replica
		for(String ptsServerUrl : listUrlReplicas) {		
			try {
				PointsClient ptsClt = new PointsClient(ptsServerUrl);
				Response<WriteResponse> resp = ptsClt.setPointsAsync(userEmail, pointsToSet, tag);
				listResp.add(resp);
			} catch (PointsClientException e) {
				System.err.println("Error creating PTS client.");
			}
		}
		
		//obtem respostas duma maioria das replicas
		while(numRespostas < minWriteReplicas) {
			for(Response<WriteResponse> resp : listResp) {
				try {
					if(resp.isDone()) {
						//TODO Preciso deste if?
						if(resp.get().getReturn() > -1) {
							listResp.remove(resp);
						}
						numRespostas++;
						if(numRespostas >= minWriteReplicas) {
							break;
						}
					}
				} catch (InterruptedException e) {
					System.err.println("Caught InterruptedException");
					System.err.println("Cause: " + e.getCause());
				} catch (ExecutionException e) {
					System.err.println("Caught ExecutionException");
					System.err.println("One server is down, but i can still run :)");
				}
			}
		}
		return pointsToSet;
	}

    // Exception helpers -----------------------------------------------------
    
    /** Helper to throw a new InvalidPointsFault exception. */
    private void throwInvalidPointsFaultException(final String message) throws InvalidPointsFault_Exception {
        throw new InvalidPointsFault_Exception(message);
    }
    
    /** Helper to throw a new NotEnoughBalanceFault exception. */
    private void throwNotEnoughBalanceFaultException(final String message) throws NotEnoughBalanceFault_Exception {
        throw new NotEnoughBalanceFault_Exception(message);
    }
    
    /** Helper to throw a new InvalidEmailFault exception. */
    private void throwInvalidEmailFaultException(final String message) throws InvalidEmailFault_Exception {
        throw new InvalidEmailFault_Exception(message);
    }
    
    /** Helper to throw a new EmailAlreadyExists exception. */
    private void throwEmailAlreadyExistsFaultException(final String message) throws EmailAlreadyExistsFault_Exception {
        final EmailAlreadyExistsFault faultInfo = new EmailAlreadyExistsFault();
        faultInfo.setMessage(message);
        throw new EmailAlreadyExistsFault_Exception(message, faultInfo);
    }
}
