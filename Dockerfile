FROM openjdk:8-jdk
EXPOSE 8090
COPY target/address-book-service.jar.jar /opt/app/address-book-service.jar
ENTRYPOINT ["java","-jar","/opt/app/address-book-service.jar"]