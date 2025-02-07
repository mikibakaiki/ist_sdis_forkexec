# Points Web Service

## Authors

Group T26

### Lead developer 

Nome: Alexandre Dinis Pedro Seixas Caldeira, Numero: 85254, GitHubID: [@AlexCaldeira](https://github.com/AlexCaldeira)  

### Contributors

Nome: Alexandre Dinis Pedro Seixas Caldeira, Numero: 85254, GitHubID: [@AlexCaldeira](https://github.com/AlexCaldeira)  

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

[http://localhost:8091/pts-ws/endpoint?WSDL](http://localhost:8091/pts-ws/endpoint?WSDL)

To call the service you will need a web service client,
including code generated from the WSDL.


## To configure the Maven project in Eclipse

'File', 'Import...', 'Maven'-'Existing Maven Projects'

'Select root directory' and 'Browse' to the project base folder.

Check that the desired POM is selected and 'Finish'.


----
