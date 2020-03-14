# CC Client

## Authors

Group T26

João Miguel P. Campos, 75785, [@mikibakaiki](https://github.com/mikibakaiki)


### Contributors


---
## About

This is a Java Web Service defined by the WSDL file that generates Java code
(contract-first approach, also called top-down approach).

The service runs in a stand-alone HTTP server.


## Instructions for using Maven

To compile:

```
mvn compile
```

To run using exec plugin:

```
mvn exec:java
```

When running, the web service awaits connections from clients.
You can check if the service is running using your web browser 
to see the generated WSDL file:

[http://ws.sd.rnl.tecnico.ulisboa.pt:8080/cc?WSDL](http://ws.sd.rnl.tecnico.ulisboa.pt:8080/cc?WSDL)

----
