FROM openjdk:11
EXPOSE 8080
ADD target/clientworker.jar clientworker.jar
ENTRYPOINT ["java","-jar","/clientworker.jar"]