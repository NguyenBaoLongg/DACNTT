/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.managementfastfood;
import util.DBConnection;
import com.mycompany.managementfastfood.ui.Loginform;
import com.mycompany.managementfastfood.ui.dashboard;
import java.sql.Connection;
import util.DBConnection;
import javax.swing.JFrame;

/**
 *
 * @author Acer
 */
public class ManagementFastFood {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
        dashboard db = new dashboard();
        db.setSize(1450, 770);       
        db.setLocationRelativeTo(null); 
        db.setResizable(false);       
        db.setVisible(true);
    });
    }
}