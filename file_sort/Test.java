package file_sort;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws IOException {
        File dataFile = new Generator().generate("data.txt", 80);
        System.out.println(new Validator(dataFile).isSorted()); // false
        System.out.println("Time before sorting: " + new Date());
        File sortedFile = new Sorter(20).sortFile(dataFile);
        System.out.println("Time after sorting: " + new Date());

        System.out.println(new Validator(sortedFile).isSorted()); // true
    }
}
