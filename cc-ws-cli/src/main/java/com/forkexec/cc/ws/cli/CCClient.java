package com.forkexec.cc.ws.cli;

import pt.ulisboa.tecnico.sdis.ws.CreditCard;
import pt.ulisboa.tecnico.sdis.ws.CreditCardImplService;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import javax.xml.ws.BindingProvider;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class CCClient implements CreditCard {

    CreditCardImplService service = null;

    CreditCard port = null;

    /** UDDI server URL */
    private String uddiURL = null;

    /** WS name */
    private String wsName = null;

    /** WS end point address */
    private String wsURL = null; // default value is defined inside WSDL

    public String getWsURL() {
        return wsURL;
    }

    /** output option **/
    private boolean verbose = false;

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /** constructor with provided web service URL */
    public CCClient(String wsURL) throws CCClientException {
        this.wsURL = wsURL;
        createStub();
    }

    /** constructor with provided UDDI location and name */
    public CCClient(String uddiURL, String wsName) throws CCClientException {
        this.uddiURL = uddiURL;
        this.wsName = wsName;
        uddiLookup();
        createStub();
    }


    /** UDDI lookup */
    private void uddiLookup() throws CCClientException {
        try {
            if (verbose)
                System.out.printf("Contacting UDDI at %s%n", uddiURL);
            UDDINaming uddiNaming = new UDDINaming(uddiURL);

            if (verbose)
                System.out.printf("Looking for '%s'%n", wsName);
            wsURL = uddiNaming.lookup(wsName);

        } catch (Exception e) {
            String msg = String.format("Client failed lookup on UDDI at %s!", uddiURL);
            throw new CCClientException(msg, e);
        }

        if (wsURL == null) {
            String msg = String.format("Service with name %s not found on UDDI at %s", wsName, uddiURL);
            throw new CCClientException(msg);
        }
    }

    /** Stub creation and configuration */
    private void createStub() {
        if (verbose)
            System.out.println("Creating stub ...");
        service = new CreditCardImplService();
        port = service.getCreditCardImplPort();

        if (wsURL != null) {
            if (verbose)
                System.out.println("Setting endpoint address ...");
            BindingProvider bindingProvider = (BindingProvider) port;
            Map<String, Object> requestContext = bindingProvider.getRequestContext();
            requestContext.put(ENDPOINT_ADDRESS_PROPERTY, wsURL);
        }
    }

    // remote invocation methods ----------------------------------------------
    @Override
    public String ping(String name) {
        return port.ping(name);
    }

    @Override
    public boolean validateNumber(String numberAsString) {
        return port.validateNumber(numberAsString);
    }
}
