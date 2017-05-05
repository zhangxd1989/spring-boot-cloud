FROM java:8
VOLUME /tmp
ADD ./target/monitor.jar /app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8040 8041
ENTRYPOINT ["java","-jar","/app.jar"]