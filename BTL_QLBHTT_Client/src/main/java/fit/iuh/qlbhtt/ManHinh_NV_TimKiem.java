/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package fit.iuh.qlbhtt;

import fit.iuh.dao.*;
import fit.iuh.entity.*;
import fit.iuh.util.RMIClientUtil;

import java.awt.Color;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DMX
 */
public  class ManHinh_NV_TimKiem extends javax.swing.JPanel {

    private DefaultTableModel modelNhanVien;
    private INhanVienDao daoNhanVien;
    public  static boolean activeThayDoiTT = false;
    public  static NhanVien nhanVien = null;
    private final NhanVien nhanVien_Login = Login.nhanVien;

    /**
     * Creates new form quanly
     */
    public  ManHinh_NV_TimKiem() throws SQLException, RemoteException {
        daoNhanVien = RMIClientUtil.getNhanVienDao();
        initComponents();
        activeThayDoiTT = false;
        tbl_NhanVien.setDefaultEditor(Object.class, null); //Không cho chỉnh sửa cột
        tbl_NhanVien.getTableHeader().setReorderingAllowed(false); //Không cho di chuyển cột

        docDuLieuNhanVien();
        loadDuLieuChucVu();
        setRole();
    }

    /**
     * Cài đặt role khi nhân viên đăng nhập
     */
    public synchronized void setRole() {
        if (nhanVien_Login.getChuVu().equalsIgnoreCase("Nhân Viên")) {
            btn_CapNhat.setEnabled(false);
        }
    }

    /**
     * Load dữ liệu comboBox chức vụ
     */
    public synchronized void loadDuLieuChucVu() throws RemoteException {
        String giaTriKiemTa = null;
        for (NhanVien nv : daoNhanVien.getAllNhanVien()) {
            if (giaTriKiemTa != null && giaTriKiemTa.equals(nv.getChuVu())) {
                continue;
            }
            giaTriKiemTa = nv.getChuVu();
            cmb_ChucVu.addItem(nv.getChuVu());
        }

    }

    /**
     * Xóa trắng các Jtext filed
     */
    public synchronized void xoaTrangTxt() {
        txt_TenNV.setText("");
        txt_SoDienThoai.setText("");
        txt_DiaChi.setText("");
        txt_Email.setText("");
        cmb_ChucVu.setSelectedIndex(0);
        txt_MaNV.setText("");
        rad_Nam.setSelected(false);
        rad_Nu.setSelected(false);
    }

    public synchronized void xoaDongBang() {
        modelNhanVien = (DefaultTableModel) tbl_NhanVien.getModel();
        modelNhanVien.setRowCount(0);
    }

    /**
     * Load dữ liệu vào bảng
     */
    public synchronized void docDuLieuNhanVien() throws RemoteException {

        modelNhanVien = (DefaultTableModel) tbl_NhanVien.getModel();
        modelNhanVien.setRowCount(0);
        for (NhanVien nv : daoNhanVien.getAllNhanVien()) {
            Object[] object = new Object[8];
            object[0] = nv.getMaNV();
            object[1] = nv.getHoTen();
            object[2] = nv.getGioiTinh();
            object[3] = nv.getChuVu();
            object[4] = nv.getDiaChi();
            object[5] = nv.getSdt();
            object[6] = nv.getEmail();
            object[7] = nv.isTrangThai() ? "Đang làm" : "Nghỉ làm";
            System.out.println("nv " + nv.toString());
            modelNhanVien.addRow(object);
        }
    }

