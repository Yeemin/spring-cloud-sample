package org.example.h2db.starter;

import org.example.h2db.starter.server.H2TcpServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class H2TcpServerStarter {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(H2TcpServer.class);
    }

}
