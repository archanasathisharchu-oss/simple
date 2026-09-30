package com.chat;

import java.sql.Connection;
import java.sql.DriverManager;

public class DButil {

    private static final String URL =
        "jdbc:mysql://db01.dbhost.dev:5051/db_454s6j9ek";

    private static final String USER =
        "user_454s6j9ek";

    private static final String PASSWORD =
        "p454s6j9ek";

    public static Connection getConnection() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(
            URL,
            USER,
            PASSWORD
        );
    }
}