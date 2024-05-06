/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package fit.iuh.qlbhtt;


import fit.iuh.dao.IKichThuocDao;
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
public  class ManHinh_QA_KichThuoc extends javax.swing.JPanel {

    private IKichThuocDao dao_KichThuoc;
    private DefaultTableModel modelKichThuoc;
    private boolean kiemTraThem = false;
    private boolean kiemTraCapNhat = false;

    /**
     * Creates new form quanly
     */
    public  ManHinh_QA_KichThuoc() throws SQLException, RemoteException {
        dao_KichThuoc = RMIClientUtil.getKichThuocDao();
        initComponents();

        tbl_KichThuoc.setDefaultEditor(Object.class, null); //Không cho chỉnh sửa cột
        tbl_KichThuoc.getTableHeader().setReorderingAllowed(false); //Không cho di chuyển cột

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
    public synchronized void kiemTraTextNhap(boolean kiemTra) {
        if (kiemTraThem || kiemTraCapNhat) {
            txt_MaKichThuoc.setEditable(!kiemTra);

        } else {
            txt_MaKichThuoc.setEditable(kiemTra);
        }

    }

    /**
     * Xóa trắng text field
     */
    public synchronized void xoaTrang() {
        txt_MaKichThuoc.setText("");
        txt_TenKichThuoc.setText("");
    }

    /**
     * Đọc dữ liệu và load dữ liệu lên table
     */
    public synchronized void docDuLieuLenBang() throws RemoteException {
        modelKichThuoc = (DefaultTableModel) tbl_KichThuoc.getModel();
        modelKichThuoc.setRowCount(0);
        for (KichThuoc kichThuoc : dao_KichThuoc.getAllKichThuoc()) {
            Object[] o = new Object[2];
            o[0] = kichThuoc.getMaKichThuoc();
            o[1] = kichThuoc.getKichThuoc();
            modelKichThuoc.addRow(o);
        }
    }

    /**
     * Xử lý thêm kich Thuoc
     */
    public synchronized void xuLyThemKichThuoc() throws RemoteException {
        String tenKichThuoc = txt_TenKichThuoc.getText();

        KichThuoc kichThuoc = new KichThuoc(tenKichThuoc);
        dao_KichThuoc.themDLKichThuoc(kichThuoc);

        modelKichThuoc = (DefaultTableModel) tbl_KichThuoc.getModel();
        Object[] object = new Object[2];
        object[0] = kichThuoc.getMaKichThuoc();
        object[1] = kichThuoc.getKichThuoc();
        modelKichThuoc.addRow(object);
        docDuLieuLenBang();
        xoaTrang();
        JOptionPane.showMessageDialog(this, "Thêm thành công");
    }

    /**
     * Xử lý xóa Kich Thuoc
     */
    public synchronized void xuLyXoaKichThuoc() throws RemoteException {
        int row = tbl_KichThuoc.getSelectedRow();
        if (row != -1) {
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc là xóa dòng này không?", "Cảnh Báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                long maKichThuoc = Long.parseLong(txt_MaKichThuoc.getText());
                dao_KichThuoc.xoaDLKichThuoc(maKichThuoc);
                modelKichThuoc.removeRow(row);
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                xoaTrang();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
        }
    }

    /**
     * Xử lý cập nhật Kich Thuoc
     */
    public synchronized void xuLyCapNhatKichThuoc() throws RemoteException {
        long maKichThuoc = Long.parseLong(txt_MaKichThuoc.getText());
        String tenKichThuoc = txt_TenKichThuoc.getText();

        KichThuoc kichThuoc = new KichThuoc(maKichThuoc, tenKichThuoc);
        int row = tbl_KichThuoc.getSelectedRow();
        if (row != -1) {
            dao_KichThuoc.capNhatDLKichThuoc(kichThuoc);
            for (int i = 0; i < tbl_KichThuoc.getRowCount(); i++) {
                long maKichThuocTable = Long.parseLong(tbl_KichThuoc.getValueAt(row, 0).toString());
                if (maKichThuocTable == maKichThuoc) {
                    tbl_KichThuoc.setValueAt(tenKichThuoc, row, 1);
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
    public synchronized void xuLyTimKiemKichThuoc() throws RemoteException {
        String convertString = null;
        if (txt_MaKichThuoc.getText().equals("")) {
            convertString = "0";
        } else {
            convertString = txt_MaKichThuoc.getText();
        }
        long tuKhoaMaKichThuoc = Long.parseLong(convertString);
        String tuKhoaTenKichThuoc = txt_TenKichThuoc.getText();

        if (tuKhoaMaKichThuoc == 0 && tuKhoaTenKichThuoc.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm");
            return;
        }

        String maKTString = null;
        if (tuKhoaMaKichThuoc == 0) {
            maKTString = "";
        }

        modelKichThuoc = (DefaultTableModel) tbl_KichThuoc.getModel();
        modelKichThuoc.setRowCount(0);
        if (maKTString != null) {
            if (tuKhoaTenKichThuoc.equals("")) {
                KichThuoc kichThuoc = dao_KichThuoc.getDLKichThuocTheoMa(tuKhoaMaKichThuoc);
                if (kichThuoc != null) {
                    modelKichThuoc.setRowCount(0);
                    Object[] object = new Object[2];
                    object[0] = kichThuoc.getMaKichThuoc();
                    object[1] = kichThuoc.getKichThuoc();
                    modelKichThuoc.addRow(object);
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy kích thước");
                }
            } else if (maKTString.equals("")) {
                KichThuoc kichThuoc = dao_KichThuoc.getKichThuocTheoTen(tuKhoaTenKichThuoc);
                if (kichThuoc != null) {
                    modelKichThuoc.setRowCount(0);
                    Object[] object = new Object[2];
                    object[0] = kichThuoc.getMaKichThuoc();
                    object[1] = kichThuoc.getKichThuoc();
                    modelKichThuoc.addRow(object);
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy kích thước");
                }
            }
        } else {
            KichThuoc kichThuoc = dao_KichThuoc.getDLKichThuocTheoMa(tuKhoaMaKichThuoc);
            if (kichThuoc != null) {
                modelKichThuoc.setRowCount(0);
                Object[] object = new Object[2];
                object[0] = kichThuoc.getMaKichThuoc();
                object[1] = kichThuoc.getKichThuoc();
                modelKichThuoc.addRow(object);
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy kích thước");
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
        pnl_DanhSachKichThuoc = new javax.swing.JPanel();
        scr_DanhSachKichThuoc = new javax.swing.JScrollPane();
        tbl_KichThuoc = new javax.swing.JTable();
        pnl_KT_TieuDe = new javax.swing.JPanel();
        lbl_TieuDe = new javax.swing.JLabel();
        pnl_ThongTin = new javax.swing.JPanel();
        txt_MaKichThuoc = new javax.swing.JTextField();
        lbl_MaKichThuoc = new javax.swing.JLabel();
        lbl_TenKichThuoc = new javax.swing.JLabel();
        txt_TenKichThuoc = new javax.swing.JTextField();
        pnl_NutChucNang = new javax.swing.JPanel();
        btn_Them = new javax.swing.JButton();
        btn_CapNhat = new javax.swing.JButton();
        btn_Luu = new javax.swing.JButton();
        btn_XoaTrang = new javax.swing.JButton();
        btn_TimKiem = new javax.swing.JButton();

        setBackground(new java.awt.Color(199, 210, 213));
        setMinimumSize(new java.awt.Dimension(1000, 550));
        setPreferredSize(new java.awt.Dimension(1000, 550));

        pnl_DanhSachKichThuoc.setPreferredSize(new java.awt.Dimension(2000, 324));

        scr_DanhSachKichThuoc.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách kích thước", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        tbl_KichThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbl_KichThuoc.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Mã kích thước", "Tên kích thước"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class
            };

            public synchronized Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        tbl_KichThuoc.setMaximumSize(new java.awt.Dimension(2147483647, 196));
        tbl_KichThuoc.setPreferredSize(new java.awt.Dimension(750, 600));
        tbl_KichThuoc.setRowHeight(35);
        tbl_KichThuoc.setShowGrid(true);
        tbl_KichThuoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_KichThuocMouseClicked(evt);
            }
        });
        scr_DanhSachKichThuoc.setViewportView(tbl_KichThuoc);

        lbl_TieuDe.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        lbl_TieuDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TieuDe.setText("QUẢN LÝ KÍCH THƯỚC");
        lbl_TieuDe.setMaximumSize(new java.awt.Dimension(32767, 32767));
        lbl_TieuDe.setPreferredSize(new java.awt.Dimension(181, 40));

        javax.swing.GroupLayout pnl_KT_TieuDeLayout = new javax.swing.GroupLayout(pnl_KT_TieuDe);
        pnl_KT_TieuDe.setLayout(pnl_KT_TieuDeLayout);
        pnl_KT_TieuDeLayout.setHorizontalGroup(
                pnl_KT_TieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_KT_TieuDeLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbl_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );
        pnl_KT_TieuDeLayout.setVerticalGroup(
                pnl_KT_TieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbl_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnl_DanhSachKichThuocLayout = new javax.swing.GroupLayout(pnl_DanhSachKichThuoc);
        pnl_DanhSachKichThuoc.setLayout(pnl_DanhSachKichThuocLayout);
        pnl_DanhSachKichThuocLayout.setHorizontalGroup(
                pnl_DanhSachKichThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnl_KT_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scr_DanhSachKichThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        pnl_DanhSachKichThuocLayout.setVerticalGroup(
                pnl_DanhSachKichThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_DanhSachKichThuocLayout.createSequentialGroup()
                                .addComponent(pnl_KT_TieuDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(scr_DanhSachKichThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
        );

        pnl_ThongTin.setBackground(new java.awt.Color(199, 210, 213));
        pnl_ThongTin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin kích thước", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        pnl_ThongTin.setPreferredSize(new java.awt.Dimension(945, 285));

        txt_MaKichThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_MaKichThuoc.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_MaKichThuoc.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_MaKichThuoc.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MaKichThuocActionPerformed(evt);
            }
        });

        lbl_MaKichThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_MaKichThuoc.setText("Mã kích thước");

        lbl_TenKichThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TenKichThuoc.setText("Tên kích thước");

        txt_TenKichThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_TenKichThuoc.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_TenKichThuoc.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_TenKichThuoc.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TenKichThuocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_ThongTinLayout = new javax.swing.GroupLayout(pnl_ThongTin);
        pnl_ThongTin.setLayout(pnl_ThongTinLayout);
        pnl_ThongTinLayout.setHorizontalGroup(
                pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGap(100, 100, 100)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbl_MaKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt_MaKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(106, 106, 106)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbl_TenKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt_TenKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_ThongTinLayout.setVerticalGroup(
                pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_ThongTinLayout.createSequentialGroup()
                                                .addComponent(lbl_TenKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txt_TenKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_ThongTinLayout.createSequentialGroup()
                                                .addComponent(lbl_MaKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txt_MaKichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(184, Short.MAX_VALUE))
        );

        pnl_NutChucNang.setBackground(new java.awt.Color(199, 210, 213));
        pnl_NutChucNang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chức năng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        btn_Them.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_Them.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-add-30.png")); // NOI18N
        btn_Them.setText("Thêm");
        btn_Them.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_Them.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_ThemMouseEntered(evt);
            }

            public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
                btn_ThemMouseExited(evt);
            }
        });
        btn_Them.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemActionPerformed(evt);
            }
        });

