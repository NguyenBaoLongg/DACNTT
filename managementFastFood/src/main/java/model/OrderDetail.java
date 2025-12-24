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
    private int foodID;
    private String name;
    private double price;
    private int quantity;
    private String note;

    public OrderDetail(int foodID, String name, double price) {
        this.foodID = foodID;
        this.name = name;
        this.price = price;
        this.quantity = 1;
        this.note = "";
    }

    // getter và setter
    public int getFoodID() { return foodID; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}