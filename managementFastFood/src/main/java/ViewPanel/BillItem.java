/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ViewPanel;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import model.Order;

/**
 *
 * @author Acer
 */
public class BillItem extends javax.swing.JPanel {


    /**
     * Creates new form BillItem
     */
    public BillItem() {
        initComponents();
    }
    
    public BillItem(Order order) {
        initComponents();
        setBillData(order);
    }

    public void setBillData(Order order) {
        if (order == null) return;

        // 1. Hiển thị ID Hóa đơn
        idBill.setText("#" + order.getOrderID());

        if (order.getOrderDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            timeBill.setText(sdf.format(order.getOrderDate()));
        } else {
            timeBill.setText("---");
        }

        // 3. Hiển thị Tổng tiền
        DecimalFormat df = new DecimalFormat("#,### đ");
        priceBill.setText(df.format(order.getTotalAmount()));

        if (order.getOrderType() == 0) {
            statusBill.setText("Ăn đây");  
            statusBill.setForeground(new Color(0, 102, 204)); 
        } else {
            statusBill.setText("Mang về"); 
            statusBill.setForeground(new Color(0, 153, 51));
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        idBill = new javax.swing.JLabel();
        timeBill = new javax.swing.JLabel();
        statusBill = new javax.swing.JLabel();
        priceBill = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        idBill.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        idBill.setForeground(new java.awt.Color(237, 128, 50));
        idBill.setText("jLabel1");

        timeBill.setText("Time");

        statusBill.setBackground(new java.awt.Color(0, 0, 0));
        statusBill.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        statusBill.setForeground(new java.awt.Color(0, 0, 0));
        statusBill.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusBill.setText("status");

        priceBill.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        priceBill.setForeground(new java.awt.Color(237, 128, 50));
        priceBill.setText("Price");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusBill, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idBill, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timeBill, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(priceBill, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idBill, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeBill, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(statusBill, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 8, Short.MAX_VALUE))
                    .addComponent(priceBill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel idBill;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel priceBill;
    private javax.swing.JLabel statusBill;
    private javax.swing.JLabel timeBill;
    // End of variables declaration//GEN-END:variables
}
