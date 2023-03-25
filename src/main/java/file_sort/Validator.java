package src.file_sort;

import java.io.*;
import java.util.*;

public class Validator {
    private File file;

    public Validator(File file) {
        this.file = file;
    }

    public boolean isSorted() {
        try (Scanner scanner = new Scanner(new FileInputStream(file))) {
            long prev = Long.MIN_VALUE;
            while (scanner.hasNextLong()) {
                long current = scanner.nextLong();
                if (current < prev) {
                    return false;
                } else {
                    prev = current;
                }
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}