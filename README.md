# flink-apps

## Pre-requisites
### Set up Kafka and Flink
Install Java 1.8 and adjust your PATH accordingly
Install Maven and adjust your PATH accordingly
Install docker and adjust your PATH accordingly
Clone the git repository https://github.com/dinbab1984/docker-compose  
Start Kafka and  Flink as described in https://github.com/dinbab1984/docker-compose#readme\  
Clone this git repository


## Flink-App : Kafka2Kafka one-to-one mapping without data enrichment
### Setup for kafka topics
#### Create Input Topic:
````docker exec kafka kafka-topics --create --topic input-topic --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092````
#### Create Output Topic:
````docker exec kafka kafka-topics --create --topic output-topic --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092````

#### Compile and Build package using mvn
````mvn clean compile assembly:single````

#### Submit the jar in target folder via Flink UI
http://localhost:8081/#/submit

### Test with sample messages
publish messages to input topic : ````docker exec -it kafka kafka-console-producer --topic input-topic --broker-list kafka:9092````

consume messages from test topic : `````docker exec -it kafka kafka-console-consumer --topic output-topic --from-beginning --bootstrap-server kafka:9092````