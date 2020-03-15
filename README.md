# ForkExec

**Fork**: delicious menus for **Exec**utives

Distributed Systems 2018-2019, 2nd semester project

## Authors

#### Group T26

| Name | IST Number | Github Username |
| :---:         |     :---:      |          :---: |
| João Miguel P. Campos   | 75785     | [@mikibakaiki](https://github.com/mikibakaiki)    |
| Gonçalo Santos     | 77915      | [@71514311215](https://github.com/71514311215)      |
| Alexandre Caldeira     | 85254      | [@AlexCaldeira](https://github.com/AlexCaldeira)      |




For each module, the README file must identify the lead developer and the contributors.
The leads should be evenly divided among the group members.

### TO USE JACOCO
run `mvn clean install -DskipTests` from `root directory`
then, on a client of your choosing, run `mvn verify`

To see results, go to `<client-choosen>/target/site/jacoco/index.html`

### To Run

#### 1. Setup:

* Install all dependencies with the command `mvn clean install -DskipTests`

* Then, we need to launch the servers, each one on a different terminal
   * `cd hub-ws; mvn compile exec:java`
   * `cd pts-ws; mvn compile exec:java`
   * `cd rst-ws; mvn compile exec:java`
   * `cd-rst-ws; mvn compile exec:java -Dws.i=2`
   

#### 1. Run Individually:

* Go to each /xxx-ws-cli and execute `mvn verify`

#### 2. All modules at the same time

* Go to the source folder of the project and run `mvn verify`



### Code identification

In all the source files (including POMs), please replace __CXX__ with your Campus: A (Alameda) or T (Tagus); and your group number with two digits.

This is important for code dependency management 
i.e. making sure that your code runs using the correct components and not someone else's.


## Getting Started

The overall system is composed of multiple services and clients.
The main service is the _hub_ service that is aided by the _pts_ service. 
There are also multiple _rst_ services, one for each participating restaurant.

See the project statement for a full description of the domain and the system.



### Prerequisites

Java Developer Kit 8 is required running on Linux, Windows or Mac.
Maven 3 is also required.

To confirm that you have them installed, open a terminal and type:

```
javac -version

mvn -version
```


### Installing

To compile and install all modules:

```
mvn clean install -DskipTests
```

The tests are skipped because they require each server to be running.


## Built With

* [Maven](https://maven.apache.org/) - Build Tool and Dependency Management
* [JAX-WS](https://javaee.github.io/metro-jax-ws/) - SOAP Web Services implementation for Java



## Versioning

We use [SemVer](http://semver.org/) for versioning. 



