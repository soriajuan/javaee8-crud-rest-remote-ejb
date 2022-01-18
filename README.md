# Person

REST API for Person (Java EE 8)

### Requirements
- Java SE 8
- Maven 3.8.4
- Application Server supporting Java EE 8 or higher (development on Wildfly 26 standalone configuration)

### application structure
- **person-common**  
Jar archive with classes used by person-ejb and person-web

- **person-ejb**  
Ejb-jar archive with the remote ejb bean and database configuration/service.

- **person-ear**  
Enterprise archive with person-ejb and person-common packages

- **person-web**  
War archive with the rest api

- **person-it**  
Integration tests with cargo plugin and wildfly 26

### Unit Testing
mvn clean test

### Integration Testing
mvn clean verify -P wildfly26-integration-testing

### Build
mvn clean package

### Deploy/Undeploy to Wildfly on localhost:8080
- mvn clean package wildfly:deploy
- mvn clean package wildfly:undeploy
