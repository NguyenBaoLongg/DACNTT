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

public class OrderDAO {

    public int insertOrder(int orderType, double totalAmount) {
        int orderId = -1;
        String sql = "INSERT INTO Orders (OrderType, TotalAmount, Status, OrderDate) VALUES (?, ?, 1, NOW())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = (con != null) ? con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : null) {

            if (con == null || ps == null) return -1;

            ps.setInt(1, orderType);
            ps.setDouble(2, totalAmount);

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1); // Lấy ID hóa đơn vừa tạo
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }

    // 2. TẠO CHI TIẾT HÓA ĐƠN (Hàm mới cần thêm)
    public boolean insertOrderDetail(int orderID, int foodID, int quantity, double price, String note) {
        String sql = "INSERT INTO OrderDetails (OrderID, FoodID, Quantity, Price, Note) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = (con != null) ? con.prepareStatement(sql) : null) {

            if (con == null || ps == null) return false;

            ps.setInt(1, orderID);
            ps.setInt(2, foodID);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.setString(5, note);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}