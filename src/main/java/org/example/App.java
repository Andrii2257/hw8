package org.example;

import org.flywaydb.core.Flyway;
import static org.example.data.ConfigOsbb.*;

public final class App {
    private App() {
    }

    public static void main(final String[] args) {

        Flyway.configure()
                .dataSource(URL, LOGIN, PASSWORD)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        try (OsbbDbService osbb = new OsbbDbService();) {
            osbb.init();
            osbb.selectMembersWithNotAutoAllowed();
        }
    }
}
