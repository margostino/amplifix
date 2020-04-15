#!make
SHELL = /bin/sh
DOCKER_PROJECT_NAME='amplifix'
DOCKER_HOST=$(ifconfig | grep -E "([0-9]{1,3}\.){3}[0-9]{1,3}" | grep -v 127.0.0.1 | awk '{ print $2 }' | cut -f2 -d: | head -n1)

.PHONY: build
build:
	./gradlew clean build -x :test

.PHONY: unit-test
unit-test:
	./gradlew clean test

.PHONY: stop
stop: DOCKER_PROJECT_NAME=${DOCKER_PROJECT_NAME} ./bin/local docker_compose_down

.PHONY: run-amplifix
run-amplifix:
	./bin/local remove_container_if_exists amplifix_server_1
	./bin/local remove_container_if_exists amplifix_server_2
	docker run -d --name amplifix_server_1 --rm -e HZ_HOST=$(value DOCKER_HOST) -p 5001:8880 -p 5002:8881 -i -t amplifix:test
	docker run -d --name amplifix_server_2 --rm -e HZ_HOST=$(value DOCKER_HOST) -p 5003:8880 -p 5004:8881 -i -t amplifix:test

.PHONY: prepare
prepare:
	DOCKER_HOST=$(value DOCKER_HOST) ./bin/local prepare_config_files

.PHONY: run-dependencies
run-dependencies:
	DOCKER_PROJECT_NAME=${DOCKER_PROJECT_NAME} ./bin/local docker_compose_up

.PHONY: run
run: build prepare run-dependencies run-amplifix

generate-flow:
	./bin/flow_generator $(amount)
