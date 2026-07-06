package com.jpmc.midascore;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileLoader {

    public String[] loadStrings(String filePath) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            InputStream inputStream = resource.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            List<String> lines = reader.lines().collect(Collectors.toList());

            return lines.toArray(new String[0]);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load file: " + filePath, e);
        }
    }
}