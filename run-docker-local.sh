#!/bin/sh

set_docker_host_ip() {
    export DOCKERHOST=$(ifconfig | grep -E "([0-9]{1,3}\.){3}[0-9]{1,3}" | grep -v 127.0.0.1 | awk '{ print $2 }' | cut -f2 -d: | head -n1)
}

set_docker_host_ip

services="grafana prometheus"
docker-compose stop ${services}
# Remove containers if they exist, if a new version exist there will be a name conflict if not removing them
docker-compose rm -v --force ${services}
docker-compose pull ${services}
docker-compose up --build -d ${services}
