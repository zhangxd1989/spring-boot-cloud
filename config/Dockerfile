FROM java:8

ADD ./target/config.jar /app/

EXPOSE 8888
ENTRYPOINT ["java","-jar","/app/config.jar"]