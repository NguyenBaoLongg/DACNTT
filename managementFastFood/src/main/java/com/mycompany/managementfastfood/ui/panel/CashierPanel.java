/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.managementfastfood.ui.panel;

import DAO.FoodDAO;
import ViewPanel.OnAddFoodListener;
import ViewPanel.OnOrderListener;
import ViewPanel.OrderDetails;
import ViewPanel.orderItem;
import java.util.List;
import javax.swing.BoxLayout;
import model.Food;
import model.OrderDetail;


/**
 *
 * @author Acer
 */
public class CashierPanel extends javax.swing.JPanel implements OnOrderListener {

    private FoodDAO foodDAO = new FoodDAO(); // Khởi tạo DAO
    private int selectedOrderType = -1; // -1: Chưa chọn, 0: Tại chỗ, 1: Mang về
    private java.awt.Color CAT_ACTIVE_BG = new java.awt.Color(237, 128, 50);  
    private java.awt.Color CAT_ACTIVE_TEXT = java.awt.Color.WHITE;             
    private java.awt.Color CAT_INACTIVE_BG = new java.awt.Color(243, 243, 243);
    private java.awt.Color CAT_INACTIVE_TEXT = new java.awt.Color(102, 102, 102);
    
    // Định nghĩa màu sắc
    private java.awt.Color COLOR_ACTIVE = new java.awt.Color(237, 128, 50); 
    private java.awt.Color COLOR_INACTIVE = java.awt.Color.WHITE;            
    private java.awt.Color TEXT_ACTIVE = java.awt.Color.WHITE;
    private java.awt.Color TEXT_INACTIVE = java.awt.Color.BLACK;
    
    private String currentCategory = "Tất cả";

    public CashierPanel() {
        initComponents();
        initCustomLayout();
        
        addOrderTypeEvents(); 
        addPayEvent();
        initCategoryEvents();
        addSearchEvents();
        loadFoodData("Tất cả");
        
        resetCategoryStyles();
        
        pnlAll.setBackground(CAT_ACTIVE_BG);
        lbAll.setForeground(CAT_ACTIVE_TEXT);
    }

    private void initCustomLayout() {
        // Cài đặt layout cho Panel CON nằm trong ScrollPane
        pnlFoodsChild.setLayout(new java.awt.GridLayout(0, 3, 10, 10)); 
        orderDetailContainerChild.setLayout(new BoxLayout(orderDetailContainerChild, BoxLayout.Y_AXIS));
    }

 private void loadFoodData(String categoryName) {
    this.currentCategory = categoryName;

    String keyword = search.getText().trim();
    
    if (keyword.equals("Tìm món...")) {
        keyword = "";
    }

    List<Food> allFoods = foodDAO.getAllFood();
    pnlFoodsChild.removeAll(); 

    for (Food f : allFoods) {
        boolean isCategoryMatch = false;
        boolean isSearchMatch = false;

        if (categoryName.equals("Tất cả")) {
            isCategoryMatch = true;
        } else {
            if (f.getCategory() != null && f.getCategory().equalsIgnoreCase(categoryName)) {
                isCategoryMatch = true;
            }
        }

        if (keyword.isEmpty()) {
            isSearchMatch = true; 
        } else {
            String foodName = f.getFoodName().toLowerCase();
            String input = keyword.toLowerCase();
            if (foodName.contains(input)) {
                isSearchMatch = true;
            }
        }

        if (isCategoryMatch && isSearchMatch) {
            orderItem item = new orderItem(f, this);
            pnlFoodsChild.add(item);
        }
    }
    
    pnlFoodsChild.revalidate();
    pnlFoodsChild.repaint();
}

