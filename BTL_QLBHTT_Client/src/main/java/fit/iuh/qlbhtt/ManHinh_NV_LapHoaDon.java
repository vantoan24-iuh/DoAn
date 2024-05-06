/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package fit.iuh.qlbhtt;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fit.iuh.dao.*;
import fit.iuh.entity.*;
import fit.iuh.util.RMIClientUtil;

import java.awt.event.ItemEvent;
import java.io.File;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import static fit.iuh.qlbhtt.HomePage.pnl_GiaoDienChucNang;

/**
 *
 * @author DMX
 */
public  class ManHinh_NV_LapHoaDon extends javax.swing.JPanel implements XyLyCloseFrame {

    private DefaultTableModel modelSanPham;
    private DefaultTableModel modelGioHang;
    private ISanPhamDao dao_SanPham;
    private IChatLieuDao dao_ChatLieu;
    private IKichThuocDao dao_KichThuoc;
    private IMauSacDao dao_MauSac;
    private IPhanLoaiDao dao_PhanLoai;
    private INhaCungCapDao dao_NhaCungCap;
    private IKhachHangDao dao_KhachHang;
    private INhanVienDao dao_NhanVien;
    private IPhieuDatHangDao dao_PhieuDatHang;
    private ICTPhieuDatHangDao dao_CTPhieuDatHang;
    private IHoaDonDao dao_HoaDon;
    private ICTHDDao dao_CTHD;
    private ISendMail dao_sendEmail;
    private File file = null;
    KhachHang khachHang = null;
    long maPDH = 0;
    ArrayList<SanPham> gioHang;

    public  static NhanVien nhanVien = Login.nhanVien;
    private boolean ngonNgu = Login.ngonNgu;

    /**
     * Creates new form quanly
     */
    public  ManHinh_NV_LapHoaDon() throws SQLException, RemoteException {
        dao_SanPham = RMIClientUtil.getSanPhamDao();
        dao_ChatLieu = RMIClientUtil.getChatLieuDao();
        dao_KichThuoc = RMIClientUtil.getKichThuocDao();
        dao_MauSac = RMIClientUtil.getMauSacDao();
        dao_PhanLoai = RMIClientUtil.getPhanLoaiDao();
        dao_NhaCungCap = RMIClientUtil.getNhaCungCapDao();
        dao_KhachHang = RMIClientUtil.getKhachHangDao();
        dao_PhieuDatHang = RMIClientUtil.getPhieuDatHangDao();
        dao_CTPhieuDatHang = RMIClientUtil.getCtPhieuDatHangDao();
        dao_sendEmail = RMIClientUtil.getSendMailDao();
        dao_HoaDon = RMIClientUtil.getHoaDonDao();
        dao_CTHD = RMIClientUtil.getCtHoaDonDao();
        dao_NhanVien = RMIClientUtil.getNhanVienDao();
        gioHang = new ArrayList<>();

//        connect = new Connect();
//        connect.connect();
        initComponents();
        if (!ngonNgu) {
            chuyenDoiNgonNgu();
        }
        tbl_SanPham.setDefaultEditor(Object.class, null); //Không cho chỉnh sửa cột
        tbl_SanPham.getTableHeader().setReorderingAllowed(false); //Không cho di chuyển cột

        tbl_GioHang.setDefaultEditor(Object.class, null); //Không cho chỉnh sửa cột
        tbl_GioHang.getTableHeader().setReorderingAllowed(false); //Không cho di chuyển cột

        docDuLieuSanPham();
        docDuLieuCMB();
        capNhatTongTienGioHang();

        //Tự động tính tiền giỏ hàng
        FocusAdapter focusListener = new FocusAdapter() {
            @Override
            public synchronized void focusLost(FocusEvent e) {
                // Xử lý khi trường văn bản mất focus (khi bạn nhấn vào nơi khác)
                tinhToanTienTra();
            }
        };
        txt_TienKHDua.addFocusListener(focusListener);
        //Không cho phép chỉnh sửa table
        khoaChinhSuaTable();
    }

