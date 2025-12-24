/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Acer
 */
public class Food {

    private int foodID;
    private String foodName;
    private int categoryID;
    private double price;
    private String imageURL;
    private int status;

    public Food(int foodID, String foodName, int categoryID,
                double price, String imageURL, int status) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.categoryID = categoryID;
        this.price = price;
        this.imageURL = imageURL;
        this.status = status;
    }

    public int getFoodID() { return foodID; }
    public String getFoodName() { return foodName; }
    public int getCategoryID() { return categoryID; }
    public double getPrice() { return price; }
    public String getImageURL() { return imageURL; }
    public int getStatus() { return status; }
}