    /**
     * Xử lý tìm kiếm nhân viên
     */
    public synchronized void xuLyTimKiemNhanVien() throws RemoteException {
        xoaDongBang();

        String msConvert = null;
        if(txt_MaNV.getText().equals("")) {
            msConvert = "0";
        } else {
            msConvert = txt_MaNV.getText();
        }
        long tuKhoaMaNV = Long.parseLong(msConvert);
        String tuKhoaTenNV = txt_TenNV.getText();
        String tuKhoaChucVu = cmb_ChucVu.getSelectedItem().toString();
        if (tuKhoaChucVu.equalsIgnoreCase("Tất cả")) {
            tuKhoaChucVu = "";
        }
        String tuKhoaSdt = txt_SoDienThoai.getText();
        String tuKhoaEmail = txt_Email.getText();
        String tuKhoaDiaChi = txt_DiaChi.getText();
        boolean tuKhoaTrangThai = rad_TrangThai.isSelected();
        modelNhanVien = (DefaultTableModel) tbl_NhanVien.getModel();
        ArrayList<NhanVien> listNhanVien = daoNhanVien.timKiemNhanVien(tuKhoaMaNV, tuKhoaTenNV, tuKhoaSdt, tuKhoaEmail, tuKhoaChucVu, tuKhoaDiaChi, tuKhoaTrangThai);
        if (listNhanVien != null) {
            for (NhanVien nv : listNhanVien) {
                Object[] object = new Object[8];
                object[0] = nv.getMaNV();
                object[1] = nv.getHoTen();
                object[2] = nv.getGioiTinh();
                object[3] = nv.getChuVu();
                object[4] = nv.getDiaChi();
                object[5] = nv.getSdt();
                object[6] = nv.getEmail();
                object[7] = nv.isTrangThai() ? "Còn hoạt động" : "Nghỉ làm";
                modelNhanVien.addRow(object);
                System.out.println(nv.toString());
            }

        }
        xoaTrangTxt();

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
        pnl_DanhSachNhanVien = new javax.swing.JPanel();
        scr_DanhSachNhanVien = new javax.swing.JScrollPane();
        tbl_NhanVien = new javax.swing.JTable();
        pnl_NV_TieuDe = new javax.swing.JPanel();
        lbl_TieuDe = new javax.swing.JLabel();
        pnl_ThongTin = new javax.swing.JPanel();
        txt_TenNV = new javax.swing.JTextField();
        txt_MaNV = new javax.swing.JTextField();
        lbl_MaNV = new javax.swing.JLabel();
        lbl_ChucVu = new javax.swing.JLabel();
        lbl_TenNV = new javax.swing.JLabel();
        lbl_DiaChi = new javax.swing.JLabel();
        lbl_GioiTInh = new javax.swing.JLabel();
        lbl_SoDienThoai = new javax.swing.JLabel();
        lbl_Email = new javax.swing.JLabel();
        rad_Nam = new javax.swing.JRadioButton();
        rad_Nu = new javax.swing.JRadioButton();
        txt_SoDienThoai = new javax.swing.JTextField();
        txt_Email = new javax.swing.JTextField();
        txt_DiaChi = new javax.swing.JTextField();
        cmb_ChucVu = new javax.swing.JComboBox<>();
        lbl_TrangThai = new javax.swing.JLabel();
        rad_TrangThai = new javax.swing.JRadioButton();
        pnl_NutChucNang = new javax.swing.JPanel();
        btn_TimKiem = new javax.swing.JButton();
        btn_XoaTrang = new javax.swing.JButton();
        btn_CapNhat = new javax.swing.JButton();

        setBackground(new java.awt.Color(199, 210, 213));
        setMinimumSize(new java.awt.Dimension(1000, 550));
        setPreferredSize(new java.awt.Dimension(1000, 550));

        tbl_NhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbl_NhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã nhân viên", "Tên nhân viên", "Giới tính", "Chức vụ", "Địa chỉ", "Số điện thoại", "Email", "Trạng thái"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public synchronized Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbl_NhanVien.setRowHeight(35);
        tbl_NhanVien.setShowGrid(true);
        tbl_NhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_NhanVienMouseClicked(evt);
            }
            public synchronized void mousePressed(java.awt.event.MouseEvent evt) {
                tbl_NhanVienMousePressed(evt);
            }
        });
        scr_DanhSachNhanVien.setViewportView(tbl_NhanVien);

        lbl_TieuDe.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        lbl_TieuDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TieuDe.setText("TÌM KIẾM NHÂN VIÊN");
        lbl_TieuDe.setMaximumSize(new java.awt.Dimension(32767, 32767));
        lbl_TieuDe.setPreferredSize(new java.awt.Dimension(181, 40));

        javax.swing.GroupLayout pnl_NV_TieuDeLayout = new javax.swing.GroupLayout(pnl_NV_TieuDe);
        pnl_NV_TieuDe.setLayout(pnl_NV_TieuDeLayout);
        pnl_NV_TieuDeLayout.setHorizontalGroup(
            pnl_NV_TieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_NV_TieuDeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnl_NV_TieuDeLayout.setVerticalGroup(
            pnl_NV_TieuDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnl_DanhSachNhanVienLayout = new javax.swing.GroupLayout(pnl_DanhSachNhanVien);
        pnl_DanhSachNhanVien.setLayout(pnl_DanhSachNhanVienLayout);
        pnl_DanhSachNhanVienLayout.setHorizontalGroup(
            pnl_DanhSachNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scr_DanhSachNhanVien)
            .addComponent(pnl_NV_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnl_DanhSachNhanVienLayout.setVerticalGroup(
            pnl_DanhSachNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_DanhSachNhanVienLayout.createSequentialGroup()
                .addComponent(pnl_NV_TieuDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scr_DanhSachNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
        );

        pnl_ThongTin.setBackground(new java.awt.Color(199, 210, 213));
        pnl_ThongTin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txt_TenNV.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txt_MaNV.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lbl_MaNV.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_MaNV.setText("Mã nhân viên");

        lbl_ChucVu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_ChucVu.setText("Chức vụ");

        lbl_TenNV.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TenNV.setText("Tên nhân viên");

        lbl_DiaChi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_DiaChi.setText("Địa chỉ");

        lbl_GioiTInh.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_GioiTInh.setText("Giới tính");

        lbl_SoDienThoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_SoDienThoai.setText("Số điện thoại");

        lbl_Email.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_Email.setText("Email");

        rad_Nam.setBackground(new java.awt.Color(199, 210, 213));
        buttonGroup1.add(rad_Nam);
        rad_Nam.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rad_Nam.setText("Nam");
        rad_Nam.setEnabled(false);

        rad_Nu.setBackground(new java.awt.Color(199, 210, 213));
        buttonGroup1.add(rad_Nu);
        rad_Nu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rad_Nu.setText("Nữ");
        rad_Nu.setEnabled(false);

        txt_SoDienThoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txt_Email.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txt_DiaChi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        cmb_ChucVu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmb_ChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        cmb_ChucVu.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_ChucVuItemStateChanged(evt);
            }
        });

        lbl_TrangThai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TrangThai.setText("Trạng thái làm việc");

        rad_TrangThai.setBackground(new java.awt.Color(199, 210, 213));
        rad_TrangThai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rad_TrangThai.setText("Đang làm");

        javax.swing.GroupLayout pnl_ThongTinLayout = new javax.swing.GroupLayout(pnl_ThongTin);
        pnl_ThongTin.setLayout(pnl_ThongTinLayout);
        pnl_ThongTinLayout.setHorizontalGroup(
            pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_MaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_MaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_DiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_DiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_ChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmb_ChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_SoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_SoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(52, 52, 52)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rad_TrangThai)
                    .addComponent(lbl_GioiTInh, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addComponent(rad_Nam)
                        .addGap(18, 18, 18)
                        .addComponent(rad_Nu))
                    .addComponent(lbl_TrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_ThongTinLayout.setVerticalGroup(
            pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_ThongTinLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_MaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_DiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_GioiTInh, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_MaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rad_Nam)
                    .addComponent(rad_Nu)
                    .addComponent(txt_DiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_SoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_SoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addComponent(lbl_TrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rad_TrangThai)))
                .addGap(32, 32, 32)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_ChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmb_ChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        pnl_NutChucNang.setBackground(new java.awt.Color(199, 210, 213));

        btn_TimKiem.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_TimKiem.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-search-30.png")); // NOI18N
        btn_TimKiem.setText("Tìm kiếm");
        btn_TimKiem.setBorder(null);
        btn_TimKiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_TimKiemMouseEntered(evt);
            }
            public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
                btn_TimKiemMouseExited(evt);
            }
        });
        btn_TimKiem.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_TimKiemActionPerformed(evt);
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

        btn_CapNhat.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_CapNhat.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-reset-30.png")); // NOI18N
        btn_CapNhat.setText("Thay đổi trạng thái");
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

        javax.swing.GroupLayout pnl_NutChucNangLayout = new javax.swing.GroupLayout(pnl_NutChucNang);
        pnl_NutChucNang.setLayout(pnl_NutChucNangLayout);
        pnl_NutChucNangLayout.setHorizontalGroup(
            pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btn_XoaTrang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_TimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_CapNhat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 60, Short.MAX_VALUE))
        );
        pnl_NutChucNangLayout.setVerticalGroup(
            pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(btn_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btn_XoaTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btn_CapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnl_DanhSachNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_NutChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_DanhSachNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnl_ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(pnl_NutChucNang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private synchronized  void btn_TimKiemActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_TimKiemActionPerformed
        xuLyTimKiemNhanVien();
    }//GEN-LAST:event_btn_TimKiemActionPerformed

    private synchronized  void btn_XoaTrangActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_XoaTrangActionPerformed
        xoaTrangTxt();
        docDuLieuNhanVien();
    }//GEN-LAST:event_btn_XoaTrangActionPerformed

    private synchronized  void btn_TimKiemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_TimKiemMouseEntered
        btn_TimKiem.setBackground(new Color(0x9EDDFF));
        btn_TimKiem.setForeground(new Color(0x141E46));
    }//GEN-LAST:event_btn_TimKiemMouseEntered

    private synchronized  void btn_TimKiemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_TimKiemMouseExited
        btn_TimKiem.setBackground(UIManager.getColor("Menu.background"));
        btn_TimKiem.setForeground(UIManager.getColor("Menu.foreground"));
    }//GEN-LAST:event_btn_TimKiemMouseExited

    private synchronized  void btn_XoaTrangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_XoaTrangMouseEntered
        btn_XoaTrang.setBackground(new Color(0x9EDDFF));
        btn_XoaTrang.setForeground(new Color(0x141E46));
    }//GEN-LAST:event_btn_XoaTrangMouseEntered

    private synchronized  void btn_XoaTrangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_XoaTrangMouseExited
        btn_XoaTrang.setBackground(UIManager.getColor("Menu.background"));
        btn_XoaTrang.setForeground(UIManager.getColor("Menu.foreground"));
    }//GEN-LAST:event_btn_XoaTrangMouseExited

    private synchronized  void cmb_ChucVuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_ChucVuItemStateChanged

    }//GEN-LAST:event_cmb_ChucVuItemStateChanged

    private synchronized  void tbl_NhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_NhanVienMouseClicked
        int row = tbl_NhanVien.getSelectedRow();
        if (row != -1) {
            txt_MaNV.setText(tbl_NhanVien.getValueAt(row, 0).toString());
            txt_TenNV.setText(tbl_NhanVien.getValueAt(row, 1).toString());

            if (tbl_NhanVien.getValueAt(row, 2).toString().equalsIgnoreCase("Nam")) {
                rad_Nam.setSelected(true);
            } else if (tbl_NhanVien.getValueAt(row, 2).toString().equalsIgnoreCase("Nữ")) {
                rad_Nu.setSelected(true);
            }
            cmb_ChucVu.setSelectedItem(tbl_NhanVien.getValueAt(row, 3).toString());
            txt_DiaChi.setText(tbl_NhanVien.getValueAt(row, 4).toString());
            txt_SoDienThoai.setText(tbl_NhanVien.getValueAt(row, 5).toString());
            txt_Email.setText(tbl_NhanVien.getValueAt(row, 6).toString());
            if (tbl_NhanVien.getValueAt(row, 7).toString().equalsIgnoreCase("Đang làm")) {
                rad_TrangThai.setSelected(true);
            } else if (tbl_NhanVien.getValueAt(row, 7).toString().equalsIgnoreCase("Nghỉ làm")) {
                rad_TrangThai.setSelected(false);
            }
        }
    }//GEN-LAST:event_tbl_NhanVienMouseClicked

    private synchronized  void btn_CapNhatMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CapNhatMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CapNhatMouseEntered

    private synchronized  void btn_CapNhatMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CapNhatMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_CapNhatMouseExited

    private synchronized  void btn_CapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CapNhatActionPerformed
        int row = tbl_NhanVien.getSelectedRow();
        activeThayDoiTT = true;

        if (row != -1) {
            HomePage.activeQLNVForm();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần thay đổi trạng thái làm việc");
        }

    }//GEN-LAST:event_btn_CapNhatActionPerformed

    private synchronized  void tbl_NhanVienMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_NhanVienMousePressed
        int row = tbl_NhanVien.getSelectedRow();

        if (row != -1) {
            long maNV = Long.parseLong(tbl_NhanVien.getValueAt(row, 0).toString());
            String TenNV = tbl_NhanVien.getValueAt(row, 1).toString();
            String gioiTinh = tbl_NhanVien.getValueAt(row, 2).toString();

            String chucVu = tbl_NhanVien.getValueAt(row, 3).toString();
            String diaChi = tbl_NhanVien.getValueAt(row, 4).toString();
            String sdt = tbl_NhanVien.getValueAt(row, 5).toString();
            String email = tbl_NhanVien.getValueAt(row, 6).toString();
            boolean trangThai = false;
            if (tbl_NhanVien.getValueAt(row, 7).toString().equalsIgnoreCase("Còn hoạt động")) {
                trangThai = true;
            } else if (tbl_NhanVien.getValueAt(row, 7).toString().equalsIgnoreCase("Nghỉ làm")) {
                trangThai = false;
            }
            nhanVien = new NhanVien(maNV, TenNV, chucVu, email, sdt, diaChi, gioiTinh, trangThai);
        }
    }//GEN-LAST:event_tbl_NhanVienMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public  static javax.swing.JButton btn_CapNhat;
    private javax.swing.JButton btn_TimKiem;
    private javax.swing.JButton btn_XoaTrang;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmb_ChucVu;
    private javax.swing.JLabel lbl_ChucVu;
    private javax.swing.JLabel lbl_DiaChi;
    private javax.swing.JLabel lbl_Email;
    private javax.swing.JLabel lbl_GioiTInh;
    private javax.swing.JLabel lbl_MaNV;
    private javax.swing.JLabel lbl_SoDienThoai;
    private javax.swing.JLabel lbl_TenNV;
    private javax.swing.JLabel lbl_TieuDe;
    private javax.swing.JLabel lbl_TrangThai;
    private javax.swing.JPanel pnl_DanhSachNhanVien;
    private javax.swing.JPanel pnl_NV_TieuDe;
    private javax.swing.JPanel pnl_NutChucNang;
    private javax.swing.JPanel pnl_ThongTin;
    private javax.swing.JRadioButton rad_Nam;
    private javax.swing.JRadioButton rad_Nu;
    private javax.swing.JRadioButton rad_TrangThai;
    private javax.swing.JScrollPane scr_DanhSachNhanVien;
    private javax.swing.JTable tbl_NhanVien;
    private javax.swing.JTextField txt_DiaChi;
    private javax.swing.JTextField txt_Email;
    private javax.swing.JTextField txt_MaNV;
    private javax.swing.JTextField txt_SoDienThoai;
    private javax.swing.JTextField txt_TenNV;
    // End of variables declaration//GEN-END:variables
}
