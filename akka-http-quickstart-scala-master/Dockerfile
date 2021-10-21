FROM openjdk:8-jre-alpine
RUN apk add --update \ curl \ && rm -rf /var/cache/apk/*
WORKDIR .
COPY TicketBookingApplication-assembly-0.1.jar /
CMD java -jar TicketBookingApplication-assembly-0.1.jar


