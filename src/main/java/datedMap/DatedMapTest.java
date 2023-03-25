package src.datedMap;

public class DatedMapTest {
    public static void main(String[] args) throws InterruptedException {
        DatedMap datedMap = new DatedMapImpl();
        datedMap.put("Hello", "Ylab");
        System.out.println("Key \"Hello\" has been added at " + datedMap.getKeyLastInsertionDate("Hello"));
        Thread.sleep(10000); // for demonstrate
        datedMap.put("Hello", "Ylab");
        System.out.println("Key \"Hello\" has been refreshed at " + datedMap.getKeyLastInsertionDate("Hello"));

    }
}
