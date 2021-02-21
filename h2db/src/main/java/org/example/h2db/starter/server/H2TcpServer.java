package org.example.h2db.starter.server;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.example.h2db.starter.alias.AliasUtil;
import org.h2.tools.Server;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class H2TcpServer implements InitializingBean {

    private static final Log log = LogFactory.get();

    public H2TcpServer() throws SQLException {
        Server server = Server.createTcpServer("-ifNotExists");
        server.start();
    }

    @Override
    public void afterPropertiesSet() {
        log.info("CREATE ALIAS");
        log.info(AliasUtil.createAlias("test"));
    }
}
