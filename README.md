
# Perfana Client as Code demo

Demonstrates how to use the Perfana `event-scheduler` with `perfana-java-client` from code.

To build, use java 17:

    ./mvnw clean package

Then start the Perfana stub:

    java -jar pjc-perfana-stub/target/pcs-perfana-stub-0.0.1-SNAPSHOT.jar

Next start plain old java:

    java -jar pjc-plain-old-java/target/pcs-plain-old-java-0.0.1-SNAPSHOT.jar

Or start spring boot demo:

    java -jar pjc-spring-boot/target/pcs-spring-boot-0.0.1-SNAPSHOT.jar

The `io.perfana.demo.client.service.PerfanaClientServiceApplication` contains multiple examples of test
run setups and Perfana Java Client configuration.

This demo uses the Perfana event-scheduler to orchestrate all events of the test
and the perfana-java-client to communicate with Perfana:

* [event-scheduler](https://github.com/perfana/event-scheduler)
* [perfana-java-client](https://github.com/perfana/perfana-java-client)
