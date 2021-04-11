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
- Twitter Source Connector - Distributed Mode - Part 1
  - This sample will be using the following connector [kafka-connect-twitter](https://github.com/Eneco/kafka-connect-twitter)
  - Keys from a Twitter app is needed. [Create one here](https://developer.twitter.com/)
  - Add these keys to the properties file in demo-3
- Twitter Source Connector - Distributed Mode - Part 2
  - Log in to the Kafka Tools, create the topic and a consumer
```
docker run --rm -it -v ${PWD}:/tutorial --net=host landoop/fast-data-dev:latest bash
kafka-topics --create --topic demo-3-twitter --partitions 3 --replication-factor 1 --zookeeper 127.0.0.1:2181
kafka-console-consumer --topic demo-3-twitter --bootstrap-server 127.0.0.1:9092
```
  - Go to Kafka Connect UI
    - click on new
    - select the twitter source connector
    - paste the content of the properties file created in demo-3
    - if no errors, click create
    - However, there is currently a bug here.
      - the connector also needs the `tweets` property (in addition to the `tweet`)
      - the UI will not enable the create button
      - go to the CURL section
        - copy the content
        - change the server location to `localhost:8083/connectors`
        - paste it to the command line to run it
        - The output should be the resulting json object for the connector
    - Tweets should now start to roll in to your consumer
- Section Summary

### Section 7: Kafka Connect Sink - Hands On
- Kafka Connect Sink Architecture Design
- ElasticSearch Sink Connector - Distributed Mode
```
docker-compose up -d kafka-cluster elasticsearch postgres
```
  - Verify elasticsearch is running by getting `http://localhost:9200/` in postman
  - In the kafka connect UI, apply the configuration from `sink/demo-elastic/sink-elastic-twitter-distributed.properties`
  - Verify in the  [dejavu UI](http://127.0.0.1:9200/_plugin/dejavu "The Missing Web UI for Elasticsearch")
    - Here we can also add queries like the examples in the demo folder
- Kafka Connect REST API
  - First start a session with the kafka tools
```
docker run --rm -it -v ${PWD}:/tutorial --net=host landoop/fast-data-dev:latest bash
```
  - REST API Calls
    - Get Worker Information:
      ```
      curl -s 127.0.0.1:8083/ | jq
      ```
    - List connectors available on a worker:
      ```
      curl -s 127.0.0.1:8083/connector-plugins | jq
      ```
    - Ask about active connectors:
      ```
      curl -s 127.0.0.1:8083/connectors | jq
      ```
    - Get information about a connectors tasks and configs:
      ```
      curl -s 127.0.0.1:8083/connectors/source-twitter-distributed/tasks | jq
      ```
    - Get connector status:
      ```
      curl -s 127.0.0.1:8083/connectors/file-stream-demo-distributed/status | jq
      ```
    - Pause / resume a connector:
      ```
      curl -s -X PUT 127.0.0.1:8083/connectors/file-stream-demo-distributed/pause
      curl -s -X PUT 127.0.0.1:8083/connectors/file-stream-demo-distributed/resume
      ```
    - Delete a connector:
      ```
      curl -s -X DELETE 127.0.0.1:8083/connectors/file-stream-demo-distributed
      ```
    - Create a new connector:
      ```
      curl -s -X POST -H "Content-Type: application/json" --data '{"name": "file-stream-demo-distributed", "config":{"connector.class":"org.apache.kafka.connect.file.FileStreamSourceConnector","key.converter.schemas.enable":"true","file":"demo-file.txt","tasks.max":"1","value.converter.schemas.enable":"true","name":"file-stream-demo-distributed","topic":"demo-2-distributed","value.converter":"org.apache.kafka.connect.json.JsonConverter","key.converter":"org.apache.kafka.connect.json.JsonConverter"}}' http://127.0.0.1:8083/connectors | jq
      ```
    - Update connector configuration:
      ```
      curl -s -X PUT -H "Content-Type: application/json" --data '{"connector.class":"org.apache.kafka.connect.file.FileStreamSourceConnector","key.converter.schemas.enable":"true","file":"demo-file.txt","tasks.max":"2","value.converter.schemas.enable":"true","name":"file-stream-demo-distributed","topic":"demo-2-distributed","value.converter":"org.apache.kafka.connect.json.JsonConverter","key.converter":"org.apache.kafka.connect.json.JsonConverter"}' 127.0.0.1:8083/connectors/file-stream-demo-distributed/config | jq
      ```
    - Get connector configuration:
      ```
      curl -s 127.0.0.1:8083/connectors/file-stream-demo-distributed | jq
      ```
- JDBC Sink Connector - Distributed Mode
  - sink made by `code/sink/demo-postgres/sink-postgres-twitter-distributed.properties`

### Section 8: Writing your own Kafka Connector
- Goal of the section: GitHubSourceConnector
    - Understand how connectors are made using the GitHubSourceConnector as an example
        - Dependencies
        - ConfigDef
        - Connector
        - Schema & Struct
        - Source Partition & Source Offsets
        - Task
    - Understand how to deploy your connectors (or any connectors)
        - Package Jars
        - Run Jars in standalone mode
        - Deploy Jars
- Finding the code and installing required software
- Description of the GitHub Issues API
  - [API Documentation](https://docs.github.com/en/rest/reference/issues#list-repository-issues "List issues in a repository.")
- Using the Maven Archetype to get started
  - [Maven Archetype](https://github.com/jcustenborder/kafka-connect-archtype)
  - Create an IntelliJ project using the following maven archetype to get skeleton files
    - `GroupId` = `com.github.jcustenborder.kafka.connect`
    - `ArtifactId` = `kafka-connect-quickstart`
    - `Version` = `2.4.0`
- Config Definitions
- Connector Class
- Writing a schema
- Data Model for our Objects
- Writing our GitHub API HTTP Client
- Source Partition & Source Offsets
  - Source Partition allows Kafka Connect to know which source you've been reading from
  - Source Offsets allows Kafka Connect to track your progress in reading from the Source Partition
  - These are different from the partitions and offsets in Kafka Topic
  - Source Partition & Source Offsets are for Kafka Connect Source
- Source Task
- Building and running a Connector in Standalone Mode
- Deploying our Connector on the `Landoop` cluster  
  - Deploy the built jars to Kafka using Docker Volumes:
    ```
    docker run -it --rm -p 2181:2181 -p 3030:3030 -p 8081:8081 -p 8082:8082 -p 8083:8083 -p 9092:9092 -e ADV_HOST=127.0.0.1 -e RUNTESTS=0 -v /c/usr/local/projects/java/kurs/apache-kafka-series/kafka-connect-hands-on-learning/code/kafka-connect-custom-sample/target/kafka-connect-target/usr/share/kafka-connect/kafka-connect-custom-sample:/connectors/GitHub landoop/fast-data-dev
    ```
  - Create the connector in the UI (or by using the RestAPI)

# Links
- [Lenses IO Github](https://github.com/lensesio/fast-data-dev "Lenses IO Github")
- [Confluent Hub](https://www.confluent.io/hub/ "Confluent Hub for Kafka® connectors and more")


# Github setup
```
git remote add origin https://github.com/johnwr-response/aks-kafka-connect-hands-on-learning.git
```
