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
    // Thay vì lưu rời rạc, ta lưu luôn đối tượng Food
    private Food food; 
    private int quantity;
    private String note; // Ghi chú (ví dụ: Không hành, ít đá...)

    // Constructor nhận vào Food và số lượng ban đầu (thường là 1)
    public OrderDetail(Food food, int quantity) {
        this.food = food;
        this.quantity = quantity;
        this.note = "";
    }

    // --- GETTERS & SETTERS ---

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
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

    // --- HÀM QUAN TRỌNG CẦN THÊM ---
    
    // Hàm tính tổng tiền của dòng này (Giá món x Số lượng)
    public double getTotalPrice() {
        return food.getPrice() * quantity;
    }
}