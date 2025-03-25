FROM openjdk:8
EXPOSE 9090
ADD target/sia-docker.jar sia-docker.jar
ENTRYPOINT [ "java","-jar","/sia-docker.jar"]