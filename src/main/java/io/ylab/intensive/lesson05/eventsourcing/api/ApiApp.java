package io.ylab.intensive.lesson05.eventsourcing.api;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        PersonApi personApi = applicationContext.getBean(PersonApi.class);
        // code below demonstrate work only when table person already exist
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
}