        btn_CapNhat.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_CapNhat.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-update-30.png")); // NOI18N
        btn_CapNhat.setText("Cập nhật");
        btn_CapNhat.setBorder(null);
        btn_CapNhat.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_CapNhatMouseEntered(evt);
            }

            public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
                btn_CapNhatMouseExited(evt);
            }
        });
        btn_CapNhat.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CapNhatActionPerformed(evt);
            }
        });

        btn_Luu.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_Luu.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-save-30.png")); // NOI18N
        btn_Luu.setText("Lưu");
        btn_Luu.setBorder(null);
        btn_Luu.setEnabled(false);
        btn_Luu.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_LuuMouseEntered(evt);
            }

            public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
                btn_LuuMouseExited(evt);
            }
        });
        btn_Luu.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
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
            public synchronized void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_XoaTrangMouseEntered(evt);
            }

            public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
                btn_XoaTrangMouseExited(evt);
            }
        });
        btn_XoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
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
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
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
                        .addComponent(pnl_DanhSachKichThuoc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(pnl_DanhSachKichThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnl_ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pnl_NutChucNang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private synchronized  void txt_MaKichThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MaKichThuocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_MaKichThuocActionPerformed

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
            xuLyThemKichThuoc();
        } else if (kiemTraCapNhat) {
            xuLyCapNhatKichThuoc();
        }
    }//GEN-LAST:event_btn_LuuActionPerformed

    private synchronized  void txt_TenKichThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TenKichThuocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TenKichThuocActionPerformed

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

    private synchronized  void tbl_KichThuocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_KichThuocMouseClicked
        int row = tbl_KichThuoc.getSelectedRow();
        if (row != -1) {
            txt_MaKichThuoc.setText(tbl_KichThuoc.getValueAt(row, 0).toString());
            txt_TenKichThuoc.setText(tbl_KichThuoc.getValueAt(row, 1).toString());
        }
    }//GEN-LAST:event_tbl_KichThuocMouseClicked

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
        xuLyTimKiemKichThuoc();
    }//GEN-LAST:event_btn_TimKiemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_CapNhat;
    private javax.swing.JButton btn_Luu;
    private javax.swing.JButton btn_Them;
    private javax.swing.JButton btn_TimKiem;
    private javax.swing.JButton btn_XoaTrang;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel lbl_MaKichThuoc;
    private javax.swing.JLabel lbl_TenKichThuoc;
    private javax.swing.JLabel lbl_TieuDe;
    private javax.swing.JPanel pnl_DanhSachKichThuoc;
    private javax.swing.JPanel pnl_KT_TieuDe;
    private javax.swing.JPanel pnl_NutChucNang;
    private javax.swing.JPanel pnl_ThongTin;
    private javax.swing.JScrollPane scr_DanhSachKichThuoc;
    private javax.swing.JTable tbl_KichThuoc;
    private javax.swing.JTextField txt_MaKichThuoc;
    private javax.swing.JTextField txt_TenKichThuoc;
    // End of variables declaration//GEN-END:variables

}
