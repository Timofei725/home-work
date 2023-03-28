package io.ylab.intensive.lesson04.eventsourcing.db;

import javax.sql.DataSource;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import static io.ylab.intensive.lesson04.RabbitMQUtil.buildConnectionFactory;

public class DbApp {
    private DataSource dataSource;
    private String queueName;
    private ConnectionFactory connectionFactory;

    public DbApp(DataSource dataSource, String queueName, ConnectionFactory connectionFactory) {
        this.dataSource = dataSource;
        this.queueName = queueName;
        this.connectionFactory = connectionFactory;
    }

    public static void main(String[] args) {
        try {
            DataSource dataSource = initDb();
            String queueName = "queue";
            ConnectionFactory connectionFactory = initMQ();
            DbApp dbApp = new DbApp(dataSource, queueName, connectionFactory);
            dbApp.consumeMessages();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


    }

    private static ConnectionFactory initMQ() throws Exception {
        return buildConnectionFactory();
    }

    private static DataSource initDb() throws SQLException {
        String ddl = "" + "drop table if exists person;"
                + "create table if not exists person (\n"
                + "person_id bigint primary key,\n"
                + "first_name varchar,\n"
                + "last_name varchar,\n"
                + "middle_name varchar\n" + ")";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(ddl, dataSource);
        return dataSource;
    }

    private void consumeMessages() {
        try (Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            while (!Thread.currentThread().isInterrupted()) {
                GetResponse message = channel.basicGet(queueName, true);
                if (message == null) {
                    // no messages
                } else {
                    String body = new String(message.getBody());
                    String routingKey = message.getEnvelope().getRoutingKey();
                    if (routingKey.equals("delete_person")) {
                        deletePerson(body, dataSource);
                    } else if (routingKey.equals("save_person")) {
                        savePerson(body, dataSource);
                    }


                }
            }
        } catch (IOException | TimeoutException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private void deletePerson(String body, DataSource dataSource) {
        String deletePerson = "DELETE FROM person WHERE person_id = ?";
        try (java.sql.Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(deletePerson);
            preparedStatement.setLong(1, Long.parseLong(body));
            int deleteResult = preparedStatement.executeUpdate();
            if (deleteResult == 0) {
                System.out.println("there was an attempt to delete, but no data was found");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void savePerson(String body, DataSource dataSource) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (java.sql.Connection connection = dataSource.getConnection()) {
            Person thisPerson = objectMapper.readValue(body, Person.class);
            if (isPersonExist(thisPerson.getId(), connection)) {
                updatePerson(thisPerson, connection);
            } else {
                createPerson(thisPerson, connection);
            }
        } catch (IOException | SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public boolean isPersonExist(Long personId, java.sql.Connection connection) {
        String findPerson = "SELECT * FROM person Where person_id =?  ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(findPerson)) {
            preparedStatement.setLong(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            resultSet.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }

    private void createPerson(Person thisPerson, java.sql.Connection connection) {
        String createPerson = "INSERT INTO person (person_id,first_name,last_name,middle_name)" + "VALUES(?,?,?,?) ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createPerson)) {
            preparedStatement.setLong(1, thisPerson.getId());
            preparedStatement.setString(2, thisPerson.getName());
            preparedStatement.setString(3, thisPerson.getLastName());
            preparedStatement.setString(4, thisPerson.getMiddleName());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void updatePerson(Person thisPerson, java.sql.Connection connection) {
        String updatePerson = "UPDATE PERSON SET first_name=?, last_name=?, middle_name=? WHERE person_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updatePerson)) {
            preparedStatement.setString(1, thisPerson.getName());
            preparedStatement.setString(2, thisPerson.getLastName());
            preparedStatement.setString(3, thisPerson.getMiddleName());
            preparedStatement.setLong(4, thisPerson.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
