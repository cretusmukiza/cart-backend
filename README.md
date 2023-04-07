# Retail Backend Spring Boot Application

The app  is the multi module spring boot application based on gRPC.
The app has two modules namely:
- Server module contains all the service implementation.
- Proto module contains the proto definition files

## Requirements

For building and running the application you need:

- [Java 11](https://www.oracle.com/java/technologies/javase/11all-relnotes.html)
- [Maven 3 or later](https://maven.apache.org)
- [protoc](http://google.github.io/proto-lens/installing-protoc.html)
- [Docker](https://www.docker.com/)
- [Docker compose](https://docs.docker.com/compose/)

## API Documentation(Postman Collection)
To view the API documentation add the API definition in your postman
- https://www.postman.com/blue-space-373658/workspace/shopping-api/collection/642f9d520b475125d27df09d

## Running the application locally
There are several ways to run this application locally you need
- Maven
- Docker
- Java
- Protoc
- Postman

The steps for running the app are:
- Clone
- Install the app dependencies using maven
```shell
mvn install
```
- Package the app using maven
```shell
mvn package
```
- You can also use build  image for this project ``troy12/app-retail-backend``
- The project has docker-compose setup with the application and MYSQL database.
  To start the app on the local environment run the command
```shell
docker-compose up --build
```
- The app will start on port 9090 add the postman collection, import the api definition and
test the app on localhost:9090.
- Change the ``SERVER_URL`` to reflect the your set port and host
- Use the use example message button to generate sample message and replace the generate values
 with relevant values for given fields.
- All the methods in the CartService are limited to authorized users.
- To use them first register the user using the AuthService ``createUser`` method.
- Use the register credential to get the token using AuthService ``authorizeUser`` method.
- Set the editor environment to ``Test Environment``
- Edit the ``TOKEN`` set it to the value returned by ``authorizeUser`` method
- The rest of the methods are not authorized  were meant for admin are meant to be deployed as internal APIs.

## Running the test
To run the test run the command
```shell
mvn test
```


## Deploying the application to kubernetes

The projects includes the kubernetes manifests in the k8s folder.
It contains:
- The MYSQL setup service, deployment, persistent volume and persistent volume claim manifests
- The Application deployment and services manifests
- The secrets manifest for database password and jwt secrets.

To deploy the app on the kubernetes cluster run the command:
```shell
kubectl apply -f k8s
```




