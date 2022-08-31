package io.perfana.demo.client.poj;

import io.perfana.event.PerfanaEventConfig;
import io.perfana.eventscheduler.EventScheduler;
import io.perfana.eventscheduler.EventSchedulerBuilder;
import io.perfana.eventscheduler.api.EventLogger;
import io.perfana.eventscheduler.api.config.EventConfig;
import io.perfana.eventscheduler.api.config.EventSchedulerConfig;
import io.perfana.eventscheduler.api.config.TestConfig;
import io.perfana.eventscheduler.log.EventLoggerStdOut;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PlainOldJavaPerfana {

    public static void main(String[] args) throws Exception {
        EventLogger eventLogger = EventLoggerStdOut.INSTANCE_DEBUG;

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
        perfanaEventConfig.setAssertResultsEnabled(true);

        List<EventConfig> eventConfigs = new ArrayList<>();
        eventConfigs.add(perfanaEventConfig);

        EventSchedulerConfig eventSchedulerConfig = EventSchedulerConfig.builder()
                .testConfig(testConfig)
                .eventConfigs(eventConfigs)
                .debugEnabled(true)
                .build();

        EventScheduler scheduler = EventSchedulerBuilder.of(eventSchedulerConfig, eventLogger);

        scheduler.startSession();

        try {
            // instead of a sleep run or start a load test
            Duration duration = Duration.ofSeconds(10);
            System.out.println(">>> sleep " + duration);
            Thread.sleep(duration.toMillis());
        } finally {
            scheduler.stopSession();
        }
    }
}
