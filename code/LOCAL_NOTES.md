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

# Links
- [Lenses IO Github](https://github.com/lensesio/fast-data-dev "Lenses IO Github")


# Github setup
```
git remote add origin https://github.com/johnwr-response/aks-kafka-connect-hands-on-learning.git
```
