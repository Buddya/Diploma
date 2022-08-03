FROM osangenis/openjdk-18-go-1.16-alpine

EXPOSE 8080

ADD target/diploma-0.0.1-SNAPSHOT.jar diploma.jar

ENTRYPOINT ["java", "-jar", "/netologyDiploma.jar"]