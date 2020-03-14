package com.forkexec.pts.ws;

import javax.jws.WebService;

import com.forkexec.pts.domain.Points;
import com.forkexec.pts.domain.TagDomain;
import com.forkexec.pts.domain.User;
import com.forkexec.pts.domain.exception.*;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.pts.ws.PointsPortType", 
			wsdlLocation = "PointsService.wsdl", 
			name = "PointsWebService", 
			portName = "PointsPort", 
			targetNamespace = "http://ws.pts.forkexec.com/", 
			serviceName = "PointsService")
public class PointsPortImpl implements PointsPortType {

    /**
     * The Endpoint manager controls the Web Service instance during its whole
     * lifecycle.
     */
    private final PointsEndpointManager endpointManager;

    /** Constructor receives a reference to the endpoint manager. */
    public PointsPortImpl(final PointsEndpointManager endpointManager) {
	this.endpointManager = endpointManager;
    }

    // Main operations -------------------------------------------------------

    @Override
	public void activateUser(final String userEmail) throws EmailAlreadyExistsFault_Exception {
		try {
			Points.getInstance().activateUser(userEmail);
		} catch (UserAlreadyExistsException e) {
			throwEmailAlreadyExistsFaultException("Email already exists: " + userEmail);
		}		
    }

    /*@Override
    public int pointsBalance(final String userEmail) throws InvalidEmailFault_Exception {
		try {
			return Points.getInstance().getUser(userEmail).pointsBalance();
		} catch (InvalidEmailException | UserNotFoundException e) {
			throwInvalidEmailFaultException("Invalid email: " + userEmail);			
		}
      return -1;
    }

    @Override
    public int addPoints(final String userEmail, final int pointsToAdd)
	    throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
        
    	try {
			
			return Points.getInstance().getUser(userEmail).addPoints(pointsToAdd);
			
		} catch (InvalidPointsException e) {
			throwInvalidPointsFaultException("Invalid Points: " + pointsToAdd);
		} catch (InvalidEmailException | UserNotFoundException e) {
			throwInvalidEmailFaultException("Invalid Email: " + pointsToAdd);
		}
    	return -1;
    }

    @Override
    public int spendPoints(final String userEmail, final int pointsToSpend)
	    throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        
    	try {
			return Points.getInstance().getUser(userEmail).spendPoints(pointsToSpend);
		} catch (InvalidPointsException e) {
			throwInvalidPointsFaultException("Invalid Points: " + pointsToSpend);
		} catch (InvalidEmailException e) {
			throwInvalidEmailFaultException("Invalid Email: " + userEmail);
		} catch (UserNotFoundException e) {
			System.err.println("User " + userEmail +" Not Found");
		} catch (InsufficientPointsException e) {
			throwNotEnoughBalanceFaultException("Not Enough Balance: " + userEmail);
		}
        return -1;
    }*/
    
    @Override
    public PointsTag read(final String userEmail) {
    	PointsTag res = new PointsTag();
    	
		User user = Points.getInstance().getUser(userEmail);
		res.setPoints(user.pointsBalance());
		res.setTag(newTag(user.getTag()));
		
    	return res;
    }
    
    @Override
    public int write(final String userEmail, int points, Tag newTag) {
    	
		User user = Points.getInstance().getUser(userEmail);
		Tag tag = newTag(user.getTag());
		
		if((newTag.getSeq() > tag.getSeq())){
	    	user.setBalance(points);
	    	user.setTag(newTagDomain(newTag));
	    	return 1;
	    }
    	return -1;
    }

    // Control operations ----------------------------------------------------
    // TODO
    /** Diagnostic operation to check if service is running. */
    @Override
    public String ctrlPing(String inputMessage) {
	// If no input is received, return a default name.
	if (inputMessage == null || inputMessage.trim().length() == 0)
	    inputMessage = "friend";

	// If the park does not have a name, return a default.
	String wsName = endpointManager.getWsName();
	if (wsName == null || wsName.trim().length() == 0)
	    wsName = "Park";

	// Build a string with a message to return.
	final StringBuilder builder = new StringBuilder();
	builder.append("Hello ").append(inputMessage);
	builder.append(" from ").append(wsName);
	return builder.toString();
    }

    
    
    /** Return all variables to default values. */
    @Override
    public void ctrlClear() {
        Points.getInstance().reset();
    }

    /** Set variables with specific values. */
    @Override
    public void ctrlInit(final int startPoints) throws BadInitFault_Exception {
		try {
			Points.getInstance().init(startPoints);
		} catch (BadInitException e) {
			throwBadInit("Bad init values: " + startPoints);
		}
    }

    // View helpers ----------------------------------------------------------
    
    private Tag newTag(TagDomain tag) {
    	Tag view = new Tag();
    	view.setSeq(tag.getSeq());
    	return view;
    }
    
    private TagDomain newTagDomain(Tag tag) {
    	TagDomain view = new TagDomain();
    	view.setSeq(tag.getSeq());
    	return view;
    }
    
    // Exception helpers -----------------------------------------------------

    /** Helper to throw a new BadInit exception. */
    private void throwBadInit(final String message) throws BadInitFault_Exception {
        final BadInitFault faultInfo = new BadInitFault();
        faultInfo.message = message;
        throw new BadInitFault_Exception(message, faultInfo);
    }
    
    /** Helper to throw a new EmailAlreadyExists exception. */
    private void throwEmailAlreadyExistsFaultException(final String message) throws EmailAlreadyExistsFault_Exception {
        final EmailAlreadyExistsFault faultInfo = new EmailAlreadyExistsFault();
        faultInfo.message = message;
        throw new EmailAlreadyExistsFault_Exception(message, faultInfo);
    }
    
}
