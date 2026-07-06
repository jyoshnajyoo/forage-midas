package com.jpmc.midascore;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.List;

@Component
public class DataLoader {

    public String[] loadStrings(String path) throws Exception {

        var resource = new ClassPathResource(path);

        List<String> lines = Files.readAllLines(resource.getFile().toPath());

        return lines.toArray(new String[0]);
    }
}