    @Override
    public void onAddFood(Food food, String note) {
        boolean isExist = false;
        
        for (java.awt.Component comp : orderDetailContainerChild.getComponents()) {
            if (comp instanceof OrderDetails) {
                OrderDetails detailUI = (OrderDetails) comp;
                if (detailUI.getOrderDetail().getFood().getFoodID() == food.getFoodID()) {
                    int currentQty = detailUI.getOrderDetail().getQuantity();
                    detailUI.getOrderDetail().setQuantity(currentQty + 1);
                    
                    detailUI.updateQuantityUI();
                    isExist = true;
                    break;
                }
            }
        }

        if (!isExist) {
            OrderDetail od = new OrderDetail(food, 1);
            
            od.setNote(note); 
            
            OrderDetails newRow = new OrderDetails(od, this);
            orderDetailContainerChild.add(newRow); 
            orderDetailContainerChild.revalidate();
            orderDetailContainerChild.repaint();
        }

        calculateTotal();
    }

    @Override
    public void onUpdateOrder() {
       calculateTotal();
    }

    private void calculateTotal() {
        double total = 0;
        for (java.awt.Component comp : orderDetailContainerChild.getComponents()) {
            if (comp instanceof OrderDetails) {
                OrderDetails detailUI = (OrderDetails) comp;
                if (detailUI.getOrderDetail().getQuantity() > 0) {
                    total += detailUI.getOrderDetail().getTotalPrice();
                }
            }
        }
        jLabel26.setText(String.format("%,.0f đ", total));
    }

    private void addOrderTypeEvents() {
        // Nút "Tại chỗ"
        he.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        he.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectedOrderType = 0; // 0: Ăn tại chỗ
                updateButtonStyles();  
            }
        });

        // Nút "Mang về"
        he1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        he1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectedOrderType = 1; // 1: Mang về
                updateButtonStyles();  
            }
        });
    }

    // Hàm đổi màu nút
    private void updateButtonStyles() {
        if (selectedOrderType == 0) {
            // Chọn Tại chỗ
            he.setBackground(COLOR_ACTIVE);
            btnHere.setForeground(TEXT_ACTIVE);
            
            he1.setBackground(COLOR_INACTIVE);
            btnTakeAway1.setForeground(TEXT_INACTIVE); // Sửa tên biến thành btnTakeAway1
        } else if (selectedOrderType == 1) {
            // Chọn Mang về
            he.setBackground(COLOR_INACTIVE);
            btnHere.setForeground(TEXT_INACTIVE);
            
            he1.setBackground(COLOR_ACTIVE);
            btnTakeAway1.setForeground(TEXT_ACTIVE); // Sửa tên biến thành btnTakeAway1
        }
    }
    
    private void addPayEvent() {
        if (btnPay == null) return;

        btnPay.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPay.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                
                if (selectedOrderType == -1) {
                    javax.swing.JOptionPane.showMessageDialog(null, 
                        "Vui lòng chọn: Ăn tại chỗ hoặc Mang về!", 
                        "Chưa chọn hình thức", 
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return; 
                }
                
                if (orderDetailContainerChild.getComponentCount() == 0) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Giỏ hàng đang trống!");
                    return;
                }

                saveOrderToDB(); 
            }
        });
    }

    private void saveOrderToDB() {
        double totalAmount = 0;
        for (java.awt.Component comp : orderDetailContainerChild.getComponents()) {
            if (comp instanceof OrderDetails) {
                OrderDetails detailUI = (OrderDetails) comp;
                if (detailUI.getOrderDetail().getQuantity() > 0) {
                    totalAmount += detailUI.getOrderDetail().getTotalPrice();
                }
            }
        }
        
        // Gọi DAO
        DAO.OrderDAO orderDAO = new DAO.OrderDAO(); 
        int orderID = orderDAO.insertOrder(selectedOrderType, totalAmount);
        
        if (orderID > 0) {
            javax.swing.JOptionPane.showMessageDialog(null, "Thanh toán thành công! Mã đơn: #" + orderID);
            resetUI(); 
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Lỗi khi lưu hóa đơn!");
        }
    }
    
    private void resetUI() {
        orderDetailContainerChild.removeAll();
        orderDetailContainerChild.repaint();
        
        // Reset tổng tiền về 0
        jLabel26.setText("0 đ");
        
        selectedOrderType = -1; // Reset chọn
        // Reset màu nút về trắng
        he.setBackground(COLOR_INACTIVE);
        btnHere.setForeground(TEXT_INACTIVE);
        he1.setBackground(COLOR_INACTIVE);
        btnTakeAway1.setForeground(TEXT_INACTIVE); // Sửa tên biến thành btnTakeAway1
    }
    
    private void resetCategoryStyles() {
    // 1. Tất cả
    pnlAll.setBackground(CAT_INACTIVE_BG);
    lbAll.setForeground(CAT_INACTIVE_TEXT);
    
    // 2. Gà rán (Chicken)
    PnlChicken.setBackground(CAT_INACTIVE_BG);
    lbChicken.setForeground(CAT_INACTIVE_TEXT);
    
    // 3. Burger
    PnlBurger.setBackground(CAT_INACTIVE_BG);
    lbBurger.setForeground(CAT_INACTIVE_TEXT);
    
    // 4. Pizza
    PnlPizza.setBackground(CAT_INACTIVE_BG);
    lbPizza.setForeground(CAT_INACTIVE_TEXT);
    
    // 5. Món phụ (ExtraFood)
    PnlExtraFood.setBackground(CAT_INACTIVE_BG);
    lbExtraFood.setForeground(CAT_INACTIVE_TEXT);
    
    // 6. Đồ uống (Drink)
    PnlDrink.setBackground(CAT_INACTIVE_BG);
    lbDrink.setForeground(CAT_INACTIVE_TEXT);
}

