package com.secretminc.devhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
public class TestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/")
    public String home() {
        return "DevHub Application is running!";
    }

//    @GetMapping("/db-test")
//    public String dbTest() {
//        String url = "jdbc:mysql://dev-secretm-mysql.cerc2oyyyxv5.us-east-1.rds.amazonaws.com:3306/secretm_db";
//        String username = "admin";
//        String password = "Tkfkdgo1!";
//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            java.sql.Connection connection = java.sql.DriverManager.getConnection(url, username, password);
//            String result = "DB Connection Success! Database: " + connection.getCatalog();
//            connection.close();
//            return result;
//        } catch (Exception e) {
//            return "DB Connection Failed: " + e.getMessage();
//        }
//    }

    @GetMapping("/db-test")
    public String dbTest() {
        try (Connection connection = dataSource.getConnection()) {
            return "DB Connection Success! Database: " + connection.getCatalog();
        } catch (Exception e) {
            return "DB Connection Failed: " + e.getMessage();
        }
    }


}