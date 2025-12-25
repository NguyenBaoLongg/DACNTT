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
        Loginform lg = new Loginform();
        lg.setSize(1450, 770);       
        lg.setLocationRelativeTo(null); 
        lg.setResizable(false);       
        lg.setVisible(true);
    });
    }
}