    public synchronized  void chuyenDoiNgonNgu() {
        btn_LapHoaDon.setText("Make an Invoice");
        btn_Them.setText("Insert");
        btn_Giam.setText("Subtract");
        btn_ThemKhachHang.setText("Insert Cus");
        btn_TimKH.setText("Search");
        lbl_GiaBan.setText("Price");
        lbl_HinhAnhSanPham.setText("Image");
        lbl_KichThuoc.setText("Size");

        lbl_MaSP.setText("Clothing ID");
        lbl_MauSac.setText("Color");

        lbl_PhanLoai.setText("Category");
        lbl_NhapSoLuong.setText("Enter Quantity");
        lbl_TenSP.setText("Accessories Name");

        lbl_SoDienThoai.setText("Phone Number");
        lbl_TenKH.setText("Customer Name");
        lbl_TongTienGioHang.setText("Total cart amount: ");

        lbl_TienKHDua.setText("Guest money: ");
        lbl_TienTra.setText("Surplus money: ");
        lbl_TongTien.setText("Total amount: ");

        lbl_TieuDe.setText("MAKE AN INVOICE");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private synchronized void initComponents() {

        pnl_DanhSachSanPham = new javax.swing.JPanel();
        scr_DanhSachSanPham = new javax.swing.JScrollPane();
        tbl_SanPham = new javax.swing.JTable();
        pnl_DieuChinhGioHang = new javax.swing.JPanel();
        lbl_NhapSoLuong = new javax.swing.JLabel();
        lbl_TongTienGioHang = new javax.swing.JLabel();
        txt_SoLuongNhap = new javax.swing.JTextField();
        btn_Giam = new javax.swing.JButton();
        btn_Them = new javax.swing.JButton();
        txt_TongTienGioHang = new javax.swing.JTextField();
        lbl_DonViGia = new javax.swing.JLabel();
        pnl_GioHang = new javax.swing.JPanel();
        scr_GioHang = new javax.swing.JScrollPane();
        tbl_GioHang = new javax.swing.JTable();
        pnl_NV_TieuDe = new javax.swing.JPanel();
        lbl_TieuDe = new javax.swing.JLabel();
        pnl_ThongTin = new javax.swing.JPanel();
        txt_TenSP = new javax.swing.JTextField();
        txt_MaSP = new javax.swing.JTextField();
        lbl_MaSP = new javax.swing.JLabel();
        lbl_PhanLoai = new javax.swing.JLabel();
        lbl_TenSP = new javax.swing.JLabel();
        lbl_KichThuoc = new javax.swing.JLabel();
        pnl_HinhAnhSanPham = new javax.swing.JPanel();
        lbl_HinhAnhSanPham = new javax.swing.JLabel();
        cmb_PhanLoai = new javax.swing.JComboBox<>();
        cmb_KichThuoc = new javax.swing.JComboBox<>();
        lbl_MauSac = new javax.swing.JLabel();
        lbl_GiaBan = new javax.swing.JLabel();
        cmb_MauSac = new javax.swing.JComboBox<>();
        txt_GiaBan = new javax.swing.JTextField();
        pnl_KhuVucTTKhachHang = new javax.swing.JPanel();
        lbl_TenKH = new javax.swing.JLabel();
        txt_TenKH = new javax.swing.JTextField();
        btn_TimKH = new javax.swing.JButton();
        lbl_SoDienThoai = new javax.swing.JLabel();
        txt_SoDienThoai = new javax.swing.JTextField();
        btn_ThemKhachHang = new javax.swing.JButton();
        pnl_NutChucNang = new javax.swing.JPanel();
        btn_LapHoaDon = new javax.swing.JButton();
        lbl_TongTien = new javax.swing.JLabel();
        lbl_SoTienTong = new javax.swing.JLabel();
        txt_TienKHDua = new javax.swing.JTextField();
        lbl_TienTra = new javax.swing.JLabel();
        lbl_SoTienTra = new javax.swing.JLabel();
        lbl_TienKHDua = new javax.swing.JLabel();
        lbl_DonViTien = new javax.swing.JLabel();
        lbl_DonViTienTra = new javax.swing.JLabel();
        btn_TaiLaiTinhToan = new javax.swing.JButton();

        setBackground(new java.awt.Color(199, 210, 213));
        setMinimumSize(new java.awt.Dimension(1000, 550));
        setPreferredSize(new java.awt.Dimension(1000, 550));

        if(ngonNgu){
            scr_DanhSachSanPham.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        } else {
            scr_DanhSachSanPham.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "List of products", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        }
        scr_DanhSachSanPham.setAutoscrolls(true);
        scr_DanhSachSanPham.setPreferredSize(new java.awt.Dimension(600, 427));

        tbl_SanPham.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        if(ngonNgu){
            tbl_SanPham.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Mã quần áo", "Tên quần áo", "Phân loại", "Giá bán ", "Kích cỡ", "Màu sắc", "Chất liệu", "Nhà cung cấp", "Số lượng tồn"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };

                public synchronized Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
        } else {
            tbl_SanPham.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Clothing ID", "Clothing name", "Catagory", "Price", "Size", "Color", "Fabric", "Supplier", "Quantity"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };

                public synchronized Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
        }
        tbl_SanPham.setAutoscrolls(false);
        tbl_SanPham.setMinimumSize(new java.awt.Dimension(135, 800));
        tbl_SanPham.setPreferredSize(new java.awt.Dimension(675, 800));
        tbl_SanPham.setRowHeight(30);
        tbl_SanPham.setShowGrid(true);
        tbl_SanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    tbl_SanPhamMouseClicked(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        scr_DanhSachSanPham.setViewportView(tbl_SanPham);

        pnl_DieuChinhGioHang.setBackground(new java.awt.Color(204, 204, 204));
        pnl_DieuChinhGioHang.setPreferredSize(new java.awt.Dimension(711, 50));

        lbl_NhapSoLuong.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lbl_NhapSoLuong.setText("Nhập số lượng:");

        lbl_TongTienGioHang.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lbl_TongTienGioHang.setText("Tổng tiền giỏ hàng:");

        txt_SoLuongNhap.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        btn_Giam.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_Giam.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-delete-cart-25.png")); // NOI18N
        btn_Giam.setText("Giảm");
        btn_Giam.setBorder(null);
        btn_Giam.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_GiamMouseEntered(evt);
            }
            public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
                btn_GiamMouseExited(evt);
            }
        });
        btn_Giam.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GiamActionPerformed(evt);
            }
        });

        btn_Them.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_Them.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-buy-cart-27.png")); // NOI18N
        btn_Them.setText("Thêm");
        btn_Them.setBorder(null);
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

        txt_TongTienGioHang.setEditable(false);
        txt_TongTienGioHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_TongTienGioHang.setText("250000");

        lbl_DonViGia.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lbl_DonViGia.setForeground(new java.awt.Color(255, 0, 0));
        lbl_DonViGia.setText("VND");
        lbl_DonViGia.setPreferredSize(new java.awt.Dimension(21, 30));

        javax.swing.GroupLayout pnl_DieuChinhGioHangLayout = new javax.swing.GroupLayout(pnl_DieuChinhGioHang);
        pnl_DieuChinhGioHang.setLayout(pnl_DieuChinhGioHangLayout);
        pnl_DieuChinhGioHangLayout.setHorizontalGroup(
            pnl_DieuChinhGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_DieuChinhGioHangLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(lbl_NhapSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_SoLuongNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Giam, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_TongTienGioHang, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_TongTienGioHang, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_DonViGia, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        pnl_DieuChinhGioHangLayout.setVerticalGroup(
            pnl_DieuChinhGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_DieuChinhGioHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_DieuChinhGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_DieuChinhGioHangLayout.createSequentialGroup()
                        .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnl_DieuChinhGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_NhapSoLuong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_TongTienGioHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_SoLuongNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_TongTienGioHang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_DonViGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btn_Giam, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pnl_GioHang.setBackground(new java.awt.Color(255, 255, 255));

        if(ngonNgu) {
            scr_GioHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giỏ Hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        } else {
            scr_GioHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cart", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        }
        scr_GioHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tbl_GioHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        if(ngonNgu){
            tbl_GioHang.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Mã sản phẩm", "Tên sản phẩm", "Phân loại", "Giá bán ", "Kích cỡ", "Màu sắc", "Chất liệu", "Nhà cung cấp", "Số lượng"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };

                public synchronized Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
        } else {
            tbl_GioHang.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                    "Clothing ID", "Clothing name", "Catagory", "Price", "Size", "Color", "Fabric", "Supplier", "Quantity"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };

                public synchronized Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
        }
        tbl_GioHang.setRowHeight(30);
        tbl_GioHang.setShowGrid(true);
        tbl_GioHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    tbl_GioHangMouseClicked(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        scr_GioHang.setViewportView(tbl_GioHang);

        javax.swing.GroupLayout pnl_GioHangLayout = new javax.swing.GroupLayout(pnl_GioHang);
        pnl_GioHang.setLayout(pnl_GioHangLayout);
        pnl_GioHangLayout.setHorizontalGroup(
            pnl_GioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scr_GioHang)
        );
        pnl_GioHangLayout.setVerticalGroup(
            pnl_GioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_GioHangLayout.createSequentialGroup()
                .addComponent(scr_GioHang, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        lbl_TieuDe.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        lbl_TieuDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TieuDe.setText("LẬP HÓA ĐƠN");
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

        javax.swing.GroupLayout pnl_DanhSachSanPhamLayout = new javax.swing.GroupLayout(pnl_DanhSachSanPham);
        pnl_DanhSachSanPham.setLayout(pnl_DanhSachSanPhamLayout);
        pnl_DanhSachSanPhamLayout.setHorizontalGroup(
            pnl_DanhSachSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scr_DanhSachSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_DieuChinhGioHang, javax.swing.GroupLayout.DEFAULT_SIZE, 1174, Short.MAX_VALUE)
            .addComponent(pnl_GioHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_NV_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnl_DanhSachSanPhamLayout.setVerticalGroup(
            pnl_DanhSachSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_DanhSachSanPhamLayout.createSequentialGroup()
                .addComponent(pnl_NV_TieuDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scr_DanhSachSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnl_DieuChinhGioHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnl_GioHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl_ThongTin.setBackground(new java.awt.Color(199, 210, 213));
        if(ngonNgu) {
            pnl_ThongTin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        } else {
            pnl_ThongTin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Information product", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        }
        pnl_ThongTin.setPreferredSize(new java.awt.Dimension(926, 300));

        txt_TenSP.setEditable(false);
        txt_TenSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N


        txt_MaSP.setEditable(false);
        txt_MaSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N


        lbl_MaSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_MaSP.setText("Mã sản phẩm");

        lbl_PhanLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_PhanLoai.setText("Phân loại");

        lbl_TenSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TenSP.setText("Tên sản phẩm");

        lbl_KichThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_KichThuoc.setText("Kích thước");

        pnl_HinhAnhSanPham.setBackground(new java.awt.Color(255, 255, 255));
        pnl_HinhAnhSanPham.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        pnl_HinhAnhSanPham.setMaximumSize(new java.awt.Dimension(175, 141));
        pnl_HinhAnhSanPham.setMinimumSize(new java.awt.Dimension(175, 141));
        pnl_HinhAnhSanPham.setPreferredSize(new java.awt.Dimension(175, 141));

        lbl_HinhAnhSanPham.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_HinhAnhSanPham.setText("Hình Ảnh");
        lbl_HinhAnhSanPham.setMaximumSize(new java.awt.Dimension(175, 141));
        lbl_HinhAnhSanPham.setMinimumSize(new java.awt.Dimension(175, 141));
        lbl_HinhAnhSanPham.setPreferredSize(new java.awt.Dimension(175, 141));

        javax.swing.GroupLayout pnl_HinhAnhSanPhamLayout = new javax.swing.GroupLayout(pnl_HinhAnhSanPham);
        pnl_HinhAnhSanPham.setLayout(pnl_HinhAnhSanPhamLayout);
        pnl_HinhAnhSanPhamLayout.setHorizontalGroup(
            pnl_HinhAnhSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_HinhAnhSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
        );
        pnl_HinhAnhSanPhamLayout.setVerticalGroup(
            pnl_HinhAnhSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_HinhAnhSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
        );

        cmb_PhanLoai.setEditable(true);
        cmb_PhanLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmb_PhanLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
//        cmb_PhanLoai.setEnabled(false);
        cmb_PhanLoai.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    cmb_PhanLoaiItemStateChanged(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        cmb_KichThuoc.setEditable(true);
        cmb_KichThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmb_KichThuoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
//        cmb_KichThuoc.setEnabled(false);
        cmb_KichThuoc.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    cmb_KichThuocItemStateChanged(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });



        lbl_MauSac.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_MauSac.setText("Màu sắc");

        lbl_GiaBan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_GiaBan.setText("Giá bán");

        cmb_MauSac.setEditable(true);
        cmb_MauSac.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmb_MauSac.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        cmb_MauSac.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    cmb_MauSacItemStateChanged(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
//        cmb_MauSac.setEnabled(false);

        txt_GiaBan.setEditable(false);
        txt_GiaBan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N


        pnl_KhuVucTTKhachHang.setBackground(new java.awt.Color(199, 210, 213));
        if(ngonNgu) {
            pnl_KhuVucTTKhachHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        } else {
            pnl_KhuVucTTKhachHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Information Customer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        }

        lbl_TenKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TenKH.setText("Tên khách hàng");

        txt_TenKH.setEditable(false);
        txt_TenKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btn_TimKH.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_TimKH.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-search-client-30.png")); // NOI18N
        btn_TimKH.setText("Tìm ");
        btn_TimKH.setPreferredSize(new java.awt.Dimension(100, 37));
        btn_TimKH.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_TimKHMouseEntered(evt);
            }
            public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
                btn_TimKHMouseExited(evt);
            }
        });
        btn_TimKH.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_TimKHActionPerformed(evt);
            }
        });

        lbl_SoDienThoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_SoDienThoai.setText("Số điện thoại");

        txt_SoDienThoai.setEditable(false);
        txt_SoDienThoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btn_ThemKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_ThemKhachHang.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-add-30.png")); // NOI18N
        btn_ThemKhachHang.setText("Thêm");
        btn_ThemKhachHang.setPreferredSize(new java.awt.Dimension(110, 37));
        btn_ThemKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_ThemKhachHangMouseEntered(evt);
            }
            public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
                btn_ThemKhachHangMouseExited(evt);
            }
        });
        btn_ThemKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemKhachHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_KhuVucTTKhachHangLayout = new javax.swing.GroupLayout(pnl_KhuVucTTKhachHang);
        pnl_KhuVucTTKhachHang.setLayout(pnl_KhuVucTTKhachHangLayout);
        pnl_KhuVucTTKhachHangLayout.setHorizontalGroup(
            pnl_KhuVucTTKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_KhuVucTTKhachHangLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pnl_KhuVucTTKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_KhuVucTTKhachHangLayout.createSequentialGroup()
                        .addComponent(btn_TimKH, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_ThemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_KhuVucTTKhachHangLayout.createSequentialGroup()
                        .addGroup(pnl_KhuVucTTKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_TenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_SoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(170, 170, 170))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_KhuVucTTKhachHangLayout.createSequentialGroup()
                        .addGroup(pnl_KhuVucTTKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_SoDienThoai, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_TenKH))
                        .addContainerGap())))
        );
        pnl_KhuVucTTKhachHangLayout.setVerticalGroup(
            pnl_KhuVucTTKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_KhuVucTTKhachHangLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lbl_SoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_SoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_TenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(txt_TenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(pnl_KhuVucTTKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_ThemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_TimKH, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnl_ThongTinLayout = new javax.swing.GroupLayout(pnl_ThongTin);
        pnl_ThongTin.setLayout(pnl_ThongTinLayout);
        pnl_ThongTinLayout.setHorizontalGroup(
            pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmb_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_GiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_GiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmb_KichThuoc, 0, 210, Short.MAX_VALUE)
                            .addComponent(lbl_KichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmb_MauSac, 0, 210, Short.MAX_VALUE))))
                .addGap(50, 50, 50)
                .addComponent(pnl_HinhAnhSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(pnl_KhuVucTTKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnl_ThongTinLayout.setVerticalGroup(
            pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnl_KhuVucTTKhachHang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(lbl_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_ThongTinLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(lbl_KichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmb_KichThuoc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbl_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmb_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbl_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_GiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(pnl_HinhAnhSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmb_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_GiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pnl_NutChucNang.setBackground(new java.awt.Color(199, 210, 213));
        if(ngonNgu) {
            pnl_NutChucNang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thanh Toán", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        } else {
            pnl_NutChucNang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pay", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        }

        btn_LapHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_LapHoaDon.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-bill-30.png")); // NOI18N
        btn_LapHoaDon.setText("Lập hóa đơn");
        btn_LapHoaDon.setBorder(null);
        btn_LapHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_LapHoaDonMouseEntered(evt);
            }
            public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
                btn_LapHoaDonMouseExited(evt);
            }
        });
        btn_LapHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_LapHoaDonActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        lbl_TongTien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TongTien.setText("Tổng tiền:");

        lbl_SoTienTong.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_SoTienTong.setForeground(new java.awt.Color(255, 0, 0));
        lbl_SoTienTong.setText("1000000");

        txt_TienKHDua.setEditable(false);
        txt_TienKHDua.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_TienKHDua.setToolTipText("Tiền khách đưa");
        txt_TienKHDua.addKeyListener(new java.awt.event.KeyAdapter() {
            public synchronized void keyReleased(java.awt.event.KeyEvent evt) {
                txt_TienKHDuaKeyReleased(evt);
            }
        });

        lbl_TienTra.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TienTra.setText("Tiền trả lại:");

        lbl_SoTienTra.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_SoTienTra.setForeground(new java.awt.Color(255, 0, 0));

        lbl_TienKHDua.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TienKHDua.setText("Tiền khách đưa:");

        lbl_DonViTien.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_DonViTien.setForeground(new java.awt.Color(255, 0, 0));
        lbl_DonViTien.setText("VND");

        lbl_DonViTienTra.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_DonViTienTra.setForeground(new java.awt.Color(255, 0, 0));
        lbl_DonViTienTra.setText("VND");

        btn_TaiLaiTinhToan.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-reset-30.png")); // NOI18N

        javax.swing.GroupLayout pnl_NutChucNangLayout = new javax.swing.GroupLayout(pnl_NutChucNang);
        pnl_NutChucNang.setLayout(pnl_NutChucNangLayout);
        pnl_NutChucNangLayout.setHorizontalGroup(
            pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                        .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                                .addComponent(lbl_TongTien)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_SoTienTong)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl_DonViTien))
                            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                                .addComponent(lbl_TienTra)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_SoTienTra)
                                .addGap(18, 18, 18)
                                .addComponent(lbl_DonViTienTra)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_NutChucNangLayout.createSequentialGroup()
                        .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btn_LapHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                                .addComponent(lbl_TienKHDua)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_TienKHDua, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_TaiLaiTinhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(15, 15, 15))))
        );
        pnl_NutChucNangLayout.setVerticalGroup(
            pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_TongTien)
                    .addComponent(lbl_SoTienTong)
                    .addComponent(lbl_DonViTien))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_TienKHDua, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_TienKHDua))
                    .addComponent(btn_TaiLaiTinhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_TienTra)
                    .addComponent(lbl_SoTienTra)
                    .addComponent(lbl_DonViTienTra))
                .addGap(35, 35, 35)
                .addComponent(btn_LapHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnl_DanhSachSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_NutChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_DanhSachSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnl_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(pnl_NutChucNang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents


    private synchronized void btn_TimKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TimKHActionPerformed
        try {
            //xuLyTimKiemKhachHang();
            xuLyChonKhachHang();
        } catch (SQLException ex) {
            Logger.getLogger(ManHinh_NV_LapHoaDon.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }//GEN-LAST:event_btn_TimKHActionPerformed

    private synchronized void btn_ThemKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemKhachHangActionPerformed
        ManHinh_KH_QuanLy pnl_KH_QuanLy = null;
        try {
            pnl_KH_QuanLy = new ManHinh_KH_QuanLy();
        } catch (SQLException ex) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        pnl_GiaoDienChucNang.removeAll();
        pnl_GiaoDienChucNang.add(pnl_KH_QuanLy);
        pnl_GiaoDienChucNang.revalidate();
        pnl_GiaoDienChucNang.repaint();
    }//GEN-LAST:event_btn_ThemKhachHangActionPerformed

    private synchronized void btn_TimKHMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_TimKHMouseEntered
        btn_TimKH.setBackground(new Color(0x9EDDFF));
        btn_TimKH.setForeground(new Color(0x141E46));
    }//GEN-LAST:event_btn_TimKHMouseEntered

    private synchronized void btn_TimKHMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_TimKHMouseExited
        btn_TimKH.setBackground(UIManager.getColor("Menu.background"));
        btn_TimKH.setForeground(UIManager.getColor("Menu.foreground"));
    }//GEN-LAST:event_btn_TimKHMouseExited

    private synchronized void btn_ThemKhachHangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ThemKhachHangMouseEntered
        btn_ThemKhachHang.setBackground(new Color(0x9EDDFF));
        btn_ThemKhachHang.setForeground(new Color(0x141E46));
    }//GEN-LAST:event_btn_ThemKhachHangMouseEntered

    private synchronized void btn_ThemKhachHangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ThemKhachHangMouseExited
        btn_ThemKhachHang.setBackground(UIManager.getColor("Menu.background"));
        btn_ThemKhachHang.setForeground(UIManager.getColor("Menu.foreground"));
    }//GEN-LAST:event_btn_ThemKhachHangMouseExited

    private synchronized void btn_ThemMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ThemMouseEntered
        btn_Them.setBackground(new Color(0xFCE9F1));
        btn_Them.setForeground(new Color(0x141E46));
    }//GEN-LAST:event_btn_ThemMouseEntered

    private synchronized void btn_ThemMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ThemMouseExited
        btn_Them.setBackground(UIManager.getColor("Menu.background"));
        btn_Them.setForeground(UIManager.getColor("Menu.foreground"));
    }//GEN-LAST:event_btn_ThemMouseExited

    private synchronized void btn_GiamMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_GiamMouseEntered
        btn_Giam.setBackground(new Color(0xFCE9F1));
        btn_Giam.setForeground(new Color(0x141E46));
    }//GEN-LAST:event_btn_GiamMouseEntered

    private synchronized void btn_GiamMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_GiamMouseExited
        btn_Giam.setBackground(UIManager.getColor("Menu.background"));
        btn_Giam.setForeground(UIManager.getColor("Menu.foreground"));
    }//GEN-LAST:event_btn_GiamMouseExited

    private synchronized void btn_LapHoaDonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LapHoaDonMouseExited
        btn_LapHoaDon.setBackground(UIManager.getColor("Menu.background"));
        btn_LapHoaDon.setForeground(UIManager.getColor("Menu.foreground"));
    }//GEN-LAST:event_btn_LapHoaDonMouseExited

    private synchronized void btn_LapHoaDonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LapHoaDonMouseEntered
        btn_LapHoaDon.setBackground(new Color(0x9EDDFF));
        btn_LapHoaDon.setForeground(new Color(0x141E46));
    }//GEN-LAST:event_btn_LapHoaDonMouseEntered

    public synchronized  ImageIcon ResizeImage(String imgPath, String desc) {
        ImageIcon myImage = new ImageIcon(imgPath);
        Image img = myImage.getImage();
        Image newImg = img.getScaledInstance(pnl_HinhAnhSanPham.getWidth(), pnl_HinhAnhSanPham.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        image.setDescription(desc);
        return image;
    }

    private synchronized void tbl_SanPhamMouseClicked(java.awt.event.MouseEvent evt) throws RemoteException {//GEN-FIRST:event_tbl_SanPhamMouseClicked
        int row = tbl_SanPham.getSelectedRow();
        if (row != -1) {
            //Load thong tin vao txt, combobox
            long maSanPham = Long.parseLong(tbl_SanPham.getValueAt(row, 0).toString());
            SanPham sanPham = dao_SanPham.getSanPhamTheoMa(maSanPham);

            txt_MaSP.setText(tbl_SanPham.getValueAt(row, 0).toString());
            txt_TenSP.setText(tbl_SanPham.getValueAt(row, 1).toString());
//            cmb_PhanLoai.setSelectedItem(tbl_SanPham.getValueAt(row, 2).toString());
            txt_GiaBan.setText(tbl_SanPham.getValueAt(row, 3).toString());
//            cmb_KichThuoc.setSelectedItem(tbl_SanPham.getValueAt(row, 4).toString());
//            cmb_MauSac.setSelectedItem(tbl_SanPham.getValueAt(row, 5).toString());
            //Load hinh anh
            File file = new File("");
            String path = file.getAbsolutePath();
            String imagePath = path + "/data/" + sanPham.getHinhAnh();
            File imageFile = new File(imagePath);

            //ImageIcon imageIcon = new ImageIcon(imagePath);
            //imageIcon.setDescription("" + sanPham.getHinhAnh()); // Đặt mô tả cho ImageIcon
            lbl_HinhAnhSanPham.setIcon(ResizeImage(imagePath, sanPham.getHinhAnh()));
            //System.out.println("Path: " + path);
            //lbl_HinhAnhSP.setIcon(ResizeImage(path + "/data/"+sanPham.getHinhAnh()));
        }
    }//GEN-LAST:event_tbl_SanPhamMouseClicked

    private synchronized void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed
        if (kiemTraThongTinKH()) {
            txt_TienKHDua.setEditable(true);
            xuLyThemVaoGioHang();
        } else
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin khách hàng để dùng các thao tác");
    }//GEN-LAST:event_btn_ThemActionPerformed

    private synchronized void tbl_GioHangMouseClicked(java.awt.event.MouseEvent evt) throws RemoteException {//GEN-FIRST:event_tbl_GioHangMouseClicked
        int row = tbl_GioHang.getSelectedRow();
        if (row != -1) {
            //Load thong tin vao txt, combobox
            long maSanPham = Long.parseLong(tbl_GioHang.getValueAt(row, 0).toString());
            SanPham sanPham = dao_SanPham.getSanPhamTheoMa(maSanPham);

            txt_MaSP.setText(tbl_GioHang.getValueAt(row, 0).toString());
            txt_TenSP.setText(tbl_GioHang.getValueAt(row, 1).toString());
//            cmb_PhanLoai.setSelectedItem(tbl_GioHang.getValueAt(row, 2).toString());
            txt_GiaBan.setText(tbl_GioHang.getValueAt(row, 3).toString());
//            cmb_KichThuoc.setSelectedItem(tbl_GioHang.getValueAt(row, 4).toString());
//            cmb_MauSac.setSelectedItem(tbl_GioHang.getValueAt(row, 5).toString());
            //Load hinh anh
            File file = new File("");
            String path = file.getAbsolutePath();
            String imagePath = path + "/data/" + sanPham.getHinhAnh();
            File imageFile = new File(imagePath);

            //ImageIcon imageIcon = new ImageIcon(imagePath);
            //imageIcon.setDescription("" + sanPham.getHinhAnh()); // Đặt mô tả cho ImageIcon
            lbl_HinhAnhSanPham.setIcon(ResizeImage(imagePath, sanPham.getHinhAnh()));
            //System.out.println("Path: " + path);
            //lbl_HinhAnhSP.setIcon(ResizeImage(path + "/data/"+sanPham.getHinhAnh()));
        }
    }//GEN-LAST:event_tbl_GioHangMouseClicked


    private synchronized void btn_GiamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GiamActionPerformed
        if (kiemTraThongTinKH()) {
            xuLyGiamGioHang();
        } else
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin khách hàng để dùng các thao tác");
    }//GEN-LAST:event_btn_GiamActionPerformed

    private synchronized void btn_LapHoaDonActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_LapHoaDonActionPerformed
        if (kiemTraThongTinKH()) {
            if (kiemTraTienKHDua() && kiemTraSoLuongMua()) {
                xulyLapHoaDon();
            }
        } else
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin khách hàng để dùng các thao tác");
    }//GEN-LAST:event_btn_LapHoaDonActionPerformed

    private synchronized void txt_TienKHDuaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TienKHDuaKeyReleased
        // TODO add your handling code here:
        try {
            Double.parseDouble(txt_TienKHDua.getText().replace(",", "").trim());
            tinhToanTienTra();
        } catch (NumberFormatException ex) {

        }

    }//GEN-LAST:event_txt_TienKHDuaKeyReleased

    private synchronized void cmb_PhanLoaiItemStateChanged(ItemEvent evt) throws RemoteException {
        thongKeDsSanPhamTheoTieuChi();
    }

    private synchronized void cmb_MauSacItemStateChanged(ItemEvent evt) throws RemoteException {
        thongKeDsSanPhamTheoTieuChi();
    }

    private synchronized void cmb_KichThuocItemStateChanged(ItemEvent evt) throws RemoteException {
        thongKeDsSanPhamTheoTieuChi();
    }


    /**
     * Thông kê danh sách sản phẩm theo các tiêu chí
     */
    public synchronized  void thongKeDsSanPhamTheoTieuChi() throws RemoteException {
        modelSanPham = (DefaultTableModel) tbl_SanPham.getModel();
        modelSanPham.setRowCount(0);

        String phanLoai = Objects.requireNonNull(cmb_PhanLoai.getSelectedItem()).toString();
        String ktPhanLoai = cmb_PhanLoai.getSelectedItem().toString();
        if (ktPhanLoai.equalsIgnoreCase("Tất cả")) {
            phanLoai = "";
        }

        String mauSac = Objects.requireNonNull(cmb_MauSac.getSelectedItem()).toString();
        String ktMauSac = cmb_MauSac.getSelectedItem().toString();
        if (ktMauSac.equalsIgnoreCase("Tất cả")) {
            mauSac = "";
        }

        String kichThuoc = Objects.requireNonNull(cmb_KichThuoc.getSelectedItem()).toString();
        String ktKichThuoc = cmb_KichThuoc.getSelectedItem().toString();
        if (ktKichThuoc.equalsIgnoreCase("Tất cả")) {
            kichThuoc = "";
        }

        ArrayList<SanPham> listSanPham = dao_SanPham.getAllSanPhamTheoTieuChi(phanLoai, mauSac, kichThuoc);
        for (SanPham sp : listSanPham) {
            PhanLoai pl = dao_PhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = dao_KichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
            MauSac ms = dao_MauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
            ChatLieu cl = dao_ChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = dao_NhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());
            Object[] o = new Object[9];
            o[0] = sp.getMaSP();
            o[1] = sp.getTenSP();
            o[2] = pl.getLoaiSanPham();
            o[3] = NumberFormat.getInstance().format(sp.getGiaBan());
            o[4] = kt.getKichThuoc();
            o[5] = ms.getMauSac();
            o[6] = cl.getChatLieu();
            o[7] = ncc.getTenNCC();
            o[8] = sp.getSoLuong();
            modelSanPham.addRow(o);

        }
    }

    public synchronized  void xuLyThemVaoGioHang() {
        int row = tbl_SanPham.getSelectedRow();
        if (row != -1) {
            String soLuongMuaString = txt_SoLuongNhap.getText().trim();
            if (soLuongMuaString.length() > 0) { // So luong mua khong duoc rong
                try {
                    int soLuongMua = Integer.parseInt(soLuongMuaString);
                    if (soLuongMua <= 0) {
                        JOptionPane.showMessageDialog(this, "Số lượng mua phải lớn hơn 0");
                        txt_SoLuongNhap.requestFocus();
                    } else {
                        //Lay thong tin san pham can them vao gio hang
                        long maSanPham = Long.parseLong(tbl_SanPham.getValueAt(row, 0).toString());
                        SanPham sanPham = dao_SanPham.getSanPhamTheoMa(maSanPham);
                        //Hai trường hợp: 1 là đã có trong giỏ hàng thì tăng số lượng
                        //                2 là chưa có thì thêm mới
                        int truongHop = 2;
                        int index = 0;
                        for (SanPham sp : gioHang) {
//                            System.out.println("sp" + sp);
                            if (sp.getMaSP() == sanPham.getMaSP()) {
                                truongHop = 1;
                                //Kiểm tra số lượng mua so với số lượng tồn
                                if ((sp.getSoLuong() + soLuongMua) <= sanPham.getSoLuong()) {
                                    //Cap nhat so luong san pham trong danh sach gio hang
                                    sp.setSoLuong(sp.getSoLuong() + soLuongMua);

                                    //Cap nhat model gio hang
                                    modelGioHang = (DefaultTableModel) tbl_GioHang.getModel();
                                    modelGioHang.setValueAt(sp.getSoLuong(), index, 8);

                                    //Xoa trang txt_SoLuongNhap
                                    txt_SoLuongNhap.setText("");
                                    tbl_SanPham.clearSelection();
                                    //Thong bao nguoi dung
                                    JOptionPane.showMessageDialog(this, "Thêm thành công vào giỏ hàng(Tăng số lượng)");
                                    capNhatTongTienGioHang();
                                } else {
                                    JOptionPane.showMessageDialog(this, "Sản phẩm đã có trong giỏ hàng, kho không đủ hàng cho số lượng tăng thêm");
                                }
                            }
                            index++;
                        }
                        if (truongHop == 2) {
                            //Kiểm tra số lượng mua so với số lượng tồn
                            if (soLuongMua <= sanPham.getSoLuong()) {
                                tbl_SanPham.clearSelection();
                                //Hợp lệ, tiến hành thêm
                                sanPham.setSoLuong(soLuongMua);
//                                System.out.println("sl mua1 " + soLuongMua);
//                                System.out.println("sl mua2 " + sanPham.getSoLuong());
                                //Them vao danh sach gio hang
                                gioHang.add(sanPham);

                                //Them vao model gio hang
                                modelGioHang = (DefaultTableModel) tbl_GioHang.getModel();
                                PhanLoai pl = dao_PhanLoai.getDLPhanLoaiSPTheoMa(sanPham.getPhanLoai().getMaPhanLoai());
                                KichThuoc kt = dao_KichThuoc.getDLKichThuocTheoMa(sanPham.getKichThuoc().getMaKichThuoc());
                                MauSac ms = dao_MauSac.getDLMauSacTheoMa(sanPham.getMauSac().getMaMauSac());
                                ChatLieu cl = dao_ChatLieu.getDLChatLieuTheoMa(sanPham.getChatLieu().getMaChatLieu());
                                NhaCungCap ncc = dao_NhaCungCap.getNhaCungCapTheoMa(sanPham.getNhaCungCap().getMaNCC());
                                Object[] o = new Object[9];
                                o[0] = sanPham.getMaSP();
                                o[1] = sanPham.getTenSP();
                                o[2] = pl.getLoaiSanPham();
                                o[3] = NumberFormat.getInstance().format(sanPham.getGiaBan());
                                o[4] = kt.getKichThuoc();
                                o[5] = ms.getMauSac();
                                o[6] = cl.getChatLieu();
                                o[7] = ncc.getTenNCC();
                                o[8] = sanPham.getSoLuong();
                                modelGioHang.addRow(o);

                                //Xoa trang txt_SoLuongNhap
                                txt_SoLuongNhap.setText("");
                                JOptionPane.showMessageDialog(this, "Thêm thành công vào giỏ hàng(Thêm mới)");
                                capNhatTongTienGioHang();
                            } else {
                                JOptionPane.showMessageDialog(this, "Hàng tồn kho không đủ số lượng để đáp ứng!");
                            }
                        }
                    }
                } catch (NumberFormatException ex) { // phai la ky tu so
                    JOptionPane.showMessageDialog(this, "Số lượng mua phải là số");
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Số lượng mua không được rỗng");
                txt_SoLuongNhap.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm trong danh sách sản phẩm!");
        }
    }

    public synchronized  void xuLyGiamGioHang() {
        int row = tbl_GioHang.getSelectedRow();
        if (row != -1) {
            String soLuongGiamString = txt_SoLuongNhap.getText().trim();
            if (soLuongGiamString.length() > 0) { // So luong mua khong duoc rong
                try {
                    int soLuongGiam = Integer.parseInt(soLuongGiamString);
                    if (soLuongGiam <= 0) {
                        JOptionPane.showMessageDialog(this, "Số lượng giảm phải lớn hơn 0");
                        txt_SoLuongNhap.requestFocus();
                    } else {
                        //Lay thong tin san pham can giam so luong
                        long maSanPham = Long.parseLong(tbl_GioHang.getValueAt(row, 0).toString());
                        for (SanPham sanPham : gioHang) {

                            if (sanPham.getMaSP() == maSanPham) {
                                //Hai trường hợp: 1 là giảm số lượng nhưng số lượng mua vẫn > 0
                                //                2 là giảm số lượng mua đến 0, tiến hành xóa sản phẩm ra khỏi giỏ hàng
                                if (sanPham.getSoLuong() < soLuongGiam) {
                                    JOptionPane.showMessageDialog(this, "Số lượng giảm vượt quá số lượng mua trong giỏ hàng");
                                } else if (sanPham.getSoLuong() > soLuongGiam) {
                                    //Cap nhat gio hang: giam so luong mua cua san pham
                                    sanPham.setSoLuong(sanPham.getSoLuong() - soLuongGiam);
                                    //Cap nhat model gio hang
                                    modelGioHang.setValueAt(sanPham.getSoLuong(), row, 8);
                                    //Xoa trang txt_SoLuongNhap
                                    txt_SoLuongNhap.setText("");
                                    xoaTrang();
                                    //Thong bao nguoi dung
                                    JOptionPane.showMessageDialog(this, "Giảm số lượng mua  của " + sanPham.getMaSP() + " thành công!");
                                    capNhatTongTienGioHang();
                                    tbl_GioHang.clearSelection();
                                } else {
                                    //Cap nhat gio hang: xoa san pham
                                    gioHang.remove(sanPham);
                                    //Cap nhat model
                                    modelGioHang.removeRow(row);
                                    //Xoa trang txt_SoLuongNhap
                                    txt_SoLuongNhap.setText("");
                                    xoaTrang();
                                    //Thong bao nguoi dung
                                    JOptionPane.showMessageDialog(this, "Xoa san pham " + sanPham.getMaSP() + " ra khỏi giỏ hàng thành công!");
                                    capNhatTongTienGioHang();
                                    tbl_GioHang.clearSelection();
                                }
                            }
                        }
                    }
                } catch (NumberFormatException ex) { // phai la ky tu so
                    JOptionPane.showMessageDialog(this, "Số lượng giảm phải là số");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Số lượng giảm không được rỗng");
                txt_SoLuongNhap.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm trong danh sách giỏ hàng!");
        }
    }

    private synchronized void kiemTraDonDatCuaKH(KhachHang kh) throws RemoteException {
        System.out.println("kh2" + kh.getMaKH());
        PhieuDatHang pdh = dao_PhieuDatHang.getPDTTheoMaKH(kh.getMaKH());
        modelGioHang = (DefaultTableModel) tbl_GioHang.getModel();
        modelGioHang.setRowCount(0);
        if (pdh != null) {
            maPDH = pdh.getMaPhieuDat();
            ArrayList<CTPhieuDatHang> ctpdh = dao_CTPhieuDatHang.getAllCTPhieuDatHang(pdh.getMaPhieuDat());
            for (CTPhieuDatHang cTPhieuDatHang : ctpdh) {
                SanPham sanPhamAddGioHang = dao_SanPham.getSanPhamTheoMa(cTPhieuDatHang.getSanPham().getMaSP());
                sanPhamAddGioHang.setSoLuong(cTPhieuDatHang.getSoLuong());
                //Them vao gio hang
                gioHang.add(sanPhamAddGioHang);
//                System.out.println("sp trong chtd: " + sanPhamAddGioHang);
                //Them vao model gio hang

                SanPham sanPham = dao_SanPham.getSanPhamTheoMa(cTPhieuDatHang.getSanPham().getMaSP());

                PhanLoai pl = dao_PhanLoai.getDLPhanLoaiSPTheoMa(sanPham.getPhanLoai().getMaPhanLoai());
                KichThuoc kt = dao_KichThuoc.getDLKichThuocTheoMa(sanPham.getKichThuoc().getMaKichThuoc());
                MauSac ms = dao_MauSac.getDLMauSacTheoMa(sanPham.getMauSac().getMaMauSac());
                ChatLieu cl = dao_ChatLieu.getDLChatLieuTheoMa(sanPham.getChatLieu().getMaChatLieu());
                NhaCungCap ncc = dao_NhaCungCap.getNhaCungCapTheoMa(sanPham.getNhaCungCap().getMaNCC());

                Object[] object = new Object[9];
                object[0] = sanPham.getMaSP();
                object[1] = sanPham.getTenSP();
                object[2] = pl.getLoaiSanPham();
                object[3] = NumberFormat.getInstance().format(sanPham.getGiaBan());
                object[4] = kt.getKichThuoc();
                object[5] = ms.getMauSac();
                object[6] = cl.getChatLieu();
                object[7] = ncc.getTenNCC();
                object[8] = cTPhieuDatHang.getSoLuong();
                modelGioHang.addRow(object);

                //Xoa trang txt_SoLuongNhap
                txt_SoLuongNhap.setText("");
            }
            capNhatTongTienGioHang();
            txt_TienKHDua.setEditable(true);
            JOptionPane.showMessageDialog(this, "Khách hàng có một đơn đặt trước đó");
        } else {
            boolean xoa = gioHang.removeAll(gioHang);
            System.out.println("sp xoa: " + xoa);
            lbl_SoTienTong.setText("");
            txt_TongTienGioHang.setText("");
        }
    }
    @Override
    public synchronized  void xuLyFrameClose() throws RemoteException {
        if (Form_DanhSachKhachHang.khachHang_Form != null) {
            khachHang = Form_DanhSachKhachHang.khachHang_Form;
            System.out.println("kh" + khachHang);
            txt_TenKH.setText(khachHang.getHoTen());
            txt_SoDienThoai.setText(khachHang.getSdt());
            kiemTraDonDatCuaKH(khachHang);
        }
    }

    private synchronized void xuLyChonKhachHang() throws SQLException, RemoteException {
        Form_DanhSachKhachHang frm = new Form_DanhSachKhachHang(this);
        frm.setVisible(true);
    }

    private synchronized void xuLyTimKiemKhachHang() throws RemoteException {
        String tenKhachHang = txt_TenKH.getText().trim();
        String soDienThoai = txt_SoDienThoai.getText().trim();

        if (tenKhachHang.equals("") && soDienThoai.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin khách hàng");
        } else {
            ArrayList<KhachHang> listKhachHang = dao_KhachHang.timKiemKhachHang(0, tenKhachHang, soDienThoai, "");

            if (listKhachHang != null) {
                for (KhachHang kh : listKhachHang) {
                    txt_TenKH.setText(kh.getHoTen());
                    txt_SoDienThoai.setText(kh.getSdt());
                    khachHang = kh;
                    System.out.println("kh" + kh);
                    JOptionPane.showMessageDialog(this, "Tìm thấy thông tin khách hàng");
                }

                kiemTraDonDatCuaKH(khachHang);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin khách hàng");
            }
        }
    }

    public synchronized  void xuLyGiamSLSanPhamTrongKho() throws RemoteException {
        for (SanPham sanPham : gioHang) {
//            System.out.println("sp" + sanPham);
            dao_SanPham.giamSoLuongSanPham(sanPham);
        }
    }

    public synchronized  void xulyLapHoaDon() throws RemoteException {
        HoaDon hd = new HoaDon(khachHang, Login.nhanVien, new Date());
        //Them vao csdl HoaDon
        dao_HoaDon.themHoaDon(hd);
        hd.setMaHoaDon(dao_HoaDon.getHoaDon().getMaHoaDon());

        System.out.println("Ma hoa don: " + hd.getMaHoaDon());
        //Them vao csdl CTHD
        for (SanPham sanPham : gioHang) {
//            System.out.println("sp1:" + sanPham);
            CTHD cthd = new CTHD(sanPham, hd, sanPham.getSoLuong());
            dao_CTHD.themCTHD(cthd);
        }
        xuLyGiamSLSanPhamTrongKho();

        xuatHoaDon(hd);
        guiHoaDonVeEmail(hd);

        if (maPDH != 0) {
            long maKH = khachHang.getMaKH();
            PhieuDatHang maPDH_KH = dao_PhieuDatHang.getPDTTheoMaKH(maKH);
            if(maPDH_KH != null){
                maPDH = maPDH_KH.getMaPhieuDat();
                //Xoa CTPhieuDatHang
                dao_CTPhieuDatHang.xoaCTPhieuDatHang(maPDH);
                //Xóa PhieuDatHang
                dao_PhieuDatHang.xoaPhieuDatHang(maPDH);
            }
        }
        resetPanel();
//        xuatHoaDon(hd);
        JOptionPane.showMessageDialog(this, " Hóa đơn đã được gửi về gmail của khách hàng");

        resetPanel();
    }

    public synchronized  void xuatHoaDon(HoaDon hd) {
        try {

            Font fontMain = FontFactory.getFont("src/main/java/fit/iuh/fonts/vuArial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font fontTD = FontFactory.getFont("src/main/java/fit/iuh/fonts/vuArial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            fontTD.setSize(22);
            fontTD.setFamily(Font.BOLD + "");

            Font fontCH = FontFactory.getFont("src/main/java/fit/iuh/fonts/vuArial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            fontCH.setSize(12);
            String pathFull = "data/HoaDon/" + "HoaDon" + hd.getMaHoaDon() + ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pathFull)); //Tạo ra đối tượng ghi dữ liệu vào tài liệu PDF
            document.open();

            //Thông tin cửa hàng
            Paragraph paragraphtenCH = new Paragraph("CLOTHING - CỬA HÀNG THỜI TRANG", fontCH);
            paragraphtenCH.setAlignment(Element.ALIGN_CENTER);
            Paragraph paragraphDiaChi = new Paragraph("Địa chỉ: 12 Nguyễn Văn Bảo, Phường 4, Quận Gò Vấp, Thành phố Hồ Chí Minh", fontCH);
            paragraphDiaChi.setAlignment(Element.ALIGN_CENTER);
            Paragraph paragraphHotline = new Paragraph("Hotline: 036 7494 904", fontCH);
            paragraphHotline.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraphtenCH);
            document.add(paragraphDiaChi);
            document.add(paragraphHotline);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            //Tiêu đề Main
            Paragraph paragraphMain = new Paragraph("Hóa đơn bán hàng", fontTD);

            paragraphMain.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraphMain);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            //Tạo Mục
            PdfPTable tableMuc = new PdfPTable(2);
            tableMuc.setWidthPercentage(100); //Đặt chiều rộng ứng với 100% trang
            tableMuc.setSpacingBefore(10f); //Đặt khoảng cách là 10
            tableMuc.setSpacingAfter(10f);

            float[] chieuRongCot = {1f, 1f};
            tableMuc.setWidths(chieuRongCot);

            //Mục mã hóa đơn
            PdfPCell cellMaHD = new PdfPCell(new Paragraph("Mã hóa đơn: " + hd.getMaHoaDon(), fontMain));
            cellMaHD.setBorderColor(BaseColor.WHITE);
            tableMuc.addCell(cellMaHD);

            //Mục ngày lập
            SimpleDateFormat fomatter = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
            String ngayInformat = fomatter.format(hd.getNgayNhap());
            PdfPCell cellNgayIn = new PdfPCell(new Paragraph("Ngày in: " + ngayInformat, fontMain));
            cellNgayIn.setBorderColor(BaseColor.WHITE);
            tableMuc.addCell(cellNgayIn);

            //Mục ten nhan vien

            NhanVien nv = dao_NhanVien.getNhanVienTheoMa(Login.nhanVien.getMaNV());

            PdfPCell cellTenNV = new PdfPCell(new Paragraph("Tên Nhân viên: " + nv.getHoTen(), fontMain));
            cellTenNV.setBorderColor(BaseColor.WHITE);
            tableMuc.addCell(cellTenNV);

            //Mục chức vụ
            PdfPCell cellChucVu = new PdfPCell(new Paragraph("Chức vụ: " + nv.getChuVu(), fontMain));
            cellChucVu.setBorderColor(BaseColor.WHITE);
            tableMuc.addCell(cellChucVu);

            document.add(tableMuc);
            //Tạo Mục
            PdfPTable tableKH = new PdfPTable(1);
            tableKH.setWidthPercentage(100); //Đặt chiều rộng ứng với 100% trang
            tableKH.setSpacingBefore(10f); //Đặt khoảng cách là 10
            tableKH.setSpacingAfter(10f);

            float[] chieuRongCot_KH = {1f};
            tableKH.setWidths(chieuRongCot_KH);

            //Mục khách hàng
            KhachHang kh = dao_KhachHang.getKhachHangTheoMa(hd.getKhachHang().getMaKH());
            PdfPCell celltenKH = new PdfPCell(new Paragraph("Tên khách hàng: " + kh.getHoTen(), fontMain));
            celltenKH.setBorderColor(BaseColor.WHITE);
            tableKH.addCell(celltenKH);

            document.add(tableKH);

            //Tạo bảng sản phẩm
            PdfPTable tableDsSP = new PdfPTable(5);
            tableDsSP.setWidthPercentage(100); //Đặt chiều rộng ứng với 100% trang
            tableDsSP.setSpacingBefore(10f); //Đặt khoảng cách là 10
            tableDsSP.setSpacingAfter(10f);

            //Tiêu đề bảng
            float[] chieuRongCotSP = {2f, 4f, 2f, 2f, 2f};
            tableDsSP.setWidths(chieuRongCotSP);

            //STT
            PdfPCell cellTblSP_STT = new PdfPCell(new Paragraph("STT ", fontMain));
            cellTblSP_STT.setBorderColor(BaseColor.BLACK);
            cellTblSP_STT.setVerticalAlignment(Element.ALIGN_MIDDLE);//Chỉnh text của cột theo chiều dọc
            cellTblSP_STT.setHorizontalAlignment(Element.ALIGN_CENTER);// Chỉnh text cửa cột theo chiều ngang
            tableDsSP.addCell(cellTblSP_STT);

            //Tên sản phẩm
            PdfPCell cellTblSP_tenSP = new PdfPCell(new Paragraph("Tên sản phẩm ", fontMain));
            cellTblSP_tenSP.setBorderColor(BaseColor.BLACK);
            cellTblSP_tenSP.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_tenSP.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_tenSP);

            //Giá bán
            PdfPCell cellTblSP_giaBan = new PdfPCell(new Paragraph("Giá bán ", fontMain));
            cellTblSP_giaBan.setBorderColor(BaseColor.BLACK);
            cellTblSP_giaBan.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_giaBan.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_giaBan);

            //Số lượng
            PdfPCell cellTblSP_SL = new PdfPCell(new Paragraph("Số lượng bán", fontMain));
            cellTblSP_SL.setBorderColor(BaseColor.BLACK);
            cellTblSP_SL.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_SL.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_SL);

            //Thành tiền
            PdfPCell cellTblSP_thanhTien = new PdfPCell(new Paragraph("Thành tiền ", fontMain));
            cellTblSP_thanhTien.setBorderColor(BaseColor.BLACK);
            cellTblSP_thanhTien.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_thanhTien.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_thanhTien);

            int stt = 1;
            //Thong tin san pham
            for (SanPham sp : gioHang) {
//                System.out.println("sp xhoa don: " + sp);
                //STT
                PdfPCell cellTblSP_STT_giaTri = new PdfPCell(new Paragraph(stt + "", fontMain));
                cellTblSP_STT_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_STT_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);//Chỉnh text của cột theo chiều dọc
                cellTblSP_STT_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);// Chỉnh text cửa cột theo chiều ngang
                tableDsSP.addCell(cellTblSP_STT_giaTri);

                //Tên sản phẩm
                PdfPCell cellTblSP_tenSP_giaTri = new PdfPCell(new Paragraph(sp.getTenSP(), fontMain));
                cellTblSP_tenSP_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_tenSP_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableDsSP.addCell(cellTblSP_tenSP_giaTri);

                //Giá bán
                PdfPCell cellTblSP_giaBan_giaTri = new PdfPCell(new Paragraph(NumberFormat.getInstance().format(sp.getGiaBan()) + "", fontMain));
                cellTblSP_giaBan_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_giaBan_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_giaBan_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_giaBan_giaTri);

                //Số lượng
                PdfPCell cellTblSP_SL_giaTri = new PdfPCell(new Paragraph(sp.getSoLuong() + "", fontMain));
                cellTblSP_SL_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_SL_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_SL_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_SL_giaTri);

                double thanhTien = dao_CTHD.tinhThanhTienSanPham(hd.getMaHoaDon(), sp.getMaSP());

                //Doanh Thu
                PdfPCell cellTblSP_doanhThu_giaTri = new PdfPCell(new Paragraph(NumberFormat.getInstance().format(thanhTien), fontMain));
                cellTblSP_doanhThu_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_doanhThu_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_doanhThu_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_doanhThu_giaTri);

                stt++;
            }

            document.add(tableDsSP);

            //Tạo Mục
            PdfPTable tableTien = new PdfPTable(1);
            tableTien.setWidthPercentage(100); //Đặt chiều rộng ứng với 100% trang
            tableTien.setSpacingBefore(10f); //Đặt khoảng cách là 10
            tableTien.setSpacingAfter(10f);

            float[] chieuRongCot_Tien = {1f};
            tableMuc.setWidths(chieuRongCot);

            //tổng tiền
            PdfPCell celltongTien = new PdfPCell(new Paragraph("Tổng tiền: " + NumberFormat.getInstance().format(Double.parseDouble(lbl_SoTienTong.getText().replace(",", ""))) + " VND", fontMain));
            celltongTien.setBorderColor(BaseColor.WHITE);
            tableTien.addCell(celltongTien);

            //Mục khách đưa
            PdfPCell cellTienKHDua = new PdfPCell(new Paragraph("Tiền khách đưa: " + NumberFormat.getInstance().format(Double.parseDouble(txt_TienKHDua.getText().replace(",", ""))) + " VND", fontMain));
            cellTienKHDua.setBorderColor(BaseColor.WHITE);
            tableTien.addCell(cellTienKHDua);

            //Mục tiền trả
            PdfPCell cellTienTra = new PdfPCell(new Paragraph("Tiền trả: " + NumberFormat.getInstance().format(Double.parseDouble(lbl_SoTienTra.getText().replace(",", ""))) + " VND", fontMain));
            cellTienTra.setBorderColor(BaseColor.WHITE);
            tableTien.addCell(cellTienTra);

            document.add(tableTien);
            document.close();

            // mở file pdf
            try {
                File file = new File(pathFull);
                //Kiểm tra xem tệp có tồn tại hay không
                if (file.exists()) {
                    Desktop.getDesktop().open(file); //Mở file trên ứng dụng mặc định của tệp
                } else {
                    System.out.println("File này không tồn tại!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean kiemTraSoLuongMua() throws RemoteException {
        for (SanPham sanPham : gioHang) {
            int slTonDuKien = dao_SanPham.getSanPhamTheoMa(sanPham.getMaSP()).getSoLuong() - sanPham.getSoLuong();
            if (sanPham.getSoLuong() > dao_SanPham.getSanPhamTheoMa(sanPham.getMaSP()).getSoLuong() || slTonDuKien < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng mua của " + sanPham.getMaSP() + " vượt quá số lượng tồn");
                return false;
            }
        }
        return true;
    }

    public synchronized  void resetPanel() throws RemoteException {
        modelGioHang = (DefaultTableModel) tbl_GioHang.getModel();
        modelGioHang.setRowCount(0);
        modelSanPham = (DefaultTableModel) tbl_SanPham.getModel();
        modelSanPham.setRowCount(0);
        docDuLieuSanPham();
        gioHang.clear();

        capNhatTongTienGioHang();
        xoaTrang();

        khachHang = null;
    }

    public synchronized  void xoaTrang() {
        txt_MaSP.setText("");
        txt_TenSP.setText("");
        txt_GiaBan.setText("");
        txt_TenKH.setText("");
        txt_SoDienThoai.setText("");
        txt_TienKHDua.setText("");
        lbl_SoTienTra.setText("");
        lbl_HinhAnhSanPham.setIcon(null);
        cmb_KichThuoc.setSelectedIndex(0);
        cmb_MauSac.setSelectedIndex(0);
        cmb_PhanLoai.setSelectedIndex(0);
        file = null;
    }

    public synchronized  void capNhatTongTienGioHang() {
        if (gioHang.size() != 0) {
            double tongTien = 0;
            for (SanPham sanPham : gioHang) {
                tongTien += sanPham.getGiaBan() * sanPham.getSoLuong();
            }
            lbl_SoTienTong.setText(NumberFormat.getInstance().format(tongTien) + "");
            txt_TongTienGioHang.setText(NumberFormat.getInstance().format(tongTien) + "");
        } else {
            lbl_SoTienTong.setText("0");
            txt_TongTienGioHang.setText("0");
        }
    }

    public synchronized boolean kiemTraThongTinKH() {
        if (khachHang != null) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean kiemTraTienKHDua() {
        String tienKhachDua = txt_TienKHDua.getText().replace(",", "").trim();

        if (gioHang.size() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng rỗng, không thể lập hóa đơn");
            return false;
        } else if (tienKhachDua.equals("0")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền khách đưa");
            txt_TienKHDua.requestFocus();
            return false;
        } else if (tienKhachDua.length() <= 0) {
            JOptionPane.showMessageDialog(this, "Số tiền khách đưa không được rỗng");
            txt_TienKHDua.requestFocus();
            return false;
        }
        try {
            int tienKHDua = Integer.parseInt(tienKhachDua);
            Double tienTra = Double.parseDouble(lbl_SoTienTra.getText().replace(",", "").trim());
            if (tienKHDua < 0) {
                JOptionPane.showMessageDialog(this, "Số tiền khách đưa phải lớn hơn 0");
                txt_TienKHDua.requestFocus();
                return false;
            }else if (tienTra < 0) {
                JOptionPane.showMessageDialog(this, "Số tiền khách ít hơn tổng tiền");
                txt_TienKHDua.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) { // phai la ky tu so
            JOptionPane.showMessageDialog(this, "Số tiền khách đưa phải là số");
            txt_TienKHDua.requestFocus();
            return false;
        }
        return true;
    }

    public synchronized  void tinhToanTienTra() {
        String tongTienString = lbl_SoTienTong.getText().replace(",", "").trim();
        String tienKHDuaString = txt_TienKHDua.getText().replace(",", "").trim();
        double tienTra;
        //if (kiemTraTienKHDua()) {
            try {
                double tienKHDua = Double.parseDouble(tienKHDuaString);
                double tongTien = Double.parseDouble(tongTienString);

                tienTra = tienKHDua - tongTien;
                //if (tienTra >= 0) {
                    lbl_SoTienTra.setText(NumberFormat.getInstance().format(tienTra) + "");
                //} else {
                    //JOptionPane.showMessageDialog(this, "Tiền khách đưa ít hơn tổng tiền hàng");
                //}

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số tiền khách đưa phải là số");
            }
        //}
    }

    public synchronized  void docDuLieuSanPham() throws RemoteException {
        modelSanPham = (DefaultTableModel) tbl_SanPham.getModel();
        for (SanPham qa : dao_SanPham.getAllSanPham()) {
            PhanLoai pl = dao_PhanLoai.getDLPhanLoaiSPTheoMa(qa.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = dao_KichThuoc.getDLKichThuocTheoMa(qa.getKichThuoc().getMaKichThuoc());
            MauSac ms = dao_MauSac.getDLMauSacTheoMa(qa.getMauSac().getMaMauSac());
            ChatLieu cl = dao_ChatLieu.getDLChatLieuTheoMa(qa.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = dao_NhaCungCap.getNhaCungCapTheoMa(qa.getNhaCungCap().getMaNCC());

            Object[] object = new Object[9];
            object[0] = qa.getMaSP();
            object[1] = qa.getTenSP();
            object[2] = pl.getLoaiSanPham();
            object[3] = NumberFormat.getInstance().format(qa.getGiaBan());
            object[4] = kt.getKichThuoc();
            object[5] = ms.getMauSac();
            object[6] = cl.getChatLieu();
            object[7] = ncc.getTenNCC();
            object[8] = qa.getSoLuong();
            modelSanPham.addRow(object);
        }
    }

    public synchronized  void docDuLieuCMB() throws RemoteException {
        //Doc du lieu ComboBox chat lieu
        //ArrayList<ChatLieu> ds_ChatLieu = new ArrayList<>();
        //ds_ChatLieu = dao_ChatLieu.getAllChatLieu();

        //cmb_ChatLieu.removeAllItems();
        //for (ChatLieu chatLieu : ds_ChatLieu) {
        //    cmb_ChatLieu.addItem(chatLieu.getChatLieu());
        //}
        //Doc du lieu ComboBox kich thuoc
        ArrayList<KichThuoc> ds_KichThuoc = new ArrayList<>();
        ds_KichThuoc = dao_KichThuoc.getAllKichThuoc();

//        cmb_KichThuoc.removeAllItems();
        for (KichThuoc kichThuoc : ds_KichThuoc) {
            cmb_KichThuoc.addItem(kichThuoc.getKichThuoc());
        }

        //Doc du lieu ComboBox mau sac
        ArrayList<MauSac> ds_MauSac = new ArrayList<>();
        ds_MauSac = dao_MauSac.getAllMauSac();

//        cmb_MauSac.removeAllItems();
        for (MauSac mauSac : ds_MauSac) {
            cmb_MauSac.addItem(mauSac.getMauSac());
        }

        //Doc du lieu ComboBox phan loai
        ArrayList<PhanLoai> ds_PhanLoai = new ArrayList<>();
        ds_PhanLoai = dao_PhanLoai.getAllPhanLoai();

//        cmb_PhanLoai.removeAllItems();
        for (PhanLoai phanLoai : ds_PhanLoai) {
            cmb_PhanLoai.addItem(phanLoai.getLoaiSanPham());
        }

        //Doc du lieu ComboBox nha cung cap
        //ArrayList<NhaCungCap> ds_NhaCungCap = new ArrayList<>();
        //ds_NhaCungCap = dao_NhaCungCap.getAllNhaCungCap();
        //cmb_NCC.removeAllItems();
        //for (NhaCungCap nhaCungCap : ds_NhaCungCap) {
        //    cmb_NCC.addItem(nhaCungCap.getTenNCC());
        //}
    }

    public synchronized  void khoaChinhSuaTable() {
        // Tạo một TableCellEditor tùy chỉnh để không cho phép chỉnh sửa
        TableCellEditor nonEditableEditor = new TableCellEditor() {
            @Override
            public synchronized Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return null; // Trả về null để không cho phép chỉnh sửa
            }

            @Override
            public synchronized Object getCellEditorValue() {
                return null;
            }

            @Override
            public synchronized boolean isCellEditable(EventObject anEvent) {
                return false;
            }

            @Override
            public synchronized boolean shouldSelectCell(EventObject anEvent) {
                return false;
            }

            @Override
            public synchronized boolean stopCellEditing() {
                return false;
            }

            @Override
            public synchronized void cancelCellEditing() {
            }

            @Override
            public synchronized void addCellEditorListener(CellEditorListener l) {
            }

            @Override
            public synchronized void removeCellEditorListener(CellEditorListener l) {
            }
        };

        modelGioHang = (DefaultTableModel) tbl_GioHang.getModel();
        modelSanPham = (DefaultTableModel) tbl_SanPham.getModel();

        for (int i = 0; i < modelGioHang.getColumnCount(); i++) {
            tbl_GioHang.getColumnModel().getColumn(i).setCellEditor(nonEditableEditor);
        }

        for (int i = 0; i < modelSanPham.getColumnCount(); i++) {
            tbl_SanPham.getColumnModel().getColumn(i).setCellEditor(nonEditableEditor);
        }
    }

    public synchronized  void guiHoaDonVeEmail(HoaDon hd) throws RemoteException {
        KhachHang kh = dao_KhachHang.getKhachHangTheoMa(hd.getKhachHang().getMaKH());
        String emailKhachHang = kh.getEmail();
        SimpleDateFormat fomtter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String noiDungHeader = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "     <style> body {\n"
                + "            color: black;\n"
                + "        }"
                + "table { border-collapse: collapse; width: 100%; }"
                + " th, td { border: 1px solid black; padding: 8px; text-align: left; }"
                + "h3 {text-align: center} "
                + "</style> "
                + "</head>"
                + "<body>\n"
                + "    <p>Clothing, xin chào <strong>" + kh.getHoTen() + "</strong></p>\n"
                + "    <p>Cảm ơn Quý khách đã mua hàng tại của hàng chúng tôi. Cửa hàng xin thông báo hóa đơn của Quý khách như sau:</p>\n"
                + "    <p>Ngày mua: " + fomtter.format(hd.getNgayNhap()) + " </p>\n"
                + "    <table>\n"
                + "        <thead>\n"
                + "            <tr>\n"
                + "                <th>Sản phẩm</th>\n"
                + "                <th>Số lượng</th>\n"
                + "                <th>Giá</th>\n"
                + "            </tr>\n"
                + "        </thead>\n"
                + "        <tbody>\n";
        String noiDungSP = "";
        for (CTHD sp : dao_CTHD.getAllCTHD(hd.getMaHoaDon())) {

            HoaDon hoaDon = dao_HoaDon.getHoaDonTheoMa(sp.getHoaDon().getMaHoaDon());
            SanPham sanPham = dao_SanPham.getSanPhamTheoMa(sp.getSanPham().getMaSP());

            double thanhTien = dao_CTHD.tinhThanhTienSanPham(hoaDon.getMaHoaDon(), sanPham.getMaSP());

            noiDungSP = noiDungSP + "            <tr>"
                    + "               <th>" + sanPham.getTenSP() + "</th>\n"
                    + "               <th>" + sp.getSoLuong() + "</th>\n"
                    + "               <th>" + NumberFormat.getInstance().format(thanhTien) + "</th>\n"
                    + "            </tr>\n";
        }
        double tongTien = dao_HoaDon.tongTienHoaDon(hd.getMaHoaDon());
        String noiDungPhanCuoi = " </tbody>\n"
                + "    </table>\n"
                + "    <p>Tổng tiền: " + NumberFormat.getInstance().format(tongTien) + " VND" + "</p>\n"
                + "    <h3>CLOTHING XIN CẢM ƠN</h3>\n"
                + "    <br>\n"
                + "    <br>\n"
                + "    <p style=\"font-style: italic;\">Nếu có góp ý hoặc  vấn đề cần khiếu nại thì liên hệ: (+84) 367494904</p>\n"
                + "    <p>Địa chỉ: 12 Nguyễn Văn Bảo, Phường 4, Quận Gò Vấp, Thành phố Hồ Chí Minh</p>"
                + "</body>\n"
                + "</html>";
//        String emailContent = "<html> <head> <style> table { border-collapse: collapse; width: 100%; } th, td { border: 1px solid black; padding: 8px; text-align: left; } </style> </head> <body><p>Xin chào <strong>" + "Sinh" + "</strong></p><p>Cảm ơn bạn đã mua hàng tại cửa hàng của chúng tôi. Dưới đây là chi tiết đơn hàng của bạn:</p><table><tr><th>Sản phẩm</th><th>Số lượng</th><th>Giá</th></tr></table><p>Tổng tiền: " + "22222" + "</p><p>Hãy liên hệ với chúng tôi nếu bạn có bất kỳ câu hỏi hoặc yêu cầu hỗ trợ nào khác.</p><p>Trân trọng,</p><p><b>Nhà Sách Thuận Lợi</b></p></body></html>";
        String noiDungHoaDon = noiDungHeader + noiDungSP + noiDungPhanCuoi;

//        System.out.println(noiDung.toString());
        dao_sendEmail.guiHoaDonVeEmail(emailKhachHang, "Thông báo giao dịch thành công", noiDungHoaDon);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Giam;
    private javax.swing.JButton btn_LapHoaDon;
    private javax.swing.JButton btn_TaiLaiTinhToan;
    private javax.swing.JButton btn_Them;
    private javax.swing.JButton btn_ThemKhachHang;
    private javax.swing.JButton btn_TimKH;
    private javax.swing.JComboBox<String> cmb_KichThuoc;
    private javax.swing.JComboBox<String> cmb_MauSac;
    private javax.swing.JComboBox<String> cmb_PhanLoai;
    private javax.swing.JLabel lbl_DonViGia;
    private javax.swing.JLabel lbl_DonViTien;
    private javax.swing.JLabel lbl_DonViTienTra;
    private javax.swing.JLabel lbl_GiaBan;
    private javax.swing.JLabel lbl_HinhAnhSanPham;
    private javax.swing.JLabel lbl_KichThuoc;
    private javax.swing.JLabel lbl_MaSP;
    private javax.swing.JLabel lbl_MauSac;
    private javax.swing.JLabel lbl_NhapSoLuong;
    private javax.swing.JLabel lbl_PhanLoai;
    private javax.swing.JLabel lbl_SoDienThoai;
    private javax.swing.JLabel lbl_SoTienTong;
    private javax.swing.JLabel lbl_SoTienTra;
    private javax.swing.JLabel lbl_TenKH;
    private javax.swing.JLabel lbl_TenSP;
    private javax.swing.JLabel lbl_TienKHDua;
    private javax.swing.JLabel lbl_TienTra;
    private javax.swing.JLabel lbl_TieuDe;
    private javax.swing.JLabel lbl_TongTien;
    private javax.swing.JLabel lbl_TongTienGioHang;
    private javax.swing.JPanel pnl_DanhSachSanPham;
    private javax.swing.JPanel pnl_DieuChinhGioHang;
    private javax.swing.JPanel pnl_GioHang;
    private javax.swing.JPanel pnl_HinhAnhSanPham;
    private javax.swing.JPanel pnl_KhuVucTTKhachHang;
    private javax.swing.JPanel pnl_NV_TieuDe;
    private javax.swing.JPanel pnl_NutChucNang;
    private javax.swing.JPanel pnl_ThongTin;
    private javax.swing.JScrollPane scr_DanhSachSanPham;
    private javax.swing.JScrollPane scr_GioHang;
    private javax.swing.JTable tbl_GioHang;
    private javax.swing.JTable tbl_SanPham;
    private javax.swing.JTextField txt_GiaBan;
    private javax.swing.JTextField txt_MaSP;
    private javax.swing.JTextField txt_SoDienThoai;
    private javax.swing.JTextField txt_SoLuongNhap;
    private javax.swing.JTextField txt_TenKH;
    private javax.swing.JTextField txt_TenSP;
    private javax.swing.JTextField txt_TienKHDua;
    private javax.swing.JTextField txt_TongTienGioHang;
    // End of variables declaration//GEN-END:variables
}