private void initCategoryEvents() {
    pnlAll.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    pnlAll.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            resetCategoryStyles(); 
            pnlAll.setBackground(CAT_ACTIVE_BG);
            lbAll.setForeground(CAT_ACTIVE_TEXT);
            
            loadFoodData("Tất cả"); 
        }
    });

    // --- 2. SỰ KIỆN NÚT GÀ RÁN (Chicken) ---
    PnlChicken.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    PnlChicken.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            resetCategoryStyles();
            PnlChicken.setBackground(CAT_ACTIVE_BG);
            lbChicken.setForeground(CAT_ACTIVE_TEXT);
            
            loadFoodData("Gà rán"); // Tên phải khớp với trong Database
        }
    });

    // --- 3. SỰ KIỆN NÚT BURGER ---
    PnlBurger.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    PnlBurger.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            resetCategoryStyles();
            PnlBurger.setBackground(CAT_ACTIVE_BG);
            lbBurger.setForeground(CAT_ACTIVE_TEXT);
            
            loadFoodData("Burger");
        }
    });

    // --- 4. SỰ KIỆN NÚT PIZZA ---
    PnlPizza.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    PnlPizza.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            resetCategoryStyles();
            PnlPizza.setBackground(CAT_ACTIVE_BG);
            lbPizza.setForeground(CAT_ACTIVE_TEXT);
            
            loadFoodData("Pizza");
        }
    });

    // --- 5. SỰ KIỆN NÚT MÓN PHỤ (ExtraFood) ---
    java.awt.event.MouseAdapter extraEvent = new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            resetCategoryStyles();
            PnlExtraFood.setBackground(CAT_ACTIVE_BG);
            lbExtraFood.setForeground(CAT_ACTIVE_TEXT);
            loadFoodData("Món phụ"); 
        }
    };
    PnlExtraFood.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    PnlExtraFood.addMouseListener(extraEvent);
    lbExtraFood.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    lbExtraFood.addMouseListener(extraEvent);

    // --- 6. SỰ KIỆN NÚT ĐỒ UỐNG (Drink) ---
    PnlDrink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    PnlDrink.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            resetCategoryStyles();
            PnlDrink.setBackground(CAT_ACTIVE_BG);
            lbDrink.setForeground(CAT_ACTIVE_TEXT);
            
            loadFoodData("Đồ uống"); // Hoặc "Drink" tùy database
        }
    });
}

