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
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DMX
 */
public  class ManHinh_NV_XemDSHoaDon extends javax.swing.JPanel {

    private IHoaDonDao daoHoaDon;
    private ICTHDDao daoCTHD;
    private IKhachHangDao daoKhachHang;
    private ISanPhamDao daoSanPham;
    private INhanVienDao daoNhanVien;
    private DefaultTableModel modelHoaDon;
    private DefaultTableModel modelCTHD;

    /**
     * Creates new form quanly
     */
    public  ManHinh_NV_XemDSHoaDon() throws SQLException, RemoteException {
        daoHoaDon = RMIClientUtil.getHoaDonDao();
        daoCTHD = RMIClientUtil.getCtHoaDonDao();
        daoKhachHang = RMIClientUtil.getKhachHangDao();
        daoSanPham = RMIClientUtil.getSanPhamDao();
        daoNhanVien = RMIClientUtil.getNhanVienDao();
        initComponents();

        modelHoaDon = (DefaultTableModel) tbl_HoaDon.getModel();
        modelCTHD = (DefaultTableModel) tbl_CTHD.getModel();

        tbl_HoaDon.setDefaultEditor(Object.class, null); //Không cho chỉnh sửa cột
        tbl_HoaDon.getTableHeader().setReorderingAllowed(false); //Không cho di chuyển cột

        tbl_CTHD.setDefaultEditor(Object.class, null); //Không cho chỉnh sửa cột
        tbl_CTHD.getTableHeader().setReorderingAllowed(false); //Không cho di chuyển cột

        khoiTaoNgayHienTai();
        docDuLieuLenBangDsHoaDon();
    }

    //Setup ngày hiện tại khi load lên
    public synchronized void khoiTaoNgayHienTai() {
        dch_TuNgay.setDate(new Date());
        dch_DenNgay.setDate(new Date());
    }

    /**
     * Điều kiện từ ngày
     */
    public synchronized boolean dieuKienTuNgay() {
        Date ngayHienTai = new Date();
        Date tuNgay = dch_TuNgay.getDate();

        if (tuNgay.after(ngayHienTai)) {
            JOptionPane.showMessageDialog(this, "Ngày phải trước ngày hiện tại!");
            dch_TuNgay.setDate(new Date());
            return false;
        }
        return true;
    }

    /**
     * Điều kiện đến ngày
     */
    public synchronized boolean dieuKienDenNgay() {
        Date ngayHienTai = new Date();
        Date tuNgay = dch_TuNgay.getDate();
        Date denNgay = dch_DenNgay.getDate();

        if (tuNgay.after(denNgay)) {
            JOptionPane.showMessageDialog(this, "Từ ngày phải trước ngày đến!");
            dch_DenNgay.setDate(new Date());
            dch_TuNgay.setDate(new Date());
            return false;
        }

        if (denNgay.after(ngayHienTai)) {
            JOptionPane.showMessageDialog(this, "Ngày phải trước ngày hiện tại!");
            dch_DenNgay.setDate(new Date());
            return false;
        }
        return true;
    }

