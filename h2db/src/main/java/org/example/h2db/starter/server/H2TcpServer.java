package org.example.h2db.starter.server;

import org.example.h2db.starter.alias.AliasUtil;
import org.h2.tools.Server;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

@Service
public class H2TcpServer implements InitializingBean {

    @PostConstruct
    public void startServer() throws SQLException {
        Server server = Server.createTcpServer("-ifNotExists");
        server.start();
    }

    @Override
    public void afterPropertiesSet() {
        AliasUtil.createAlias("test");
    }
}
