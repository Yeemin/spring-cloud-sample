package org.example.h2db.starter.server;

import cn.hutool.db.Db;
import org.h2.tools.Server;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
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
            DataSource dataSource = JdbcConnectionPoolWrapper.getInstance().get();
            properties.entrySet().parallelStream().forEach(entry -> {
                String k = (String) entry.getKey();
                String v = (String) entry.getValue();
                String dropAliasSQL = "DROP ALIAS IF EXISTS " + k;
                try {
                    Db.use(dataSource).execute(dropAliasSQL);
                } catch (SQLException ignored) {
                }
                if (!StringUtils.isNullOrEmpty(v)) {
                    String createAliasSQL = "CREATE ALIAS " + k + " FOR \"" + v + "\"";
                    try {
                        Db.use(dataSource).execute(createAliasSQL);
                    } catch (SQLException ignored) {
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
