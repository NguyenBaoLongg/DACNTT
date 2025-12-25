/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import util.DBConnection;

/**
 *
 * @author Acer
 */
public class AccountDAO {

    public Account checkLogin(String user, String pass) {
        Account acc = null;
        String hashedPass = hashPassword(pass);

        String sql = "SELECT a.*, r.RoleName FROM Accounts a " +
                     "JOIN Roles r ON a.RoleID = r.RoleID " +
                     "WHERE a.UserName = ? AND a.Password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = (con != null) ? con.prepareStatement(sql) : null) {
            
            if (ps != null) {
                ps.setString(1, user);
                ps.setString(2, hashedPass); 

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        acc = new Account(
                            rs.getString("UserName"),
                            rs.getString("FullName"),
                            rs.getString("PhoneNumber"),
                            rs.getInt("Gender"),
                            rs.getDate("BirthDate"), 
                            rs.getInt("Status"),
                            rs.getInt("RoleID"),
                            rs.getString("RoleName")
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acc;
    }
    
    // 1. GET ALL ACCOUNTS
    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT a.*, r.RoleName FROM Accounts a " +
                     "JOIN Roles r ON a.RoleID = r.RoleID " +
                     "ORDER BY a.FullName ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Account acc = new Account();
                acc.setUserName(rs.getString("UserName")); 
                Account a = new Account(
                    rs.getString("UserName"),
                    rs.getString("FullName"),
                    rs.getString("PhoneNumber"), // Phone
                    rs.getInt("Gender"),
                    rs.getDate("BirthDate"),
                    rs.getInt("Status"),
                    rs.getInt("RoleID"),
                    rs.getString("RoleName")
                );
                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. ADD ACCOUNT
    public boolean addAccount(Account acc, String password) {
        String sql = "INSERT INTO Accounts (UserName, FullName, Password, PhoneNumber, Gender, BirthDate, RoleID, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, acc.getUserName());
            ps.setString(2, acc.getFullName());
            ps.setString(3, hashPassword(password)); 
            ps.setString(4, acc.getPhoneNumber());
            ps.setInt(5, acc.getGender());
            if (acc.getBirthDate() != null) {
                ps.setDate(6, new java.sql.Date(acc.getBirthDate().getTime()));
            } else {
                ps.setDate(6, null);
            }
            ps.setInt(7, acc.getRoleID());
            ps.setInt(8, acc.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAccount(Account acc, String newPassword) {
        String sql = "UPDATE Accounts SET FullName=?, PhoneNumber=?, Gender=?, BirthDate=?, RoleID=?, Status=?";
        
        // Nếu có nhập mật khẩu mới thì thêm vào câu lệnh update
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            sql += ", Password=?";
        }
        sql += " WHERE UserName=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, acc.getFullName());
            ps.setString(2, acc.getPhoneNumber());
            ps.setInt(3, acc.getGender());
            
            if (acc.getBirthDate() != null) {
                ps.setDate(4, new java.sql.Date(acc.getBirthDate().getTime()));
            } else {
                ps.setDate(4, null);
            }
            
            ps.setInt(5, acc.getRoleID());
            ps.setInt(6, acc.getStatus());
            
            int paramIndex = 7;
            
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                ps.setString(paramIndex++, hashPassword(newPassword));
            }
            ps.setString(paramIndex, acc.getUserName());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DELETE ACCOUNT
    public boolean deleteAccount(String username) {
        String sql = "DELETE FROM Accounts WHERE UserName=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private String hashPassword(String plainPassword) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plainPassword.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return plainPassword;
        }
    }
    
    public List<Account> searchAccounts(String keyword) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT a.*, r.RoleName FROM Accounts a " +
                     "JOIN Roles r ON a.RoleID = r.RoleID " +
                     "WHERE a.FullName LIKE ? OR a.UserName LIKE ? " +
                     "ORDER BY a.FullName ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Account a = new Account(
                        rs.getString("UserName"),
                        rs.getString("FullName"),
                        rs.getString("PhoneNumber"),
                        rs.getInt("Gender"),
                        rs.getDate("BirthDate"),
                        rs.getInt("Status"),
                        rs.getInt("RoleID"),
                        rs.getString("RoleName")
                    );
                    list.add(a);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
}
