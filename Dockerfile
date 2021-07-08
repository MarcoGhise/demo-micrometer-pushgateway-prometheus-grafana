FROM openjdk:11.0.1-jre-slim
VOLUME /tmp
ARG JAR_FILE=target/job-1.0.jar
ADD ${JAR_FILE} job-1.0.jar

ENTRYPOINT ["java","-jar","/job-1.0.jar"]