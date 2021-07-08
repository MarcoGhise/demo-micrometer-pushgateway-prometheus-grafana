# Monitoring Spring Boot Job with Pushgateway,Prometheus and Grafana

This small demo project contains an example setup of Prometheus and Grafana to monitor Spring Boot Job.

The project contains a default Grafana Prometheus datasource and scrapes the Spring Boot project and Prometheus server 
for monitoring information.

If you want to login to Grafana you can use the `admin / password` combination.

For monitoring Spring Boot applications I highly recommend the [JVM Micrometer dashboard](https://grafana.com/dashboards/4701) created by [Michael Weirauch](https://twitter.com/emwexx).

## Building the project

First build the spring boot application.

```bash
mvn clean package
```

Now when the application has been build we can start running our services by running:

```bash
docker-compose up
```

After all services have started successfully, you can navigate to the following urls:

- Spring Boot app - http://localhost:8080/
- Pushgateway     - http://localhost:9091/
- Prometheus      - http://localhost:9090/
- Grafana         - http://localhost:3000/

## Update
In this Fork I added/updated these features:

- Updated Grafana to 7.4.3;
- Added Fake-smtp docker image: Server for sending email.
- Added Fake-smtp-web docker image: Web console for checking the email.yaml
- Created a new Grafana Dashboard;
- Added snuids-trafficlights-panel plugin;
- Added log messages to monitoring-demo app;
- Created a new instance of monitoring-demo called node2; 
- Added order.purchase metric;
- Added notification alerts inside Grafana.               