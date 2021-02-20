package org.example.h2db.test;

import cn.hutool.cache.Cache;
import cn.hutool.cache.impl.AbstractCache;
import cn.hutool.db.Db;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.sql.SQLException;

public class CacheTest {

    private static final Log log = LogFactory.get();

    public static void main(String[] args) {
        String url = "jdbc:h2:tcp://localhost:9092/~/h2/test";
        JdbcConnectionPool connectionPool = JdbcConnectionPool.create(url, "", "");
        connectionPool.setMaxConnections(8);
        Cache<String, String> cache = new H2DbCache(connectionPool, "h2db");
        cache.put("name", "yeemin");
        System.out.println(cache.get("name"));
    }

    static class H2DbCache extends AbstractCache<String, String> {

        private final DataSource dataSource;

        private final String cacheName;

        public H2DbCache(DataSource dataSource, String cacheName) {
            this.dataSource = dataSource;
            this.cacheName = cacheName;
            try {
                if (Db.use(dataSource).query("SELECT HASCACHENAME(?) AS HAS", cacheName)
                        .stream().noneMatch(entity -> "TRUE".equals(entity.getStr("HAS")))) {
                    Db.use(dataSource).query("CALL CREATE_LFUCACHE(?)", cacheName);
                }
            } catch (SQLException throwables) {
                log.error(throwables);
            }
        }

        public H2DbCache(DataSource dataSource, String cacheName, long timeout) {
            this.dataSource = dataSource;
            this.cacheName = cacheName;
            try {
                if (Db.use(dataSource).query("SELECT HASCACHENAME(?) AS HAS", cacheName)
                        .stream().noneMatch(entity -> "TRUE".equals(entity.getStr("HAS")))) {
                    Db.use(dataSource).query("CALL CREATE_TIMEDCACHE(?, ?)", cacheName, timeout);
                }
            } catch (SQLException throwables) {
                log.error(throwables);
            }
        }

        public H2DbCache(DataSource dataSource, String cacheName, int capacity) {
            this.dataSource = dataSource;
            this.cacheName = cacheName;
            try {
                if (Db.use(dataSource).query("SELECT HASCACHENAME(?) AS HAS", cacheName)
                        .stream().noneMatch(entity -> "TRUE".equals(entity.getStr("HAS")))) {
                    Db.use(dataSource).query("CALL CREATE_LFUCACHE(?, ?)", cacheName, capacity);
                }
            } catch (SQLException throwables) {
                log.error(throwables);
            }
        }

        public H2DbCache(DataSource dataSource, String cacheName, int capacity, long timeout) {
            this.dataSource = dataSource;
            this.cacheName = cacheName;
            try {
                if (Db.use(dataSource).query("SELECT HASCACHENAME(?) AS HAS", cacheName)
                        .stream().noneMatch(entity -> "TRUE".equals(entity.getStr("HAS")))) {
                    Db.use(dataSource).query("CALL CREATE_LFUCACHE(?, ?, ?)", cacheName, capacity, timeout);
                }
            } catch (SQLException throwables) {
                log.error(throwables);
            }
        }

        @Override
        protected int pruneCache() {
            return 0;
        }

        @Override
        public void put(String key, String object, long timeout) {
            try {
                Db.use(this.dataSource).query("SELECT CACHE(?, ?, ?)", this.cacheName, key, object);
            } catch (SQLException throwables) {
                log.error(throwables);
            }
        }

        @Override
        public String get(String key, boolean isUpdateLastAccess) {
            try {
                return Db.use(this.dataSource).query("SELECT GETCACHE(?, ?) AS RESULT", this.cacheName, key)
                        .stream().map(entity -> entity.getStr("RESULT")).findFirst().orElse(null);
            } catch (SQLException throwables) {
                log.error(throwables);
            }
            return null;
        }

        @Override
        public void remove(String key) {
            try {
                Db.use(this.dataSource).query("SELECT REMOVECACHE(?, ?)", this.cacheName, key);
            } catch (SQLException throwables) {
                log.error(throwables);
            }
        }

        @Override
        public void clear() {
            try {
                Db.use(this.dataSource).query("SELECT CLEARCACHE(?)", this.cacheName);
            } catch (SQLException throwables) {
                log.error(throwables);
            }
        }

    }

}
