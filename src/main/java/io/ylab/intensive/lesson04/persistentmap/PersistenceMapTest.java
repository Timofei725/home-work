package io.ylab.intensive.lesson04.persistentmap;

import io.ylab.intensive.lesson04.DbUtil;

import java.sql.SQLException;
import javax.sql.DataSource;


public class PersistenceMapTest {
    public static void main(String[] args) {
        try {
            DataSource dataSource = initDb();
            PersistentMap persistentMap = new PersistentMapImpl(dataSource);
            persistentMap.init("HelloMap");

            persistentMap.put("Hello1", "Yalab1");
            persistentMap.put("Hello2", "Yalab2");
            persistentMap.put("Hello3", "Yalab3");

            System.out.println("Value for Hello1: " + persistentMap.get("Hello1"));
            System.out.println("Value for Hello2: " + persistentMap.get("Hello2"));
            System.out.println("Value for Hello3: " + persistentMap.get("Hello3"));

            persistentMap.remove("Hello2");
            System.out.println("Value for Hello2 after removing : " + persistentMap.get("Hello2"));

            System.out.println("Contains Hello1: " + persistentMap.containsKey("Hello1"));
            System.out.println("Contains Hello2: " + persistentMap.containsKey("Hello2"));

            persistentMap.clear();
            System.out.println("Value for Hello1 after clear: " + persistentMap.get("Hello1"));
            System.out.println("Value for Hello3 after clear: " + persistentMap.get("Hello3"));

            System.out.println("Keys in HelloMap" + " : " + persistentMap.getKeys());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public static DataSource initDb() throws SQLException {
        String createMapTable = ""
                + "drop table if exists persistent_map; "
                + "CREATE TABLE if not exists persistent_map (\n"
                + "   map_name varchar,\n"
                + "   KEY varchar,\n"
                + "   value varchar\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createMapTable, dataSource);
        return dataSource;
    }
}
