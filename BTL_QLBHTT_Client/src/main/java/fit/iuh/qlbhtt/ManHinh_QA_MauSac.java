/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package fit.iuh.qlbhtt;

import fit.iuh.dao.*;
import fit.iuh.entity.*;
import fit.iuh.util.RMIClientUtil;

import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.rmi.RemoteException;
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * @author DMX
 */
public   class ManHinh_QA_MauSac extends javax.swing.JPanel {

    private IMauSacDao dao_MauSac;
    private DefaultTableModel modelMauSac;
    private boolean kiemTraThem = false;
    private boolean kiemTraCapNhat = false;

    /**
     * Creates new form quanly
     */
    public   ManHinh_QA_MauSac() throws SQLException, RemoteException {
        dao_MauSac = RMIClientUtil.getMauSacDao();
//        connect = new Connect();
//        connect.connect();
        initComponents();

        tbl_MauSac.setDefaultEditor(Object.class, null); //Không cho chỉnh sửa cột
        tbl_MauSac.getTableHeader().setReorderingAllowed(false); //Không cho di chuyển cột

        docDuLieuLenBang();
    }

    /**
     * Huy thao tac hoat dong cua componet
     */
    private synchronized  void huyThaoTacNhap() {
        kiemTraCapNhat = false;
        kiemTraThem = false;
        btn_CapNhat.setText("Cập nhật");
        btn_Them.setText("Thêm");
        btn_Luu.setEnabled(false);
        btn_CapNhat.setEnabled(true);
        btn_Them.setEnabled(true);
        kiemTraTextNhap(true);
        xoaTrang();
    }

    /**
     * Kiem tra hoat dong cua cac JtextField
     */
    public synchronized  void kiemTraTextNhap(boolean kiemTra) {
        if (kiemTraCapNhat || kiemTraThem) {
            txt_MaMauSac.setEditable(!kiemTra);

        } else {
            txt_MaMauSac.setEditable(kiemTra);

        }

    }

    /**
     * Xóa trắng text field
     */
    public synchronized  void xoaTrang() {
        txt_MaMauSac.setText("");
        txt_TenMauSac.setText("");
    }

    /**
     * Đọc dữ liệu và load dữ liệu lên table
     */
    public synchronized  void docDuLieuLenBang() throws RemoteException {
        modelMauSac = (DefaultTableModel) tbl_MauSac.getModel();
        modelMauSac.setRowCount(0);
        for (MauSac mauSac : dao_MauSac.getAllMauSac()) {
            Object[] o = new Object[2];
            o[0] = mauSac.getMaMauSac();
            o[1] = mauSac.getMauSac();
            modelMauSac.addRow(o);
        }
    }

    /**
     * Xử lý thêm Màu sắc
     */
    public synchronized  void xuLyThemMauSac() throws RemoteException {
        String tenMauSac = txt_TenMauSac.getText();

        MauSac mauSac = new MauSac(tenMauSac);
        dao_MauSac.themDLMauSac(mauSac);

        modelMauSac = (DefaultTableModel) tbl_MauSac.getModel();
        Object[] object = new Object[2];
        object[0] = mauSac.getMaMauSac();
        object[1] = mauSac.getMauSac();

        modelMauSac.addRow(object);
        docDuLieuLenBang();
        xoaTrang();
        JOptionPane.showMessageDialog(this, "Thêm thành công");
    }

//    /**
//     * Xử lý xóa Màu sắc
//     */
//    public synchronized  void xuLyXoaMauSac() {
//        int row = tbl_MauSac.getSelectedRow();
//        if (row != -1) {
//            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc là xóa dòng này không?", "Cảnh Báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//                String maMauSac = txt_MaMauSac.getText();
//                dao_MauSac.xoaDLMauSac(maMauSac);
//                modelMauSac.removeRow(row);
//                JOptionPane.showMessageDialog(this, "Xóa thành công");
//                xoaTrang();
//            }
//        } else {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
//        }
//    }

