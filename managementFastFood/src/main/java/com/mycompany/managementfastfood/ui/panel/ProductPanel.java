/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.managementfastfood.ui.panel;

import DAO.CategoryDAO;
import DAO.FoodDAO;
import ViewPanel.OnEditFoodListener;
import ViewPanel.ProductItem;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Category;
import model.Food;

/**
 *
 * @author Acer
 */
public class ProductPanel extends javax.swing.JPanel {

    private FoodDAO foodDAO = new FoodDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    
    private Food currentFood = null;
    private String currentImagePath = ""; 
    
    private final java.awt.Color COLOR_ACTIVE_BG = new java.awt.Color(237, 128, 50);
    private final java.awt.Color COLOR_ACTIVE_TEXT = java.awt.Color.WHITE;
    private final java.awt.Color COLOR_INACTIVE_BG = new java.awt.Color(243, 243, 243);
    private final java.awt.Color COLOR_INACTIVE_TEXT = new java.awt.Color(102, 102, 102);
    
    private String currentCategoryFilter = "Tất cả"; 

    public ProductPanel() {
        initComponents();
        initCustomLayout();
        
        loadCategories(); 
        
        initCategoryFilterEvents(); 
        
        initSearchEvent();
        
        updateCategorySelection(pnlAll, lbAll);
        loadDataFromDB(); 
        
        addEventForAddButtonInRightPanel();
        addEventForDeleteButton();
        addEventChooseImage();
    }
    
    private void resetForm() {
        this.currentFood = null;
        this.currentImagePath = "";

        jLabel4.setText("Thêm món mới");
        jLabel14.setText("Thêm món");

        UserName1.setText("");
        UserName2.setText("");
        jCheckBox1.setSelected(true);
        if (categoryBox.getItemCount() > 0) categoryBox.setSelectedIndex(0);
        
        image.setIcon(null);
        image.setText("Chọn ảnh");
        
        PnlDelFood.setVisible(false); 
        
        UserName1.requestFocus();
    }
    
    private void updateCategorySelection(javax.swing.JPanel activePanel, javax.swing.JLabel activeLabel) {
        resetTabStyle(pnlAll, lbAll);
        resetTabStyle(PnlBurger, lbBurger);
        resetTabStyle(PnlChicken, lbChicken);
        resetTabStyle(PnlPizza, lbPizza);
        resetTabStyle(PnlExtraFood, lbExtraFood);
        resetTabStyle(PnlDrink, lbDrink);

        activePanel.setBackground(COLOR_ACTIVE_BG);
        activeLabel.setForeground(COLOR_ACTIVE_TEXT);
    }

    private void resetTabStyle(javax.swing.JPanel pnl, javax.swing.JLabel lbl) {
        pnl.setBackground(COLOR_INACTIVE_BG);
        lbl.setForeground(COLOR_INACTIVE_TEXT);
    }

    private void initCategoryFilterEvents() {
        addFilterEvent(pnlAll, lbAll, "Tất cả");
        addFilterEvent(PnlBurger, lbBurger, "Burger");
        addFilterEvent(PnlChicken, lbChicken, "Gà rán");
        addFilterEvent(PnlPizza, lbPizza, "Pizza");
        addFilterEvent(PnlExtraFood, lbExtraFood, "Món phụ");
        addFilterEvent(PnlDrink, lbDrink, "Đồ uống"); 
    }

