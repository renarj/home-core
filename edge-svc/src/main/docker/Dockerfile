FROM java:8
VOLUME /tmp

RUN apt-get update && apt-get install -y netcat
ADD start.sh start.sh
RUN chmod +x start.sh

ADD edge-svc-*.jar edge-svc.jar
RUN bash -c 'touch /edge-svc.jar'

CMD ["/start.sh"]

EXPOSE 8080