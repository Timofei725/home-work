package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder {
    private DataSource dataSource;

    @Autowired
    public SQLQueryBuilderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String queryForTable(String tableName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, null);
            if (resultSet.next()) {
                StringBuilder resultQuery = new StringBuilder();
                resultQuery.append("SELECT ");
                resultQuery.append(resultSet.getString("COLUMN_NAME"));
                while (resultSet.next()) {
                    resultQuery.append(", ");
                    resultQuery.append(resultSet.getString("COLUMN_NAME"));
                }
                resultQuery.append(" FROM ");
                resultQuery.append(tableName);
                resultSet.close();
                return resultQuery.toString();
            }
        } catch (SQLException e) {
            // if table doesn't exist
            if ("42P01".equals(e.getSQLState())) {
                return null;
            }
            throw e;
        }
        return null;
    }


    @Override
    public List<String> getTables() throws SQLException {
        List<String> tableNames = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String[] types = {"TABLE", "SYSTEM TABLE", "VIEW"};
            ResultSet tables = metaData.getTables(null, null, "%", types);
            while (tables.next()) {
                tableNames.add(tables.getString("TABLE_NAME"));
            }
            tables.close();
        }
        return tableNames;
    }

}
