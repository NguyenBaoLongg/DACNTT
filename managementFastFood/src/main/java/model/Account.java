/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Acer
 */
public class Account {
    private String userName;
    private String fullName;
    private String password;
    private int roleID;

    // Constructor không tham số
    public Account() {
    }

    // Constructor đầy đủ
    public Account(String userName, String fullName, String password, int roleID) {
        this.userName = userName;
        this.fullName = fullName;
        this.password = password;
        this.roleID = roleID;
    }

    // --- Getters & Setters ---
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
    
    // Hàm toString để in ra kiểm tra nếu cần
    @Override
    public String toString() {
        return "Account{" + "userName=" + userName + ", fullName=" + fullName + ", roleID=" + roleID + '}';
    }
}