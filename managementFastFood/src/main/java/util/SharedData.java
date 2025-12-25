/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import model.Account;

/**
 *
 * @author Acer
 */
public class SharedData {
    private static Account currentUser;

    public static void setUser(Account user) {
        currentUser = user;
    }

    public static Account getUser() {
        return currentUser;
    }
    
    public static void logout() {
        currentUser = null;
    }
}