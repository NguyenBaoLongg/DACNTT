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
    private String categoryName; 
    private double price;
    private String imageURL;
    private int status;
    private int soldQuantity; 

    public Food() {
    }

    public Food(int foodID, String foodName, int categoryID, String categoryName, double price, String imageURL, int status, int soldQuantity) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.price = price;
        this.imageURL = imageURL;
        this.status = status;
        this.soldQuantity = soldQuantity;
    }

    public int getFoodID() { return foodID; }
    public String getFoodName() { return foodName; }
    public int getCategoryID() { return categoryID; }
    public double getPrice() { return price; }
    public String getImageURL() { return imageURL; }
    public int getStatus() { return status; }
    public int getSoldQuantity() { return soldQuantity; }
    
    public String getCategory() {
        return categoryName != null ? categoryName : "Unknown";
    }

    public void setFoodID(int foodID) { this.foodID = foodID; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public void setCategoryID(int categoryID) { this.categoryID = categoryID; }
    public void setCategory(String categoryName) { this.categoryName = categoryName; }
    public void setPrice(double price) { this.price = price; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
    public void setStatus(int status) { this.status = status; }
    public void setSoldQuantity(int soldQuantity) { this.soldQuantity = soldQuantity; }
}