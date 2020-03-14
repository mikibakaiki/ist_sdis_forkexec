package com.forkexec.cc.ws.cli;

@SuppressWarnings("Duplicates")
public class CCClientApp {

   public static void main(String[] args) throws Exception {
        // Check arguments.
        if (args.length == 0) {
            System.err.println("Argument(s) missing!");
            System.err.println("Usage: java " + CCClientApp.class.getName() + " wsURL OR uddiURL wsName");
            return;
        }
        String uddiURL = null;
        String wsName = null;
        String wsURL = null;
        if (args.length == 1) {
            wsURL = args[0];
        } else if (args.length >= 2) {
            uddiURL = args[0];
            wsName = args[1];
        }

        // Create client.
        CCClient client = null;

        if (wsURL != null) {
            System.out.printf("Creating client for server at %s%n", wsURL);
            client = new CCClient(wsURL);
        } else if (uddiURL != null) {
            System.out.printf("Creating client using UDDI at %s for server with name %s%n", uddiURL, wsName);
            client = new CCClient(uddiURL, wsName);
        }

        // The following remote invocation is just a basic example.
        // The actual tests are made using JUnit.

        System.out.println("Invoke ping()...");
        String result = client.ping("client T26");
        System.out.println("Result: ");
        System.out.println(result);
    }
}
