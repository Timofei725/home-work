package io.ylab.intensive.lesson05.eventsourcing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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
@Component
public class PersonApiImpl implements PersonApi {
    private static final String EXCHANGE_NAME = "exc";
    private ConnectionFactory connectionFactory;
    private static final String QUEUE_NAME = "queue";
    private DataSource dataSource;

    @Autowired
    public PersonApiImpl(ConnectionFactory connectionFactory, DataSource dataSource) {
        this.connectionFactory = connectionFactory;
        this.dataSource = dataSource;
        try (Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*");
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deletePerson(Long personId) {
        try (Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            channel.basicPublish(EXCHANGE_NAME, "delete_person", null, personId.toString().getBytes());
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
            channel.basicPublish(EXCHANGE_NAME, "save_person", null, jsonBytes);
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
