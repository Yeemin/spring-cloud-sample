package org.example.h2db.test;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.sql.Condition;
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchTest {

    public static void main(String[] args) throws SQLException, InterruptedException {
        // 使用连接池
        String url = "jdbc:h2:tcp://localhost:8000/~/h2/test";
        JdbcConnectionPool connectionPool = JdbcConnectionPool.create(url, "", "");

//        Db.use(connectionPool).execute("delete from test");

        long l = Db.use(connectionPool).count(Entity.create("test"));
        // 插入数据
        AtomicInteger count = new AtomicInteger((int) l);
        int size = 1_000_000;
        CountDownLatch countDownLatch = new CountDownLatch(size);
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            CompletableFuture.runAsync(() -> {
                try {
                    Db.use(connectionPool).insert(
                            Entity.create("test")
                                    .set("id", UUID.randomUUID().toString())
                                    .set("name", "hutool" + count.incrementAndGet())
                    );
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        System.out.printf("insert elapse: %s%n", (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        List<Entity> list = Db.use(connectionPool).findLike("test", "name", "hutool2", Condition.LikeType.Contains);
        System.out.printf("query elapse: %s%n", (System.currentTimeMillis() - start));
        System.out.printf("query size: %s%n", list.size());

//        Db.use(connectionPool).execute("delete from test");
    }

}
