/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import util.DBConnection;

/**
 *
 * @author Acer
 */
public class OrderDAO {

    public int insertOrder(int orderType, double totalAmount) {
        int orderId = -1;
        
        // Câu lệnh SQL
        String sql = "INSERT INTO Orders (OrderType, TotalAmount, Status, OrderDate) VALUES (?, ?, 1, NOW())";
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            
            if (con == null) {
                System.out.println("Không thể kết nối đến Database!");
                return -1;
            }
            
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, orderType);      // 0: Tại chỗ, 1: Mang về
            ps.setDouble(2, totalAmount); 
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. Đóng resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return orderId;
    }
}