    /**
     * Đọc dữ liệu lên bảng danh sách hóa đơn
     */
    public synchronized void docDuLieuLenBangDsHoaDon() throws RemoteException {
        modelHoaDon.setRowCount(0);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        for (HoaDon hd : daoHoaDon.getAllHoaDon()) {
            Double tongTien = daoHoaDon.tongTienHoaDon(hd.getMaHoaDon());
            String ngayLap = formatter.format(hd.getNgayNhap());

            KhachHang kh = daoKhachHang.getKhachHangTheoMa(hd.getKhachHang().getMaKH());
            NhanVien nv = daoNhanVien.getNhanVienTheoMa(hd.getNhanVien().getMaNV());

            Object o[] = new Object[5];
            o[0] = hd.getMaHoaDon();
            try {
                o[1] = kh.getHoTen();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            o[2] = nv.getHoTen();

            o[3] = ngayLap;
            o[4] = NumberFormat.getInstance().format(tongTien);
            modelHoaDon.addRow(o);
        }
    }

    /**
     * Đọc dữ liệu lên bảng chi tiết hóa đơn
     */
    public synchronized void docDuLieuLenBangCTHD(long maHD) throws RemoteException {
        modelCTHD.setRowCount(0);
        for (CTHD cthd : daoCTHD.getAllCTHD(maHD)) {

            HoaDon hd = daoHoaDon.getHoaDonTheoMa(cthd.getHoaDon().getMaHoaDon());
            SanPham sp = daoSanPham.getSanPhamTheoMa(cthd.getSanPham().getMaSP());

            Double thanhTien = daoCTHD.tinhThanhTienSanPham(hd.getMaHoaDon(), sp.getMaSP());
            Object[] o = new Object[4];
            o[0] = sp.getMaSP();
            o[1] = sp.getTenSP();
            o[2] = cthd.getSoLuong();
            o[3] = NumberFormat.getInstance().format(thanhTien);
            modelCTHD.addRow(o);
        }
    }

    /**
     * Đọc dữ liệu lên bảng danh sách hóa đơn
     */
    public synchronized void docDuLieuDanhSachHoaDonTheoNgay() throws ParseException, RemoteException {
        modelHoaDon.setRowCount(0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        String tuNgay = formatter.format(dch_TuNgay.getDate());
        String denNgay = formatter.format(dch_DenNgay.getDate());
        for (HoaDon hd : daoHoaDon.getAllHoaDonTheoNgay(tuNgay, denNgay)) {
            Double tongTien = daoHoaDon.tongTienHoaDon(hd.getMaHoaDon());
            String ngayLap = formatter.format(hd.getNgayNhap());

            KhachHang kh = daoKhachHang.getKhachHangTheoMa(hd.getKhachHang().getMaKH());
            NhanVien nv = daoNhanVien.getNhanVienTheoMa(hd.getNhanVien().getMaNV());

            Object o[] = new Object[5];
            o[0] = hd.getMaHoaDon();
            o[1] = kh.getHoTen();
            o[2] = nv.getHoTen();
            o[3] = ngayLap;
            o[4] = NumberFormat.getInstance().format(tongTien);
            modelHoaDon.addRow(o);
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

        pnl_DanhSachHoaDon = new javax.swing.JPanel();
        scr_DanhSachHoaDon = new javax.swing.JScrollPane();
        tbl_HoaDon = new javax.swing.JTable();
        pnl_DanhSachCTHD = new javax.swing.JPanel();
        scr_DanhSachCTHD = new javax.swing.JScrollPane();
        tbl_CTHD = new javax.swing.JTable();
        pnl_NV_TieuDe = new javax.swing.JPanel();
        lbl_TieuDe = new javax.swing.JLabel();
        pnl_ThongTin = new javax.swing.JPanel();
        lbl_TuNgay = new javax.swing.JLabel();
        lbl_DenNgay = new javax.swing.JLabel();
        chk_TatCa = new javax.swing.JCheckBox();
        dch_TuNgay = new com.toedter.calendar.JDateChooser();
        dch_DenNgay = new com.toedter.calendar.JDateChooser();

        setBackground(new java.awt.Color(199, 210, 213));
        setMinimumSize(new java.awt.Dimension(1000, 550));
        setPreferredSize(new java.awt.Dimension(1000, 550));

        scr_DanhSachHoaDon.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách hóa đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        tbl_HoaDon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbl_HoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã hóa đơn", "Tên khách hàng", "Tên nhân viên", "Ngày lập", "Tổng tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public synchronized Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbl_HoaDon.setRowHeight(35);
        tbl_HoaDon.setShowGrid(true);
        tbl_HoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mousePressed(java.awt.event.MouseEvent evt) {
                try {
                    tbl_HoaDonMousePressed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        scr_DanhSachHoaDon.setViewportView(tbl_HoaDon);

        pnl_DanhSachCTHD.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết hóa đơn tương ứng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        tbl_CTHD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbl_CTHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SP", "Tên sản phẩm", "Số lượng", "Thành tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public synchronized Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbl_CTHD.setRowHeight(35);
        tbl_CTHD.setShowGrid(true);
        scr_DanhSachCTHD.setViewportView(tbl_CTHD);

        javax.swing.GroupLayout pnl_DanhSachCTHDLayout = new javax.swing.GroupLayout(pnl_DanhSachCTHD);
        pnl_DanhSachCTHD.setLayout(pnl_DanhSachCTHDLayout);
        pnl_DanhSachCTHDLayout.setHorizontalGroup(
            pnl_DanhSachCTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_DanhSachCTHDLayout.createSequentialGroup()
                .addComponent(scr_DanhSachCTHD, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnl_DanhSachCTHDLayout.setVerticalGroup(
            pnl_DanhSachCTHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scr_DanhSachCTHD, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
        );

        lbl_TieuDe.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        lbl_TieuDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TieuDe.setText("XEM DANH SÁCH HÓA ĐƠN");
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

        javax.swing.GroupLayout pnl_DanhSachHoaDonLayout = new javax.swing.GroupLayout(pnl_DanhSachHoaDon);
        pnl_DanhSachHoaDon.setLayout(pnl_DanhSachHoaDonLayout);
        pnl_DanhSachHoaDonLayout.setHorizontalGroup(
            pnl_DanhSachHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_DanhSachHoaDonLayout.createSequentialGroup()
                .addComponent(scr_DanhSachHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_DanhSachCTHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(pnl_NV_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnl_DanhSachHoaDonLayout.setVerticalGroup(
            pnl_DanhSachHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_DanhSachHoaDonLayout.createSequentialGroup()
                .addComponent(pnl_NV_TieuDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_DanhSachHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnl_DanhSachCTHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scr_DanhSachHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        pnl_ThongTin.setBackground(new java.awt.Color(199, 210, 213));
        pnl_ThongTin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lbl_TuNgay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TuNgay.setText("Từ ngày:");

        lbl_DenNgay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_DenNgay.setText("Đến ngày:");

        chk_TatCa.setBackground(new java.awt.Color(199, 210, 213));
        chk_TatCa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chk_TatCa.setSelected(true);
        chk_TatCa.setText("Tất cả");
        chk_TatCa.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    chk_TatCaItemStateChanged(evt);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        dch_TuNgay.setDateFormatString("dd-MM-yyyy");
        dch_TuNgay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dch_TuNgay.setMinimumSize(new java.awt.Dimension(64, 22));
        dch_TuNgay.setPreferredSize(new java.awt.Dimension(64, 22));
        dch_TuNgay.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public synchronized void propertyChange(java.beans.PropertyChangeEvent evt) {
                try {
                    dch_TuNgayPropertyChange(evt);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        dch_DenNgay.setDateFormatString("dd-MM-yyyy");
        dch_DenNgay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dch_DenNgay.setMinimumSize(new java.awt.Dimension(64, 22));
        dch_DenNgay.setPreferredSize(new java.awt.Dimension(64, 22));
        dch_DenNgay.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public synchronized void propertyChange(java.beans.PropertyChangeEvent evt) {
                try {
                    dch_DenNgayPropertyChange(evt);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        javax.swing.GroupLayout pnl_ThongTinLayout = new javax.swing.GroupLayout(pnl_ThongTin);
        pnl_ThongTin.setLayout(pnl_ThongTinLayout);
        pnl_ThongTinLayout.setHorizontalGroup(
            pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_ThongTinLayout.createSequentialGroup()
                .addContainerGap(369, Short.MAX_VALUE)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chk_TatCa)
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_TuNgay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_DenNgay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dch_TuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dch_DenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(281, 281, 281))
        );
        pnl_ThongTinLayout.setVerticalGroup(
            pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_TuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dch_TuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_DenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dch_DenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(chk_TatCa)
                .addContainerGap(104, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnl_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_DanhSachHoaDon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_DanhSachHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(pnl_ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private synchronized  void tbl_HoaDonMousePressed(java.awt.event.MouseEvent evt) throws RemoteException {//GEN-FIRST:event_tbl_HoaDonMousePressed
        int row = tbl_HoaDon.getSelectedRow();
        if (row != -1) {
            long maHD = Long.parseLong(tbl_HoaDon.getValueAt(row, 0).toString());
            docDuLieuLenBangCTHD(maHD);

        }
    }//GEN-LAST:event_tbl_HoaDonMousePressed

    private synchronized  void dch_TuNgayPropertyChange(java.beans.PropertyChangeEvent evt) throws ParseException, RemoteException {//GEN-FIRST:event_dch_TuNgayPropertyChange
        if (!dieuKienTuNgay()) {
            return;
        }
        if (chk_TatCa.isSelected()) {
            docDuLieuLenBangDsHoaDon();
        } else if (!chk_TatCa.isSelected()) {
            docDuLieuDanhSachHoaDonTheoNgay();
        }
    }//GEN-LAST:event_dch_TuNgayPropertyChange

    private synchronized  void chk_TatCaItemStateChanged(java.awt.event.ItemEvent evt) throws ParseException, RemoteException {//GEN-FIRST:event_chk_TatCaItemStateChanged

        if (chk_TatCa.isSelected()) {
            docDuLieuLenBangDsHoaDon();

        } else if (!chk_TatCa.isSelected()) {
            docDuLieuDanhSachHoaDonTheoNgay();
        }
        modelCTHD.setRowCount(0);

    }//GEN-LAST:event_chk_TatCaItemStateChanged

    private synchronized  void dch_DenNgayPropertyChange(java.beans.PropertyChangeEvent evt) throws ParseException, RemoteException {//GEN-FIRST:event_dch_DenNgayPropertyChange
        if (!dieuKienDenNgay()) {
            return;
        }
        if (chk_TatCa.isSelected()) {
            docDuLieuLenBangDsHoaDon();
        } else if (!chk_TatCa.isSelected()) {
            docDuLieuDanhSachHoaDonTheoNgay();
        }
    }//GEN-LAST:event_dch_DenNgayPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chk_TatCa;
    private com.toedter.calendar.JDateChooser dch_DenNgay;
    private com.toedter.calendar.JDateChooser dch_TuNgay;
    private javax.swing.JLabel lbl_DenNgay;
    private javax.swing.JLabel lbl_TieuDe;
    private javax.swing.JLabel lbl_TuNgay;
    private javax.swing.JPanel pnl_DanhSachCTHD;
    private javax.swing.JPanel pnl_DanhSachHoaDon;
    private javax.swing.JPanel pnl_NV_TieuDe;
    private javax.swing.JPanel pnl_ThongTin;
    private javax.swing.JScrollPane scr_DanhSachCTHD;
    private javax.swing.JScrollPane scr_DanhSachHoaDon;
    private javax.swing.JTable tbl_CTHD;
    private javax.swing.JTable tbl_HoaDon;
    // End of variables declaration//GEN-END:variables
}
