FROM java:8
VOLUME /tmp
ADD ./target/svca-service.jar /app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]