private void addSearchEvents() {
    search.setText("Tìm món...");
    search.setForeground(java.awt.Color.GRAY);

    search.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (search.getText().equals("Tìm món...")) {
                search.setText("");
                search.setForeground(java.awt.Color.BLACK);
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (search.getText().isEmpty()) {
                search.setText("Tìm món...");
                search.setForeground(java.awt.Color.GRAY);
            }
        }
    });

    search.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            loadFoodData(currentCategory);
        }
    });
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        orderID = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        btnPay = new javax.swing.JLabel();
        he = new javax.swing.JPanel();
        btnHere = new javax.swing.JLabel();
        he1 = new javax.swing.JPanel();
        btnTakeAway1 = new javax.swing.JLabel();
        orderDetailContainer = new javax.swing.JScrollPane();
        orderDetailContainerChild = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        pnlAll = new javax.swing.JPanel();
        lbAll = new javax.swing.JLabel();
        PnlChicken = new javax.swing.JPanel();
        lbChicken = new javax.swing.JLabel();
        PnlDrink = new javax.swing.JPanel();
        lbDrink = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        search = new javax.swing.JTextField();
        PnlBurger = new javax.swing.JPanel();
        lbBurger = new javax.swing.JLabel();
        PnlPizza = new javax.swing.JPanel();
        lbPizza = new javax.swing.JLabel();
        PnlExtraFood = new javax.swing.JPanel();
        lbExtraFood = new javax.swing.JLabel();
        pnlFoods = new javax.swing.JScrollPane();
        pnlFoodsChild = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(245, 245, 248));
        setMaximumSize(new java.awt.Dimension(1200, 720));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(1200, 720));
        setRequestFocusEnabled(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(255, 204, 153));

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Đơn hàng");

        orderID.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        orderID.setForeground(new java.awt.Color(237, 128, 50));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderID)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
            .addComponent(orderID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel18.setBackground(new java.awt.Color(248, 247, 245));

        jLabel25.setFont(new java.awt.Font("Dialog", 0, 15)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("Tổng cộng");

        jSeparator1.setBackground(new java.awt.Color(153, 153, 153));
        jSeparator1.setForeground(new java.awt.Color(153, 153, 153));

        jLabel26.setBackground(new java.awt.Color(0, 0, 0));
        jLabel26.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("0.000 đ");

        jPanel19.setBackground(new java.awt.Color(237, 128, 50));

        btnPay.setFont(new java.awt.Font("Dialog", 1, 22)); // NOI18N
        btnPay.setForeground(new java.awt.Color(255, 255, 255));
        btnPay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnPay.setText("Thanh toán ngay");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnPay, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnPay, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        he.setBackground(new java.awt.Color(255, 255, 255));
        he.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, new java.awt.Color(153, 153, 153), java.awt.Color.lightGray, java.awt.Color.lightGray));

        btnHere.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnHere.setForeground(new java.awt.Color(0, 0, 0));
        btnHere.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnHere.setText("Tại chỗ");

        javax.swing.GroupLayout heLayout = new javax.swing.GroupLayout(he);
        he.setLayout(heLayout);
        heLayout.setHorizontalGroup(
            heLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(heLayout.createSequentialGroup()
                .addComponent(btnHere, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        heLayout.setVerticalGroup(
            heLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnHere, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
        );

        he1.setBackground(new java.awt.Color(255, 255, 255));
        he1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, new java.awt.Color(153, 153, 153), java.awt.Color.lightGray, java.awt.Color.lightGray));

        btnTakeAway1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnTakeAway1.setForeground(new java.awt.Color(0, 0, 0));
        btnTakeAway1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnTakeAway1.setText("Mang về");

        javax.swing.GroupLayout he1Layout = new javax.swing.GroupLayout(he1);
        he1.setLayout(he1Layout);
        he1Layout.setHorizontalGroup(
            he1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(he1Layout.createSequentialGroup()
                .addComponent(btnTakeAway1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        he1Layout.setVerticalGroup(
            he1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnTakeAway1, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(he, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(he1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(he, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(he1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25)
                        .addComponent(jLabel26))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        orderDetailContainer.setBackground(new java.awt.Color(255, 255, 255));

        orderDetailContainerChild.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout orderDetailContainerChildLayout = new javax.swing.GroupLayout(orderDetailContainerChild);
        orderDetailContainerChild.setLayout(orderDetailContainerChildLayout);
        orderDetailContainerChildLayout.setHorizontalGroup(
            orderDetailContainerChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );
        orderDetailContainerChildLayout.setVerticalGroup(
            orderDetailContainerChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 471, Short.MAX_VALUE)
        );

        orderDetailContainer.setViewportView(orderDetailContainerChild);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(orderDetailContainer)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderDetailContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        pnlAll.setBackground(new java.awt.Color(237, 128, 50));

        lbAll.setBackground(new java.awt.Color(135, 136, 138));
        lbAll.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        lbAll.setForeground(new java.awt.Color(255, 255, 255));
        lbAll.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbAll.setText("Tất cả");
        lbAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlAllLayout = new javax.swing.GroupLayout(pnlAll);
        pnlAll.setLayout(pnlAllLayout);
        pnlAllLayout.setHorizontalGroup(
            pnlAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAllLayout.createSequentialGroup()
                .addComponent(lbAll, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlAllLayout.setVerticalGroup(
            pnlAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAllLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbAll, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );

        PnlChicken.setBackground(new java.awt.Color(243, 243, 243));

        lbChicken.setBackground(new java.awt.Color(102, 102, 102));
        lbChicken.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        lbChicken.setForeground(new java.awt.Color(102, 102, 102));
        lbChicken.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbChicken.setText("Gà rán");
        lbChicken.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout PnlChickenLayout = new javax.swing.GroupLayout(PnlChicken);
        PnlChicken.setLayout(PnlChickenLayout);
        PnlChickenLayout.setHorizontalGroup(
            PnlChickenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlChickenLayout.createSequentialGroup()
                .addComponent(lbChicken, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PnlChickenLayout.setVerticalGroup(
            PnlChickenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbChicken, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        PnlDrink.setBackground(new java.awt.Color(243, 243, 243));

        lbDrink.setBackground(new java.awt.Color(135, 136, 138));
        lbDrink.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        lbDrink.setForeground(new java.awt.Color(102, 102, 102));
        lbDrink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDrink.setText("Đồ uống");
        lbDrink.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout PnlDrinkLayout = new javax.swing.GroupLayout(PnlDrink);
        PnlDrink.setLayout(PnlDrinkLayout);
        PnlDrinkLayout.setHorizontalGroup(
            PnlDrinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlDrinkLayout.createSequentialGroup()
                .addComponent(lbDrink, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PnlDrinkLayout.setVerticalGroup(
            PnlDrinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbDrink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel10.setBackground(new java.awt.Color(243, 243, 243));
        jPanel10.setPreferredSize(new java.awt.Dimension(738, 720));

        jLabel7.setBackground(new java.awt.Color(135, 136, 138));
        jLabel7.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        jLabel7.setToolTipText("");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        search.setBackground(new java.awt.Color(243, 243, 243));
        search.setToolTipText("");
        search.setBorder(null);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                .addGap(654, 654, 654))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                    .addContainerGap(75, Short.MAX_VALUE)
                    .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 642, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(search)
                    .addContainerGap()))
        );

        PnlBurger.setBackground(new java.awt.Color(243, 243, 243));

        lbBurger.setBackground(new java.awt.Color(135, 136, 138));
        lbBurger.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        lbBurger.setForeground(new java.awt.Color(102, 102, 102));
        lbBurger.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbBurger.setText("Burger");
        lbBurger.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout PnlBurgerLayout = new javax.swing.GroupLayout(PnlBurger);
        PnlBurger.setLayout(PnlBurgerLayout);
        PnlBurgerLayout.setHorizontalGroup(
            PnlBurgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbBurger, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
        );
        PnlBurgerLayout.setVerticalGroup(
            PnlBurgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbBurger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        PnlPizza.setBackground(new java.awt.Color(243, 243, 243));

        lbPizza.setBackground(new java.awt.Color(135, 136, 138));
        lbPizza.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        lbPizza.setForeground(new java.awt.Color(102, 102, 102));
        lbPizza.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbPizza.setText("Pizza");
        lbPizza.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout PnlPizzaLayout = new javax.swing.GroupLayout(PnlPizza);
        PnlPizza.setLayout(PnlPizzaLayout);
        PnlPizzaLayout.setHorizontalGroup(
            PnlPizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlPizzaLayout.createSequentialGroup()
                .addComponent(lbPizza, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PnlPizzaLayout.setVerticalGroup(
            PnlPizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbPizza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        PnlExtraFood.setBackground(new java.awt.Color(243, 243, 243));

        lbExtraFood.setBackground(new java.awt.Color(135, 136, 138));
        lbExtraFood.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        lbExtraFood.setForeground(new java.awt.Color(102, 102, 102));
        lbExtraFood.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbExtraFood.setText("Món phụ");
        lbExtraFood.setToolTipText("");
        lbExtraFood.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout PnlExtraFoodLayout = new javax.swing.GroupLayout(PnlExtraFood);
        PnlExtraFood.setLayout(PnlExtraFoodLayout);
        PnlExtraFoodLayout.setHorizontalGroup(
            PnlExtraFoodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbExtraFood, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
        );
        PnlExtraFoodLayout.setVerticalGroup(
            PnlExtraFoodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbExtraFood, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlFoodsChild.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlFoodsChildLayout = new javax.swing.GroupLayout(pnlFoodsChild);
        pnlFoodsChild.setLayout(pnlFoodsChildLayout);
        pnlFoodsChildLayout.setHorizontalGroup(
            pnlFoodsChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 762, Short.MAX_VALUE)
        );
        pnlFoodsChildLayout.setVerticalGroup(
            pnlFoodsChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 553, Short.MAX_VALUE)
        );

        pnlFoods.setViewportView(pnlFoodsChild);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(pnlAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PnlChicken, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PnlBurger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PnlPizza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PnlExtraFood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PnlDrink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlFoods)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PnlDrink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PnlChicken, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PnlBurger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PnlPizza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PnlExtraFood, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFoods)
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Thu Ngân");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PnlBurger;
    private javax.swing.JPanel PnlChicken;
    private javax.swing.JPanel PnlDrink;
    private javax.swing.JPanel PnlExtraFood;
    private javax.swing.JPanel PnlPizza;
    private javax.swing.JLabel btnHere;
    private javax.swing.JLabel btnPay;
    private javax.swing.JLabel btnTakeAway1;
    private javax.swing.JPanel he;
    private javax.swing.JPanel he1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbAll;
    private javax.swing.JLabel lbBurger;
    private javax.swing.JLabel lbChicken;
    private javax.swing.JLabel lbDrink;
    private javax.swing.JLabel lbExtraFood;
    private javax.swing.JLabel lbPizza;
    private javax.swing.JScrollPane orderDetailContainer;
    private javax.swing.JPanel orderDetailContainerChild;
    private javax.swing.JLabel orderID;
    private javax.swing.JPanel pnlAll;
    private javax.swing.JScrollPane pnlFoods;
    private javax.swing.JPanel pnlFoodsChild;
    private javax.swing.JTextField search;
    // End of variables declaration//GEN-END:variables
}
