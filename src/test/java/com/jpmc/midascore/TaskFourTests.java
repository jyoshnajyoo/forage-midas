package com.jpmc.midascore;

import com.jpmc.midascore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskFourTests {
    static final Logger logger = LoggerFactory.getLogger(TaskFourTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Test
    void task_four_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/alskdjfh.fhdjsk");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }
        Thread.sleep(5000); // wait 5 seconds for kafka to process

        var wilburOpt = userRepository.findByName("Wilbur");
        if (wilburOpt.isPresent()) {
            double wilburBalance = wilburOpt.get().getBalance();
            logger.info("WILBUR FINAL BALANCE: {}", wilburBalance);
        } else {
            logger.info("Wilbur not found. Try increasing Thread.sleep to 10000");
        }

        logger.info("----------------------------------------------------------");
        logger.info("use your debugger to find out what wilburs balance is after all transactions are processed");
        while (true) {
            Thread.sleep(20000);
            logger.info("...");
        }
    }
}