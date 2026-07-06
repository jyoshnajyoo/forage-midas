package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskFiveTests {

    static final Logger logger = LoggerFactory.getLogger(TaskFiveTests.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Test
    void task_five_verifier() throws InterruptedException {
        logger.info("---begin output---");

        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/alskdjfh.fhdjsk");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        Thread.sleep(8000); // wait 8 seconds for kafka to process all transactions

        // Task 5 asks for userIds 1 to 13
        for (long userId = 1; userId <= 13; userId++) {
            Balance balance = restTemplate.getForObject("/balance?userId=" + userId, Balance.class);
            logger.info("{}", balance);
        }

        logger.info("---end output---");

        while (true) {
            Thread.sleep(20000);
        }
    }
}