    private void addFilterEvent(javax.swing.JPanel panel, javax.swing.JLabel label, String categoryName) {
        java.awt.event.MouseAdapter eventClick = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                updateCategorySelection(panel, label);
                currentCategoryFilter = categoryName;
                loadDataFromDB();
            }
        };

        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(eventClick);

        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(eventClick);
    }
    
    private void initSearchEvent() {
        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { loadDataFromDB(); }
            @Override
            public void removeUpdate(DocumentEvent e) { loadDataFromDB(); }
            @Override
            public void changedUpdate(DocumentEvent e) { loadDataFromDB(); }
        });
    }

    private void initCustomLayout() {
        ListProductItemChild.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 15));
        ListProductItem.getVerticalScrollBar().setUnitIncrement(16);
        ListProductItemChild.setBackground(new java.awt.Color(255, 255, 255));
    }

    private void loadCategories() {
        try {
            List<Category> listCat = categoryDAO.getAll();
            DefaultComboBoxModel model = (DefaultComboBoxModel) categoryBox.getModel();
            model.removeAllElements();
            
            for (Category c : listCat) {
                model.addElement(new ComboItem(c.getCategoryID(), c.getCategoryName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataFromDB() {
        loadDataFromDB(currentCategoryFilter);
    }
    
    public void loadDataFromDB(String categoryToFilter) {
        this.currentCategoryFilter = categoryToFilter; // Lưu lại trạng thái lọc
        
        ListProductItemChild.removeAll();

        final int ITEM_WIDTH = 225;
        final int ITEM_HEIGHT = 350;
        java.awt.Dimension fixedSize = new java.awt.Dimension(ITEM_WIDTH, ITEM_HEIGHT);

        // A. Nút Thêm Mới
        ViewPanel.addProductItem btnAdd = new ViewPanel.addProductItem();
        btnAdd.setPreferredSize(fixedSize);
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                resetForm(); 
            }
        });
        ListProductItemChild.add(btnAdd);

        // B. Lấy dữ liệu và lọc
        java.util.List<model.Food> listFood = foodDAO.getAllFood();
        String keyword = search.getText().trim().toLowerCase(); 
        
        int count = 0;
        for (model.Food f : listFood) {
            
            // Logic lọc Danh mục
            boolean isCategoryMatch = categoryToFilter.equals("Tất cả");
            if (!isCategoryMatch) {
                String foodCat = f.getCategory(); 
                if (foodCat != null && (foodCat.equalsIgnoreCase(categoryToFilter) || 
                                        (categoryToFilter.equals("Đồ uống") && foodCat.equals("Nước uống")))) {
                    isCategoryMatch = true;
                }
            }

            boolean isSearchMatch = true;
            if (!keyword.isEmpty() && !keyword.equals("tìm món...")) {
                isSearchMatch = f.getFoodName().toLowerCase().contains(keyword);
            }
            
            if (isCategoryMatch && isSearchMatch) {
                ProductItem item = new ProductItem(f, new OnEditFoodListener() {
                    @Override
                    public void onEditFood(model.Food food) {
                        fillDataToRightPanel(food);
                    }
                });
                item.setPreferredSize(fixedSize);
                item.setBackground(new java.awt.Color(255, 255, 255));
                ListProductItemChild.add(item);
                count++;
            }
        }
        
        jLabel1.setText("Hiển thị " + count + " món ăn (" + categoryToFilter + ")");
        
        int totalItems = count + 1;
        int rows = (int) Math.ceil((double) totalItems / 3); 
        int height = rows * (ITEM_HEIGHT + 20) + 50;
        ListProductItemChild.setPreferredSize(new java.awt.Dimension(800, height));

        ListProductItemChild.revalidate();
        ListProductItemChild.repaint();
    }
    
    private void addEventForDeleteButton() {
        java.awt.event.MouseAdapter deleteEvent = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                deleteProduct();
            }
        };
        
        PnlDelFood.setCursor(new Cursor(Cursor.HAND_CURSOR));
        PnlDelFood.addMouseListener(deleteEvent);
        
        lbDelFood.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbDelFood.addMouseListener(deleteEvent);
    }

    private void deleteProduct() {
        if (currentFood == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa món: " + currentFood.getFoodName() + "?\nHành động này không thể hoàn tác.",
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // 3. Gọi DAO để xóa
            boolean success = foodDAO.deleteFood(currentFood.getFoodID());
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                
                loadDataFromDB(currentCategoryFilter); 
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Có thể món này đã có trong hóa đơn.");
            }
        }
    }


    private void fillDataToRightPanel(Food f) {
        this.currentFood = f;
        this.currentImagePath = f.getImageURL(); 
        jLabel4.setText("Sửa món ăn");
        jLabel14.setText("Cập nhật");
        UserName1.setText(f.getFoodName());
        UserName2.setText(String.format("%.0f", f.getPrice()));
        jCheckBox1.setSelected(f.getStatus() == 1);
        DefaultComboBoxModel model = (DefaultComboBoxModel) categoryBox.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            ComboItem item = (ComboItem) model.getElementAt(i);
            if (item.getId() == f.getCategoryID()) {
                categoryBox.setSelectedIndex(i);
                break;
            }
        }
        PnlDelFood.setVisible(true);
        displayImage(currentImagePath);
    }

    private void addEventChooseImage() {
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    currentImagePath = file.getAbsolutePath();
                    displayImage(currentImagePath);
                }
            }
        });
    }

    private void displayImage(String path) {
        // 1. Reset trạng thái ban đầu
        image.setIcon(null);
        image.setText("");
        if (path != null && !path.trim().isEmpty()) {
            File f = new File(path); 
            if (f.exists()) {
                try {
                    ImageIcon icon = new ImageIcon(path);
                    
                    Image img = icon.getImage().getScaledInstance(200, 174, Image.SCALE_SMOOTH);
                    
                    image.setIcon(new ImageIcon(img));
                    
                } catch (Exception e) {
                    image.setText("Lỗi file ảnh");
                    e.printStackTrace();
                }
            } else {
                image.setText("File không tồn tại");
            }
        } else {
            image.setText("Chưa chọn ảnh");
        }
        
        image.repaint();
    }

    private void addEventForAddButtonInRightPanel() {
        jPanel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                saveProduct();
            }
        });
    }

    private void saveProduct() {
        String name = UserName1.getText().trim();
        String priceStr = UserName2.getText().trim();
        if (name.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin!");
            return;
        }
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá tiền không hợp lệ!");
            return;
        }
        ComboItem selectedCat = (ComboItem) categoryBox.getSelectedItem();
        if (selectedCat == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục!");
            return;
        }
        Food f = new Food();
        f.setFoodName(name);
        f.setPrice(price);
        f.setImageURL(currentImagePath);
        f.setCategoryID(selectedCat.getId());
        f.setCategory(selectedCat.toString());
        f.setStatus(jCheckBox1.isSelected() ? 1 : 0);
        boolean success;
        if (currentFood == null) {
            success = foodDAO.addFood(f);
            if (success) JOptionPane.showMessageDialog(this, "Thêm thành công!");
        } else {
            f.setFoodID(currentFood.getFoodID());
            success = foodDAO.updateFood(f);
            if (success) JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        }
        if (success) {
            loadDataFromDB();
            resetForm();
        }
    }

    class ComboItem {
        private int id;
        private String name;
        public ComboItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public int getId() { return id; }
        @Override
        public String toString() { return name; }
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
        category = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        search = new javax.swing.JTextField();
        PnlBurger = new javax.swing.JPanel();
        lbBurger = new javax.swing.JLabel();
        pnlAll = new javax.swing.JPanel();
        lbAll = new javax.swing.JLabel();
        PnlPizza = new javax.swing.JPanel();
        lbPizza = new javax.swing.JLabel();
        PnlChicken = new javax.swing.JPanel();
        lbChicken = new javax.swing.JLabel();
        PnlExtraFood = new javax.swing.JPanel();
        lbExtraFood = new javax.swing.JLabel();
        PnlDrink = new javax.swing.JPanel();
        lbDrink = new javax.swing.JLabel();
        right = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        image = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel lbChoseImage = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        UserName1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        UserName2 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        categoryBox = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        PnlDelFood = new javax.swing.JPanel();
        lbDelFood = new javax.swing.JLabel();
        ListProductItem = new javax.swing.JScrollPane();
        ListProductItemChild = new javax.swing.JPanel();

        setBackground(new java.awt.Color(245, 245, 248));
        setMaximumSize(new java.awt.Dimension(1200, 740));
        setMinimumSize(new java.awt.Dimension(1200, 740));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Danh sách món ăn");

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Quản lý 48 món ăn và đồ uống");

        category.setBackground(new java.awt.Color(245, 245, 248));

        jPanel10.setBackground(new java.awt.Color(243, 243, 243));
        jPanel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray));
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
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
            .addComponent(search)
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
            .addGroup(PnlBurgerLayout.createSequentialGroup()
                .addComponent(lbBurger, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addContainerGap())
        );
        PnlBurgerLayout.setVerticalGroup(
            PnlBurgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbBurger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

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
                .addComponent(lbAll, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlPizzaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbPizza, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PnlPizzaLayout.setVerticalGroup(
            PnlPizzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbPizza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        PnlChicken.setBackground(new java.awt.Color(243, 243, 243));

        lbChicken.setBackground(new java.awt.Color(153, 153, 153));
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
                .addComponent(lbChicken, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        PnlChickenLayout.setVerticalGroup(
            PnlChickenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbChicken, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addComponent(lbExtraFood, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
        );
        PnlExtraFoodLayout.setVerticalGroup(
            PnlExtraFoodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbExtraFood, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
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
            .addComponent(lbDrink, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        PnlDrinkLayout.setVerticalGroup(
            PnlDrinkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbDrink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout categoryLayout = new javax.swing.GroupLayout(category);
        category.setLayout(categoryLayout);
        categoryLayout.setHorizontalGroup(
            categoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoryLayout.createSequentialGroup()
                .addContainerGap()
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
                .addComponent(PnlDrink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addContainerGap())
        );
        categoryLayout.setVerticalGroup(
            categoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(categoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(categoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(PnlDrink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PnlChicken, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PnlBurger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PnlPizza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PnlExtraFood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        right.setBackground(new java.awt.Color(255, 255, 255));
        right.setPreferredSize(new java.awt.Dimension(358, 635));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Thêm món mới");

        jLabel8.setBackground(new java.awt.Color(51, 51, 51));
        jLabel8.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Hình ảnh");

        image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        image.setText("Chọn ảnh");
        image.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel1.setBackground(new java.awt.Color(237, 128, 50));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setToolTipText("");

        lbChoseImage.setBackground(new java.awt.Color(237, 128, 50));
        lbChoseImage.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lbChoseImage.setForeground(new java.awt.Color(255, 255, 255));
        lbChoseImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbChoseImage.setText("Chọn");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbChoseImage, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbChoseImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        jCheckBox1.setBackground(new java.awt.Color(245, 245, 248));
        jCheckBox1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(0, 0, 0));
        jCheckBox1.setText("Kích hoạt bán");
        jCheckBox1.addActionListener(this::jCheckBox1ActionPerformed);

        jLabel11.setBackground(new java.awt.Color(51, 51, 51));
        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("Tên món ăn");

        UserName1.setBackground(new java.awt.Color(255, 255, 255));
        UserName1.setToolTipText("");
        UserName1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));

        jLabel12.setBackground(new java.awt.Color(51, 51, 51));
        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("Giá bán");

        UserName2.setBackground(new java.awt.Color(255, 255, 255));
        UserName2.setToolTipText("");
        UserName2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)));

        jLabel13.setBackground(new java.awt.Color(51, 51, 51));
        jLabel13.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("Danh mục");

        categoryBox.setBackground(new java.awt.Color(255, 255, 255));
        categoryBox.setForeground(new java.awt.Color(0, 0, 0));
        categoryBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPanel7.setBackground(new java.awt.Color(237, 128, 50));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.setToolTipText("");

        jLabel14.setBackground(new java.awt.Color(237, 128, 50));
        jLabel14.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Thêm món");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 147, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(27, 27, 27)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        PnlDelFood.setBackground(new java.awt.Color(255, 0, 51));
        PnlDelFood.setForeground(new java.awt.Color(255, 255, 255));
        PnlDelFood.setToolTipText("");

        lbDelFood.setBackground(new java.awt.Color(255, 0, 0));
        lbDelFood.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lbDelFood.setForeground(new java.awt.Color(255, 255, 255));
        lbDelFood.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDelFood.setText("Xóa món");

        javax.swing.GroupLayout PnlDelFoodLayout = new javax.swing.GroupLayout(PnlDelFood);
        PnlDelFood.setLayout(PnlDelFoodLayout);
        PnlDelFoodLayout.setHorizontalGroup(
            PnlDelFoodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 147, Short.MAX_VALUE)
            .addGroup(PnlDelFoodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lbDelFood, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
        );
        PnlDelFoodLayout.setVerticalGroup(
            PnlDelFoodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(PnlDelFoodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lbDelFood, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout rightLayout = new javax.swing.GroupLayout(right);
        right.setLayout(rightLayout);
        rightLayout.setHorizontalGroup(
            rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel8)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator1)
                        .addGroup(rightLayout.createSequentialGroup()
                            .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(UserName1))
                    .addGroup(rightLayout.createSequentialGroup()
                        .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UserName2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(categoryBox, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(rightLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PnlDelFood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        rightLayout.setVerticalGroup(
            rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rightLayout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(rightLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addComponent(jCheckBox1)
                .addGap(28, 28, 28)
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(rightLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UserName1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(UserName2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(categoryBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(rightLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(42, 42, 42)))
                .addGap(73, 73, 73)
                .addGroup(rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PnlDelFood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ListProductItemChild.setBackground(new java.awt.Color(255, 255, 255));
        ListProductItemChild.setPreferredSize(new java.awt.Dimension(832, 564));

        javax.swing.GroupLayout ListProductItemChildLayout = new javax.swing.GroupLayout(ListProductItemChild);
        ListProductItemChild.setLayout(ListProductItemChildLayout);
        ListProductItemChildLayout.setHorizontalGroup(
            ListProductItemChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 848, Short.MAX_VALUE)
        );
        ListProductItemChildLayout.setVerticalGroup(
            ListProductItemChildLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 647, Short.MAX_VALUE)
        );

        ListProductItem.setViewportView(ListProductItemChild);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(category, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ListProductItem, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(right, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(category, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ListProductItem, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(right, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ListProductItem;
    private javax.swing.JPanel ListProductItemChild;
    private javax.swing.JPanel PnlBurger;
    private javax.swing.JPanel PnlChicken;
    private javax.swing.JPanel PnlDelFood;
    private javax.swing.JPanel PnlDrink;
    private javax.swing.JPanel PnlExtraFood;
    private javax.swing.JPanel PnlPizza;
    private javax.swing.JTextField UserName1;
    private javax.swing.JTextField UserName2;
    private javax.swing.JPanel category;
    private javax.swing.JComboBox<String> categoryBox;
    private javax.swing.JLabel image;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbAll;
    private javax.swing.JLabel lbBurger;
    private javax.swing.JLabel lbChicken;
    private javax.swing.JLabel lbDelFood;
    private javax.swing.JLabel lbDrink;
    private javax.swing.JLabel lbExtraFood;
    private javax.swing.JLabel lbPizza;
    private javax.swing.JPanel pnlAll;
    private javax.swing.JScrollPane pnlFoods;
    private javax.swing.JScrollPane pnlFoods1;
    private javax.swing.JPanel pnlFoodsChild;
    private javax.swing.JPanel pnlFoodsChild1;
    private javax.swing.JPanel right;
    private javax.swing.JTextField search;
    // End of variables declaration//GEN-END:variables
}
