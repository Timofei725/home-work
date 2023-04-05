package io.ylab.intensive.lesson05.messagefilter;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.TimeoutException;

@Component
public class MessageFilterApp {
    private static final String TABLE_NAME = "bad_words";
    private DataSource dataSource;
    private static final String FILE_PATH = "src/main/java/io/ylab/intensive/lesson05/messagefilter/badWords.txt";
    private static final String INPUT_QUEUE = "input";
    private static final String OUTPUT_QUEUE = "output";
    private MessagesFilterImpl messagesFilter;


    private ConnectionFactory connectionFactory;

    @Autowired
    public MessageFilterApp(DataSource dataSource, ConnectionFactory connectionFactory, MessagesFilterImpl messagesFilter) {
        this.dataSource = dataSource;
        this.connectionFactory = connectionFactory;
        this.messagesFilter = messagesFilter;
    }


    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        MessageFilterApp messageFilterApp = applicationContext.getBean(MessageFilterApp.class);
        messageFilterApp.workOnMessages();

    }

    private void workOnMessages() {
        refreshDB();
        try (com.rabbitmq.client.Connection connection = connectionFactory.newConnection();
             Channel consumerChannel = connection.createChannel();
             Channel producerChannel = connection.createChannel()) {
            consumerChannel.queueDeclare(INPUT_QUEUE, true, false, false, null);
            producerChannel.queueDeclare(OUTPUT_QUEUE, true, false, false, null);
            while (!Thread.currentThread().isInterrupted()) {
                GetResponse message = consumerChannel.basicGet(INPUT_QUEUE, true);
                if (message == null) {
                    // no messages
                } else {
                    String text = new String(message.getBody());
                    text = messagesFilter.filter(text);
                    producerChannel.basicPublish("", OUTPUT_QUEUE, null, text.getBytes());


                }
            }
        } catch (IOException | TimeoutException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void refreshDB() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, TABLE_NAME, null);
            String query;
            if (resultSet.next()) {
                query = "DELETE FROM " + TABLE_NAME;
            } else {
                query = "CREATE TABLE " + TABLE_NAME + " (id BIGSERIAL, word VARCHAR(255));";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            resultSet.close();
            preparedStatement.close();
            saveWords(connection, FILE_PATH);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void saveWords(Connection connection, String filePath) throws SQLException, IOException {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO "
                             + TABLE_NAME + " (word) VALUES (?)");
             BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                preparedStatement.setString(1, line);
                preparedStatement.executeUpdate();
            }
        }
    }


}
