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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import fit.iuh.dao.*;
import fit.iuh.entity.*;
import fit.iuh.util.RMIClientUtil;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author DMX
 */
public  class ManHinh_NV_ThongKeSanPham extends javax.swing.JPanel {

    private ISanPhamDao daoSanPham;
    private IKichThuocDao daoKichThuoc;
    private IMauSacDao daoMauSac;
    private IChatLieuDao daoChatLieu;
    private IPhanLoaiDao daoPhanLoai;
    private INhaCungCapDao daoNhaCungCap;

    private DefaultTableModel model_SP;

    private boolean activeTatCa = false;
    private boolean activeHangMoiNhap = false;
    private boolean activeHetHang = false;
    private boolean activeBanChay = false;
    private boolean activeBanCham = false;
    private boolean activeNgay = false;

    private final NhanVien nhanVien = Login.nhanVien;

    /**
     * Creates new form quanly
     */
    public  ManHinh_NV_ThongKeSanPham() throws SQLException, RemoteException {
        daoSanPham = RMIClientUtil.getSanPhamDao();
        daoKichThuoc = RMIClientUtil.getKichThuocDao();
        daoMauSac = RMIClientUtil.getMauSacDao();
        daoChatLieu = RMIClientUtil.getChatLieuDao();
        daoPhanLoai = RMIClientUtil.getPhanLoaiDao();
        daoNhaCungCap = RMIClientUtil.getNhaCungCapDao();

//        connect = new Connect();
//        connect.connect();

        initComponents();
        model_SP = (DefaultTableModel) tbl_SanPham.getModel();

        tbl_SanPham.setDefaultEditor(Object.class, null); //Không cho chỉnh sửa cột
        tbl_SanPham.getTableHeader().setReorderingAllowed(false); //Không cho di chuyển cột

        khoiTaoNgayHienTai();

        docDuLieuSanPham();
        khoiTaoGiaTriCombobox();
        activeTatCa = true;

    }

    //Setup ngày hiện tại khi load lên
    public synchronized void khoiTaoNgayHienTai() {
        dch_TuNgay.setDate(new Date());
        dch_DenNgay.setDate(new Date());
    }

