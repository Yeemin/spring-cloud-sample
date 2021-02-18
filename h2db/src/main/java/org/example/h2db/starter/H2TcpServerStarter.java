package org.example.h2db.starter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class H2TcpServerStarter {

    public static void main(String[] args) throws SQLException {
        new AnnotationConfigApplicationContext("org.example.h2demo.starter");
    }

}
