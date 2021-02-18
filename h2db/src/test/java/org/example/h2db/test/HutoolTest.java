package org.example.h2db.test;

import cn.hutool.core.map.MapUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class HutoolTest {

    public static void main(String[] args) throws SQLException {
        // 使用连接池
        String url = "jdbc:h2:tcp://localhost:9092/~/h2/test";
        JdbcConnectionPool connectionPool = JdbcConnectionPool.create(url, "", "");
        // 插入数据
        Db.use(connectionPool).insert(
                Entity.create("test")
                        .set("id", UUID.randomUUID().toString())
                        .set("name", "hutool")
        );
        System.out.println("insert succeed");

        // 查询数据
        List<Entity> entityList = Db.use(connectionPool).findAll(Entity.create("test").set("name", "hutool"));
        entityList.forEach(entity ->
                System.out.printf("id: %s, name: %s%n", entity.getStr("ID"), entity.getStr("NAME"))
        );

        // 事务
        Db.use(connectionPool).tx(db -> {
            db.update(Entity.create("test").set("name", "hutool-a"), Entity.create().set("name", "hutool"));
            List<User> list = db.query("select ID, NAME from test where name = @name", User.class, MapUtil.builder("name", "hutool-a").build());
            list.forEach(System.out::println);
            db.execute("update test set name = ? where name = ?", "hutool", "hutool-a");
            List<Entity> entities = db.query("select ID, NAME from test where name = @name", MapUtil.builder("name", "hutool").build());
            entities.stream().map(entity -> entity.toBean(User.class)).forEach(System.out::println);
        });

        // 删除数据
        Db.use(connectionPool).del(
                Entity.create("test")
                        .set("name", "hutool")
        );
        System.out.println("delete succeed");
    }

    static class User {

        private String id;
        private String name;

        public User() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return String.format("id: %s, name: %s", id, name);
        }
    }


}
