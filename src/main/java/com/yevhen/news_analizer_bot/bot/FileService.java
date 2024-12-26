package com.yevhen.news_analizer_bot.bot;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileService {

    private String path;

    public FileService(@Value("${telegram.bot.strings.path}") String path) {
        if (Files.exists(Path.of(path))) this.path = path;
    }

    public void addLine(String lineToAdd) throws IOException {
        if (lineToAdd == null) return;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(lineToAdd);
            writer.newLine();
        }
    }

    public Set<String> readLines() throws IOException {
        return new HashSet<>(Files.readAllLines(Path.of(path)));
    }

    public void removeLine(String lineToRemove) throws IOException {
        if (lineToRemove == null) return;
        Set<String> lines = readLines();
        lines.stream().filter(line -> !line.equals(lineToRemove)).collect(Collectors.toList());
        Files.write(Path.of(path), lines);
    }
}
