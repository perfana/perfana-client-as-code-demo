package io.perfana.demo.client.service;

import io.perfana.client.PerfanaClient;
import io.perfana.client.PerfanaClientBuilder;
import io.perfana.client.api.*;
import io.perfana.event.PerfanaEventConfig;
import io.perfana.eventscheduler.EventScheduler;
import io.perfana.eventscheduler.EventSchedulerBuilder;
import io.perfana.eventscheduler.api.EventLogger;
import io.perfana.eventscheduler.api.config.EventConfig;
import io.perfana.eventscheduler.api.config.EventSchedulerConfig;
import io.perfana.eventscheduler.api.config.TestConfig;
import io.perfana.eventscheduler.exception.EventCheckFailureException;
import io.perfana.eventscheduler.log.EventLoggerStdOut;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.util.*;

@SpringBootApplication
public class PerfanaClientServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PerfanaClientServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//perfanaTestEventsExample();

		perfanaTestEventsMinimalExample();

		//perfanaClientExample();
	}

	private void perfanaClientExample() {
		PerfanaClientLogger testLogger = new PerfanaClientLoggerStdOut();

		PerfanaConnectionSettings settings = new PerfanaConnectionSettingsBuilder()
				.setPerfanaUrl("http://localhost:4312")
				.setApiKey("perfana-api-key-XXX-YYY-ZZZ")
				.setRetryMaxCount("5")
				.setRetryTimeInSeconds("3")
				.build();

		TestContext context = new TestContextBuilder()
				.setSystemUnderTest("sut")
				.setWorkload("workload")
				.setTestEnvironment("env")
				.setTestRunId("testRunId")
				.setCIBuildResultsUrl("http://url")
				.setVersion("version")
				.setRampupTimeInSeconds("10")
				.setConstantLoadTimeInSeconds("300")
				.setAnnotations("annotation")
				.setVariables(new HashMap<>())
				.setTags(new ArrayList<>())
				.build();

		PerfanaClient client = new PerfanaClientBuilder()
				.setPerfanaConnectionSettings(settings)
				.setTestContext(context)
				.setAssertResultsEnabled(true)
				.setLogger(testLogger)
				.build();

	}

	private void perfanaTestEventsMinimalExample() throws InterruptedException {
		EventLogger eventLogger = EventLoggerStdOut.INSTANCE;

		TestConfig testConfig = TestConfig.builder()
				.workload("testType")
				.testEnvironment("testEnv")
				.testRunId("testRunId")
				.buildResultsUrl("http://url")
				.version("version")
				.rampupTimeInSeconds(10)
				.constantLoadTimeInSeconds(300)
				.build();

		// enable the Perfana events
		PerfanaEventConfig perfanaEventConfig = new PerfanaEventConfig();
		perfanaEventConfig.setPerfanaUrl("http://localhost:4312");
		perfanaEventConfig.setApiKey("perfana-api-key-XXX-YYY-ZZZ");

		List<EventConfig> eventConfigs = new ArrayList<>();
		eventConfigs.add(perfanaEventConfig);

		EventSchedulerConfig eventSchedulerConfig = EventSchedulerConfig.builder()
				.testConfig(testConfig)
				.eventConfigs(eventConfigs)
				.build();

		EventScheduler scheduler = EventSchedulerBuilder.of(eventSchedulerConfig, eventLogger);

		scheduler.startSession();

		try {
			// instead of a sleep run or start a load test
			fakeLoadTest();
		} finally {
			scheduler.stopSession();
		}

	}

	private void perfanaTestEventsExample() throws InterruptedException {
		EventLogger eventLogger = EventLoggerStdOut.INSTANCE;

		String scheduleScript1 =
				"PT1S|restart(restart to reset replicas)|{ 'server':'myserver' 'replicas':2, 'tags': [ 'first', 'second' ] }\n"
						+ "PT10S|scale-down|{ 'replicas':1 }\n"
						+ "PT18S|heapdump|server=myserver.example.com;port=1567";

		TestConfig testConfig = TestConfig.builder()
				.workload("testType")
				.testEnvironment("testEnv")
				.testRunId("testRunId")
				.buildResultsUrl("http://url")
				.version("version")
				.rampupTimeInSeconds(10)
				.constantLoadTimeInSeconds(300)
				.annotations("annotation")
				.tags(Arrays.asList("tag1","tag2"))
				.build();

		// enable the Perfana events
		PerfanaEventConfig perfanaEventConfig = new PerfanaEventConfig();
		perfanaEventConfig.setPerfanaUrl("http://localhost:4312");
		perfanaEventConfig.setApiKey("perfana-api-key-XXX-YYY-ZZZ");
		perfanaEventConfig.setAssertResultsEnabled(false);
		Map<String, String> variables = Map.of("var1", "value1", "var2", "value2");
		perfanaEventConfig.setVariables(variables);

		List<EventConfig> eventConfigs = new ArrayList<>();
		eventConfigs.add(perfanaEventConfig);

		EventSchedulerConfig eventSchedulerConfig = EventSchedulerConfig.builder()
				.schedulerEnabled(true)
				.debugEnabled(false)
				.continueOnEventCheckFailure(false)
				.failOnError(true)
				.keepAliveIntervalInSeconds(120)
				.testConfig(testConfig)
				.eventConfigs(eventConfigs)
				.scheduleScript(scheduleScript1)
				.build();

		EventScheduler scheduler = EventSchedulerBuilder.of(eventSchedulerConfig, eventLogger);

		scheduler.startSession();

		try {
			// instead of a sleep run or start a load test
			fakeLoadTest();
		} finally {
			scheduler.stopSession();
		}

		try {
			scheduler.checkResults();
		} catch (EventCheckFailureException e) {
			// deal with failed checks: e.g. fail the CI run
		}
	}

	private void fakeLoadTest() throws InterruptedException {
		Duration duration = Duration.ofSeconds(10);
		System.out.println(">>> sleep " + duration);
		Thread.sleep(duration.toMillis());
	}
}