    /**
     * Khởi tạo, load giá trị vào combobox
     */
    public synchronized void khoiTaoGiaTriCombobox() throws RemoteException {
        for (KichThuoc kt : daoKichThuoc.getAllKichThuoc()) {
            cmb_KichThuoc.addItem(kt.getKichThuoc());
        }

        for (MauSac ms : daoMauSac.getAllMauSac()) {
            cmb_MauSac.addItem(ms.getMauSac());
        }

        for (PhanLoai pl : daoPhanLoai.getAllPhanLoai()) {
            cmb_PhanLoai.addItem(pl.getLoaiSanPham());
        }
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
            dch_TuNgay.setDate(new Date());
            dch_DenNgay.setDate(new Date());
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
     * Đọc dữ liệu sản phẩm và load vào bảng
     */
    public synchronized void docDuLieuSanPham() throws RemoteException {

        for (SanPham sp : daoSanPham.getAllSanPham()) {

            PhanLoai pl = daoPhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = daoKichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
            MauSac ms = daoMauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
            ChatLieu cl = daoChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = daoNhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String ngayNhapForMat = formatter.format(sp.getNgayNhap());
            Object[] o = new Object[11];
            o[0] = sp.getMaSP();
            o[1] = sp.getTenSP();
            o[2] = pl.getLoaiSanPham();
            o[3] = NumberFormat.getInstance().format(sp.getGiaBan());
            o[4] = NumberFormat.getInstance().format(sp.getGiaNhap());
            o[5] = ngayNhapForMat;
            o[6] = kt.getKichThuoc();
            o[7] = ms.getMauSac();
            o[8] = cl.getChatLieu();
            o[9] = ncc.getTenNCC();
            o[10] = sp.getSoLuong();
            model_SP.addRow(o);

        }
        int soLuongSP = daoSanPham.getAllSanPham().size();
        thongKeTongSanPham(soLuongSP);
    }

    /**
     * Tổng số lượng sản phẩm
     */
    public synchronized void thongKeTongSanPham(int soLuongSP) {
        txt_TongSanPham.setText(soLuongSP + "");
    }

    /**
     * Thông kê danh sách sản phẩm hết hàng trong kho
     */
    public synchronized void thongKeDsSanPhamHetHang() throws RemoteException {
        model_SP.setRowCount(0);

        String phanLoai = cmb_PhanLoai.getSelectedItem().toString();
        String ktPhanLoai = cmb_PhanLoai.getSelectedItem().toString();
        if (ktPhanLoai.equalsIgnoreCase("Tất cả")) {
            phanLoai = "";
        }

        String mauSac = cmb_MauSac.getSelectedItem().toString();
        String ktMauSac = cmb_MauSac.getSelectedItem().toString();
        if (ktMauSac.equalsIgnoreCase("Tất cả")) {
            mauSac = "";
        }

        String kichThuoc = cmb_KichThuoc.getSelectedItem().toString();
        String ktKichThuoc = cmb_KichThuoc.getSelectedItem().toString();
        if (ktKichThuoc.equalsIgnoreCase("Tất cả")) {
            kichThuoc = "";
        }

        ArrayList<SanPham> listSanPham = daoSanPham.getAllSanPhamHetHang(phanLoai, mauSac, kichThuoc);
        for (SanPham sp : listSanPham) {

            PhanLoai pl = daoPhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = daoKichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
            MauSac ms = daoMauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
            ChatLieu cl = daoChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = daoNhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String ngayNhapForMat = formatter.format(sp.getNgayNhap());
            Object[] o = new Object[11];
            o[0] = sp.getMaSP();
            o[1] = sp.getTenSP();
            o[2] = pl.getLoaiSanPham();
            o[3] = NumberFormat.getInstance().format(sp.getGiaBan());
            o[4] = NumberFormat.getInstance().format(sp.getGiaNhap());
            o[5] = ngayNhapForMat;
            o[6] = kt.getKichThuoc();
            o[7] = ms.getMauSac();
            o[8] = cl.getChatLieu();
            o[9] = ncc.getTenNCC();
            o[10] = sp.getSoLuong();
            model_SP.addRow(o);
        }

        int soLuongSP = listSanPham.size();
        thongKeTongSanPham(soLuongSP);
    }

    /**
     * Thông kê danh sách sản phẩm theo các tiêu chí
     */
    public synchronized void thongKeDsSanPhamTheoTieuChi() throws RemoteException {
        model_SP.setRowCount(0);

        String phanLoai = cmb_PhanLoai.getSelectedItem().toString();
        String ktPhanLoai = cmb_PhanLoai.getSelectedItem().toString();
        if (ktPhanLoai.equalsIgnoreCase("Tất cả")) {
            phanLoai = "";
        }

        String mauSac = cmb_MauSac.getSelectedItem().toString();
        String ktMauSac = cmb_MauSac.getSelectedItem().toString();
        if (ktMauSac.equalsIgnoreCase("Tất cả")) {
            mauSac = "";
        }

        String kichThuoc = cmb_KichThuoc.getSelectedItem().toString();
        String ktKichThuoc = cmb_KichThuoc.getSelectedItem().toString();
        if (ktKichThuoc.equalsIgnoreCase("Tất cả")) {
            kichThuoc = "";
        }

        ArrayList<SanPham> listSanPham = daoSanPham.getAllSanPhamTheoTieuChi(phanLoai, mauSac, kichThuoc);
        for (SanPham sp : listSanPham) {

            PhanLoai pl = daoPhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = daoKichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
            MauSac ms = daoMauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
            ChatLieu cl = daoChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = daoNhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String ngayNhapForMat = formatter.format(sp.getNgayNhap());
            Object[] o = new Object[11];
            o[0] = sp.getMaSP();
            o[1] = sp.getTenSP();
            o[2] = pl.getLoaiSanPham();
            o[3] = NumberFormat.getInstance().format(sp.getGiaBan());
            o[4] = NumberFormat.getInstance().format(sp.getGiaNhap());
            o[5] = ngayNhapForMat;
            o[6] = kt.getKichThuoc();
            o[7] = ms.getMauSac();
            o[8] = cl.getChatLieu();
            o[9] = ncc.getTenNCC();
            o[10] = sp.getSoLuong();
            model_SP.addRow(o);

        }

        int soLuongSP = listSanPham.size();
        thongKeTongSanPham(soLuongSP);
    }

    /**
     * Thông kê danh sách sản phẩm mới nhập
     */
    public synchronized void thongKeDsSanPhamMoiNhap() throws RemoteException {
        model_SP.setRowCount(0);

        String phanLoai = cmb_PhanLoai.getSelectedItem().toString();
        String ktPhanLoai = cmb_PhanLoai.getSelectedItem().toString();
        if (ktPhanLoai.equalsIgnoreCase("Tất cả")) {
            phanLoai = "";
        }

        String mauSac = cmb_MauSac.getSelectedItem().toString();
        String ktMauSac = cmb_MauSac.getSelectedItem().toString();
        if (ktMauSac.equalsIgnoreCase("Tất cả")) {
            mauSac = "";
        }

        String kichThuoc = cmb_KichThuoc.getSelectedItem().toString();
        String ktKichThuoc = cmb_KichThuoc.getSelectedItem().toString();
        if (ktKichThuoc.equalsIgnoreCase("Tất cả")) {
            kichThuoc = "";
        }

        Date ngayHienTai = new Date();
        //Điều kiện trước ngày hiện tại 1 ngày
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ngayHienTai);
        calendar.add(Calendar.DAY_OF_YEAR, -2);

        Date ngayTruoc1Ngay = calendar.getTime();
        int count = 0;
        ArrayList<SanPham> listSanPham = daoSanPham.getAllSanPhamTheoTieuChi(phanLoai, mauSac, kichThuoc);
        for (SanPham sp : listSanPham) {

            PhanLoai pl = daoPhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = daoKichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
            MauSac ms = daoMauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
            ChatLieu cl = daoChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = daoNhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

            Date ngayNhap = sp.getNgayNhap();

            if (ngayNhap.before(ngayHienTai) && ngayNhap.after(ngayTruoc1Ngay)) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String ngayNhapFormat = formatter.format(sp.getNgayNhap());
                Object[] o = new Object[11];
                o[0] = sp.getMaSP();
                o[1] = sp.getTenSP();
                o[2] = pl.getLoaiSanPham();
                o[3] = NumberFormat.getInstance().format(sp.getGiaBan());
                o[4] = NumberFormat.getInstance().format(sp.getGiaNhap());
                o[5] = ngayNhapFormat;
                o[6] = kt.getKichThuoc();
                o[7] = ms.getMauSac();
                o[8] = cl.getChatLieu();
                o[9] = ncc.getTenNCC();
                o[10] = sp.getSoLuong();
                model_SP.addRow(o);
                count++;
            }

        }

