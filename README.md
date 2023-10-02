# flink-apps

## Pre-requisites
### Set up Kafka and Flink
Install Java 11 and adjust your PATH accordingly  
Install Maven and adjust your PATH accordingly  
Install docker and adjust your PATH accordingly  
Clone the git repository https://github.com/dinbab1984/docker-compose  
Start Kafka and  Flink as described in https://github.com/dinbab1984/docker-compose#readme\  
Clone this git repository


## Flink-App : Kafka2KafkaApp one-to-one mapping without data enrichment
### Setup for kafka topics
#### Create Input Topic:
````docker exec kafka kafka-topics --create --topic input-topic --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092````
#### Create Output Topic:
````docker exec kafka kafka-topics --create --topic output-topic --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092````

### Compile and Build package using mvn
hint: current folder should be : kafka2kafka
````mvn clean compile assembly:single````

### Submit the jar in target folder via Flink UI
http://localhost:8081/#/submit (assign argument : kafka:9092)

### Test with sample messages
publish messages to input topic : ````docker exec -it kafka kafka-console-producer --topic input-topic --broker-list kafka:9092````

consume messages from output topic : ````docker exec -it kafka kafka-console-consumer --topic output-topic --from-beginning --bootstrap-server kafka:9092````

## Flink-App : APIDataEnrichmentApp with data enrichment via API
### Setup API
Follow the steps as described in https://github.com/dinbab1984/Python-API/blob/main/README.md  
we use the following api : Get User : http:localhost:8000/users/{id}  
### Create docker test-network and connect both flink and python-api to it
````docker network create test-network````  
````docker network connect test-network python_api````  
````docker network connect test-network flink_taskmanager````
### Setup for kafka topics
#### Create Input Topic:
````docker exec kafka kafka-topics --create --topic input-topic-api --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092````
#### Create Output Topic:
````docker exec kafka kafka-topics --create --topic output-topic-api --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092````

### Compile and Build package using mvn
hint: current folder should be : api_data_enrichment
````mvn clean compile assembly:single```` 
### Submit the jar in target folder via Flink UI
http://localhost:8081/#/submit (assign arguments as follows: kafka:9092 python_api:8000) 

### Test with sample messages
publish messages to input topic : ````docker exec -it kafka kafka-console-producer --topic input-topic-api --broker-list kafka:9092````

consume messages from output topic : ````docker exec -it kafka kafka-console-consumer --topic output-topic-api --from-beginning --bootstrap-server kafka:9092````