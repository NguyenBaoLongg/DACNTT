/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import model.Food;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Acer
 */
public class FoodDAO {

    public List<Food> getAllFood() {
        List<Food> list = new ArrayList<>();
        // JOIN bảng Foods với FoodCategories
        String sql = "SELECT f.FoodID, f.FoodName, f.Price, f.ImageURL, f.Status, f.SoldQuantity, f.CategoryID, c.CategoryName " +
                     "FROM Foods f " +
                     "LEFT JOIN FoodCategories c ON f.CategoryID = c.CategoryID " +
                     "ORDER BY f.FoodID DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Food f = new Food();
                f.setFoodID(rs.getInt("FoodID"));
                f.setFoodName(rs.getString("FoodName"));
                f.setPrice(rs.getDouble("Price"));
                f.setImageURL(rs.getString("ImageURL"));
                f.setStatus(rs.getInt("Status"));
                f.setSoldQuantity(rs.getInt("SoldQuantity"));
                f.setCategoryID(rs.getInt("CategoryID"));
                f.setCategory(rs.getString("CategoryName")); // Tên danh mục lấy từ bảng FoodCategories
                
                list.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addFood(Food f) {
        // Không cần insert SoldQuantity vì mặc định là 0
        String sql = "INSERT INTO Foods (FoodName, CategoryID, Price, ImageURL, Status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, f.getFoodName());
            ps.setInt(2, f.getCategoryID());
            ps.setDouble(3, f.getPrice());
            ps.setString(4, f.getImageURL());
            ps.setInt(5, f.getStatus());

            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateFood(Food f) {
        String sql = "UPDATE Foods SET FoodName=?, CategoryID=?, Price=?, ImageURL=?, Status=? WHERE FoodID=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, f.getFoodName());
            ps.setInt(2, f.getCategoryID());
            ps.setDouble(3, f.getPrice());
            ps.setString(4, f.getImageURL());
            ps.setInt(5, f.getStatus());
            ps.setInt(6, f.getFoodID());

            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteFood(int foodID) {
        String sql = "DELETE FROM Foods WHERE FoodID=?";
        try (Connection conn = util.DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, foodID);
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateSoldQuantity(int foodID, int quantitySold) {
        String sql = "UPDATE Foods SET SoldQuantity = SoldQuantity + ? WHERE FoodID = ?";
        try (Connection conn = util.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantitySold); 
            ps.setInt(2, foodID);      
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}