package org.example.h2db.starter;

import org.h2.tools.Server;

import java.sql.SQLException;

public class H2dbTcpServerStarter {

    public static void main(String[] args) throws SQLException {
        Server server = Server.createTcpServer("-ifNotExists", "-tcpPort", "8000");
        server.start();
    }

}
