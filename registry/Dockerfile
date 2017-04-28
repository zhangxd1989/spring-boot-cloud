FROM java:8

ADD ./target/registry.jar /app/

EXPOSE 8761
ENTRYPOINT ["java","-jar","/app/registry.jar"]