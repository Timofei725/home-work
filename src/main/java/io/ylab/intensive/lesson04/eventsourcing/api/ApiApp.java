package io.ylab.intensive.lesson04.eventsourcing.api;


import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;

import javax.sql.DataSource;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        String exchangeName = "exc";
        String queueName = "queue";
        ConnectionFactory connectionFactory = initMQ();
        DataSource dataSource = DbUtil.buildDataSource();
        PersonApi personApi = new PersonApiImpl(exchangeName, queueName, connectionFactory, dataSource);
        // code below demonstrate work only when DbApp run first
        long firstPersonId = 1l;
        personApi.savePerson(firstPersonId, "Alexey", "Antonov", "Ivanovich");
        Thread.sleep(500);
        System.out.println("First person added " + personApi.findPerson(firstPersonId));

        personApi.deletePerson(firstPersonId);
        System.out.println("First person deleted");
        Thread.sleep(500);
        System.out.println("First person now: " + personApi.findPerson(firstPersonId));


        personApi.savePerson(firstPersonId, "Alexey", "Antonov", "Ivanovich");
        Thread.sleep(500);
        System.out.println("First person added again " + personApi.findPerson(firstPersonId));

        personApi.savePerson(firstPersonId, "Evgeny", "Solovyov", "Ivanovich");
        Thread.sleep(500);
        System.out.println("First person updated " + personApi.findPerson(firstPersonId));

        System.out.println("Second person added, there are two persons now: ");
        personApi.savePerson(2l, "Andrew", "Chernyshev", "Gennadievich");
        Thread.sleep(500);

        System.out.println(personApi.findAll());


    }


    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }
}
