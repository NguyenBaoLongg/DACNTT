/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.DBConnection;
/**
 *
 * @author Acer
 */
public class StatisticalDAO {
    public double getRevenue(Date from, Date to) {
        double revenue = 0;
        String sql = "SELECT SUM(TotalAmount) FROM Orders WHERE OrderDate BETWEEN ? AND ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, new java.sql.Timestamp(from.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(to.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                revenue = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenue;
    }

    // 2. ĐẾM TỔNG SỐ ĐƠN HÀNG
    public int getTotalOrders(Date from, Date to) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Orders WHERE OrderDate BETWEEN ? AND ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, new java.sql.Timestamp(from.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(to.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // 3. ĐẾM LOẠI ĐƠN HÀNG (0: Tại chỗ, 1: Mang về)
    public int getOrderTypeCount(int type, Date from, Date to) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Orders WHERE OrderType = ? AND OrderDate BETWEEN ? AND ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, type);
            ps.setTimestamp(2, new java.sql.Timestamp(from.getTime()));
            ps.setTimestamp(3, new java.sql.Timestamp(to.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // Trả về mảng Object[] gồm {Tên món, Số lượng, Doanh thu}
    public List<Object[]> getTopSellingProducts(Date from, Date to) {
        List<Object[]> list = new ArrayList<>();
        
        String sql = "SELECT f.FoodName, f.ImageURL, SUM(od.Quantity) as Qty, SUM(od.Price * od.Quantity) as Total " +
                     "FROM OrderDetails od " +
                     "JOIN Foods f ON od.FoodID = f.FoodID " +
                     "JOIN Orders o ON od.OrderID = o.OrderID " +
                     "WHERE o.OrderDate BETWEEN ? AND ? " +
                     "GROUP BY f.FoodName, f.ImageURL " + 
                     "ORDER BY Qty DESC LIMIT 4";
        
        try (Connection con = util.DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setTimestamp(1, new java.sql.Timestamp(from.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(to.getTime()));
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("FoodName"),
                    rs.getString("ImageURL"), 
                    rs.getInt("Qty"),
                    rs.getDouble("Total")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Double> getMonthlyRevenue(int year) {
        List<Double> list = new ArrayList<>();
        // Khởi tạo 12 tháng bằng 0
        for (int i = 0; i < 12; i++) list.add(0.0);

        String sql = "SELECT MONTH(OrderDate) as Month, SUM(TotalAmount) as Total " +
                     "FROM Orders WHERE YEAR(OrderDate) = ? " +
                     "GROUP BY MONTH(OrderDate)";
        
        try (java.sql.Connection con = util.DBConnection.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, year);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int month = rs.getInt("Month");
                double total = rs.getDouble("Total");
                if (month >= 1 && month <= 12) {
                    list.set(month - 1, total);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
