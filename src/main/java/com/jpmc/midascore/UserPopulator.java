package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class UserPopulator {

    private final UserRepository userRepository;

    public UserPopulator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void populate() {

        System.out.println(">>> Populating users...");

        userRepository.deleteAll();

        userRepository.save(new UserRecord(1L, "wilbur", 1000f));
        userRepository.save(new UserRecord(2L, "charlie", 1000f));
        userRepository.save(new UserRecord(3L, "bob", 1000f));
        userRepository.save(new UserRecord(4L, "alice", 1000f));
        userRepository.save(new UserRecord(5L, "david", 1000f));
        userRepository.save(new UserRecord(6L, "emma", 1000f));
        userRepository.save(new UserRecord(7L, "john", 1000f));
        userRepository.save(new UserRecord(8L, "mike", 1000f));
        userRepository.save(new UserRecord(9L, "sara", 1000f));
        userRepository.save(new UserRecord(10L, "tom", 1000f));

        System.out.println(">>> Users inserted successfully.");
    }
}