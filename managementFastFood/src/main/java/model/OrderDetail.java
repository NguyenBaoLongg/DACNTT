/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Acer
 */
public class OrderDetail {
    private Food food;       
    private String foodName; 
    private int quantity;
    private double price;    
    private String note;

    public OrderDetail(Food food, int quantity) {
        this.food = food;
        this.quantity = quantity;
        this.note = "";
        if (food != null) {
            this.foodName = food.getFoodName();
            this.price = food.getPrice();
        }
    }

    public OrderDetail(String foodName, int quantity, double price) {
        this.foodName = foodName;
        this.quantity = quantity;
        this.price = price;
        this.food = null; 
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
        if (food != null) {
            this.foodName = food.getFoodName();
            this.price = food.getPrice();
        }
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getTotalPrice() {
        return this.price * this.quantity;
    }
}