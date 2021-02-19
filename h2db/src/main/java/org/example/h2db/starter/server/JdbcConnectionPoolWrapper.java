package org.example.h2db.starter.server;

import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;

public final class JdbcConnectionPoolWrapper {

    private final JdbcConnectionPool dataSource;

    private JdbcConnectionPoolWrapper() {
        this.dataSource = JdbcConnectionPool.create("jdbc:h2:tcp://localhost:9092/~/h2/test", "", "");
        this.dataSource.setMaxConnections(8);
    }

    public static JdbcConnectionPoolWrapper getInstance() {
        return Singleton.INSTANCE.getWrapper();
    }

    public DataSource get() {
        return dataSource;
    }

    enum Singleton {

        INSTANCE;

        final JdbcConnectionPoolWrapper wrapper;

        Singleton() {
            this.wrapper = new JdbcConnectionPoolWrapper();
        }

        public JdbcConnectionPoolWrapper getWrapper() {
            return wrapper;
        }
    }

}
