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

    // Lấy tất cả món ăn đang bán (Status = 1)
    public List<Food> getAllFood() {
        List<Food> list = new ArrayList<>();
        String sql = "SELECT * FROM Foods WHERE Status = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Food food = new Food(
                    rs.getInt("FoodID"),
                    rs.getString("FoodName"),
                    rs.getInt("CategoryID"),
                    rs.getDouble("Price"),
                    rs.getString("ImageURL"),
                    rs.getInt("Status")
                );
                list.add(food);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}