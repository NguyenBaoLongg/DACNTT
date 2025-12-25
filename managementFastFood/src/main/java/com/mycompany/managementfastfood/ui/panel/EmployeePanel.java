/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.managementfastfood.ui.panel;

import DAO.AccountDAO;
import ViewPanel.OnPersonActionListener;
import ViewPanel.PersonItem;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.Account;

/**
 *
 * @author Acer
 */
public class EmployeePanel extends javax.swing.JPanel {

    private AccountDAO accountDAO = new AccountDAO();
    private Account currentAccount = null; // To store the account currently being edited
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public EmployeePanel() {
        initComponents();
        initCustomLayout();
        initFormComponents();
        
        loadDataFromDB();
        addEventHandlers();
    }

    private void initCustomLayout() {
        // Layout for the scrollable list
        ListPersontItemChild.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 15));
        ListPersontItem.getVerticalScrollBar().setUnitIncrement(16);
    }

    private void initFormComponents() {
        // Initialize ComboBoxes
        textSex.setModel(new DefaultComboBoxModel<>(new String[]{"Nam", "Nữ"}));
        
        // Hardcoded roles for now based on your DB (1: Manager, 2: Employee)
        // Ideally, load this from Roles table via a RoleDAO
        role.setModel(new DefaultComboBoxModel<>(new String[]{"Quản lý", "Nhân viên"}));
        
        // Hide Delete button initially
        PnlDelFood.setVisible(false);
    }

    // --- LOAD DATA ---
    private void loadDataFromDB() {
        ListPersontItemChild.removeAll();
        List<Account> list = accountDAO.getAllAccounts();
        
        // Update count label
        jLabel1.setText("Quản lý " + list.size() + " nhân viên");

        for (Account acc : list) {
            // Create PersonItem and attach listener
            PersonItem item = new PersonItem(acc, new OnPersonActionListener() {
                @Override
                public void onEditPerson(Account account) {
                    fillDataToRightPanel(account);
                }
            });
            ListPersontItemChild.add(item);
        }

        ListPersontItemChild.revalidate();
        ListPersontItemChild.repaint();
    }

    // --- FILL DATA TO FORM ---
    private void fillDataToRightPanel(Account acc) {
        this.currentAccount = acc;
        
        jLabel4.setText("Sửa nhân viên");
        btnAccepAdd.setText("Cập nhật");
        UserName1.setText(acc.getUserName());
        UserName1.setEditable(false); // Username cannot be changed
        
        UserName5.setText(""); // Password field blank on edit (unless changing)
        fullname.setText(acc.getFullName());
        birth.setText(acc.getBirthDate() != null ? dateFormat.format(acc.getBirthDate()) : "");
        numberPhone.setText(acc.getPhoneNumber());
        
        // Select Gender
        textSex.setSelectedIndex(acc.getGender() == 1 ? 0 : 1); // 0: Nam, 1: Nữ
        
        // Select Role (Assuming 1: Quản lý, 2: Nhân viên)
        role.setSelectedIndex(acc.getRoleID() == 1 ? 0 : 1);
        
        status.setSelected(acc.getStatus() == 1);
        
        // Show Delete button
        PnlDelFood.setVisible(true);
        
        // Update Image Preview
        updateImagePreview(acc.getGender());
    }

    // --- RESET FORM ---
    private void resetForm() {
        this.currentAccount = null;
        
        jLabel4.setText("Thêm nhân viên");
        btnAccepAdd.setText("Thêm mới");
        
        UserName1.setText("");
        UserName1.setEditable(true);
        UserName5.setText("");
        fullname.setText("");
        birth.setText("");
        numberPhone.setText("");
        
        textSex.setSelectedIndex(0);
        role.setSelectedIndex(1); // Default to Employee
        status.setSelected(true); // Default Active
        
        PnlDelFood.setVisible(false);
        image.setIcon(null);
        image.setText("Ảnh");
    }

    // --- UPDATE IMAGE PREVIEW ---
    private void updateImagePreview(int gender) {
        String iconPath = (gender == 1) ? "/images/male.png" : "/images/female.png";
        try {
            java.net.URL imgUrl = getClass().getResource(iconPath);
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                image.setText("");
                image.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            image.setText("No Image");
        }
    }

    private void addEventHandlers() {
        // 1. Button "Thêm nhân viên" (Top)
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetForm();
            }
        });

        btnAccepAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jPanel7.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        
        MouseAdapter saveEvent = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveData();
            }
        };
        btnAccepAdd.addMouseListener(saveEvent);
        jPanel7.addMouseListener(saveEvent);

        btnDel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        PnlDelFood.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        MouseAdapter delEvent = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deleteData();
            }
        };
        btnDel.addMouseListener(delEvent);
        PnlDelFood.addMouseListener(delEvent);
        
        textSex.addActionListener(e -> {
            int g = textSex.getSelectedIndex() == 0 ? 1 : 0;
            updateImagePreview(g);
        });
        
        search1.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String keyword = search1.getText().trim();
                searchEmployee(keyword);
            }
        });
    }

    
    private void searchEmployee(String keyword) {
        if (keyword.isEmpty()) {
            loadDataFromDB();
            return;
        }

        ListPersontItemChild.removeAll();
        List<Account> list = accountDAO.searchAccounts(keyword);
        jLabel1.setText("Tìm thấy " + list.size() + " kết quả");
        for (Account acc : list) {
            PersonItem item = new PersonItem(acc, new OnPersonActionListener() {
                @Override
                public void onEditPerson(Account account) {
                    fillDataToRightPanel(account);
                }
            });
            ListPersontItemChild.add(item);
        }

        ListPersontItemChild.revalidate();
        ListPersontItemChild.repaint();
    }
    
    // --- SAVE LOGIC ---
    private void saveData() {
        // Validation
        if (UserName1.getText().trim().isEmpty() || fullname.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Username và Họ tên!");
            return;
        }

        Account acc = new Account();
        acc.setUserName(UserName1.getText().trim());
        acc.setFullName(fullname.getText().trim());
        acc.setPhoneNumber(numberPhone.getText().trim());
        
        // Gender: 0 index is Nam(1), 1 index is Nữ(0)
        acc.setGender(textSex.getSelectedIndex() == 0 ? 1 : 0);
        
        // BirthDate
       try {
            java.util.Date utilDate = dateFormat.parse(birth.getText().trim());
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            acc.setBirthDate(sqlDate); 
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ (yyyy-MM-dd)!");
            return;
        }
        
        acc.setStatus(status.isSelected() ? 1 : 0);
        acc.setRoleID(role.getSelectedIndex() == 0 ? 1 : 2);
        String pwd = UserName5.getText().trim();
        boolean success = false;
        if (currentAccount == null) {
            // ADD NEW
            if (pwd.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu cho nhân viên mới!");
                return;
            }
            success = accountDAO.addAccount(acc, pwd);
            if (success) JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
        } else {
            // UPDATE
            success = accountDAO.updateAccount(acc, pwd); 
            if (success) JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        }

        if (success) {
            loadDataFromDB();
            resetForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thao tác thất bại (Có thể Username đã tồn tại)!");
        }
    }

    // --- DELETE LOGIC ---
    private void deleteData() {
        if (currentAccount == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn chắc chắn muốn xóa tài khoản: " + currentAccount.getUserName() + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (accountDAO.deleteAccount(currentAccount.getUserName())) {
                JOptionPane.showMessageDialog(this, "Đã xóa!");
                loadDataFromDB();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        category1 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        search1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JLabel();
        right = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        image = new javax.swing.JLabel();
        status = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        UserName1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        fullname = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        textSex = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        btnAccepAdd = new javax.swing.JLabel();
        PnlDelFood = new javax.swing.JPanel();
        btnDel = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        birth = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        numberPhone = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        role = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        UserName5 = new javax.swing.JTextField();
        ListPersontItem = new javax.swing.JScrollPane();
        ListPersontItemChild = new javax.swing.JPanel();

        setBackground(new java.awt.Color(245, 245, 248));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Danh sách nhân viên");

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Quản lý 48 món ăn và đồ uống");

        category1.setBackground(new java.awt.Color(245, 245, 248));

        jPanel11.setBackground(new java.awt.Color(243, 243, 243));
        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray));
        jPanel11.setPreferredSize(new java.awt.Dimension(738, 720));

        search1.setBackground(new java.awt.Color(243, 243, 243));
        search1.setToolTipText("");
        search1.setBorder(null);

        jLabel8.setBackground(new java.awt.Color(135, 136, 138));
        jLabel8.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        jLabel8.setToolTipText("");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 353, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(search1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(search1, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(237, 128, 50));

        btnAdd.setBackground(new java.awt.Color(237, 128, 50));
        btnAdd.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnAdd.setText("XN Thêm");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 147, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 41, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout category1Layout = new javax.swing.GroupLayout(category1);
        category1.setLayout(category1Layout);
        category1Layout.setHorizontalGroup(
            category1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(category1Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        category1Layout.setVerticalGroup(
            category1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(category1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(category1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        right.setBackground(new java.awt.Color(255, 255, 255));
        right.setPreferredSize(new java.awt.Dimension(358, 635));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 22)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Thông tin nhân viên");

        jLabel9.setBackground(new java.awt.Color(51, 51, 51));
        jLabel9.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Hình ảnh");

        image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        image.setText("Ảnh");

        status.setBackground(new java.awt.Color(245, 245, 248));
        status.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        status.setForeground(new java.awt.Color(0, 0, 0));
        status.setText("Tình trạng");
        status.addActionListener(this::statusActionPerformed);

        jLabel11.setBackground(new java.awt.Color(51, 51, 51));
        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("Tên tài khoản");

        UserName1.setBackground(new java.awt.Color(255, 255, 255));
        UserName1.setToolTipText("");
        UserName1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));

        jLabel12.setBackground(new java.awt.Color(51, 51, 51));
        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("Họ& tên");

        fullname.setBackground(new java.awt.Color(255, 255, 255));
        fullname.setToolTipText("");
        fullname.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));

        jLabel13.setBackground(new java.awt.Color(51, 51, 51));
        jLabel13.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("Giới tính");

        textSex.setBackground(new java.awt.Color(255, 255, 255));
        textSex.setForeground(new java.awt.Color(0, 0, 0));
        textSex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPanel7.setBackground(new java.awt.Color(237, 128, 50));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.setToolTipText("");

        btnAccepAdd.setBackground(new java.awt.Color(237, 128, 50));
        btnAccepAdd.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnAccepAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAccepAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnAccepAdd.setText("XN Thêm");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 153, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnAccepAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnAccepAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
        );

        PnlDelFood.setBackground(new java.awt.Color(255, 0, 51));
        PnlDelFood.setForeground(new java.awt.Color(255, 255, 255));
        PnlDelFood.setToolTipText("");

        btnDel.setBackground(new java.awt.Color(255, 0, 0));
        btnDel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnDel.setForeground(new java.awt.Color(255, 255, 255));
        btnDel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnDel.setText("Xóa NV");

        javax.swing.GroupLayout PnlDelFoodLayout = new javax.swing.GroupLayout(PnlDelFood);
        PnlDelFood.setLayout(PnlDelFoodLayout);
        PnlDelFoodLayout.setHorizontalGroup(
            PnlDelFoodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(PnlDelFoodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PnlDelFoodLayout.createSequentialGroup()
                    .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 33, Short.MAX_VALUE)))
        );
        PnlDelFoodLayout.setVerticalGroup(
            PnlDelFoodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(PnlDelFoodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnDel, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
        );

        jLabel15.setBackground(new java.awt.Color(51, 51, 51));
        jLabel15.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setText("Năm sinh");

        birth.setBackground(new java.awt.Color(255, 255, 255));
        birth.setToolTipText("");
        birth.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));

        jLabel16.setBackground(new java.awt.Color(51, 51, 51));
        jLabel16.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("SĐT");

        numberPhone.setBackground(new java.awt.Color(255, 255, 255));
        numberPhone.setToolTipText("");
        numberPhone.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));

        jLabel17.setBackground(new java.awt.Color(51, 51, 51));
        jLabel17.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setText("Vai trò");

        role.setBackground(new java.awt.Color(255, 255, 255));
        role.setForeground(new java.awt.Color(0, 0, 0));
        role.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel18.setBackground(new java.awt.Color(51, 51, 51));
        jLabel18.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setText("Mật khẩu");

        UserName5.setBackground(new java.awt.Color(255, 255, 255));
        UserName5.setToolTipText("");
        UserName5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout rightLayout = new javax.swing.GroupLayout(right);
        right.setLayout(rightLayout);
        rightLayout.setHorizontalGroup(
            rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PnlDelFood, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(rightLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(textSex, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(rightLayout.createSequentialGroup()
                        .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fullname, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(numberPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(birth, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(role, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 21, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rightLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel9))
                    .addComponent(image, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(81, 81, 81))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49))
                    .addComponent(UserName1)
                    .addGroup(rightLayout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(rightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(UserName5)
                    .addGroup(rightLayout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(rightLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        rightLayout.setVerticalGroup(
            rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(status)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UserName1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UserName5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(rightLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fullname, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(rightLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(birth, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(role, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textSex, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(rightLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(42, 42, 42)))
                .addGap(18, 18, 18)
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PnlDelFood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ListPersontItemChild.setBackground(new java.awt.Color(255, 255, 255));
        ListPersontItemChild.setPreferredSize(new java.awt.Dimension(832, 564));

        javax.swing.GroupLayout ListPersontItemChildLayout = new javax.swing.GroupLayout(ListPersontItemChild);
        ListPersontItemChild.setLayout(ListPersontItemChildLayout);
        ListPersontItemChildLayout.setHorizontalGroup(
            ListPersontItemChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 858, Short.MAX_VALUE)
        );
        ListPersontItemChildLayout.setVerticalGroup(
            ListPersontItemChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 643, Short.MAX_VALUE)
        );

        ListPersontItem.setViewportView(ListPersontItemChild);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(category1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ListPersontItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 861, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(right, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(category1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ListPersontItem, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(right, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ListPersontItem;
    private javax.swing.JPanel ListPersontItemChild;
    private javax.swing.JPanel PnlDelFood;
    private javax.swing.JTextField UserName1;
    private javax.swing.JTextField UserName5;
    private javax.swing.JTextField birth;
    private javax.swing.JLabel btnAccepAdd;
    private javax.swing.JLabel btnAdd;
    private javax.swing.JLabel btnDel;
    private javax.swing.JPanel category1;
    private javax.swing.JTextField fullname;
    private javax.swing.JLabel image;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField numberPhone;
    private javax.swing.JPanel right;
    private javax.swing.JComboBox<String> role;
    private javax.swing.JTextField search1;
    private javax.swing.JCheckBox status;
    private javax.swing.JComboBox<String> textSex;
    // End of variables declaration//GEN-END:variables
}
