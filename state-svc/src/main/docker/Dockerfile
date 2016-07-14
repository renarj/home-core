FROM java:8
VOLUME /tmp

RUN apt-get update && apt-get install -y netcat

ADD start.sh start.sh
RUN chmod +x start.sh

ADD state-svc-*.jar state-svc.jar
RUN bash -c 'touch /state-svc.jar'

CMD ["/start.sh"]

EXPOSE 8080