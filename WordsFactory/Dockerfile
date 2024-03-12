FROM openjdk:17.0.2-jdk
LABEL maintainer="nir"
COPY WordsFactory/target/words-api.jar /words-api.jar
ENTRYPOINT ["java","-jar","/words-api.jar"]