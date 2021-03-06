package org.example.h2db.test;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcTest {

    private static final Log log = LogFactory.get();

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/~/h2/test")) {
            // 插入一条数据
            try (PreparedStatement pst = connection.prepareStatement("INSERT INTO TEST(ID, NAME) VALUES (?, ?)")) {
                pst.setString(1, UUID.randomUUID().toString());
                pst.setString(2, "yeemin");
                pst.execute();
                log.info("insert succeed");
            }
            // 查询
            try (PreparedStatement pst = connection.prepareStatement("SELECT ID, NAME FROM TEST WHERE NAME = ?")) {
                pst.setString(1, "yeemin");
                ResultSet resultSet = pst.executeQuery();
                class User {
                    private final String id;
                    private final String name;

                    public User(String id, String name) {
                        this.id = id;
                        this.name = name;
                    }

                    @Override
                    public String toString() {
                        return String.format("id: %s, name: %s", id, name);
                    }
                }
                List<User> userList = new ArrayList<>();
                User user;
                while (resultSet.next()) {
                    user = new User(resultSet.getString("ID"), resultSet.getString("NAME"));
                    userList.add(user);
                }
                userList.forEach(e -> log.info(e.toString()));
                resultSet.close();
            }

            // 删除
            try (PreparedStatement pst = connection.prepareStatement("DELETE FROM TEST WHERE NAME = ?")) {
                pst.setString(1, "yeemin");
                pst.execute();
                log.info("delete succeed");
            }
        }
    }

}
