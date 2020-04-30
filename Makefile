#!make
SHELL = /bin/sh
.DEFAULT: help
DOCKER_PROJECT_NAME='amplifix'
DOCKER_HOST=$(ifconfig | grep -E "([0-9]{1,3}\.){3}[0-9]{1,3}" | grep -v 127.0.0.1 | awk '{ print $2 }' | cut -f2 -d: | head -n1)
BUILD_VERSION=latest

help:
	@echo  'Amplifix testing'
	@echo  ''
	@echo  'Usage:'
	@echo  '  run                           - Spin up all environments (Demo 2 instances, Grafana, Prometheus and Hazelcast)'
	@echo  '  generate-flow max=5 port=8082 - Generate traffic given a max number of request '
	@echo  ''
	@echo  'Set project-level environment variables in .env file:'
	@echo  '  SERVICE_NAME=amplifix'
	@echo  '  DOCKER_REPO='
	@echo  '  COMPOSE_PROJECT_NAME=amplifix'
	@echo  ''
	@echo  'Note: Store protected environment variables in .env.local or .env.*.local'
	@echo  ''


.PHONY: build-toolkit
build-toolkit:
	./toolkit/gradlew -p toolkit clean build -PbuildVersion=${BUILD_VERSION} copyJarToDemo -x :test

.PHONY: build-demo-spring
build-demo-spring:
	./demo-spring/gradlew -p demo-spring clean build -PbuildVersion=${BUILD_VERSION} copyJarToRoot -x :test

.PHONY: build-demo-vertx
build-demo-vertx:
	./demo-vertx/gradlew -p demo-vertx clean build -PbuildVersion=${BUILD_VERSION} -x :test

.PHONY: build-docker-image
build-docker-image:
	./bin/pipeline build_docker_image

.PHONY: unit-test
unit-test:
	./gradlew clean test

.PHONY: stop
stop: DOCKER_PROJECT_NAME=${DOCKER_PROJECT_NAME} ./bin/pipeline docker_compose_down

.PHONY: run-demo
run-demo:
	./bin/pipeline remove_container_if_exists amplifix_demo_server_1
	./bin/pipeline remove_container_if_exists amplifix_demo_server_2
	docker run -d --name amplifix_demo_server_1 --rm -e HZ_HOST=$(value DOCKER_HOST) -p 5001:8880 -p 5002:8881 -i -t amplifix-demo:latest
	docker run -d --name amplifix_demo_server_2 --rm -e HZ_HOST=$(value DOCKER_HOST) -p 5003:8880 -p 5004:8881 -i -t amplifix-demo:latest

.PHONY: prepare-config-files
prepare-config-files:
	DOCKER_HOST=$(value DOCKER_HOST) ./bin/pipeline prepare_config_files

.PHONY: stop-the-world
stop-the-world:
	DOCKER_PROJECT_NAME=${DOCKER_PROJECT_NAME} ./bin/pipeline stop_and_clean_docker_world

.PHONY: run-dependencies
run-dependencies:
	DOCKER_PROJECT_NAME=${DOCKER_PROJECT_NAME} ./bin/pipeline docker_compose_up

.PHONY: start-spring
start-spring: stop-the-world build-toolkit build-demo-spring build-docker-image prepare-config-files run-dependencies run-demo

.PHONY: start-vertx
start-vertx: stop-the-world build-toolkit build-demo-vertx build-docker-image prepare-config-files run-dependencies run-demo

.PHONY: run
run: prepare-config-files run-dependencies run-demo

generate-flow:
	./bin/flowgen $(max) $(port)
