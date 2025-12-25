/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.managementfastfood.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import util.SharedData;
/**
 *
 * @author Acer
 */
public class Loginform extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Loginform.class.getName());

    public Loginform() {
        initComponents();
        this.setLocationRelativeTo(null); // Căn giữa màn hình

        // 1. Cài đặt chữ gợi ý (Placeholder)
        setupPlaceholders();
        
        // 2. Cài đặt sự kiện click nút Đăng nhập
        setupLoginButton();
        
        // 3. Cài đặt sự kiện hiện mật khẩu
        setupShowPassword();
    }
    
    // --- HÀM XỬ LÝ PLACEHOLDER ---
    private void setupPlaceholders() {
        // --- Xử lý cho UserName ---
        UserName1.setForeground(Color.GRAY);
        UserName1.setText("Nhập tên đăng nhập");
        
        UserName1.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (UserName1.getText().equals("Nhập tên đăng nhập")) {
                    UserName1.setText("");
                    UserName1.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (UserName1.getText().isEmpty()) {
                    UserName1.setForeground(Color.GRAY);
                    UserName1.setText("Nhập tên đăng nhập");
                }
            }
        });

        // --- Xử lý cho Password ---
        Password1.setForeground(Color.GRAY);
        Password1.setText("Nhập mật khẩu");
        Password1.setEchoChar((char) 0);

        Password1.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                String pass = String.valueOf(Password1.getPassword());
                if (pass.equals("Nhập mật khẩu")) {
                    Password1.setText("");
                    Password1.setForeground(Color.BLACK);
                    Password1.setEchoChar('*');
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String pass = String.valueOf(Password1.getPassword());
                if (pass.isEmpty()) {
                    Password1.setForeground(Color.GRAY);
                    Password1.setText("Nhập mật khẩu");
                    Password1.setEchoChar((char) 0); // Hiện chữ lại
                }
            }
        });
    }

    // --- HÀM XỬ LÝ SỰ KIỆN NÚT ĐĂNG NHẬP ---
    private void setupLoginButton() {
        // Tạo hiệu ứng bàn tay khi di chuột vào nút
        lbLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jPanel4.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Sự kiện click
        MouseAdapter loginEvent = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleLogin();
            }
        };
        
        // Gán sự kiện cho cả chữ và nền màu cam
        lbLogin.addMouseListener(loginEvent);
        jPanel4.addMouseListener(loginEvent);
    }
    
    private void handleLogin() {
        String user = UserName1.getText();
        String pass = String.valueOf(Password1.getPassword());

        // 1. Kiểm tra rỗng (Validate)
        if (user.equals("Nhập tên đăng nhập") || user.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!", "Thông báo", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (pass.equals("Nhập mật khẩu") || pass.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!", "Thông báo", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        DAO.AccountDAO dao = new DAO.AccountDAO();
        model.Account acc = dao.checkLogin(user, pass);

        if (acc != null) {
            util.SharedData.setUser(acc);
            
            javax.swing.JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            
            dashboard mainFrame = new dashboard(); 
            mainFrame.setSize(1450, 770);       
            mainFrame.setLocationRelativeTo(null); 
            mainFrame.setResizable(false);       
            mainFrame.setVisible(true); 
            this.dispose(); 
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupShowPassword() {
        showPass1.addActionListener(e -> {
            if (showPass1.isSelected()) {
                Password1.setEchoChar((char) 0);
            } else {
                String pass = String.valueOf(Password1.getPassword());
                if (!pass.equals("Nhập mật khẩu")) {
                    Password1.setEchoChar('*');
                }
            }
        });
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        UserName1 = new javax.swing.JTextField();
        showPass1 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        lbLogin = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        Password1 = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        Image = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1920, 1080));
        setSize(new java.awt.Dimension(1920, 1080));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setForeground(new java.awt.Color(60, 63, 65));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Đăng nhập");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Mật khẩu");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Tên đăng nhập");

        UserName1.setBackground(new java.awt.Color(255, 255, 255));
        UserName1.setToolTipText("");
        UserName1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));

        showPass1.setBackground(new java.awt.Color(255, 255, 255));
        showPass1.setForeground(new java.awt.Color(102, 102, 102));
        showPass1.setText("Hiện mật khẩu");

        jPanel4.setBackground(new java.awt.Color(237, 128, 50));

        lbLogin.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lbLogin.setForeground(new java.awt.Color(255, 255, 255));
        lbLogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbLogin.setText("Đăng nhập");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lbLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
        );

        Password1.setBackground(new java.awt.Color(255, 255, 255));
        Password1.setText("jPasswordField1");
        Password1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(170, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(UserName1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(showPass1)))
                            .addComponent(jLabel6)
                            .addComponent(Password1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(82, 82, 82))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(171, 171, 171))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(134, 134, 134))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(UserName1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Password1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(showPass1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo.png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Fast Food");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Quản lý chuyên nghiệp");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(207, 207, 207)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(78, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel3))
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(202, 202, 202))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 0, 680, 740));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));
        jPanel1.setMaximumSize(new java.awt.Dimension(600, 740));
        jPanel1.setMinimumSize(new java.awt.Dimension(600, 740));
        jPanel1.setOpaque(false);

        Image.setBackground(new java.awt.Color(255, 255, 255));
        Image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backgroundLogin1.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Image, javax.swing.GroupLayout.PREFERRED_SIZE, 737, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Image, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 740, 740));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Image;
    private javax.swing.JPasswordField Password1;
    private javax.swing.JTextField UserName1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbLogin;
    private javax.swing.JCheckBox showPass1;
    // End of variables declaration//GEN-END:variables
}
