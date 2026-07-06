package com.jpmc.midascore.service;

import com.jpmc.midascore.Incentive;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String INCENTIVE_API_URL = "http://localhost:8080/incentive";

    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void processTransaction(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId()).orElse(null);
        UserRecord recipient = userRepository.findById(transaction.getRecipientId()).orElse(null);

        if (sender == null || recipient == null) {
            System.out.println("TXN RECEIVED: " + transaction);
            System.out.println("SENDER: " + sender.getName() + " BAL: " + sender.getBalance());
            System.out.println("RECIPIENT: " + recipient.getName() + " BAL: " + recipient.getBalance());
            System.out.println(">>> PROCESSING TXN: " + transaction);
            return;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            System.out.println(">>> DISCARDING: Insufficient funds - " + transaction + " senderBalance=" + sender.getBalance());
            return;
        }

        // TASK 4: CALL INCENTIVE API
        Incentive incentive = restTemplate.postForObject(INCENTIVE_API_URL, transaction, Incentive.class);
        float incentiveAmount = incentive != null ? (float) incentive.getAmount() : 0.0f; // <-- CAST TO FLOAT

        // Update balances - CAST EVERYTHING TO FLOAT
        sender.setBalance(sender.getBalance() - (float) transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + (float) transaction.getAmount() + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord record = new TransactionRecord(
                sender,
                recipient,
                transaction.getAmount(),
                incentiveAmount
        );

        transactionRepository.save(record);

        System.out.println(">>> SAVED: " + sender.getName() + " newBalance=" + sender.getBalance() +
                " -> " + recipient.getName() + " newBalance=" + recipient.getBalance() + " incentive=" + incentiveAmount);

        // Print waldorf balance for Task 3
        userRepository.findByName("waldorf").ifPresent(w ->
                System.err.println(">>> WALDORF BALANCE: " + w.getBalance())
        );
    }
}