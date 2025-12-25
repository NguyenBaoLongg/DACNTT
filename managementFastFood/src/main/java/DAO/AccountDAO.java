/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Account;
import util.DBConnection;

/**
 *
 * @author Acer
 */
public class AccountDAO {

    public Account checkLogin(String user, String pass) {
        Account acc = null;
        String sql = "SELECT * FROM Accounts WHERE UserName = ? AND Password = UPPER(SHA2(?, 256))";
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            if (con != null) {
                ps = con.prepareStatement(sql);
                ps.setString(1, user);
                ps.setString(2, pass); 

                rs = ps.executeQuery();

                if (rs.next()) {
                    acc = new Account();
                    acc.setUserName(rs.getString("UserName"));
                    acc.setFullName(rs.getString("FullName"));
                    acc.setPassword(rs.getString("Password"));
                    acc.setRoleID(rs.getInt("RoleID"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return acc;
    }
}
