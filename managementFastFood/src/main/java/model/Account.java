/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author Acer
 */
public class Account {
    private String userName;
    private String fullName;
    private String password;
    private String phoneNumber;
    private int gender; // 1: Nam, 0: Nữ
    private Date birthDate;
    private int roleID;
    private String roleName;
    private int status;

    public Account() {
    }

    // Constructor đầy đủ
    public Account(String userName, String fullName, String phoneNumber, int gender, Date birthDate, int status, int roleID, String roleName) {
        this.userName = userName;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.status = status; 
        this.roleID = roleID;
        this.roleName = roleName;
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
    
    @Override
    public String toString() {
        return "Account{" + "userName=" + userName + ", fullName=" + fullName + ", roleID=" + roleID + '}';
    }
    public String getPhoneNumber() { return phoneNumber; }
    public int getGender() { return gender; }
    public String getRoleName() { return roleName; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public Date getBirthDate() { 
        return birthDate; 
    }

    public void setBirthDate(Date birthDate) { 
        this.birthDate = birthDate; 
    }

    public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber; 
}

    public void setGender(int gender) {
    this.gender = gender;
}
}