<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)
* [Roadmap](#roadmap)
* [Contributing](#contributing)
* [License](#license)

## About The Project

![architecture](./documentation/images/architecture.png#center)

Amplifix is Java toolkit built on top of [Micrometer](https://micrometer.io/) to provides a declarative and simpler way to build and expose metrics from any Java service (not necessarily RESTful API). This toolkit is framework agnostic.

The main motivation is to isolate the metric handling from any service which only should care of business logic.
The build and posting happen asyncronous using a [Vertx Event Bus](https://vertx.io/docs/vertx-core/java/).

The idea behind is to enable the developer a simple way to declare and publish metrics to the choice monitoring service (Datadog, Grafana, etc.). The toolkit follows the [OpenMetric](https://github.com/OpenObservability/OpenMetrics) standard for transmitting metrics at scale.

The developer needs to concentrate energy in business logic. Metrics handling should be automated extracting values from events and processing them in a standard format in asyncronous fashion. The only thing the developer should do is (in every desired place):

```
eventBus.send(event)
```

Under the wood the toolkit needs to underderstand the source, the type of metric and the values required.

**IMPORTANT:** This project is an experiment and it is in continuous progress, adding, changing features and evaluating performance and new functionalities. Once this toolkit is stable, and with a bounded scope, the first version will be properly released and documented. So it should not be used in Production environments.

There are many great README templates available on GitHub, however, I didn't find one that really suit my needs so I created this enhanced one. I want to create a README template so amazing that it'll be the last one you ever need.


### Built With
The toolkit is built on top of [Micrometer](https://micrometer.io/) and uses [Vertx](https://vertx.io) to enable the event bus along the service.
Also it uses [Hazelcast](https://hazelcast.com) to allow building complex metric such as conversion rate given an event (more info is coming...).


## Getting Started

This repo includes 3 projects:
* [Amplifix Toolkit](https://github.com/margostino/amplifix/tree/master/toolkit)
* [Demo Spring RESTful API](https://github.com/margostino/amplifix/tree/master/demo-spring)
* [Demo Vertx RESTful API](https://github.com/margostino/amplifix/tree/master/demo-vertx)

The [Makefile ](https://github.com/margostino/amplifix/blob/master/Makefile) includes all commands available to build the toolkit, the demo, integrate both and run in a docker enviroment with all the dependendencies by default to test the functionalities.

#### Showcase dependencies:

* Prometheus: as default monitoring tool.
* Grafana: as default metrics visualization service.
* Nginx: as default load balancer to distribute requests to the Demo which runs in a (small) cluster of 2 instances.
* Hazelcast (management-center): in-memory data grid to monitor and manage your cluster members running Hazelcast IMDG by the toolkit.

#### (1) If you want to run all in one:

<br/>

##### Run Spring Demo:
```
make start-spring
```

##### Run Vertx Demo:
```
make start-vertx
```

#### (2) If you want to build and run separately:

<br/>

##### Build Toolkit:
```
make build-toolkit
```

##### Build Spring Demo:
```
make build-spring
```

##### Build Vertx Demo:
```
make build-vertx
```

##### Run Demo (either Spring or Vertx):
```
make run
```

### Prerequisites

The tookit runs with Java 8 and the Showcase enviroments also need Docker in place.

## Usage

Once the showcase dependencies are up running:

![docker-showcase](./documentation/images/docker-showcase.png#center)

Run the flow generator command to perform N request against the Demo:

```
make generate-flow max=10 port=5000
```

The port parameter is where the Demo Service is listening.


Visualize the metrics in OpenMetric format:
```
http://localhost:5000/metrics
```

![openmetrics](./documentation/images/openmetrics.png#center)

Visualize the default dashboard with metrics in Grafana:
```
http://localhost:3000
```

![grafana](./documentation/images/grafana.png#center)

Manage the Hazelcast IMDG cluster:

```
http://localhost:8080/hazelcast-mancenter/dev
```

![hazelcast-mancenter](./documentation/images/hazelcast-mancenter.png#center)


<!-- ROADMAP -->
## Roadmap

[coming soon...]



<!-- CONTRIBUTING -->
## Contributing

[coming soon...]


<!-- LICENSE -->
## License

Distributed under Apache License. See [LICENSE](https://github.com/margostino/amplifix/blob/master/LICENSE) for more information.
