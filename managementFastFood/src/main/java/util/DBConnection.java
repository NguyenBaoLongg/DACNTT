package util;


import java.sql.Connection;
import java.sql.DriverManager;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Acer
 */
public class DBConnection {

    private static final String URL =
    "jdbc:mysql://localhost:3306/FastFoodManagement"
  + "?useSSL=false"
  + "&allowPublicKeyRetrieval=true"
  + "&serverTimezone=UTC";

private static final String USER = "root";
private static final String PASSWORD = "baolong1105";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
