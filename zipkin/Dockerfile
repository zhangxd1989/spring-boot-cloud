FROM java:8
VOLUME /tmp
ADD ./target/zipkin.jar /app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9411
ENTRYPOINT ["java","-jar","/app.jar"]