package com.ust.tripStack.containers;

import com.ust.tripStack.support.TestEnvironment;
import org.flywaydb.core.Flyway;
import org.testcontainers.containers.MySQLContainer;

public final class MySQLTestContainer {

    private static final MySQLContainer<?> CONTAINER;
    private static final String JDBC_URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        boolean useTestContainers =
                Boolean.parseBoolean(TestEnvironment.required("TEST_USE_TESTCONTAINERS"));

        if (useTestContainers) {

            CONTAINER = new MySQLContainer<>("mysql:8.0")
                    .withDatabaseName("tripstack")
                    .withUsername("root")
                    .withPassword(TestEnvironment.required("DB_PASSWORD"));

            CONTAINER.start();

            JDBC_URL = CONTAINER.getJdbcUrl();
            USERNAME = CONTAINER.getUsername();
            PASSWORD = CONTAINER.getPassword();

            Flyway.configure()
                    .dataSource(JDBC_URL, USERNAME, PASSWORD)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .load()
                    .migrate();

        } else {

            CONTAINER = null;

            JDBC_URL = TestEnvironment.required("DB_JDBC_URL");
            USERNAME = TestEnvironment.required("DB_USER");
            PASSWORD = TestEnvironment.required("DB_PASSWORD");

            Flyway.configure()
                    .dataSource(JDBC_URL, USERNAME, PASSWORD)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .load()
                    .migrate();
        }
    }

    private MySQLTestContainer() {
    }

    /** Only non-null when TEST_USE_TESTCONTAINERS=true. */
    public static MySQLContainer<?> getInstance() {
        return CONTAINER;
    }

    public static String jdbcUrl() {
        return JDBC_URL;
    }

    public static String username() {
        return USERNAME;
    }

    public static String password() {
        return PASSWORD;
    }
}