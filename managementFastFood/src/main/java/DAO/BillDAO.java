/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import model.OrderDetail;
import util.DBConnection;
/**
 *
 * @author Acer
 */
public class BillDAO {

    public List<Order> getAllBills() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM Orders ORDER BY OrderDate DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Order o = new Order();
                o.setOrderID(rs.getInt("OrderID"));
                o.setOrderType(rs.getInt("OrderType"));
                o.setTotalAmount(rs.getDouble("TotalAmount"));
                o.setStatus(rs.getInt("Status"));
                o.setOrderDate(rs.getTimestamp("OrderDate"));
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> searchBills(String keyword) {
        List<Order> list = new ArrayList<>();
        // Tìm theo ID (ép kiểu ID sang string để so sánh like)
        String sql = "SELECT * FROM Orders WHERE CAST(OrderID AS CHAR) LIKE ? ORDER BY OrderDate DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setOrderID(rs.getInt("OrderID"));
                o.setOrderType(rs.getInt("OrderType"));
                o.setTotalAmount(rs.getDouble("TotalAmount"));
                o.setStatus(rs.getInt("Status"));
                o.setOrderDate(rs.getTimestamp("OrderDate"));
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<OrderDetail> getBillDetails(int orderID) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT f.FoodName, od.Quantity, od.Price " +
                     "FROM OrderDetails od " +
                     "JOIN Foods f ON od.FoodID = f.FoodID " +
                     "WHERE od.OrderID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Constructor: (Tên món, Số lượng, Giá bán)
                list.add(new OrderDetail(
                    rs.getString("FoodName"),
                    rs.getInt("Quantity"),
                    rs.getDouble("Price")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
