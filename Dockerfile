FROM quay.io/wildfly/wildfly:39.0.0.Final-jdk17

COPY --chown=jboss:jboss \
    docker/wildfly/drivers/postgresql-42.7.14.jar \
    /opt/jboss/wildfly/standalone/deployments/

COPY --chown=jboss:jboss \
    docker/wildfly/configure.cli \
    /opt/jboss/wildfly/configure.cli

RUN /opt/jboss/wildfly/bin/jboss-cli.sh \
    --file=/opt/jboss/wildfly/configure.cli