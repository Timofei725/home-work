package io.ylab.intensive.lesson04.filesort;


import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class FileSortImpl implements FileSorter {
    private DataSource dataSource;
    private long numbersCounts;

    public FileSortImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public File sort(File data) {
        try {
            insertDataToDB(data);
            File newFile = new File("src/main/java/io/ylab/intensive/lesson04/filesort", "sorting_result.txt");
            writeSortedNumbersToFile(newFile);
            return newFile;
        } catch (IOException | SQLException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }

    }

    private void insertDataToDB(File data) throws FileNotFoundException {
        String insertNumbersSQL = "INSERT INTO numbers(val) " + "VALUES (?)";
        try (BufferedReader br = new BufferedReader(new FileReader(data));
             Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertNumbersSQL)) {

            String line;
            while ((line = br.readLine()) != null) {
                preparedStatement.setLong(1, Long.valueOf(line));
                preparedStatement.addBatch();
                numbersCounts++;
            }
            preparedStatement.executeBatch();

        } catch (IOException | SQLException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }

    }

    private void writeSortedNumbersToFile(File newFile) throws SQLException, IOException {
        String selectNumbersSQL = "SELECT val FROM numbers ORDER BY val DESC";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectNumbersSQL);
             ResultSet resultSet = preparedStatement.executeQuery();
             BufferedWriter bf = new BufferedWriter(new FileWriter(newFile))) {
            preparedStatement.setFetchSize((int) Math.round(Math.sqrt(numbersCounts)));
            while (resultSet.next()) {
                bf.write(String.valueOf(resultSet.getLong("val")));
                bf.newLine();
            }
            bf.flush();
        }
    }
}
