# flink-apps

## Pre-requisites
### Set up Kafka, Flink and Postgres
#### Clone the git repository https://github.com/dinbab1984/docker-compose
#### Start Kafka, Flink and Postgres as described in https://github.com/dinbab1984/docker-compose#readme
#### Clone this git repository

## Setup for kafka topics
### Create Topic for kafkaSourceConnect
````docker exec kafka kafka-topics --create --topic kafkaSourceConnect --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092````
### Create Topic for kafkaSinkonnect
````docker exec kafka kafka-topics --create --topic kafkaSinkConnect --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092````

