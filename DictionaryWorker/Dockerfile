FROM openjdk:17.0.2-jdk
LABEL maintainer="nir"
COPY DictionaryWorker/target/dictionary-worker.jar /dictionary-worker.jar
ENTRYPOINT ["java","-jar","/dictionary-worker.jar"]