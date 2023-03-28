package io.ylab.intensive.lesson04.eventsourcing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;


/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {
    private String exchangeName;
    private ConnectionFactory connectionFactory;
    private DataSource dataSource;


    public PersonApiImpl(String exchangeName, String queueName, ConnectionFactory connectionFactory, DataSource dataSource) {
        this.exchangeName = exchangeName;
        this.connectionFactory = connectionFactory;
        this.dataSource = dataSource;
        try (Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, "*");
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deletePerson(Long personId) {
        try (Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            channel.basicPublish(exchangeName, "delete_person", null, personId.toString().getBytes());
        } catch (IOException | TimeoutException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            Person thisPerson = new Person(personId, firstName, lastName, middleName);
            byte[] jsonBytes = objectMapper.writeValueAsBytes(thisPerson);
            channel.basicPublish(exchangeName, "save_person", null, jsonBytes);
        } catch (IOException | TimeoutException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public Person findPerson(Long personId) {
        String findPerson = "SELECT * FROM person Where person_id =?  ";
        Person person = new Person();
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findPerson)) {
            preparedStatement.setLong(1, personId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                person.setId(resultSet.getLong("person_id"));
                person.setName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setMiddleName(resultSet.getString("middle_name"));

            } else {
                resultSet.close();
                return null;
            }
            resultSet.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return person;
    }


    @Override
    public List<Person> findAll() {
        String findPerson = "SELECT * FROM person";
        List<Person> persons = new ArrayList<>();
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findPerson);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Person thisPerson = new Person();
                thisPerson.setId(resultSet.getLong("person_id"));
                thisPerson.setName(resultSet.getString("first_name"));
                thisPerson.setLastName(resultSet.getString("last_name"));
                thisPerson.setMiddleName(resultSet.getString("middle_name"));
                persons.add(thisPerson);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return persons;
    }

}
