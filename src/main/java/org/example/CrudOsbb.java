package org.example;

import org.example.data.ConfigOsbb;
import org.flywaydb.core.Flyway;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;

public class CrudOsbb implements Closeable {
    private Connection connection;
    public void init() {
        String url = ConfigOsbb.getUrl();
        String login = ConfigOsbb.getLogin();
        String password = ConfigOsbb.getPassword();

        Flyway.configure()
                .dataSource(url, login, password)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loading success");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, login, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        public void selectMembersWithNotAutoAllowed() {
            Statement statement = null;
            PreparedStatement preparedStatement = null;
            String query = """
                    select m.fullname, m.email, b.address, a.square, a.number
                    from member as m
                    join apartment as a on m.id = a.member_id
                    left join resident as r on m.resident_id = r.id
                    join building as b on a.building_id = b.id
                    where not r.entercar and r.id is not null
                    group by m.fullname, m.email, b.address, a.square, a.number
                    having count(a.id) < ?;
                    """;
            try {preparedStatement = connection.prepareStatement(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ResultSet resultSet = null;
            try {
                preparedStatement.setInt(1, 2);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    System.out.println("" + resultSet.getRow() + ". " + resultSet.getString("fullname")
                            + ", " + resultSet.getString("email")
                            + ", " + resultSet.getString("address")
                            + ", " + resultSet.getFloat("square")
                            + ", " + resultSet.getInt("number"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
