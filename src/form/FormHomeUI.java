package form;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.raven.swing.*;
import component.ItemProduct;
import entity.HoaDon;
import entity.HoaDonChiTiet;
import entity.KhachHang;
import entity.SanPham;
import entity.SanPhamChiTiet;
import event.EventItem;
import event.ItemClickListener;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import services.HoaDonCTService;
import services.HoaDonService;
import services.SPCTService;
import services.SanPhamService;
import swing.PanelActionAdd;
import swing.TableActionCellEditer;
import swing.TableActionCellRender;
import event.TableActionEvent;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import services.KhachHangServiceV1;
import ultis.MsgHelper;
import ultis.ValidationUtils;

public final class FormHomeUI extends javax.swing.JPanel implements ItemClickListener {

    private EventItem event;
    private final TableActionEvent event1;
    private final SanPhamService sanPhamService = new SanPhamService();
    private final HoaDonService donService = new HoaDonService();
    private final SPCTService cTService = new SPCTService();
    private final HoaDonCTService hoaDonCTService = new HoaDonCTService();
    private KhachHangServiceV1 hangServiceV1 = new KhachHangServiceV1();
    List<HoaDonChiTiet> list = new ArrayList<>();

    public FormHomeUI() {
        initComponents();
        scroll.setVerticalScrollBar(new ScrollBar());
        list = hoaDonCTService.getByIDHD(Integer.parseInt(jLabel7.getText()));
        event1 = (int row) -> {
            int sumMoney = 0;
            hoaDonCTService.remove(list.get(row).getIdHDCT());
            initModel(hoaDonCTService.getByIDHD(Integer.parseInt(jLabel7.getText())));
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                sumMoney += Integer.valueOf(jTable1.getValueAt(i, 2).toString());
            }
            DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
            dfs.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#,##0", dfs);
            totalAmount.setText(df.format(sumMoney));
            panelItem.removeAll();
            for (SanPhamChiTiet o : cTService.getAll()) {
                this.addItem(o);
            }
        };
        try {
            paymentAmount.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    String temp = "";
                    String[] arStr = totalAmount.getText().split("\\.");
                    for (String item : arStr) {
                        temp += item;
                    }
                    temp = temp.replace(",", ""); // Loại bỏ dấu phẩy trong chuỗi
                    int value = Integer.parseInt(temp);
                    String formattedValue = String.format("t: %,.0f", (double) value);
                    int input = Integer.parseInt(paymentAmount.getText()); // Giá trị nhập từ jTextField1
                    if (input <= value) {
                        refundAmount.setText("0");
                    } else {
                        int tienTra = input - value;
                        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
                        dfs.setDecimalSeparator('.');
                        DecimalFormat df = new DecimalFormat("#,##0", dfs);
                        refundAmount.setText(df.format(tienTra));
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (!paymentAmount.getText().isBlank()) {
                        String temp = "";
                        String[] arStr = totalAmount.getText().split("\\.");
                        for (String item : arStr) {
                            temp += item;
                        }
                        temp = temp.replace(",", ""); // Loại bỏ dấu phẩy trong chuỗi
                        int value = Integer.parseInt(temp);
                        String formattedValue = String.format("t: %,.0f", (double) value);
                        int input = Integer.parseInt(paymentAmount.getText()); // Giá trị nhập từ jTextField1
                        if (input <= value) {
                            refundAmount.setText("0");
                        } else {
                            int tienTra = input - value;
                            DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
                            dfs.setDecimalSeparator('.');
                            DecimalFormat df = new DecimalFormat("#,##0", dfs);
                            refundAmount.setText(df.format(tienTra));
                        }
                    }

                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    String temp = "";
                    String[] arStr = totalAmount.getText().split("\\.");
                    for (String item : arStr) {
                        temp += item;
                    }
                    temp = temp.replace(",", ""); // Loại bỏ dấu phẩy trong chuỗi
                    int value = Integer.parseInt(temp);
                    String formattedValue = String.format("t: %,.0f", (double) value);
                    int input = Integer.parseInt(paymentAmount.getText()); // Giá trị nhập từ jTextField1
                    if (input <= value) {
                        refundAmount.setText("0");
                    } else {
                        int tienTra = input - value;
                        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
                        dfs.setDecimalSeparator('.');
                        DecimalFormat df = new DecimalFormat("#,##0", dfs);
                        refundAmount.setText(df.format(tienTra));
                    }
                }
            });

            phoneNumber.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    KhachHang khachHang = hangServiceV1.getBySDT(phoneNumber.getText());
                    if (khachHang != null) {
                        customerName.setText(khachHang.getTen());
                    } else {
                        customerName.setText("");
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    customerName.setText("");
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });
        } catch (Exception e) {
        }

    }

    public void setEvent(EventItem event) {
        this.event = event;
    }

    DefaultTableModel model = new DefaultTableModel();

    void initModel(List<HoaDonChiTiet> list) {
        model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        jTable1.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender(new PanelActionAdd()));
        jTable1.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditer(event1));
        for (HoaDonChiTiet o : list) {
            SanPhamChiTiet spct = cTService.getByID(o.getIdSPCT());
            SanPham sp = sanPhamService.getByID(spct.getIdSanPham());
            model.addRow(new Object[]{
                sp.getTenSp(),
                o.getSoLongMua(),
                spct.getGia() * o.getSoLongMua()
            });
        }
    }

    public void addItem(SanPhamChiTiet data) {
        if (data != null) {
            ItemProduct item = new ItemProduct();
            item.setData(data);
            item.setItemClickListener(this);
            item.setButtonClickListener((ActionEvent e) -> {
                event.itemClick(item, data);
            });
            panelItem.add(item);
            panelItem.repaint();
            panelItem.revalidate();
        } else {
            System.out.println("Invalid ModelItem data provided");
        }
    }

    @Override
    public void onItemClick(SanPhamChiTiet data) {
        int sumMoney = 0;
        list = hoaDonCTService.getByIDHD(Integer.parseInt(jLabel7.getText()));
        initModel(list);
        panelItem.removeAll();
        for (SanPhamChiTiet o : cTService.getAll()) {
            this.addItem(o);
        }
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            sumMoney += Integer.valueOf(jTable1.getValueAt(i, 2).toString());
        }
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#,##0", dfs);
        totalAmount.setText(df.format(sumMoney));
    }

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();

    HoaDon getModelHD() {
        return HoaDon.builder()
                .idNhanVien(1)
                .trangThai("Chờ thanh toán")
                .ngayMua(dateFormat.format(date))
                .build();
    }

    HoaDon getModelHD_v2() {
        String temp = "";
        String[] arStr = totalAmount.getText().split("\\.");
        for (String item : arStr) {
            temp += item;
        }
        temp = temp.replace(",", ""); // Loại bỏ dấu phẩy trong chuỗi
        KhachHang kh = hangServiceV1.getBySDT(phoneNumber.getText());
        return HoaDon.builder()
                .idNhanVien(1)
                .trangThai("Thanh toán thành công")
                .ngayMua(dateFormat.format(date))
                .idKhachHang(kh.getId())
                .tien(Integer.parseInt(temp)) // Chuyển đổi chuỗi thành số nguyên
                .build();
    }

    HoaDon getModelHD_v3() {
        return HoaDon.builder()
                .idNhanVien(1)
                .trangThai("Hủy")
                .ngayMua(dateFormat.format(date))
                .build();
    }

    public void setSelected(Component item) {
        if (item != null) {
            for (Component com : panelItem.getComponents()) {
                ItemProduct i = (ItemProduct) com;
                if (i.isSelected()) {
                    i.setSelected(false);
                }
            }
            ((ItemProduct) item).setSelected(true);
        } else {
            System.out.println("Invalid component provided for selection");
        }
    }

    public Point getPanelItemLocation() {
        Point p = scroll.getLocation();
        return new Point(p.x, p.y - scroll.getViewport().getViewPosition().y);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroll = new javax.swing.JScrollPane();
        panelItem = new com.raven.swing.PanelItem();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        totalAmount = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        paymentAmount = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        refundAmount = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        createdDate = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        phoneNumber = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        customerName = new javax.swing.JTextField();

        setOpaque(false);

        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setViewportView(panelItem);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên sản phẩm", "Số lượng", "Giá", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(30);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(170);
        }

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 102, 51));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Hóa đơn");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Tổng tiền");

        totalAmount.setFont(new java.awt.Font("Segoe UI Historic", 0, 21)); // NOI18N
        totalAmount.setText("00.000");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Khách đưa");

        paymentAmount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        paymentAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentAmountActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Trả");

        refundAmount.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        refundAmount.setText("00.000");

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton1.setText("Thanh toán");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton2.setText("Hủy");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setText("+");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Ngày tạo:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("0");

        createdDate.setText("00/00/0000");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Số điện thoại:");

        phoneNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneNumberActionPerformed(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Tên khách hàng:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(refundAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymentAmount)
                    .addComponent(totalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(createdDate, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addComponent(phoneNumber)
                    .addComponent(customerName))
                .addGap(21, 21, 21))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton3)
                        .addComponent(jLabel7))
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(phoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(customerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(createdDate)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(paymentAmount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(refundAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 701, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scroll)
        );
    }// </editor-fold>//GEN-END:initComponents
    Boolean ischeckHD = true;
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (ischeckHD) {
            ItemProduct.isEnabled = true;
            panelItem.removeAll();
            for (SanPhamChiTiet o : cTService.getAll()) {
                addItem(o);
            }
            donService.add(getModelHD());
            jLabel7.setText(donService.getAll().get(donService.getAll().size() - 1).getIdHoaDon().toString());
            createdDate.setText(getModelHD().getNgayMua());
            ischeckHD = false;
        } else {
            MsgHelper.alert(this, "Thanh toán xong, hoặc hủy hóa đơn");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (Integer.valueOf(jLabel7.getText()) == 0) {
            MsgHelper.alert(this, "Không thanh toán được");
            return;
        }
        if (jTable1.getRowCount() <= 0) {
            MsgHelper.alert(this, "Thêm sản phẩm");
            return;
        }
        //Valdate for phoneNumber 
        if (!ValidationUtils.isPhoneNumber(phoneNumber.getText())) {
            MsgHelper.alert(this, "Số điện thoại không hợp lệ!");
        }
        
        //Validate for paymentAmount
        if(!ValidationUtils.isNumber(paymentAmount.getText())){
             MsgHelper.alert(this, "Số tiền thanh toán không hợp lệ!");
        }
        if (customerName.getText().isBlank()) {
            MsgHelper.alert(this, "Điển thông tin khách hàng");
            return;
        }
//        if (paymentAmount.getText().isBlank()) {
//            MsgHelper.alert(this, "Điển tiền khách trả");
//            return;
//        }
        String temp = "";
        String[] arStr = totalAmount.getText().split("\\.");
        for (String item : arStr) {
            temp += item;
        }
        temp = temp.replace(",", ""); // Loại bỏ dấu phẩy trong chuỗi
        int value = Integer.parseInt(temp);
        String formattedValue = String.format("t: %,.0f", (double) value);
        int input = Integer.parseInt(paymentAmount.getText()); // Giá trị nhập từ jTextField1
        if (input < value) {
            MsgHelper.alert(this, "Không đủ tiền");
            return;
        }
// Create a new PDF document.
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        document.open();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("src/image/hoadonchitiet.pdf"));
            // Open the PDF document.
            document.open();
// Tiêu đề Hóa đơn
            Paragraph title = new Paragraph("HOA DON", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setSpacingAfter(20);

// Đầu tiên, thêm đối tượng LineSeparator để phân chia các phần
            LineSeparator line = new LineSeparator();
            line.setLineColor(new BaseColor(0, 0, 0, 68)); // Màu đen nhạt
            document.add(new Chunk(line));
// Thông tin khách hàng
            Paragraph thongTinKH = new Paragraph("Thong tin khach hang", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
            thongTinKH.setAlignment(Element.ALIGN_CENTER);
            document.add(thongTinKH);
            Paragraph tenKH = new Paragraph("Ten khach hang: " + customerName.getText());
            tenKH.setAlignment(Element.ALIGN_CENTER);
            document.add(tenKH);
            Paragraph sdtKH = new Paragraph("SDT: " + phoneNumber.getText());
            sdtKH.setAlignment(Element.ALIGN_CENTER);
            document.add(sdtKH);
            document.add(new Chunk(line));

// Thông tin hóa đơn
            PdfPCell cell = null;
            cell = new PdfPCell(new Paragraph("Thong tin hoa don", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            int check = jTable1.getRowCount();
            table.addCell("Ten san pham");
            table.addCell("So luong");
            table.addCell("Gia");
            for (int i = 0; i < check; i++) {
                table.addCell(jTable1.getValueAt(i, 0).toString());
                table.addCell(jTable1.getValueAt(i, 1).toString());
                table.addCell(jTable1.getValueAt(i, 2).toString());
            }

            document.add(table);

// Cuối cùng, thêm một đối tượng LineSeparator để kết thúc hóa đơn
            document.add(new Chunk(line));

// Thêm lời cảm ơn
            Paragraph thankYou = new Paragraph("Cam on quy khach!", FontFactory.getFont(FontFactory.HELVETICA, 12));
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);
            document.close();

        } catch (DocumentException | FileNotFoundException ex) {
        }

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File("src/image/hoadonchitiet.pdf"));
            } catch (IOException e) {
            }
        }

        KhachHang khachHang = hangServiceV1.getBySDT(phoneNumber.getText());
        String temp1 = "";
        String[] arStr1 = totalAmount.getText().split("\\.");
        for (String item : arStr1) {
            temp1 += item;
        }
        temp1 = temp1.replace(",", ""); // Loại bỏ dấu phẩy trong chuỗi
        int value1 = Integer.parseInt(temp1);
        if (khachHang == null) {
            hangServiceV1.create(KhachHang.builder()
                    .sdt(phoneNumber.getText())
                    .tichDiem(value1 / 1000)
                    .ngaySinh("1900-01-01")
                    .ten(customerName.getText())
                    .build());
        } else {
            hangServiceV1.updateDiem(KhachHang.builder().tichDiem(value1 / 1000).build(), khachHang.getId());
        }
