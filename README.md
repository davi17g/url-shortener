# URL Shortener
## Inroduction:
This is a simple Url Shortener implementation using aerospike database.

## Prerequisite:
- Docker
- Java Runtime Environment
- Java Development Kit
- Maven

## Installation:
In order to run the service follow these steps:
- pull Aerospike Database Image: `docker pull aerospike/aerospike-server`
- run: `docker-compose up -d`
- Build the project using maven: `mvn package`
- Run the service: `java -cp target/url-shortener-1.0-SNAPSHOT.jar com.davi17g.Main`

## Run:
- Request the service as following: `curl --location --request POST 'http://127.0.0.1:1488/davi17g'` 
- You will receive the generated url in the response body.
- Put the generated url in the Browser, make a request, and it will be redirected to a previous url.
