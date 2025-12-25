/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ViewPanel;

import model.Food;

/**
 *
 * @author Acer
 */
public interface OnOrderListener {
    void onAddFood(Food food, String note);
    
    void onUpdateOrder();
}