//
        donService.update(getModelHD_v2(), Integer.parseInt(jLabel7.getText()));
        MsgHelper.alert(this, "Thanh toán thành công");
//
        totalAmount.setText("0");
        refundAmount.setText("0");
        paymentAmount.setText("");
        phoneNumber.setText("");
        customerName.setText("");
        jLabel7.setText("0");
        createdDate.setText("00/00/0000");
        ischeckHD = true;
        model.setRowCount(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        donService.update(getModelHD_v3(), Integer.parseInt(jLabel7.getText()));
        hoaDonCTService.huyHD(Integer.parseInt(jLabel7.getText()));
        MsgHelper.alert(this, "Hủy thành công");
        panelItem.removeAll();
        for (SanPhamChiTiet o : cTService.getAll()) {
            this.addItem(o);
        }
        totalAmount.setText("0");
        refundAmount.setText("0");
        paymentAmount.setText("");
        jLabel7.setText("0");
        createdDate.setText("00/00/0000");
        ischeckHD = true;
        model.setRowCount(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void paymentAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentAmountActionPerformed

    private void phoneNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneNumberActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel createdDate;
    private javax.swing.JTextField customerName;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private com.raven.swing.PanelItem panelItem;
    private javax.swing.JTextField paymentAmount;
    private javax.swing.JTextField phoneNumber;
    private javax.swing.JLabel refundAmount;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JLabel totalAmount;
    // End of variables declaration//GEN-END:variables
}
