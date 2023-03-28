package io.ylab.intensive.lesson04.filesort;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import javax.sql.DataSource;


import io.ylab.intensive.lesson04.DbUtil;

public class FileSorterTest {
    public static void main(String[] args) throws SQLException, IOException {
        DataSource dataSource = initDb();
        File dataFile = new File("src/main/java/io/ylab/intensive/lesson04/filesort", "data.txt");
        FileSorter fileSorter = new FileSortImpl(dataSource);
        System.out.println("Time before sorting: " + new Date());
        File res = fileSorter.sort(dataFile);
        System.out.println("Time after sorting: " + new Date());

    }

    public static DataSource initDb() throws SQLException {
        String createSortTable = ""
                + "drop table if exists numbers;"
                + "CREATE TABLE if not exists numbers (\n"
                + "\tval bigint\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createSortTable, dataSource);
        return dataSource;
    }
}
