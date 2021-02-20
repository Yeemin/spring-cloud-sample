package org.example.h2db.starter.server;

import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class JdbcConnectionPoolWrapper {

    private final Map<String, JdbcConnectionPool> map;

    private JdbcConnectionPoolWrapper() {
        map = new ConcurrentHashMap<>();
    }

    public static JdbcConnectionPoolWrapper getInstance() {
        return Singleton.INSTANCE.getWrapper();
    }

    public synchronized DataSource get(String database) {
        if (!this.map.containsKey(database)) {
            String url = "jdbc:h2:tcp://localhost:9092/~/h2/";
            JdbcConnectionPool connectionPool = JdbcConnectionPool.create(url + database, "", "");
            connectionPool.setMaxConnections(4);
            this.map.put(database, connectionPool);
        }
        return this.map.get(database);
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