    /**
     * Xử lý cập nhật Màu sắc
     */
    public synchronized  void xuLyCapNhatMauSac() throws RemoteException {
        long maMauSac = Long.parseLong(txt_MaMauSac.getText());
        String tenMauSac = txt_TenMauSac.getText();

        MauSac mauSac = new MauSac(maMauSac, tenMauSac);
        int row = tbl_MauSac.getSelectedRow();
        if (row != -1) {
            dao_MauSac.capNhatDLMauSac(mauSac);
            for (int i = 0; i < tbl_MauSac.getRowCount(); i++) {
                long maMauSacTable = Long.parseLong(tbl_MauSac.getValueAt(row, 0).toString());
                if (maMauSacTable == maMauSac) {
                    tbl_MauSac.setValueAt(tenMauSac, row, 1);
                }

            }
            xoaTrang();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần cập nhật!");
        }

    }

    /**
     * Tìm kiếm chất liệu
     */
    public synchronized  void xuLyTimKiemMauSac() throws RemoteException {
        String msConvert = null;
        if (txt_MaMauSac.getText().equals("")) {
            msConvert = "0";
        } else {
            msConvert = txt_MaMauSac.getText();
        }
        long tuKhoaMaMauSac = Long.parseLong(msConvert);
        String tuKhoaTenMauSac = txt_TenMauSac.getText();

        if (tuKhoaMaMauSac == 0 && tuKhoaTenMauSac.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm");
            return;
        }

        String maMSString = null;
        if (tuKhoaMaMauSac == 0) {
            maMSString = "";
        }

        modelMauSac = (DefaultTableModel) tbl_MauSac.getModel();
        modelMauSac.setRowCount(0);
        if (maMSString != null) {
            if (tuKhoaTenMauSac.equals("")) {
                MauSac mauSac = dao_MauSac.getDLMauSacTheoMa(tuKhoaMaMauSac);
                if (mauSac != null) {
                    modelMauSac.setRowCount(0);
                    Object[] object = new Object[2];
                    object[0] = mauSac.getMaMauSac();
                    object[1] = mauSac.getMauSac();
                    modelMauSac.addRow(object);
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy màu sắc");
                }
            } else if (maMSString.equals("")) {
                MauSac mauSac = dao_MauSac.getMauSacTheoTen(tuKhoaTenMauSac);
                if (mauSac != null) {
                    modelMauSac.setRowCount(0);
                    Object[] object = new Object[2];
                    object[0] = mauSac.getMaMauSac();
                    object[1] = mauSac.getMauSac();
                    modelMauSac.addRow(object);
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy màu sắc");
                }
            }
        } else {
            MauSac mauSac = dao_MauSac.getDLMauSacTheoMa(tuKhoaMaMauSac);
            if (mauSac != null) {
                modelMauSac.setRowCount(0);
                Object[] object = new Object[2];
                object[0] = mauSac.getMaMauSac();
                object[1] = mauSac.getMauSac();
                modelMauSac.addRow(object);
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy màu sắc");
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
    private synchronized  void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        pnl_DanhSachMauSac = new javax.swing.JPanel();
        scr_DanhSachMauSac = new javax.swing.JScrollPane();
        tbl_MauSac = new javax.swing.JTable();
        pnl_MS_TieuDe = new javax.swing.JPanel();
        lbl_TieuDe = new javax.swing.JLabel();
        pnl_ThongTin = new javax.swing.JPanel();
        txt_MaMauSac = new javax.swing.JTextField();
        lbl_MaMauSac = new javax.swing.JLabel();
        lbl_TenMauSac = new javax.swing.JLabel();
        txt_TenMauSac = new javax.swing.JTextField();
        pnl_NutChucNang = new javax.swing.JPanel();
        btn_Them = new javax.swing.JButton();
        btn_CapNhat = new javax.swing.JButton();
        btn_Luu = new javax.swing.JButton();
        btn_XoaTrang = new javax.swing.JButton();
        btn_TimKiem = new javax.swing.JButton();

        setBackground(new java.awt.Color(199, 210, 213));
        setMinimumSize(new java.awt.Dimension(1000, 550));
        setPreferredSize(new java.awt.Dimension(1000, 550));

        pnl_DanhSachMauSac.setPreferredSize(new java.awt.Dimension(2000, 324));

        scr_DanhSachMauSac.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách màu sắc", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        tbl_MauSac.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbl_MauSac.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Mã màu sắc", "Tên màu sắc"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class
            };

            public synchronized  Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        tbl_MauSac.setMaximumSize(new java.awt.Dimension(2147483647, 196));
        tbl_MauSac.setPreferredSize(new java.awt.Dimension(750, 600));
        tbl_MauSac.setRowHeight(35);
        tbl_MauSac.setShowGrid(true);
        tbl_MauSac.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized  void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_MauSacMouseClicked(evt);
            }
        });
        scr_DanhSachMauSac.setViewportView(tbl_MauSac);

        lbl_TieuDe.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        lbl_TieuDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TieuDe.setText("QUẢN LÝ MÀU SẮC");
        lbl_TieuDe.setMaximumSize(new java.awt.Dimension(32767, 32767));
        lbl_TieuDe.setPreferredSize(new java.awt.Dimension(181, 40));

        javax.swing.GroupLayout pnl_MS_TieuDeLayout = new javax.swing.GroupLayout(pnl_MS_TieuDe);
        pnl_MS_TieuDe.setLayout(pnl_MS_TieuDeLayout);
        pnl_MS_TieuDeLayout.setHorizontalGroup(
                pnl_MS_TieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_MS_TieuDeLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbl_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );
        pnl_MS_TieuDeLayout.setVerticalGroup(
                pnl_MS_TieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbl_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnl_DanhSachMauSacLayout = new javax.swing.GroupLayout(pnl_DanhSachMauSac);
        pnl_DanhSachMauSac.setLayout(pnl_DanhSachMauSacLayout);
        pnl_DanhSachMauSacLayout.setHorizontalGroup(
                pnl_DanhSachMauSacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnl_MS_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scr_DanhSachMauSac, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        pnl_DanhSachMauSacLayout.setVerticalGroup(
                pnl_DanhSachMauSacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_DanhSachMauSacLayout.createSequentialGroup()
                                .addComponent(pnl_MS_TieuDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(scr_DanhSachMauSac, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
        );

        pnl_ThongTin.setBackground(new java.awt.Color(199, 210, 213));
        pnl_ThongTin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin màu sắc", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        pnl_ThongTin.setPreferredSize(new java.awt.Dimension(945, 285));

        txt_MaMauSac.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_MaMauSac.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_MaMauSac.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_MaMauSac.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MaMauSacActionPerformed(evt);
            }
        });

        lbl_MaMauSac.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_MaMauSac.setText("Mã màu sắc");

        lbl_TenMauSac.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TenMauSac.setText("Tên màu sắc");

        txt_TenMauSac.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_TenMauSac.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_TenMauSac.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_TenMauSac.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TenMauSacActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_ThongTinLayout = new javax.swing.GroupLayout(pnl_ThongTin);
        pnl_ThongTin.setLayout(pnl_ThongTinLayout);
        pnl_ThongTinLayout.setHorizontalGroup(
                pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGap(100, 100, 100)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbl_MaMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt_MaMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(106, 106, 106)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbl_TenMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt_TenMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_ThongTinLayout.setVerticalGroup(
                pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_ThongTinLayout.createSequentialGroup()
                                                .addComponent(lbl_TenMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txt_TenMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_ThongTinLayout.createSequentialGroup()
                                                .addComponent(lbl_MaMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txt_MaMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(184, Short.MAX_VALUE))
        );

        pnl_NutChucNang.setBackground(new java.awt.Color(199, 210, 213));
        pnl_NutChucNang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        btn_Them.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_Them.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-add-30.png")); // NOI18N
        btn_Them.setText("Thêm");
        btn_Them.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Them.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized  void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_ThemMouseEntered(evt);
            }

            public synchronized  void mouseExited(java.awt.event.MouseEvent evt) {
                btn_ThemMouseExited(evt);
            }
        });
        btn_Them.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemActionPerformed(evt);
            }
        });

        btn_CapNhat.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_CapNhat.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-update-30.png")); // NOI18N
        btn_CapNhat.setText("Cập nhật");
        btn_CapNhat.setBorder(null);
        btn_CapNhat.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized  void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_CapNhatMouseEntered(evt);
            }

            public synchronized  void mouseExited(java.awt.event.MouseEvent evt) {
                btn_CapNhatMouseExited(evt);
            }
        });
        btn_CapNhat.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CapNhatActionPerformed(evt);
            }
        });

        btn_Luu.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_Luu.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-save-30.png")); // NOI18N
        btn_Luu.setText("Lưu");
        btn_Luu.setBorder(null);
        btn_Luu.setEnabled(false);
        btn_Luu.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized  void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_LuuMouseEntered(evt);
            }

            public synchronized  void mouseExited(java.awt.event.MouseEvent evt) {
                btn_LuuMouseExited(evt);
            }
        });
        btn_Luu.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_LuuActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btn_XoaTrang.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_XoaTrang.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-delete-30.png")); // NOI18N
        btn_XoaTrang.setText("Xóa trắng");
        btn_XoaTrang.setBorder(null);
        btn_XoaTrang.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized  void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_XoaTrangMouseEntered(evt);
            }

            public synchronized  void mouseExited(java.awt.event.MouseEvent evt) {
                btn_XoaTrangMouseExited(evt);
            }
        });
        btn_XoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_XoaTrangActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btn_TimKiem.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-search-30.png")); // NOI18N
        btn_TimKiem.setText("Tìm kiếm");
        btn_TimKiem.setBorder(null);
        btn_TimKiem.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_TimKiemActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        javax.swing.GroupLayout pnl_NutChucNangLayout = new javax.swing.GroupLayout(pnl_NutChucNang);
        pnl_NutChucNang.setLayout(pnl_NutChucNangLayout);
        pnl_NutChucNangLayout.setHorizontalGroup(
                pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btn_TimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_CapNhat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_Them, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_Luu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_XoaTrang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                                .addContainerGap(43, Short.MAX_VALUE))
        );
        pnl_NutChucNangLayout.setVerticalGroup(
                pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_CapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_Luu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_XoaTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(pnl_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(pnl_NutChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(pnl_DanhSachMauSac, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(pnl_DanhSachMauSac, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnl_ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pnl_NutChucNang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private synchronized  void txt_MaMauSacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MaMauSacActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_MaMauSacActionPerformed

    private synchronized  void btn_CapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CapNhatActionPerformed
        if (btn_CapNhat.getText().equalsIgnoreCase("Cập nhật")) {
            btn_CapNhat.setText("Hủy");
            btn_Them.setEnabled(false);
            btn_Luu.setEnabled(true);
            kiemTraCapNhat = true;
            kiemTraTextNhap(true);
            xoaTrang();
        } else if (btn_CapNhat.getText().equalsIgnoreCase("Hủy")) {
            btn_CapNhat.setText("Cập nhật");
            huyThaoTacNhap();
        }
    }//GEN-LAST:event_btn_CapNhatActionPerformed

    private synchronized  void btn_LuuActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_LuuActionPerformed
        if (kiemTraThem) {
            xuLyThemMauSac();
        } else if (kiemTraCapNhat) {
            xuLyCapNhatMauSac();
        }
    }//GEN-LAST:event_btn_LuuActionPerformed

    private synchronized  void txt_TenMauSacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TenMauSacActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TenMauSacActionPerformed

    private synchronized  void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed
        if (btn_Them.getText().equalsIgnoreCase("Thêm")) {
            btn_Them.setText("Hủy");
            btn_CapNhat.setEnabled(false);
            btn_Luu.setEnabled(true);
            kiemTraThem = true;
            kiemTraTextNhap(true);
            xoaTrang();
        } else if (btn_Them.getText().equalsIgnoreCase("Hủy")) {
            btn_Them.setText("Thêm");
            huyThaoTacNhap();
        }
    }//GEN-LAST:event_btn_ThemActionPerformed

    private synchronized  void tbl_MauSacMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_MauSacMouseClicked
        int row = tbl_MauSac.getSelectedRow();
        if (row != -1) {
            txt_MaMauSac.setText(tbl_MauSac.getValueAt(row, 0).toString());
            txt_TenMauSac.setText(tbl_MauSac.getValueAt(row, 1).toString());
        }
    }//GEN-LAST:event_tbl_MauSacMouseClicked

    private synchronized  void btn_XoaTrangActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_XoaTrangActionPerformed
        xoaTrang();
        docDuLieuLenBang();
    }//GEN-LAST:event_btn_XoaTrangActionPerformed

    private synchronized  void btn_ThemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ThemMouseEntered
        if (btn_Them.isEnabled()) {
            btn_Them.setBackground(new Color(0x9EDDFF));
            btn_Them.setForeground(new Color(0x141E46));
        }
    }//GEN-LAST:event_btn_ThemMouseEntered

    private synchronized  void btn_ThemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ThemMouseExited
        if (btn_Them.isEnabled()) {
            btn_Them.setBackground(UIManager.getColor("Menu.background"));
            btn_Them.setForeground(UIManager.getColor("Menu.foreground"));
        }
    }//GEN-LAST:event_btn_ThemMouseExited

    private synchronized  void btn_CapNhatMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CapNhatMouseEntered
        if (btn_CapNhat.isEnabled()) {
            btn_CapNhat.setBackground(new Color(0x9EDDFF));
            btn_CapNhat.setForeground(new Color(0x141E46));
        }

    }//GEN-LAST:event_btn_CapNhatMouseEntered

    private synchronized  void btn_CapNhatMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CapNhatMouseExited
        if (btn_CapNhat.isEnabled()) {
            btn_CapNhat.setBackground(UIManager.getColor("Menu.background"));
            btn_CapNhat.setForeground(UIManager.getColor("Menu.foreground"));
        }

    }//GEN-LAST:event_btn_CapNhatMouseExited

    private synchronized  void btn_LuuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LuuMouseEntered
        if (btn_Luu.isEnabled()) {
            btn_Luu.setBackground(new Color(0x9EDDFF));
            btn_Luu.setForeground(new Color(0x141E46));
        }

    }//GEN-LAST:event_btn_LuuMouseEntered

    private synchronized  void btn_LuuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LuuMouseExited
        if (btn_Luu.isEnabled()) {
            btn_Luu.setBackground(UIManager.getColor("Menu.background"));
            btn_Luu.setForeground(UIManager.getColor("Menu.foreground"));
        }

    }//GEN-LAST:event_btn_LuuMouseExited

    private synchronized  void btn_XoaTrangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_XoaTrangMouseEntered
        btn_XoaTrang.setBackground(new Color(0x9EDDFF));
        btn_XoaTrang.setForeground(new Color(0x141E46));
    }//GEN-LAST:event_btn_XoaTrangMouseEntered

    private synchronized  void btn_XoaTrangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_XoaTrangMouseExited
        btn_XoaTrang.setBackground(UIManager.getColor("Menu.background"));
        btn_XoaTrang.setForeground(UIManager.getColor("Menu.foreground"));
    }//GEN-LAST:event_btn_XoaTrangMouseExited

    private synchronized  void btn_TimKiemActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_TimKiemActionPerformed
        xuLyTimKiemMauSac();
    }//GEN-LAST:event_btn_TimKiemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_CapNhat;
    private javax.swing.JButton btn_Luu;
    private javax.swing.JButton btn_Them;
    private javax.swing.JButton btn_TimKiem;
    private javax.swing.JButton btn_XoaTrang;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel lbl_MaMauSac;
    private javax.swing.JLabel lbl_TenMauSac;
    private javax.swing.JLabel lbl_TieuDe;
    private javax.swing.JPanel pnl_DanhSachMauSac;
    private javax.swing.JPanel pnl_MS_TieuDe;
    private javax.swing.JPanel pnl_NutChucNang;
    private javax.swing.JPanel pnl_ThongTin;
    private javax.swing.JScrollPane scr_DanhSachMauSac;
    private javax.swing.JTable tbl_MauSac;
    private javax.swing.JTextField txt_MaMauSac;
    private javax.swing.JTextField txt_TenMauSac;
    // End of variables declaration//GEN-END:variables

}
