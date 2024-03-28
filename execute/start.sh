#!/bin/sh
echo starting: Portale-Gestione-Utenti-Service.
export OTEL_EXPORTER_OTLP_ENDPOINT=https://f924557c0abd4906b6311f4c26800f63.apm.us-central1.gcp.cloud.es.io:443
export OTEL_EXPORTER_OTLP_HEADERS="Authorization=Bearer xxxxxxxxxxx"
export OTEL_METRICS_EXPORTER=otlp
export OTEL_LOGS_EXPORTER=otlp
export OTEL_RESOURCE_ATTRIBUTES=service.name=portale-gestione-utenti-service,service.version=1.0.0,deployment.environment=pre-production





nohup java -XX:+UseZGC -XX:+ZGenerational -Xms1g -Xmx1g -XX:+ExitOnOutOfMemoryError -javaagent:./opentelemetry-javaagent.jar  -jar ./gestioneutenti-service-portale.jar --logging.config=./logback.xml 2>&1 &
echo $! > ./pid.file