        thongKeTongSanPham(count);
    }

    /**
     * Thống kê top sản phẩm bán chạy
     */
    public synchronized void thongKeSanPhamBanChay() throws RemoteException {
        model_SP.setRowCount(0);
        for (SanPham sp : daoSanPham.getSanPhamBanChay()) {

            PhanLoai pl = daoPhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = daoKichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
            MauSac ms = daoMauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
            ChatLieu cl = daoChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = daoNhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String ngayNhapForMat = formatter.format(sp.getNgayNhap());
            Object[] o = new Object[11];
            o[0] = sp.getMaSP();
            o[1] = sp.getTenSP();
            o[2] = pl.getLoaiSanPham();
            o[3] = NumberFormat.getInstance().format(sp.getGiaBan());
            o[4] = NumberFormat.getInstance().format(sp.getGiaNhap());
            o[5] = ngayNhapForMat;
            o[6] = kt.getKichThuoc();
            o[7] = ms.getMauSac();
            o[8] = cl.getChatLieu();
            o[9] = ncc.getTenNCC();
            o[10] = sp.getSoLuong();
            model_SP.addRow(o);

        }

        int soLuongSP = daoSanPham.getSanPhamBanChay().size();
        thongKeTongSanPham(soLuongSP);
    }

    /**
     * Thống kê top sản phẩm bán chậm
     */
    public synchronized void thongKeSanPhamBanCham() throws RemoteException {
        model_SP.setRowCount(0);
        for (SanPham sp : daoSanPham.getSanPhamBanCham()) {

            PhanLoai pl = daoPhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = daoKichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
            MauSac ms = daoMauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
            ChatLieu cl = daoChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = daoNhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String ngayNhapForMat = formatter.format(sp.getNgayNhap());
            Object[] o = new Object[11];
            o[0] = sp.getMaSP();
            o[1] = sp.getTenSP();
            o[2] = pl.getLoaiSanPham();
            o[3] = NumberFormat.getInstance().format(sp.getGiaBan());
            o[4] = NumberFormat.getInstance().format(sp.getGiaNhap());
            o[5] = ngayNhapForMat;
            o[6] = kt.getKichThuoc();
            o[7] = ms.getMauSac();
            o[8] = cl.getChatLieu();
            o[9] = ncc.getTenNCC();
            o[10] = sp.getSoLuong();
            model_SP.addRow(o);

        }

        int soLuongSP = daoSanPham.getSanPhamBanCham().size();
        thongKeTongSanPham(soLuongSP);
    }

    /**
     * Thống kê sản phảm theo từ ngày và đến ngày
     */
    public synchronized void thongKeSanPhamTheoNgayNhap() throws RemoteException {
        model_SP.setRowCount(0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        String tuNgay = formatter.format(dch_TuNgay.getDate());
        String denNgay = formatter.format(dch_DenNgay.getDate());
        int count = 0;
        for (SanPham sp : daoSanPham.getAllSanPhamTheoNgay(tuNgay, denNgay)) {

            PhanLoai pl = daoPhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = daoKichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
            MauSac ms = daoMauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
            ChatLieu cl = daoChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = daoNhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

            SimpleDateFormat formatter_load = new SimpleDateFormat("dd-MM-yyyy");
            String ngayNhapFormat = formatter_load.format(sp.getNgayNhap());
            Object[] o = new Object[11];
            o[0] = sp.getMaSP();
            o[1] = sp.getTenSP();
            o[2] = pl.getLoaiSanPham();
            o[3] = NumberFormat.getInstance().format(sp.getGiaBan());
            o[4] = NumberFormat.getInstance().format(sp.getGiaNhap());
            o[5] = ngayNhapFormat;
            o[6] = kt.getKichThuoc();
            o[7] = ms.getMauSac();
            o[8] = cl.getChatLieu();
            o[9] = ncc.getTenNCC();
            o[10] = sp.getSoLuong();
            model_SP.addRow(o);
            count++;
        }
        thongKeTongSanPham(count);
    }

    /**
     * Lấy giá trị trên bảng add vào ArrayList
     */
    public synchronized ArrayList<SanPham> getGiaTriTrongBang() throws ParseException, RemoteException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        ArrayList<SanPham> listSanPham = new ArrayList<>();
        for (int i = 0; i < tbl_SanPham.getRowCount(); i++) {
            long maSP = Long.parseLong(tbl_SanPham.getValueAt(i, 0).toString());
            String tenSP = tbl_SanPham.getValueAt(i, 1).toString();
            PhanLoai phanLoai = daoPhanLoai.getPhanLoaiTheoTen(tbl_SanPham.getValueAt(i, 2).toString());
            Double giaBan = Double.parseDouble(tbl_SanPham.getValueAt(i, 3).toString().replace(",", ""));
            Double giaNhap = Double.parseDouble(tbl_SanPham.getValueAt(i, 4).toString().replace(",", ""));

            Date ngayNhap = formatter.parse(tbl_SanPham.getValueAt(i, 5).toString());

            KichThuoc kichThuoc = daoKichThuoc.getKichThuocTheoTen(tbl_SanPham.getValueAt(i, 6).toString());
            MauSac mauSac = daoMauSac.getMauSacTheoTen(tbl_SanPham.getValueAt(i, 7).toString());
            ChatLieu chatLieu = daoChatLieu.getChatLieuTheoTen(tbl_SanPham.getValueAt(i, 8).toString());
            NhaCungCap nhaCungCap = daoNhaCungCap.getNhaCungCapTheoTen(tbl_SanPham.getValueAt(i, 9).toString());
            int soLuong = Integer.parseInt(tbl_SanPham.getValueAt(i, 10).toString());

            SanPham sp = new SanPham(maSP, tenSP, soLuong, giaNhap, giaBan, ngayNhap, "", chatLieu, kichThuoc, mauSac, phanLoai, nhaCungCap);
            listSanPham.add(sp);
        }
        return listSanPham;
    }

    /**
     * Xuất bảng PDF báo cáo thông kê sản phẩm
     *
     * @param listSP
     */
    public synchronized void xuatBaoCaoThongKe(ArrayList<SanPham> listSP) {
        try {

            Font fontMain = FontFactory.getFont("src/main/java/fit/iuh/fonts/vuArial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font fontTD = FontFactory.getFont("src/main/java/fit/iuh/fonts/vuArial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            fontTD.setSize(22);
            fontTD.setFamily(Font.BOLD + "");

            // Tạo một đối tượng Random
            Random random = new Random();

            // Sinh dãy số tự nhiên ngẫu nhiên gồm 3 ký tự (bao gồm chữ cái và số từ 0 đến 9)
            StringBuilder randomNumber = new StringBuilder(3);
            for (int i = 0; i < 3; i++) {
                char randomChar;
                if (random.nextBoolean()) {
                    // Sinh ra một chữ cái ngẫu nhiên
                    randomChar = (char) (random.nextInt(26) + 'A');
                } else {
                    // Sinh ra một số ngẫu nhiên từ 0 đến 9
                    randomChar = (char) (random.nextInt(10) + '0');
                }
                randomNumber.append(randomChar);
            }
            String pathFull = null;

            if (activeTatCa) {
                pathFull = "data/BaoCaoTKSP/" + "BaoCaoSanPham" + randomNumber + ".pdf";
            } else if (activeBanCham) {
                pathFull = "data/BaoCaoTKSP/" + "BaoCaoSanPhamBanCham" + randomNumber + ".pdf";
            } else if (activeBanChay) {
                pathFull = "data/BaoCaoTKSP/" + "BaoCaoSanPhamBanChay" + randomNumber + ".pdf";
            } else if (activeHangMoiNhap) {
                pathFull = "data/BaoCaoTKSP/" + "BaoCaoSanPhamHangMoiNhap" + randomNumber + ".pdf";
            } else if (activeHetHang) {
                pathFull = "data/BaoCaoTKSP/" + "BaoCaoSanPhamHetHang" + randomNumber + ".pdf";
            } else if (activeNgay) {
                pathFull = "data/BaoCaoTKSP/" + "BaoCaoSanPhamTheoNgay" + randomNumber + ".pdf";
            }

            Document document = new Document(PageSize.A4.rotate()); //Add page khổ ngang
            PdfWriter.getInstance(document, new FileOutputStream(pathFull)); //Tạo ra đối tượng ghi dữ liệu vào tài liệu PDF
            document.open();

            //Tiêu đề 
            Paragraph paragraph = null;
            if (activeTatCa) {
                paragraph = new Paragraph("Danh Sách Sản Phẩm", fontTD);
            } else if (activeBanCham) {
                paragraph = new Paragraph("Top Sản Phẩm Bán Chậm", fontTD);
            } else if (activeBanChay) {
                paragraph = new Paragraph("Top Sản Phẩm Bán Chạy", fontTD);
            } else if (activeHangMoiNhap) {
                paragraph = new Paragraph("Danh Sách Sản Phẩm Hàng Mới Nhập", fontTD);
            } else if (activeHetHang) {
                paragraph = new Paragraph("Danh Sách Sản Phẩm Hết Hàng", fontTD);
            } else if (activeNgay) {
                paragraph = new Paragraph("Danh Sách Sản Phẩm Theo Ngày", fontTD);
            }
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            //Tạo Mục
            PdfPTable tableMuc = new PdfPTable(2);
            tableMuc.setWidthPercentage(100); //Đặt chiều rộng ứng với 100% trang
            tableMuc.setSpacingBefore(10f); //Đặt khoảng cách là 10
            tableMuc.setSpacingAfter(10f);

            float[] chieuRongCot = {1f, 1f};
            tableMuc.setWidths(chieuRongCot);

            //Mục mã nhân viên
            PdfPCell cellMaNV = new PdfPCell(new Paragraph("Mã nhân viên: " + nhanVien.getMaNV(), fontMain));
            cellMaNV.setBorderColor(BaseColor.WHITE);
            tableMuc.addCell(cellMaNV);

            //Mục ngày in
            Date ngayIn = new Date();
            SimpleDateFormat fomatter = new SimpleDateFormat("dd-MM-yyyy");
            String ngayInformat = fomatter.format(ngayIn);
            PdfPCell cellNgayIn = new PdfPCell(new Paragraph("Ngày in: " + ngayInformat, fontMain));
            cellNgayIn.setBorderColor(BaseColor.WHITE);
            tableMuc.addCell(cellNgayIn);

            //Mục ngày in
            PdfPCell cellTenNV = new PdfPCell(new Paragraph("Tên Nhân viên: " + nhanVien.getHoTen(), fontMain));
            cellTenNV.setBorderColor(BaseColor.WHITE);
            tableMuc.addCell(cellTenNV);

            //Mục chức vụ
            PdfPCell cellChucVu = new PdfPCell(new Paragraph("Chức vụ: " + nhanVien.getChuVu(), fontMain));
            cellChucVu.setBorderColor(BaseColor.WHITE);
            tableMuc.addCell(cellChucVu);

            //Mục tổng san phẩm
            PdfPTable tableTongSP = new PdfPTable(1);
            tableTongSP.setWidthPercentage(100); //Đặt chiều rộng ứng với 100% trang
            tableTongSP.setSpacingBefore(10f); //Đặt khoảng cách là 10
            tableTongSP.setSpacingAfter(10f);

            float[] chieuRongCotTongKH = {1f};
            tableTongSP.setWidths(chieuRongCotTongKH);

            PdfPCell cellTongKH = new PdfPCell(new Paragraph("Tổng sản phẩm: " + txt_TongSanPham.getText(), fontMain));
            cellTongKH.setBorderColor(BaseColor.WHITE);
            tableTongSP.addCell(cellTongKH);

            document.add(tableMuc);
            document.add(tableTongSP);

            //Tạo bảng sản phẩm
            PdfPTable tableDsSP = new PdfPTable(11);
            tableDsSP.setWidthPercentage(100); //Đặt chiều rộng ứng với 100% trang
            tableDsSP.setSpacingBefore(10f); //Đặt khoảng cách là 10
            tableDsSP.setSpacingAfter(10f);

            //Tiêu đề bảng
            float[] chieuRongCotSP = {2f, 4f, 2f, 3f, 3f, 3f, 2f, 2f, 2f, 4f, 2f};
            tableDsSP.setWidths(chieuRongCotSP);

            //Mã sản phẩm
            PdfPCell cellTblSP_maSP = new PdfPCell(new Paragraph("Mã sản phẩm ", fontMain));
            cellTblSP_maSP.setBorderColor(BaseColor.BLACK);
            cellTblSP_maSP.setVerticalAlignment(Element.ALIGN_MIDDLE);//Chỉnh text của cột theo chiều dọc
            cellTblSP_maSP.setHorizontalAlignment(Element.ALIGN_CENTER);// Chỉnh text cửa cột theo chiều ngang
            tableDsSP.addCell(cellTblSP_maSP);

            //Tên sản phẩm
            PdfPCell cellTblSP_tenSP = new PdfPCell(new Paragraph("Tên sản phẩm ", fontMain));
            cellTblSP_tenSP.setBorderColor(BaseColor.BLACK);
            cellTblSP_tenSP.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_tenSP.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_tenSP);

            //Phân loại
            PdfPCell cellTblSP_PL = new PdfPCell(new Paragraph("Phân loại ", fontMain));
            cellTblSP_PL.setBorderColor(BaseColor.BLACK);
            cellTblSP_PL.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_PL.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_PL);

            //Giá bán
            PdfPCell cellTblSP_giaBan = new PdfPCell(new Paragraph("Giá bán ", fontMain));
            cellTblSP_giaBan.setBorderColor(BaseColor.BLACK);
            cellTblSP_giaBan.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_giaBan.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_giaBan);

            //Giá nhập
            PdfPCell cellTblSP_giaNhap = new PdfPCell(new Paragraph("Giá nhập ", fontMain));
            cellTblSP_giaNhap.setBorderColor(BaseColor.BLACK);
            cellTblSP_giaNhap.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_giaNhap.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_giaNhap);

            //Ngày nhập
            PdfPCell cellTblSP_ngayNhap = new PdfPCell(new Paragraph("Ngày nhập ", fontMain));
            cellTblSP_ngayNhap.setBorderColor(BaseColor.BLACK);
            cellTblSP_ngayNhap.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_ngayNhap.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_ngayNhap);

            //Kích thước
            PdfPCell cellTblSP_KT = new PdfPCell(new Paragraph("Kích cỡ ", fontMain));
            cellTblSP_KT.setBorderColor(BaseColor.BLACK);
            cellTblSP_KT.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_KT.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_KT);

            //Màu sắc
            PdfPCell cellTblSP_MS = new PdfPCell(new Paragraph("Màu sắc", fontMain));
            cellTblSP_MS.setBorderColor(BaseColor.BLACK);
            cellTblSP_MS.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_MS.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_MS);

            //Chất liệu
            PdfPCell cellTblSP_CL = new PdfPCell(new Paragraph("Chất liệu ", fontMain));
            cellTblSP_CL.setBorderColor(BaseColor.BLACK);
            cellTblSP_CL.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_CL.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_CL);

            // Nhà cung cấp
            PdfPCell cellTblSP_NCC = new PdfPCell(new Paragraph("Nhà cung cấp ", fontMain));
            cellTblSP_NCC.setBorderColor(BaseColor.BLACK);
            cellTblSP_NCC.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_NCC.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_NCC);
            //Số lượng
            PdfPCell cellTblSP_SL = new PdfPCell(new Paragraph("Số lượng ", fontMain));
            cellTblSP_SL.setBorderColor(BaseColor.BLACK);
            cellTblSP_SL.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_SL.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_SL);

            //Thong tin san pham
            for (SanPham sp : listSP) {

                PhanLoai pl = daoPhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
                KichThuoc kt = daoKichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
                MauSac ms = daoMauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
                ChatLieu cl = daoChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
                NhaCungCap ncc = daoNhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

                //Mã sản phẩm
                PdfPCell cellTblSP_maSP_giaTri = new PdfPCell(new Paragraph(String.valueOf(sp.getMaSP()), fontMain));
                cellTblSP_maSP_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_maSP_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableDsSP.addCell(cellTblSP_maSP_giaTri);

                //Tên sản phẩm
                PdfPCell cellTblSP_tenSP_giaTri = new PdfPCell(new Paragraph(sp.getTenSP(), fontMain));
                cellTblSP_tenSP_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_tenSP_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableDsSP.addCell(cellTblSP_tenSP_giaTri);

                //Phân loại
                PdfPCell cellTblSP_PL_giaTri = new PdfPCell(new Paragraph(pl.getLoaiSanPham(), fontMain));
                cellTblSP_PL_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_PL_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_PL_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_PL_giaTri);

                //Giá bán
                PdfPCell cellTblSP_giaBan_giaTri = new PdfPCell(new Paragraph(NumberFormat.getInstance().format(sp.getGiaBan()) + "", fontMain));
                cellTblSP_giaBan_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_giaBan_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_giaBan_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_giaBan_giaTri);

                //Giá nhập
                PdfPCell cellTblSP_giaNhap_giaTri = new PdfPCell(new Paragraph(NumberFormat.getInstance().format(sp.getGiaNhap()) + "", fontMain));
                cellTblSP_giaNhap_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_giaNhap_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_giaNhap_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_giaNhap_giaTri);

                //Ngày nhập
                PdfPCell cellTblSP_ngayNhap_giaTri = new PdfPCell(new Paragraph(fomatter.format(sp.getNgayNhap()), fontMain));
                cellTblSP_ngayNhap_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_ngayNhap_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_ngayNhap_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_ngayNhap_giaTri);

                //Kích thước
                PdfPCell cellTblSP_KT_giaTri = new PdfPCell(new Paragraph(kt.getKichThuoc(), fontMain));
                cellTblSP_KT_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_KT_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_KT_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_KT_giaTri);

                //Màu sắc
                PdfPCell cellTblSP_MS_giaTri = new PdfPCell(new Paragraph(ms.getMauSac(), fontMain));
                cellTblSP_MS_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_MS_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_MS_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_MS_giaTri);

                //Chất liệu
                PdfPCell cellTblSP_CL_giaTri = new PdfPCell(new Paragraph(cl.getChatLieu(), fontMain));
                cellTblSP_CL_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_CL_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_CL_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_CL_giaTri);

                // Nhà cung cấp
                PdfPCell cellTblSP_NCC_giaTri = new PdfPCell(new Paragraph(ncc.getTenNCC(), fontMain));
                cellTblSP_NCC_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_NCC_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableDsSP.addCell(cellTblSP_NCC_giaTri);

                //Số lượng
                PdfPCell cellTblSP_SL_giaTri = new PdfPCell(new Paragraph(sp.getSoLuong() + "", fontMain));
                cellTblSP_SL_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_SL_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_SL_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_SL_giaTri);
            }

            document.add(tableDsSP);

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
        pnl_NV_TieuDe = new javax.swing.JPanel();
        lbl_TieuDe = new javax.swing.JLabel();
        pnl_ThongTin = new javax.swing.JPanel();
        lbl_PhanLoai = new javax.swing.JLabel();
        lbl_KichThuoc = new javax.swing.JLabel();
        lbl_MauSac = new javax.swing.JLabel();
        pnl_TongSanPham = new javax.swing.JPanel();
        lbl_TongSanPham = new javax.swing.JLabel();
        txt_TongSanPham = new javax.swing.JTextField();
        cmb_PhanLoai = new javax.swing.JComboBox<>();
        cmb_MauSac = new javax.swing.JComboBox<>();
        cmb_KichThuoc = new javax.swing.JComboBox<>();
        chk_TatCa = new javax.swing.JCheckBox();
        rad_HangMoiNhap = new javax.swing.JRadioButton();
        rad_HetHangTrongKho = new javax.swing.JRadioButton();
        dch_TuNgay = new com.toedter.calendar.JDateChooser();
        lbl_TuNgay = new javax.swing.JLabel();
        lbl_DenNgay = new javax.swing.JLabel();
        dch_DenNgay = new com.toedter.calendar.JDateChooser();
        pnl_NutChucNang = new javax.swing.JPanel();
        btn_XuatThongKe = new javax.swing.JButton();
        btn_BieuDo = new javax.swing.JButton();
        btn_TopSPBanChay = new javax.swing.JButton();
        btn_TopSPBanChạm = new javax.swing.JButton();

        setBackground(new java.awt.Color(199, 210, 213));
        setMinimumSize(new java.awt.Dimension(1000, 550));
        setPreferredSize(new java.awt.Dimension(1000, 550));

        scr_DanhSachSanPham.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        tbl_SanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbl_SanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Phân loại", "Giá bán ", "Giá nhập", "Ngày nhập", "Kích cỡ", "Màu sắc", "Chất liệu", "Nhà cung cấp", "Số lượng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public synchronized Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbl_SanPham.setRowHeight(35);
        tbl_SanPham.setShowGrid(true);
        scr_DanhSachSanPham.setViewportView(tbl_SanPham);

        lbl_TieuDe.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        lbl_TieuDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TieuDe.setText("THỐNG KÊ SẢN PHẨM");
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
            .addComponent(scr_DanhSachSanPham)
            .addComponent(pnl_NV_TieuDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnl_DanhSachSanPhamLayout.setVerticalGroup(
            pnl_DanhSachSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_DanhSachSanPhamLayout.createSequentialGroup()
                .addComponent(pnl_NV_TieuDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scr_DanhSachSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
        );

        pnl_ThongTin.setBackground(new java.awt.Color(199, 210, 213));
        pnl_ThongTin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pnl_ThongTin.setPreferredSize(new java.awt.Dimension(1151, 300));

        lbl_PhanLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_PhanLoai.setText("Phân loại");

        lbl_KichThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_KichThuoc.setText("Kích thước");

        lbl_MauSac.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_MauSac.setText("Màu sắc");

        pnl_TongSanPham.setBackground(new java.awt.Color(255, 255, 255));
        pnl_TongSanPham.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        lbl_TongSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TongSanPham.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TongSanPham.setText("Tổng sản phẩm");
        lbl_TongSanPham.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        txt_TongSanPham.setEditable(false);
        txt_TongSanPham.setBackground(new java.awt.Color(255, 255, 255));
        txt_TongSanPham.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_TongSanPham.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_TongSanPham.setText("0");
        txt_TongSanPham.setBorder(null);

        javax.swing.GroupLayout pnl_TongSanPhamLayout = new javax.swing.GroupLayout(pnl_TongSanPham);
        pnl_TongSanPham.setLayout(pnl_TongSanPhamLayout);
        pnl_TongSanPhamLayout.setHorizontalGroup(
            pnl_TongSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_TongSanPhamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_TongSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_TongSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(txt_TongSanPham, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        pnl_TongSanPhamLayout.setVerticalGroup(
            pnl_TongSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_TongSanPhamLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_TongSanPham)
                .addGap(42, 42, 42)
                .addComponent(txt_TongSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );

        cmb_PhanLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmb_PhanLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        cmb_PhanLoai.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    cmb_PhanLoaiItemStateChanged(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

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

        cmb_KichThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmb_KichThuoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        cmb_KichThuoc.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    cmb_KichThuocItemStateChanged(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        chk_TatCa.setBackground(new java.awt.Color(199, 210, 213));
        chk_TatCa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chk_TatCa.setSelected(true);
        chk_TatCa.setText("Tất cả");
        chk_TatCa.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    chk_TatCaItemStateChanged(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        rad_HangMoiNhap.setBackground(new java.awt.Color(199, 210, 213));
        rad_HangMoiNhap.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rad_HangMoiNhap.setText("Hàng mới nhập");
        rad_HangMoiNhap.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    rad_HangMoiNhapItemStateChanged(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        rad_HetHangTrongKho.setBackground(new java.awt.Color(199, 210, 213));
        rad_HetHangTrongKho.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rad_HetHangTrongKho.setText("Hết hàng trong kho");
        rad_HetHangTrongKho.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    rad_HetHangTrongKhoItemStateChanged(evt);
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
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        lbl_TuNgay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TuNgay.setText("Nhập từ ngày:");

        lbl_DenNgay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_DenNgay.setText("Đến ngày:");

        dch_DenNgay.setDateFormatString("dd-MM-yyyy");
        dch_DenNgay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dch_DenNgay.setMinimumSize(new java.awt.Dimension(64, 22));
        dch_DenNgay.setPreferredSize(new java.awt.Dimension(64, 22));
        dch_DenNgay.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public synchronized void propertyChange(java.beans.PropertyChangeEvent evt) {
                try {
                    dch_DenNgayPropertyChange(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        javax.swing.GroupLayout pnl_ThongTinLayout = new javax.swing.GroupLayout(pnl_ThongTin);
        pnl_ThongTin.setLayout(pnl_ThongTinLayout);
        pnl_ThongTinLayout.setHorizontalGroup(
            pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(pnl_TongSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rad_HangMoiNhap)
                    .addComponent(rad_HetHangTrongKho)
                    .addComponent(chk_TatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(76, 76, 76)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_KichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmb_MauSac, 0, 195, Short.MAX_VALUE)
                    .addComponent(cmb_KichThuoc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmb_PhanLoai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(82, 82, 82)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_TuNgay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_DenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dch_TuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dch_DenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47))
        );
        pnl_ThongTinLayout.setVerticalGroup(
            pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(pnl_TongSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lbl_KichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmb_KichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rad_HangMoiNhap)
                            .addComponent(lbl_TuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addComponent(rad_HetHangTrongKho)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chk_TatCa))
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addComponent(dch_TuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbl_DenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dch_DenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(lbl_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmb_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lbl_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmb_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );

        pnl_NutChucNang.setBackground(new java.awt.Color(199, 210, 213));

        btn_XuatThongKe.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_XuatThongKe.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-analytics-30.png")); // NOI18N
        btn_XuatThongKe.setText("Xuất thống kê");
        btn_XuatThongKe.setBorder(null);
        btn_XuatThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_XuatThongKeMouseEntered(evt);
            }
            public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
                btn_XuatThongKeMouseExited(evt);
            }
        });
        btn_XuatThongKe.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XuatThongKeActionPerformed(evt);
            }
        });

        btn_BieuDo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_BieuDo.setIcon(new javax.swing.ImageIcon("src/main/java/fit/iuh/imageGD/icons8-analytics-30.png")); // NOI18N
        btn_BieuDo.setText("Xem biểu đồ");
        btn_BieuDo.setBorder(null);
        btn_BieuDo.addMouseListener(new java.awt.event.MouseAdapter() {
            public synchronized void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_BieuDoMouseEntered(evt);
            }
            public synchronized void mouseExited(java.awt.event.MouseEvent evt) {
                btn_BieuDoMouseExited(evt);
            }
        });
        btn_BieuDo.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BieuDoActionPerformed(evt);
            }
        });

        btn_TopSPBanChay.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_TopSPBanChay.setText("Top 5 những sản phẩm bán chạy");
        btn_TopSPBanChay.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_TopSPBanChayActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btn_TopSPBanChạm.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_TopSPBanChạm.setText("Top 5 những sản phẩm bán chậm");
        btn_TopSPBanChạm.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_TopSPBanChạmActionPerformed(evt);
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
                .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_TopSPBanChay, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_TopSPBanChạm, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 36, Short.MAX_VALUE))
            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_XuatThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_BieuDo, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_NutChucNangLayout.setVerticalGroup(
            pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btn_TopSPBanChay, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_TopSPBanChạm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btn_XuatThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btn_BieuDo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnl_DanhSachSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 1180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnl_NutChucNang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_DanhSachSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnl_ThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(pnl_NutChucNang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private synchronized void btn_XuatThongKeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_XuatThongKeMouseEntered
        btn_XuatThongKe.setBackground(new Color(0x9EDDFF));
        btn_XuatThongKe.setForeground(new Color(0x141E46));
    }//GEN-LAST:event_btn_XuatThongKeMouseEntered

    private synchronized void btn_XuatThongKeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_XuatThongKeMouseExited
        btn_XuatThongKe.setBackground(UIManager.getColor("Menu.background"));
        btn_XuatThongKe.setForeground(UIManager.getColor("Menu.foreground"));
    }//GEN-LAST:event_btn_XuatThongKeMouseExited

    private synchronized void btn_TopSPBanChayActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_TopSPBanChayActionPerformed
        khoiTaoNgayHienTai();
        cmb_KichThuoc.setSelectedIndex(0);
        cmb_MauSac.setSelectedIndex(0);
        cmb_PhanLoai.setSelectedIndex(0);
        thongKeSanPhamBanChay();
        chk_TatCa.setSelected(false);
        rad_HangMoiNhap.setSelected(false);
        rad_HetHangTrongKho.setSelected(false);

        activeTatCa = false;
        activeHangMoiNhap = false;
        activeHetHang = false;
        activeBanChay = true;
        activeBanCham = false;
        activeNgay = false;
    }//GEN-LAST:event_btn_TopSPBanChayActionPerformed

    private synchronized void btn_TopSPBanChạmActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_TopSPBanChạmActionPerformed
        khoiTaoNgayHienTai();
        cmb_KichThuoc.setSelectedIndex(0);
        cmb_MauSac.setSelectedIndex(0);
        cmb_PhanLoai.setSelectedIndex(0);
        thongKeSanPhamBanCham();
        chk_TatCa.setSelected(false);
        rad_HangMoiNhap.setSelected(false);
        rad_HetHangTrongKho.setSelected(false);

        activeTatCa = false;
        activeHangMoiNhap = false;
        activeHetHang = false;
        activeBanChay = false;
        activeBanCham = true;
        activeNgay = false;
    }//GEN-LAST:event_btn_TopSPBanChạmActionPerformed

    private synchronized void cmb_KichThuocItemStateChanged(java.awt.event.ItemEvent evt) throws RemoteException {//GEN-FIRST:event_cmb_KichThuocItemStateChanged
        khoiTaoNgayHienTai();

        if (rad_HetHangTrongKho.isSelected()) {
            thongKeDsSanPhamHetHang();
            activeHetHang = true;
            activeTatCa = false;
        } else {
            thongKeDsSanPhamTheoTieuChi();
            activeTatCa = true;
            activeHetHang = false;
        }

        activeHangMoiNhap = false;
        activeBanChay = false;
        activeBanCham = false;
        activeNgay = false;
        khoiTaoNgayHienTai();
    }//GEN-LAST:event_cmb_KichThuocItemStateChanged

    private synchronized void cmb_MauSacItemStateChanged(java.awt.event.ItemEvent evt) throws RemoteException {//GEN-FIRST:event_cmb_MauSacItemStateChanged
        khoiTaoNgayHienTai();

        if (rad_HetHangTrongKho.isSelected()) {
            thongKeDsSanPhamHetHang();
            activeHetHang = true;
            activeTatCa = false;
        } else {
            thongKeDsSanPhamTheoTieuChi();
            activeTatCa = true;
            activeHetHang = false;
        }
        activeHangMoiNhap = false;
        activeBanChay = false;
        activeBanCham = false;
        activeNgay = false;
    }//GEN-LAST:event_cmb_MauSacItemStateChanged

    private synchronized void cmb_PhanLoaiItemStateChanged(java.awt.event.ItemEvent evt) throws RemoteException {//GEN-FIRST:event_cmb_PhanLoaiItemStateChanged
        khoiTaoNgayHienTai();

        if (rad_HetHangTrongKho.isSelected()) {
            thongKeDsSanPhamHetHang();
            activeHetHang = true;
            activeTatCa = false;
        } else {
            thongKeDsSanPhamTheoTieuChi();
            activeTatCa = true;
            activeHetHang = false;
        }
        activeHangMoiNhap = false;
        activeBanChay = false;
        activeBanCham = false;
        activeNgay = false;

    }//GEN-LAST:event_cmb_PhanLoaiItemStateChanged

    private synchronized void chk_TatCaItemStateChanged(java.awt.event.ItemEvent evt) throws RemoteException {//GEN-FIRST:event_chk_TatCaItemStateChanged
        if (chk_TatCa.isSelected()) {
            rad_HangMoiNhap.setSelected(false);
            rad_HetHangTrongKho.setSelected(false);
            thongKeDsSanPhamTheoTieuChi();
        }
        activeTatCa = true;
        activeHangMoiNhap = false;
        activeHetHang = false;
        activeBanChay = false;
        activeBanCham = false;
        activeNgay = false;

    }//GEN-LAST:event_chk_TatCaItemStateChanged

    private synchronized void rad_HetHangTrongKhoItemStateChanged(java.awt.event.ItemEvent evt) throws RemoteException {//GEN-FIRST:event_rad_HetHangTrongKhoItemStateChanged

        if (rad_HetHangTrongKho.isSelected()) {
            rad_HangMoiNhap.setSelected(false);
            rad_HetHangTrongKho.setSelected(true);
            chk_TatCa.setSelected(false);
            thongKeDsSanPhamHetHang();
        }

        activeTatCa = false;
        activeHangMoiNhap = false;
        activeHetHang = true;
        activeBanChay = false;
        activeBanCham = false;
        activeNgay = false;
        khoiTaoNgayHienTai();

    }//GEN-LAST:event_rad_HetHangTrongKhoItemStateChanged

    private synchronized void rad_HangMoiNhapItemStateChanged(java.awt.event.ItemEvent evt) throws RemoteException {//GEN-FIRST:event_rad_HangMoiNhapItemStateChanged
        cmb_KichThuoc.setSelectedItem(0);
        cmb_MauSac.setSelectedItem(0);
        cmb_PhanLoai.setSelectedItem(0);
        if (rad_HangMoiNhap.isSelected()) {
            rad_HetHangTrongKho.setSelected(false);
            rad_HangMoiNhap.setSelected(true);

            chk_TatCa.setSelected(false);
            thongKeDsSanPhamMoiNhap();
        }
        activeTatCa = false;
        activeHangMoiNhap = true;
        activeHetHang = false;
        activeBanChay = false;
        activeBanCham = false;
        activeNgay = false;
        khoiTaoNgayHienTai();

    }//GEN-LAST:event_rad_HangMoiNhapItemStateChanged

    private synchronized void btn_XuatThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XuatThongKeActionPerformed
        try {
            ArrayList<SanPham> listSP = getGiaTriTrongBang();
            xuatBaoCaoThongKe(listSP);
        } catch (ParseException ex) {
            Logger.getLogger(ManHinh_NV_ThongKeSanPham.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }//GEN-LAST:event_btn_XuatThongKeActionPerformed

    private synchronized void btn_BieuDoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_BieuDoMouseEntered
        btn_BieuDo.setBackground(new Color(0x9EDDFF));
        btn_BieuDo.setForeground(new Color(0x141E46));
    }//GEN-LAST:event_btn_BieuDoMouseEntered

    private synchronized void btn_BieuDoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_BieuDoMouseExited
        btn_BieuDo.setBackground(UIManager.getColor("Menu.background"));
        btn_BieuDo.setForeground(UIManager.getColor("Menu.foreground"));
    }//GEN-LAST:event_btn_BieuDoMouseExited

    private synchronized void btn_BieuDoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BieuDoActionPerformed
        try {
            new ManHinh_NV_BieuDoThongKeSP().setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(ManHinh_NV_ThongKeSanPham.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }//GEN-LAST:event_btn_BieuDoActionPerformed

    private synchronized void dch_TuNgayPropertyChange(java.beans.PropertyChangeEvent evt) throws RemoteException {//GEN-FIRST:event_dch_TuNgayPropertyChange
        rad_HangMoiNhap.setSelected(false);
        rad_HetHangTrongKho.setSelected(false);
        if (!dieuKienTuNgay()) {
            return;
        }


    }//GEN-LAST:event_dch_TuNgayPropertyChange

    private synchronized void dch_DenNgayPropertyChange(java.beans.PropertyChangeEvent evt) throws RemoteException {//GEN-FIRST:event_dch_DenNgayPropertyChange
        rad_HangMoiNhap.setSelected(false);
        rad_HetHangTrongKho.setSelected(false);

        if (!dieuKienDenNgay()) {
            return;
        }

        if (chk_TatCa.isSelected()) {
            thongKeDsSanPhamTheoTieuChi();
        } else if (!chk_TatCa.isSelected() && activeHetHang == false && activeHangMoiNhap == false) {
            thongKeSanPhamTheoNgayNhap();

        }
        activeTatCa = false;
        activeHangMoiNhap = false;
        activeHetHang = false;
        activeBanChay = false;
        activeBanCham = false;
        activeNgay = true;
    }//GEN-LAST:event_dch_DenNgayPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_BieuDo;
    private javax.swing.JButton btn_TopSPBanChay;
    private javax.swing.JButton btn_TopSPBanChạm;
    private javax.swing.JButton btn_XuatThongKe;
    private javax.swing.JCheckBox chk_TatCa;
    private javax.swing.JComboBox<String> cmb_KichThuoc;
    private javax.swing.JComboBox<String> cmb_MauSac;
    private javax.swing.JComboBox<String> cmb_PhanLoai;
    private com.toedter.calendar.JDateChooser dch_DenNgay;
    private com.toedter.calendar.JDateChooser dch_TuNgay;
    private javax.swing.JLabel lbl_DenNgay;
    private javax.swing.JLabel lbl_KichThuoc;
    private javax.swing.JLabel lbl_MauSac;
    private javax.swing.JLabel lbl_PhanLoai;
    private javax.swing.JLabel lbl_TieuDe;
    private javax.swing.JLabel lbl_TongSanPham;
    private javax.swing.JLabel lbl_TuNgay;
    private javax.swing.JPanel pnl_DanhSachSanPham;
    private javax.swing.JPanel pnl_NV_TieuDe;
    private javax.swing.JPanel pnl_NutChucNang;
    private javax.swing.JPanel pnl_ThongTin;
    private javax.swing.JPanel pnl_TongSanPham;
    private javax.swing.JRadioButton rad_HangMoiNhap;
    private javax.swing.JRadioButton rad_HetHangTrongKho;
    private javax.swing.JScrollPane scr_DanhSachSanPham;
    private javax.swing.JTable tbl_SanPham;
    private javax.swing.JTextField txt_TongSanPham;
    // End of variables declaration//GEN-END:variables
}
