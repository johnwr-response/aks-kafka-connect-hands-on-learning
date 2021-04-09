# Log

### Section 1: Course Introduction
  - Important Pre-Requisites
  - Course Objectives
  - Course Structure
  - About Your Instructor

### Section 2: Code Download
  - [Course code](https://courses.datacumulus.com/downloads/kafka-connect-09a "Kafka Connect Code")

### Section 3: Kafka Connect Concepts
  - What is Kafka Connect?
  - Kafka Connect Architecture Design
  - Connectors, Configuration, Tasks, Workers
    - Kafka Connect Cluster has multiple loaded connectors
      - Each connector is a reusable piece of code (java jars)
      - `Connector` + `User configuration` is a `Task`
      - Tasks are executed by `Kafka Connect Workers`
  - Standalone vs Distributed Mode
    - `Standalone` A single process runs connectors and tasks
      - `User configuration` is bundled with the process
      - Useful for development and testing
    - `Distributed` Multiple workers runs connectors and tasks
      - ` User configuration` is submitted using REST API
      - Easy to scale, just add workers
      - Useful for production deployment
  - Distributed Architecture in Details

### Section 4: Setup and Launch Kafka Connect Cluster
- Important information about installation
- Docker on Mac (recent versions)
- Docker Toolbox on Mac (older versions)
- Docker on Linux (Ubuntu as an example)
- Docker on Windows 10 64bit
  - Install Docker Desktop
- Docker Toolbox on Windows (older versions)
- Important note for Docker Toolbox users
- Starting Kafka Connect Cluster using Docker Compose
```
cd code
docker-compose up -d kafka-cluster
```

### Section 5: Troubleshooting Kafka Connect
- It's not working! What to do?
- Where to view logs?
  - In standAlone mode they will be directly in terminal
  - In distributed mode they will be at http://localhost:3030/logs (connect-distributed.log) 
```
docker pull landoop/fast-data-dev
```

### Section 6: Kafka Connect Source - Hands On
- Kafka Connect Source Architecture Design
- FileStream Source Connector - Standalone Mode - Part 1
  - Properties for Infrastructure
    - `bootstrap.servers` points to Kafka Broker (localhost:9092)
    - These are used to set converter classes. Very often these are set to `org.apache.kafka.connect.json.JsonConverter`
    - `key.converter`
    - `value.converter`
    - `internal.key.converter` NB! Not needed after v2.0
    - `internal.value.converter` NB! Not needed after v2.0
    - These are used to enable/disable the above converters. (`boolean` value)
    - `key.converter.schemas.enable`
    - `value.converter.schemas.enable`
    - `internal.key.converter.schemas.enable` NB! Not needed after v2.0
    - `internal.value.converter.schemas.enable` NB! Not needed after v2.0
    - Rest API
    - `rest.port` (sample value: `8086`)
    - `rest.host.name` (sample value: `127.0.0.1`)
    - `these configs are only for standalone workers
    - `offset.storage.file.filename` (sample value for standalone workers: `standalone.offsets`)
    - `offset.flush.interval.ms` (sample value `10000`)
  - Properties for actual connector
    - These are the standard kafka connect parameters, need for ALL connectors
      - `name` A unique name for each connector
      - `connector.class` Class for Kafka to run the connector
      - `tasks.max` Number of tasks to run in parallel
    - Then parameters for the connectors themself ([List of parameters for FileStreamSourceConnector](https://github.com/apache/kafka/blob/trunk/connect/file/src/main/java/org/apache/kafka/connect/file/FileStreamSourceConnector.java "List of parameters for FileStreamSourceConnector"))
      - `file` (Sample for this demo = `demo-file.txt`)
      - `topic`(Sample for this demo = `demo-1-standalone`)
- FileStream Source Connector - Standalone Mode - Part 2
  - Run demo 1
```
docker run --rm -it -v ${PWD}:/tutorial --net=host landoop/fast-data-dev:latest bash
kafka-topics --create --topic demo-1-standalone --partitions 3 --replication-factor 1 --zookeeper 127.0.0.1:2181
cd /tutorial/source/demo-1
connect-standalone worker.properties file-stream-demo-standalone.properties
```
- FileStream Source Connector - Distributed Mode
  - Run demo 2
```
docker run --rm -it -v ${PWD}:/tutorial --net=host landoop/fast-data-dev:latest bash
kafka-topics --create --topic demo-2-distributed --partitions 3 --replication-factor 1 --zookeeper 127.0.0.1:2181
```
  - Create the connector in GUI with the content from the property file of demo-2 
  - Log in to the container, create the configured input text file and add some text to it
```
docker ps # to get container id
docker exec -it <container_id> bash # to log in to it
touch demo-file.txt
echo "hi" >> demo-file.txt
echo "hello" >> demo-file.txt
echo "from the other side" >> demo-file.txt
```
  - reconnect with kafka tools and check with a consumer
```
docker run --rm -it -v ${PWD}:/tutorial --net=host landoop/fast-data-dev:latest bash
kafka-console-consumer --topic demo-2-distributed --from-beginning --bootstrap-server 127.0.0.1:9092
```
- List of Available Connectors
  - [Confluent Hub](https://www.confluent.io/hub/ "Confluent Hub")
  - Not every connector will be listed there, so also search Google (and github)...
  - Using the Docker image from Lenses.io there will be some connectors already bundled to be used right away
  - To add your own, you need to map your volumes to the Docker Container at startup
    - [More information](https://github.com/lensesio/fast-data-dev#enable-additional-connectors "How to add additional connectors to Docker Image from Lenses.io")


# Links
- [Lenses IO Github](https://github.com/lensesio/fast-data-dev "Lenses IO Github")
- [Confluent Hub](https://www.confluent.io/hub/ "Confluent Hub for Kafka® connectors and more")


# Github setup
```
git remote add origin https://github.com/johnwr-response/aks-kafka-connect-hands-on-learning.git
```
