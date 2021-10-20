FROM openjdk:8-jre-alpine
RUN apk --no-cache add curl
WORKDIR .
EXPOSE 8082
COPY TicketBookingApplication-assembly-0.1.jar /
CMD java -jar TicketBookingApplication-assembly-0.1.jar

