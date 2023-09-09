package org.example;

import java.io.Closeable;
import java.sql.*;
import static org.example.data.ConfigOsbb.*;

public final class OsbbDbService implements Closeable {
    private Connection connection;

    public void init() {
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            System.out.println("З'єднання з базою даних встановлено.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectMembersWithNotAutoAllowed() {
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
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, 2);
            ResultSet resultSet = preparedStatement.executeQuery();
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
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("З'єднання з базою даних закрито.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
