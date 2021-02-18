package org.example.h2db.starter.server;

import org.h2.tools.Server;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

@Service
public class H2TcpServer implements InitializingBean {

    @PostConstruct
    public void startServer() throws SQLException {
        Server server = Server.createTcpServer("-ifNotExists");
        server.start();
    }

    @Override
    public void afterPropertiesSet() {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("alias.factories");
            if (properties.isEmpty()) {
                return;
            }
            String driverName = "org.h2.Driver";
            Class.forName(driverName);
            String url = "jdbc:h2:tcp://localhost:9092/~/h2/test";
            try (Connection connection = DriverManager.getConnection(url)) {
                properties.forEach((k, v) -> {
                    String dropAliasSQL = "DROP ALIAS IF EXISTS " + k;
                    try (PreparedStatement pst = connection.prepareStatement(dropAliasSQL)) {
                        pst.execute();
                    } catch (SQLException ignored) {
                    }
                    String createAliasSQL = "CREATE ALIAS " + k + " FOR \"" + v + "\"";
                    try (PreparedStatement pst = connection.prepareStatement(createAliasSQL)) {
                        pst.execute();
                    } catch (SQLException ignored) {
                    }
                });
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
