package io.ylab.intensive.lesson04.filesort;


import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

// class for comparing time result
public class FileSortImlWithoutBatch implements FileSorter {

    private DataSource dataSource;

    public FileSortImlWithoutBatch(DataSource dataSource) {
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
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void insertDataToDB(File data) {
        String insertNumbersSQL = "INSERT INTO numbers(val) " + "VALUES (?)";
        try (BufferedReader br = new BufferedReader(new FileReader(data)); Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertNumbersSQL)) {

            String line;
            while ((line = br.readLine()) != null) {
                preparedStatement.setLong(1, Long.valueOf(line));
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

        } catch (SQLException | IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void writeSortedNumbersToFile(File newFile) throws SQLException, IOException {
        String selectNumbersSQL = "SELECT val FROM numbers ORDER BY val DESC";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectNumbersSQL);
             ResultSet resultSet = preparedStatement.executeQuery();
             BufferedWriter bf = new BufferedWriter(new FileWriter(newFile))) {
            while (resultSet.next()) {
                bf.write(String.valueOf(resultSet.getLong("val")));
                bf.newLine();
            }
            bf.flush();
        }
    }
}
