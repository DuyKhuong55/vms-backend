FROM openjdk:oracle

WORKDIR /app

COPY ./target/cms-0.0.1-SNAPSHOT.jar vms-back.jar

COPY . .

ENTRYPOINT ["java","-jar","/vms-back.jar"]


#docker build --tag vms-test

