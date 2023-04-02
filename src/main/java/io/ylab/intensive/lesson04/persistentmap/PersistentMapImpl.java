package io.ylab.intensive.lesson04.persistentmap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Класс, методы которого надо реализовать
 */
public class PersistentMapImpl implements PersistentMap {

    private DataSource dataSource;
    private String name;


    public PersistentMapImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void init(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO persistent_map (map_name ) VALUES (?)")) {

            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            this.name = name;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean containsKey(String key) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT KEY From persistent_map WHERE map_name= ? AND KEY=?")) {

            preparedStatement.setString(1, this.name);
            preparedStatement.setString(2, key);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean result = resultSet.next();
            resultSet.close();
            return result;

        }
    }

    @Override
    public List<String> getKeys() throws SQLException {
        List<String> resultList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT KEY FROM persistent_map WHERE map_name = ? AND value IS NOT NULL")) {

            preparedStatement.setString(1, this.name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                resultList.add(resultSet.getString("KEY"));
            }
            resultSet.close();
        }

        return resultList;
    }

    @Override
    public String get(String key) throws SQLException {
        String result = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("SELECT value FROM persistent_map WHERE map_name = ? AND KEY = ?")) {

            preparedStatement.setString(1, this.name);
            preparedStatement.setString(2, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("value");
            }
            resultSet.close();
        }
        return result;
    }

    @Override
    public void remove(String key) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement("Delete FROM persistent_map WHERE map_name = ? AND KEY = ?")) {

            preparedStatement.setString(1, this.name);
            preparedStatement.setString(2, key);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void put(String key, String value) throws SQLException {
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO persistent_map (map_name,KEY,value ) VALUES (?,?,?)")) {
            if (containsKey(key)) {
                remove(key);
            }

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, key);
            preparedStatement.setString(3, value);
            preparedStatement.executeUpdate();


        }
    }

    @Override
    public void clear() throws SQLException {
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("Delete FROM persistent_map WHERE map_name = ?")) {
            preparedStatement.setString(1, this.name);
            preparedStatement.executeUpdate();
        }
    }
}
