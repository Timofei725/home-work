package src.file_sort;

import java.io.*;

import java.util.*;

import java.nio.file.*;
import java.nio.charset.*;

public class Sorter {
    private int chunkSize;

    public Sorter(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public File sortFile(File dataFile) throws IOException {
        List<File> chunks = createSortedChunks(dataFile);
        return mergeChunks(chunks);
    }

    private List<File> createSortedChunks(File dataFile) throws IOException {
        List<File> chunks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            int i = 0;
            while (true) {
                Map<Long, Integer> map = new HashMap<>();
                List<Long> uniqueData = new ArrayList<>();
                String line;
                int j = 0;
                while (j < chunkSize && (line = reader.readLine()) != null) {
                    long num = Long.parseLong(line);
                    int count = map.getOrDefault(num, 0) + 1;
                    map.put(num, count);
                    if (count == 1) {
                        uniqueData.add(num);
                    }
                    j++;
                }
                if (uniqueData.isEmpty()) {
                    break;
                }
                Collections.sort(uniqueData);
                Path chunkPath = Paths.get("chunk_" + i);
                i++;
                try (BufferedWriter writer = Files.newBufferedWriter(chunkPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                    for (Long num : uniqueData) {
                        int count = map.get(num);
                        for (int k = 0; k < count; k++) {
                            writer.write(num.toString());
                            writer.newLine();
                        }
                    }
                }
                chunks.add(chunkPath.toFile());
            }
        }
        return chunks;
    }


    private File mergeChunks(List<File> chunks) throws IOException {
        if (chunks.isEmpty()) {
            throw new IllegalArgumentException("List of chunks is empty");
        }
        while (chunks.size() > 1) {
            List<File> mergedChunks = new ArrayList<>();
            for (int i = 0; i < chunks.size(); i += 2) {
                File chunk1 = chunks.get(i);
                File chunk2 = (i + 1 < chunks.size()) ? chunks.get(i + 1) : null;
                File mergedChunk = mergeTwoChunks(chunk1, chunk2);
                mergedChunks.add(mergedChunk);
            }
            chunks = mergedChunks;
        }
        return chunks.get(0);
    }


    private File mergeTwoChunks(File chunk1, File chunk2) throws IOException {
        Path mergedPath = Files.createTempFile("merged_", null);
        try (BufferedWriter writer = Files.newBufferedWriter(mergedPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            try (BufferedReader reader1 = new BufferedReader(new FileReader(chunk1));
                 BufferedReader reader2 = (chunk2 != null) ? new BufferedReader(new FileReader(chunk2)) : null) {
                long num1 = getNext(reader1);
                long num2 = getNext(reader2);
                while (num1 != Long.MAX_VALUE || num2 != Long.MAX_VALUE) {
                    if (num1 <= num2) {
                        writer.write(Long.toString(num1));
                        writer.newLine();
                        num1 = getNext(reader1);
                    } else {
                        writer.write(Long.toString(num2));
                        writer.newLine();
                        num2 = getNext(reader2);
                    }
                }
            }
        }
        chunk1.delete();
        if (chunk2 != null) {
            chunk2.delete();
        }
        return mergedPath.toFile();
    }


    private long getNext(BufferedReader reader) throws IOException {
        String line = null;
        if (reader != null) {
            line = reader.readLine();
        }
        if (line != null) {
            return Long.parseLong(line);
        } else {
            return Long.MAX_VALUE;
        }
    }


}