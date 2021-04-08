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
docker-compose up kafka-cluster
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
    - `internal.key.converter`
    - `internal.value.converter`
    - These are used to enable/disable the above converters. (`boolean` value)
    - `key.converter.schemas.enable`
    - `value.converter.schemas.enable`
    - `internal.key.converter.schemas.enable`
    - `internal.value.converter.schemas.enable`
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




# Links
- [Lenses IO Github](https://github.com/lensesio/fast-data-dev "Lenses IO Github")


# Github setup
```
git remote add origin https://github.com/johnwr-response/aks-kafka-connect-hands-on-learning.git
```
