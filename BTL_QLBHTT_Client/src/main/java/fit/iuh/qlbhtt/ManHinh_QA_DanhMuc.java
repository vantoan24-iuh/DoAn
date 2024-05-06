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
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * @author DMX
 */
public   class ManHinh_QA_DanhMuc extends javax.swing.JPanel {
    private IPhanLoaiDao dao_PhanLoai;
    private DefaultTableModel modelPhanLoai;
    private boolean kiemTraThem = false;
    private boolean kiemTraCapNhat = false;

    /**
     * Creates new form quanly
     */
    public   ManHinh_QA_DanhMuc() throws SQLException, RemoteException {
        dao_PhanLoai = RMIClientUtil.getPhanLoaiDao();
//        connect = new Connect();
//        connect.connect();
        initComponents();

        tbl_DanhMuc.setDefaultEditor(Object.class, null); //Không cho chỉnh sửa cột
        tbl_DanhMuc.getTableHeader().setReorderingAllowed(false); //Không cho di chuyển cột

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
            txt_MaDanhMuc.setEditable(!kiemTra);
        } else {
            txt_MaDanhMuc.setEditable(kiemTra);
        }
    }

    /**
     * Xóa trắng text field
     */
    public synchronized  void xoaTrang() {
        txt_MaDanhMuc.setText("");
        txt_TenDanhMuc.setText("");
    }

    /**
     * Đọc dữ liệu và load dữ liệu lên table
     */
    public synchronized  void docDuLieuLenBang() throws RemoteException {
        modelPhanLoai = (DefaultTableModel) tbl_DanhMuc.getModel();
        modelPhanLoai.setRowCount(0);
        for (PhanLoai phanLoai : dao_PhanLoai.getAllPhanLoai()) {
            Object[] o = new Object[2];
            o[0] = phanLoai.getMaPhanLoai();
            o[1] = phanLoai.getLoaiSanPham();
            modelPhanLoai.addRow(o);
        }
    }

    /**
     * Xử lý thêm Chất Liệu
     */
    public synchronized  void xuLyThemDanhMuc() throws RemoteException {
        String tenChatLieu = txt_TenDanhMuc.getText();

        PhanLoai phanLoai = new PhanLoai(tenChatLieu);
        dao_PhanLoai.themLoaiSanPham(phanLoai);

        modelPhanLoai = (DefaultTableModel) tbl_DanhMuc.getModel();
        Object[] object = new Object[2];
        object[0] = phanLoai.getMaPhanLoai();
        object[1] = phanLoai.getLoaiSanPham();

        modelPhanLoai.addRow(object);
        docDuLieuLenBang();
        xoaTrang();
        JOptionPane.showMessageDialog(this, "Thêm thành công");
    }

    /**
     * Xử lý xóa Chất Liệu
     */
    public synchronized  void xuLyXoaChatLieu() throws RemoteException {
        int row = tbl_DanhMuc.getSelectedRow();
        if (row != -1) {
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc là xóa dòng này không?", "Cảnh Báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                long maPhanLoai = Long.parseLong(txt_MaDanhMuc.getText());
                dao_PhanLoai.xoaPhanLoaiSanPham(maPhanLoai);
                modelPhanLoai.removeRow(row);
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                xoaTrang();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
        }
    }

    /**
     * Xử lý cập nhật danh mục
     */
    public synchronized  void xuLyCapNhatChatLieu() throws RemoteException {
        long maDanhMuc = Long.parseLong(txt_MaDanhMuc.getText());
        String tenDanhMuc = txt_TenDanhMuc.getText();

        PhanLoai phanLoai = new PhanLoai(maDanhMuc, tenDanhMuc);
        int row = tbl_DanhMuc.getSelectedRow();
        if (row != -1) {
            dao_PhanLoai.catNhatLoaiSanPham(phanLoai);
            for (int i = 0; i < tbl_DanhMuc.getRowCount(); i++) {
                long maPLTable = Long.parseLong(tbl_DanhMuc.getValueAt(row, 0).toString());
                if (maPLTable == maDanhMuc) {
                    tbl_DanhMuc.setValueAt(tenDanhMuc, row, 1);
                }

            }
            xoaTrang();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần cập nhật!");
        }

    }

    public synchronized  void xuLyTimKiemChatLieu() throws RemoteException {
        String msConvert = null;
        if (txt_MaDanhMuc.getText().equals("")) {
            msConvert = "0";
        } else {
            msConvert = txt_MaDanhMuc.getText();
        }
        long tuKhoaMaPhanLoai = Long.parseLong(msConvert);
        String tuKhoaTenPhanLoai = txt_TenDanhMuc.getText();

        if (tuKhoaMaPhanLoai == 0 && tuKhoaTenPhanLoai.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm");
            return;
        }

        String maPLString = null;
        if (tuKhoaMaPhanLoai == 0) {
            maPLString = "";
        }

        modelPhanLoai = (DefaultTableModel) tbl_DanhMuc.getModel();
        modelPhanLoai.setRowCount(0);
        if (maPLString != null) {
            if (tuKhoaTenPhanLoai.equals("")) {
                PhanLoai chatLieu = dao_PhanLoai.getDLPhanLoaiSPTheoMa(tuKhoaMaPhanLoai);
                if (chatLieu != null) {
                    modelPhanLoai.setRowCount(0);
                    Object[] object = new Object[2];
                    object[0] = chatLieu.getMaPhanLoai();
                    object[1] = chatLieu.getLoaiSanPham();
                    modelPhanLoai.addRow(object);
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu");
                }
            } else if (maPLString.equals("")) {
                PhanLoai chatLieu = dao_PhanLoai.getPhanLoaiTheoTen(tuKhoaTenPhanLoai);
                if (chatLieu != null) {
                    modelPhanLoai.setRowCount(0);
                    Object[] object = new Object[2];
                    object[0] = chatLieu.getMaPhanLoai();
                    object[1] = chatLieu.getLoaiSanPham();
                    modelPhanLoai.addRow(object);
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu");
                }
            }
        } else {
            PhanLoai chatLieu = dao_PhanLoai.getDLPhanLoaiSPTheoMa(tuKhoaMaPhanLoai);
            if (chatLieu != null) {
                modelPhanLoai.setRowCount(0);
                Object[] object = new Object[2];
                object[0] = chatLieu.getMaPhanLoai();
                object[1] = chatLieu.getLoaiSanPham();
                modelPhanLoai.addRow(object);
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu");
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
        pnl_DanhSachDanhMuc = new javax.swing.JPanel();
        scr_DanhSachDanhMuc = new javax.swing.JScrollPane();
        tbl_DanhMuc = new javax.swing.JTable();
        pnl_DM_TieuDe = new javax.swing.JPanel();
        lbl_TieuDe = new javax.swing.JLabel();
        pnl_ThongTin = new javax.swing.JPanel();
        txt_MaDanhMuc = new javax.swing.JTextField();
        lbl_MaDanhMuc = new javax.swing.JLabel();
        lbl_TenDanhMuc = new javax.swing.JLabel();
        txt_TenDanhMuc = new javax.swing.JTextField();
        pnl_NutChucNang = new javax.swing.JPanel();
        btn_Them = new javax.swing.JButton();
        btn_CapNhat = new javax.swing.JButton();
        btn_Luu = new javax.swing.JButton();
        btn_XoaTrang = new javax.swing.JButton();
        btn_TimKiem = new javax.swing.JButton();

        setBackground(new java.awt.Color(199, 210, 213));
        setMinimumSize(new java.awt.Dimension(1000, 550));
        setPreferredSize(new java.awt.Dimension(1000, 550));

        pnl_DanhSachDanhMuc.setPreferredSize(new java.awt.Dimension(2000, 324));

        scr_DanhSachDanhMuc.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách danh mục", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        tbl_DanhMuc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbl_DanhMuc.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Mã danh mục", "Tên danh mục"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class
            };

            public synchronized  Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        tbl_DanhMuc.setMaximumSize(new java.awt.Dimension(2147483647, 196));
        tbl_DanhMuc.setPreferredSize(new java.awt.Dimension(750, 600));
        tbl_DanhMuc.setRowHeight(35);
        tbl_DanhMuc.setShowGrid(true);
        tbl_DanhMuc.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized  void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DanhMucMouseClicked(evt);
            }
        });
        scr_DanhSachDanhMuc.setViewportView(tbl_DanhMuc);

        lbl_TieuDe.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        lbl_TieuDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TieuDe.setText("QUẢN LÝ DANH MỤC");
        lbl_TieuDe.setMaximumSize(new java.awt.Dimension(32767, 32767));
        lbl_TieuDe.setPreferredSize(new java.awt.Dimension(181, 40));

        javax.swing.GroupLayout pnl_DM_TieuDeLayout = new javax.swing.GroupLayout(pnl_DM_TieuDe);
        pnl_DM_TieuDe.setLayout(pnl_DM_TieuDeLayout);
        pnl_DM_TieuDeLayout.setHorizontalGroup(
                pnl_DM_TieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_DM_TieuDeLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbl_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );
        pnl_DM_TieuDeLayout.setVerticalGroup(
                pnl_DM_TieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbl_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnl_DanhSachDanhMucLayout = new javax.swing.GroupLayout(pnl_DanhSachDanhMuc);
        pnl_DanhSachDanhMuc.setLayout(pnl_DanhSachDanhMucLayout);
        pnl_DanhSachDanhMucLayout.setHorizontalGroup(
                pnl_DanhSachDanhMucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnl_DM_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scr_DanhSachDanhMuc, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        pnl_DanhSachDanhMucLayout.setVerticalGroup(
                pnl_DanhSachDanhMucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_DanhSachDanhMucLayout.createSequentialGroup()
                                .addComponent(pnl_DM_TieuDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(scr_DanhSachDanhMuc, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
        );

        pnl_ThongTin.setBackground(new java.awt.Color(199, 210, 213));
        pnl_ThongTin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin danh mục", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        pnl_ThongTin.setPreferredSize(new java.awt.Dimension(945, 285));

        txt_MaDanhMuc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_MaDanhMuc.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_MaDanhMuc.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_MaDanhMuc.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MaDanhMucActionPerformed(evt);
            }
        });

        lbl_MaDanhMuc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_MaDanhMuc.setText("Mã danh mục");

        lbl_TenDanhMuc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TenDanhMuc.setText("Tên danh mục");

        txt_TenDanhMuc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_TenDanhMuc.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_TenDanhMuc.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_TenDanhMuc.addActionListener(new java.awt.event.ActionListener() {
            public synchronized  void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TenDanhMucActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_ThongTinLayout = new javax.swing.GroupLayout(pnl_ThongTin);
        pnl_ThongTin.setLayout(pnl_ThongTinLayout);
        pnl_ThongTinLayout.setHorizontalGroup(
                pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGap(100, 100, 100)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbl_MaDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt_MaDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(106, 106, 106)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbl_TenDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt_TenDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_ThongTinLayout.setVerticalGroup(
                pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_ThongTinLayout.createSequentialGroup()
                                                .addComponent(lbl_TenDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txt_TenDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_ThongTinLayout.createSequentialGroup()
                                                .addComponent(lbl_MaDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txt_MaDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                        .addComponent(pnl_DanhSachDanhMuc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(pnl_DanhSachDanhMuc, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnl_ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pnl_NutChucNang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private synchronized  void txt_MaDanhMucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MaDanhMucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_MaDanhMucActionPerformed

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
            xuLyThemDanhMuc();
        } else if (kiemTraCapNhat) {
            xuLyCapNhatChatLieu();
        }

    }//GEN-LAST:event_btn_LuuActionPerformed

    private synchronized  void txt_TenDanhMucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TenDanhMucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TenDanhMucActionPerformed

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

    private synchronized  void tbl_DanhMucMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DanhMucMouseClicked
        int row = tbl_DanhMuc.getSelectedRow();
        if (row != -1) {
            txt_MaDanhMuc.setText(tbl_DanhMuc.getValueAt(row, 0).toString());
            txt_TenDanhMuc.setText(tbl_DanhMuc.getValueAt(row, 1).toString());
        }
    }//GEN-LAST:event_tbl_DanhMucMouseClicked

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
        xuLyTimKiemChatLieu();
    }//GEN-LAST:event_btn_TimKiemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_CapNhat;
    private javax.swing.JButton btn_Luu;
    private javax.swing.JButton btn_Them;
    private javax.swing.JButton btn_TimKiem;
    private javax.swing.JButton btn_XoaTrang;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel lbl_MaDanhMuc;
    private javax.swing.JLabel lbl_TenDanhMuc;
    private javax.swing.JLabel lbl_TieuDe;
    private javax.swing.JPanel pnl_DM_TieuDe;
    private javax.swing.JPanel pnl_DanhSachDanhMuc;
    private javax.swing.JPanel pnl_NutChucNang;
    private javax.swing.JPanel pnl_ThongTin;
    private javax.swing.JScrollPane scr_DanhSachDanhMuc;
    private javax.swing.JTable tbl_DanhMuc;
    private javax.swing.JTextField txt_MaDanhMuc;
    private javax.swing.JTextField txt_TenDanhMuc;
    // End of variables declaration//GEN-END:variables


}
