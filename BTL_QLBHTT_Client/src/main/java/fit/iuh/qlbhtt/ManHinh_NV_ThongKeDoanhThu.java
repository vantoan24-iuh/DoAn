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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DMX
 */
public  class ManHinh_NV_ThongKeDoanhThu extends javax.swing.JPanel {

    private boolean initial = true;
    private IChatLieuDao dao_ChatLieu = RMIClientUtil.getChatLieuDao();
    private IKichThuocDao dao_KichThuoc = RMIClientUtil.getKichThuocDao();
    private IMauSacDao dao_MauSac = RMIClientUtil.getMauSacDao();
    private IPhanLoaiDao dao_PhanLoai = RMIClientUtil.getPhanLoaiDao();
    private INhaCungCapDao dao_NhaCungCap = RMIClientUtil.getNhaCungCapDao();
    private IHoaDonDao dao_HoaDon = RMIClientUtil.getHoaDonDao();
    private ICTHDDao dao_CTHD = RMIClientUtil.getCtHoaDonDao();

    private boolean activeTatCa = false;
    private boolean activeTKTheoNgay = false;
    private boolean activeTKDTCao = false;
    private boolean activeTKDTThap = false;

    private final NhanVien nhanVien = Login.nhanVien;
    private int tongSoSanPhamBanDuoc;
    private double tongDoanhThu;

    /**
     * Creates new form quanly
     */
    public  ManHinh_NV_ThongKeDoanhThu() throws SQLException, RemoteException {
//        connect.connect();
        initComponents();

        tbl_SanPham.setDefaultEditor(Object.class, null); //Không cho chỉnh sửa cột
        tbl_SanPham.getTableHeader().setReorderingAllowed(false); //Không cho di chuyển cột

        activeTatCa = true;
        khoiTaoGiaTri();
        loadComboxNam();
        loadDanhSachSanPham();
    }

    public synchronized void loadComboxNam() throws RemoteException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        ArrayList<Integer> namList = new ArrayList<>();
        int nam = 0;
        for (HoaDon hd : dao_HoaDon.getAllHoaDon()) {
            calendar.setTime(hd.getNgayNhap());

            if (namList.contains(calendar.get(Calendar.YEAR))) {
                continue;
            }
            nam = calendar.get(Calendar.YEAR);
            cmb_NamLap.addItem(nam + "");
            namList.add(nam);
        }
    }

    public synchronized void loadLaiGiaTriBanDau() {
        cmb_KichThuoc.setSelectedIndex(0);
        cmb_MauSac.setSelectedIndex(0);
        cmb_PhanLoai.setSelectedIndex(0);
        cmb_ThangLap.setSelectedIndex(0);
        cmb_NamLap.setSelectedIndex(0);

        dch_TuNgay.setDate(Calendar.getInstance().getTime());
        dch_DenNgay.setDate(Calendar.getInstance().getTime());
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

    private synchronized void khoiTaoGiaTri() throws RemoteException {
        //Doc du lieu cmb Phan loai
        ArrayList<PhanLoai> listPhanLoai = dao_PhanLoai.getAllPhanLoai();
        for (PhanLoai pl : listPhanLoai) {
            cmb_PhanLoai.addItem(pl.getLoaiSanPham());
        }
        //Doc du lieu cmb Mau Sac
        ArrayList<MauSac> listMauSac = dao_MauSac.getAllMauSac();
        for (MauSac ms : listMauSac) {
            cmb_MauSac.addItem(ms.getMauSac());
        }
        //Doc du lieu cmb Kich Thuoc
        ArrayList<KichThuoc> listKichThuoc = dao_KichThuoc.getAllKichThuoc();
        for (KichThuoc kt : listKichThuoc) {
            cmb_KichThuoc.addItem(kt.getKichThuoc());
        }

        chk_TatCa.setSelected(true);

        dch_TuNgay.setDate(Calendar.getInstance().getTime());
        dch_DenNgay.setDate(Calendar.getInstance().getTime());

    }

    private synchronized void clearTable() {
        DefaultTableModel dtm = (DefaultTableModel) tbl_SanPham.getModel();
        dtm.setRowCount(0);
    }

    private synchronized void loadDanhSachSanPham() throws RemoteException {
        clearTable();
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

        String thangLap = cmb_ThangLap.getSelectedItem().toString();
        String ktthangLap = cmb_ThangLap.getSelectedItem().toString();
        if (ktthangLap.equalsIgnoreCase("Tất cả")) {
            thangLap = "";
        }

        String namLap = cmb_NamLap.getSelectedItem().toString();
        String ktnamLap = cmb_NamLap.getSelectedItem().toString();
        if (ktnamLap.equalsIgnoreCase("Tất cả")) {
            namLap = "";
        }

        ArrayList<SanPham> listSanPham = dao_HoaDon.thongKeDanhSachSanPhamVoiSoLuongBanDuocByTieuChi(mauSac, phanLoai, kichThuoc);
        DefaultTableModel dtm = (DefaultTableModel) tbl_SanPham.getModel();
        tongSoSanPhamBanDuoc = listSanPham.size();
        double tongDoanhThu = 0;
        for (SanPham sp : listSanPham) {
            PhanLoai pl = dao_PhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = dao_KichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
            MauSac ms = dao_MauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
            ChatLieu cl = dao_ChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = dao_NhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

            int soLuongBanDuoc = dao_CTHD.getSoLuongSanPhamBanDuoc(sp.getMaSP(), thangLap, namLap);
            double doanhThu = dao_CTHD.getDoanhThuSanPhamBanDuoc(sp.getMaSP(), thangLap, namLap);
            tongDoanhThu += dao_CTHD.getTongDoanhThu(sp.getMaSP(), thangLap, namLap);
            double tiLeDoanhThu = (sp.getGiaBan() / tongDoanhThu) * 100;
            Object[] rowData = {sp.getMaSP(), sp.getTenSP(), pl.getLoaiSanPham(), NumberFormat.getInstance().format(sp.getGiaBan()), NumberFormat.getInstance().format(sp.getGiaNhap()), kt.getKichThuoc(),
                ms.getMauSac(), cl.getChatLieu(), ncc.getTenNCC(), soLuongBanDuoc, NumberFormat.getInstance().format(doanhThu), String.format("%.2f", tiLeDoanhThu)};
            dtm.addRow(rowData);
        }

        txt_TongSanPhamBan.setText(tongSoSanPhamBanDuoc + "");
        txt_TongDoanhThu.setText(NumberFormat.getInstance().format(tongDoanhThu));
    }

    private synchronized void loadDanhSachSanPhamTheoThoiGian() throws ParseException, RemoteException {
        clearTable();
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

        String thangLap = cmb_ThangLap.getSelectedItem().toString();
        String ktthangLap = cmb_ThangLap.getSelectedItem().toString();
        if (ktthangLap.equalsIgnoreCase("Tất cả")) {
            thangLap = "";
        }

        String namLap = cmb_NamLap.getSelectedItem().toString();
        String ktnamLap = cmb_NamLap.getSelectedItem().toString();
        if (ktnamLap.equalsIgnoreCase("Tất cả")) {
            namLap = "";
        }

        String tuNgay = new SimpleDateFormat("yyyy-MM-dd").format(dch_TuNgay.getDate());
        String denNgay = new SimpleDateFormat("yyyy-MM-dd").format(dch_DenNgay.getDate());
        ArrayList<SanPham> listSanPham = dao_HoaDon.thongKeDanhSachSanPhamVoiSoLuongBanDuocByTieuChiByTime(mauSac, phanLoai, kichThuoc, tuNgay, denNgay);
        DefaultTableModel dtm = (DefaultTableModel) tbl_SanPham.getModel();
        tongSoSanPhamBanDuoc = listSanPham.size();
        tongDoanhThu = 0;
        for (SanPham sp : listSanPham) {

            PhanLoai pl = dao_PhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = dao_KichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
            MauSac ms = dao_MauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
            ChatLieu cl = dao_ChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = dao_NhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

            int soLuongBanDuoc = dao_CTHD.getSoLuongSanPhamBanDuoc(sp.getMaSP(), thangLap, namLap);
            double doanhThu = dao_CTHD.getDoanhThuSanPhamBanDuoc(sp.getMaSP(), thangLap, namLap);
            tongDoanhThu += dao_CTHD.getTongDoanhThu(sp.getMaSP(), thangLap, namLap);
            double tiLeDoanhThu = (sp.getGiaBan() / tongDoanhThu) * 100;
            Object[] rowData = {sp.getMaSP(), sp.getTenSP(), pl.getLoaiSanPham(), NumberFormat.getInstance().format(sp.getGiaBan()), NumberFormat.getInstance().format(sp.getGiaNhap()), kt.getKichThuoc(),
                    ms.getMauSac(), cl.getChatLieu(), ncc.getTenNCC(), soLuongBanDuoc, NumberFormat.getInstance().format(doanhThu), String.format("%.2f", tiLeDoanhThu)};
            dtm.addRow(rowData);
        }

        txt_TongSanPhamBan.setText(tongSoSanPhamBanDuoc + "");
        txt_TongDoanhThu.setText(NumberFormat.getInstance().format(tongDoanhThu));
    }

    private synchronized void loadDanhSachSanPhamTheoThangNam() throws RemoteException {
        clearTable();

        String thangLap = cmb_ThangLap.getSelectedItem().toString();
        String ktthangLap = cmb_ThangLap.getSelectedItem().toString();
        if (ktthangLap.equalsIgnoreCase("Tất cả")) {
            thangLap = "";
        }

        String namLap = cmb_NamLap.getSelectedItem().toString();
        String ktnamLap = cmb_NamLap.getSelectedItem().toString();
        if (ktnamLap.equalsIgnoreCase("Tất cả")) {
            namLap = "";
        }

        ArrayList<SanPham> listSanPham = dao_HoaDon.thongKeDanhSachSanPhamTheoThangNam(thangLap, namLap);
        DefaultTableModel dtm = (DefaultTableModel) tbl_SanPham.getModel();
        if(listSanPham != null) {
            tongSoSanPhamBanDuoc = listSanPham.size();
            tongDoanhThu = 0;
            for (SanPham sp : listSanPham) {
                PhanLoai pl = dao_PhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
                KichThuoc kt = dao_KichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
                MauSac ms = dao_MauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
                ChatLieu cl = dao_ChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
                NhaCungCap ncc = dao_NhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

                int soLuongBanDuoc = dao_CTHD.getSoLuongSanPhamBanDuoc(sp.getMaSP(), thangLap, namLap);
                double doanhThu = dao_CTHD.getDoanhThuSanPhamBanDuoc(sp.getMaSP(), thangLap, namLap);
                tongDoanhThu += dao_CTHD.getTongDoanhThu(sp.getMaSP(), thangLap, namLap);
                double tiLeDoanhThu = (sp.getGiaBan() / tongDoanhThu) * 100;
                Object[] rowData = {sp.getMaSP(), sp.getTenSP(), pl.getLoaiSanPham(), NumberFormat.getInstance().format(sp.getGiaBan()), NumberFormat.getInstance().format(sp.getGiaNhap()), kt.getKichThuoc(),
                        ms.getMauSac(), cl.getChatLieu(), ncc.getTenNCC(), soLuongBanDuoc, NumberFormat.getInstance().format(doanhThu), String.format("%.2f", tiLeDoanhThu)};
                dtm.addRow(rowData);
            }

            txt_TongSanPhamBan.setText(tongSoSanPhamBanDuoc + "");
            txt_TongDoanhThu.setText(NumberFormat.getInstance().format(tongDoanhThu));
        }

    }

    private synchronized void tblTop5SanPhamDoanhThuCaoNhat() throws RemoteException {
        clearTable();

        String thangLap = cmb_ThangLap.getSelectedItem().toString();
        String ktthangLap = cmb_ThangLap.getSelectedItem().toString();
        if (ktthangLap.equalsIgnoreCase("Tất cả")) {
            thangLap = "";
        }

        String namLap = cmb_NamLap.getSelectedItem().toString();
        String ktnamLap = cmb_NamLap.getSelectedItem().toString();
        if (ktnamLap.equalsIgnoreCase("Tất cả")) {
            namLap = "";
        }

        ArrayList<SanPham> listSanPham = dao_HoaDon.thongKeTop5SPDTCN();
        DefaultTableModel dtm = (DefaultTableModel) tbl_SanPham.getModel();
        tongSoSanPhamBanDuoc = listSanPham.size();
        double tongDoanhThu = 0;
        for (SanPham sp : listSanPham) {

            PhanLoai pl = dao_PhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = dao_KichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
            MauSac ms = dao_MauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
            ChatLieu cl = dao_ChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = dao_NhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

            int soLuongBanDuoc = dao_CTHD.getSoLuongSanPhamBanDuoc(sp.getMaSP(), thangLap, namLap);
            double doanhThu = dao_CTHD.getDoanhThuSanPhamBanDuoc(sp.getMaSP(), thangLap, namLap);
            tongDoanhThu += dao_CTHD.getTongDoanhThu(sp.getMaSP(), thangLap, namLap);
            double tiLeDoanhThu = (sp.getGiaBan() / tongDoanhThu) * 100;
            Object[] rowData = {sp.getMaSP(), sp.getTenSP(), pl.getLoaiSanPham(), NumberFormat.getInstance().format(sp.getGiaBan()), NumberFormat.getInstance().format(sp.getGiaNhap()), kt.getKichThuoc(),
                    ms.getMauSac(), cl.getChatLieu(), ncc.getTenNCC(), soLuongBanDuoc, NumberFormat.getInstance().format(doanhThu), String.format("%.2f", tiLeDoanhThu)};
            dtm.addRow(rowData);
        }

        txt_TongSanPhamBan.setText(tongSoSanPhamBanDuoc + "");
        txt_TongDoanhThu.setText(NumberFormat.getInstance().format(tongDoanhThu));
    }

    private synchronized void tblTop5SanPhamDoanhThuThapNhat() throws RemoteException {
        clearTable();

        String thangLap = cmb_ThangLap.getSelectedItem().toString();
        String ktthangLap = cmb_ThangLap.getSelectedItem().toString();
        if (ktthangLap.equalsIgnoreCase("Tất cả")) {
            thangLap = "";
        }

        String namLap = cmb_NamLap.getSelectedItem().toString();
        String ktnamLap = cmb_NamLap.getSelectedItem().toString();
        if (ktnamLap.equalsIgnoreCase("Tất cả")) {
            namLap = "";
        }

        ArrayList<SanPham> listSanPham = dao_HoaDon.thongKeTop5SPDTTN();
        DefaultTableModel dtm = (DefaultTableModel) tbl_SanPham.getModel();
        tongSoSanPhamBanDuoc = listSanPham.size();
        double tongDoanhThu = 0;
        for (SanPham sp : listSanPham) {

            PhanLoai pl = dao_PhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
            KichThuoc kt = dao_KichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
            MauSac ms = dao_MauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
            ChatLieu cl = dao_ChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
            NhaCungCap ncc = dao_NhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

            int soLuongBanDuoc = dao_CTHD.getSoLuongSanPhamBanDuoc(sp.getMaSP(), thangLap, namLap);
            double doanhThu = dao_CTHD.getDoanhThuSanPhamBanDuoc(sp.getMaSP(), thangLap, namLap);
            tongDoanhThu += dao_CTHD.getTongDoanhThu(sp.getMaSP(), thangLap, namLap);
            double tiLeDoanhThu = (sp.getGiaBan() / tongDoanhThu) * 100;
            Object[] rowData = {sp.getMaSP(), sp.getTenSP(), pl.getLoaiSanPham(), NumberFormat.getInstance().format(sp.getGiaBan()), NumberFormat.getInstance().format(sp.getGiaNhap()), kt.getKichThuoc(),
                    ms.getMauSac(), cl.getChatLieu(), ncc.getTenNCC(), soLuongBanDuoc, NumberFormat.getInstance().format(doanhThu), String.format("%.2f", tiLeDoanhThu)};
            dtm.addRow(rowData);
        }

        txt_TongSanPhamBan.setText(tongSoSanPhamBanDuoc + "");
        txt_TongDoanhThu.setText(NumberFormat.getInstance().format(tongDoanhThu));
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
            PhanLoai phanLoai = dao_PhanLoai.getPhanLoaiTheoTen(tbl_SanPham.getValueAt(i, 2).toString());
            Double giaBan = Double.parseDouble(tbl_SanPham.getValueAt(i, 3).toString().replace(",", ""));
            Double giaNhap = Double.parseDouble(tbl_SanPham.getValueAt(i, 4).toString().replace(",", ""));

            KichThuoc kichThuoc = dao_KichThuoc.getKichThuocTheoTen(tbl_SanPham.getValueAt(i, 5).toString());
            MauSac mauSac = dao_MauSac.getMauSacTheoTen(tbl_SanPham.getValueAt(i, 6).toString());
            ChatLieu chatLieu = dao_ChatLieu.getChatLieuTheoTen(tbl_SanPham.getValueAt(i, 7).toString());
            NhaCungCap nhaCungCap = dao_NhaCungCap.getNhaCungCapTheoTen(tbl_SanPham.getValueAt(i, 8).toString());
            int soLuong = Integer.parseInt(tbl_SanPham.getValueAt(i, 9).toString());
            String tiLeDoanhThu = tbl_SanPham.getValueAt(i, 11).toString();
            SanPham sp = new SanPham(maSP, tenSP, soLuong, giaNhap, giaBan, new Date(), tiLeDoanhThu, chatLieu, kichThuoc, mauSac, phanLoai, nhaCungCap);
            listSanPham.add(sp);
        }
        return listSanPham;
    }

    /**
     * Xuất bảng PDF báo cáo thông kê
     *
     * @param listSP
     */
    public synchronized void xuatBaoCaoThongKe(ArrayList<SanPham> listSP) {
        try {

            String thangLap = cmb_ThangLap.getSelectedItem().toString();
            String ktthangLap = cmb_ThangLap.getSelectedItem().toString();
            if (ktthangLap.equalsIgnoreCase("Tất cả")) {
                thangLap = "";
            }

            String namLap = cmb_NamLap.getSelectedItem().toString();
            String ktnamLap = cmb_NamLap.getSelectedItem().toString();
            if (ktnamLap.equalsIgnoreCase("Tất cả")) {
                namLap = "";
            }

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
                pathFull = "data/BaoCaoTKDT/" + "BaoCaoDoanhThu" + randomNumber + ".pdf";
            } else if (activeTKTheoNgay) {
                pathFull = "data/BaoCaoTKDT/" + "BaoCaoDoanhThuTheoTG" + randomNumber + ".pdf";
            } else if (activeTKDTCao) {
                pathFull = "data/BaoCaoTKDT/" + "BaoCaoDSDoanhThuCaoNhat" + randomNumber + ".pdf";
            } else if (activeTKDTThap) {
                pathFull = "data/BaoCaoTKDT/" + "BaoCaoDSDoanhThuThapNhat" + randomNumber + ".pdf";
            }

            Document document = new Document(PageSize.A4.rotate()); //Add page khổ ngang
            PdfWriter.getInstance(document, new FileOutputStream(pathFull)); //Tạo ra đối tượng ghi dữ liệu vào tài liệu PDF
            document.open();

            //Tiêu đề 
            Paragraph paragraph = null;
            if (activeTatCa) {
                paragraph = new Paragraph("Thống Kê Doanh Thu", fontTD);
            } else if (activeTKTheoNgay) {
                paragraph = new Paragraph("Thống Kê Doanh Thu Theo Thời Gian", fontTD);
            } else if (activeTKDTCao) {
                paragraph = new Paragraph("Thống Kê Danh Sách Doanh Thu Cao", fontTD);
            } else if (activeTKDTThap) {
                paragraph = new Paragraph("Thống Kê Danh Sách Doanh Thu Thấp", fontTD);

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

            //Tạo Mục
            PdfPTable tableTong = new PdfPTable(1);
            tableTong.setWidthPercentage(100); //Đặt chiều rộng ứng với 100% trang
            tableTong.setSpacingBefore(10f); //Đặt khoảng cách là 10
            tableTong.setSpacingAfter(10f);

            float[] chieuRongCot_Tong = {1f};
            tableMuc.setWidths(chieuRongCot);
            //Mục ngày in
            PdfPCell cellTongSPBan = new PdfPCell(new Paragraph("Tổng sản phẩm bán được: " + tongSoSanPhamBanDuoc, fontMain));
            cellTongSPBan.setBorderColor(BaseColor.WHITE);
            tableTong.addCell(cellTongSPBan);

            //Mục chức vụ
            PdfPCell cellTongDoanhThu = new PdfPCell(new Paragraph("Tổng doanh thu: " + txt_TongDoanhThu.getText(), fontMain));
            cellTongDoanhThu.setBorderColor(BaseColor.WHITE);
            tableTong.addCell(cellTongDoanhThu);

            document.add(tableMuc);
            document.add(tableTong);

            //Tạo bảng sản phẩm
            PdfPTable tableDsSP = new PdfPTable(12);
            tableDsSP.setWidthPercentage(100); //Đặt chiều rộng ứng với 100% trang
            tableDsSP.setSpacingBefore(10f); //Đặt khoảng cách là 10
            tableDsSP.setSpacingAfter(10f);

            //Tiêu đề bảng
            float[] chieuRongCotSP = {2f, 4f, 2f, 2f, 2f, 2f, 2f, 2f, 4f, 2f, 2f, 2f};
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
            PdfPCell cellTblSP_SL = new PdfPCell(new Paragraph("Số lượng bán", fontMain));
            cellTblSP_SL.setBorderColor(BaseColor.BLACK);
            cellTblSP_SL.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_SL.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_SL);

            //Doanh Thu
            PdfPCell cellTblSP_doanhThu = new PdfPCell(new Paragraph("Doanh thu ", fontMain));
            cellTblSP_doanhThu.setBorderColor(BaseColor.BLACK);
            cellTblSP_doanhThu.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_doanhThu.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_doanhThu);

            //Doanh Thu
            PdfPCell cellTblSP_tiLeDoanhThu = new PdfPCell(new Paragraph("Tỉ lệ doanh thu ", fontMain));
            cellTblSP_tiLeDoanhThu.setBorderColor(BaseColor.BLACK);
            cellTblSP_tiLeDoanhThu.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTblSP_tiLeDoanhThu.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDsSP.addCell(cellTblSP_tiLeDoanhThu);

            //Thong tin san pham
            for (SanPham sp : listSP) {

                PhanLoai pl = dao_PhanLoai.getDLPhanLoaiSPTheoMa(sp.getPhanLoai().getMaPhanLoai());
                KichThuoc kt = dao_KichThuoc.getDLKichThuocTheoMa(sp.getKichThuoc().getMaKichThuoc());
                MauSac ms = dao_MauSac.getDLMauSacTheoMa(sp.getMauSac().getMaMauSac());
                ChatLieu cl = dao_ChatLieu.getDLChatLieuTheoMa(sp.getChatLieu().getMaChatLieu());
                NhaCungCap ncc = dao_NhaCungCap.getNhaCungCapTheoMa(sp.getNhaCungCap().getMaNCC());

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
                PdfPCell cellTblSP_giaNhap_giaTri = new PdfPCell(new Paragraph(NumberFormat.getInstance().format((long) sp.getGiaNhap()) + "", fontMain));
                cellTblSP_giaNhap_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_giaNhap_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_giaNhap_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_giaNhap_giaTri);

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

                double doanhThu = dao_CTHD.getDoanhThuSanPhamBanDuoc(sp.getMaSP(), thangLap, namLap);

                //Doanh Thu
                PdfPCell cellTblSP_doanhThu_giaTri = new PdfPCell(new Paragraph(NumberFormat.getInstance().format(doanhThu), fontMain));
                cellTblSP_doanhThu_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_doanhThu_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_doanhThu_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_doanhThu_giaTri);

                //Tỉ Lệ doanh THu
                PdfPCell cellTblSP_tiLeDoanhThu_giaTri = new PdfPCell(new Paragraph(sp.getHinhAnh(), fontMain));
                cellTblSP_tiLeDoanhThu_giaTri.setBorderColor(BaseColor.BLACK);
                cellTblSP_tiLeDoanhThu_giaTri.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTblSP_tiLeDoanhThu_giaTri.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableDsSP.addCell(cellTblSP_tiLeDoanhThu_giaTri);

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
        lbl_TuNgay = new javax.swing.JLabel();
        dch_TuNgay = new com.toedter.calendar.JDateChooser();
        pnl_TongSanPham = new javax.swing.JPanel();
        lbl_TongSanPhamBan = new javax.swing.JLabel();
        txt_TongSanPhamBan = new javax.swing.JTextField();
        cmb_PhanLoai = new javax.swing.JComboBox<>();
        cmb_MauSac = new javax.swing.JComboBox<>();
        cmb_KichThuoc = new javax.swing.JComboBox<>();
        pnl_TongDoanhThu = new javax.swing.JPanel();
        lbl_TongDoanhThu = new javax.swing.JLabel();
        txt_TongDoanhThu = new javax.swing.JTextField();
        lbl_DenNgay = new javax.swing.JLabel();
        dch_DenNgay = new com.toedter.calendar.JDateChooser();
        chk_TatCa = new javax.swing.JCheckBox();
        btn_Top5SPDTCN = new javax.swing.JButton();
        btn_Top5SPDTTN = new javax.swing.JButton();
        btn_LamMoi = new javax.swing.JButton();
        lbl_ThangLap = new javax.swing.JLabel();
        cmb_ThangLap = new javax.swing.JComboBox<>();
        lbl_NamLap = new javax.swing.JLabel();
        cmb_NamLap = new javax.swing.JComboBox<>();
        pnl_NutChucNang = new javax.swing.JPanel();
        btn_XuatThongKe = new javax.swing.JButton();
        btn_BieuDo = new javax.swing.JButton();

        setBackground(new java.awt.Color(199, 210, 213));
        setMinimumSize(new java.awt.Dimension(1000, 550));
        setPreferredSize(new java.awt.Dimension(1000, 550));

        scr_DanhSachSanPham.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        tbl_SanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbl_SanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Phân loại", "Giá bán ", "Giá nhập", "Kích cỡ", "Màu sắc", "Chất liệu", "Nhà cung cấp", "Số lượng đã bán", "Doanh thu", "Tỷ lệ doanh thu"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public synchronized Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbl_SanPham.setRowHeight(35);
        tbl_SanPham.setShowGrid(true);
        scr_DanhSachSanPham.setViewportView(tbl_SanPham);
        if (tbl_SanPham.getColumnModel().getColumnCount() > 0) {
            tbl_SanPham.getColumnModel().getColumn(11).setMinWidth(100);
        }

        lbl_TieuDe.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        lbl_TieuDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TieuDe.setText("THỐNG KÊ DOANH THU");
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

        lbl_PhanLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_PhanLoai.setText("Phân loại");

        lbl_KichThuoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_KichThuoc.setText("Kích thước");

        lbl_MauSac.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_MauSac.setText("Màu sắc");

        lbl_TuNgay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TuNgay.setText("Từ ngày:");

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

        pnl_TongSanPham.setBackground(new java.awt.Color(255, 255, 255));
        pnl_TongSanPham.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        lbl_TongSanPhamBan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TongSanPhamBan.setText("Tổng sản phẩm bán được");

        txt_TongSanPhamBan.setEditable(false);
        txt_TongSanPhamBan.setBackground(new java.awt.Color(255, 255, 255));
        txt_TongSanPhamBan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_TongSanPhamBan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_TongSanPhamBan.setText("0");
        txt_TongSanPhamBan.setBorder(null);

        javax.swing.GroupLayout pnl_TongSanPhamLayout = new javax.swing.GroupLayout(pnl_TongSanPham);
        pnl_TongSanPham.setLayout(pnl_TongSanPhamLayout);
        pnl_TongSanPhamLayout.setHorizontalGroup(
            pnl_TongSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_TongSanPhamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_TongSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_TongSanPhamLayout.createSequentialGroup()
                        .addComponent(lbl_TongSanPhamBan)
                        .addGap(0, 17, Short.MAX_VALUE))
                    .addComponent(txt_TongSanPhamBan, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        pnl_TongSanPhamLayout.setVerticalGroup(
            pnl_TongSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_TongSanPhamLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_TongSanPhamBan)
                .addGap(43, 43, 43)
                .addComponent(txt_TongSanPhamBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        cmb_PhanLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmb_PhanLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        cmb_PhanLoai.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    cmb_PhanLoaiItemStateChanged(evt);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
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
                } catch (ParseException e) {
                    throw new RuntimeException(e);
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
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        pnl_TongDoanhThu.setBackground(new java.awt.Color(255, 255, 255));
        pnl_TongDoanhThu.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        lbl_TongDoanhThu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_TongDoanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_TongDoanhThu.setText("Tổng doanh thu");

        txt_TongDoanhThu.setEditable(false);
        txt_TongDoanhThu.setBackground(new java.awt.Color(255, 255, 255));
        txt_TongDoanhThu.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt_TongDoanhThu.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_TongDoanhThu.setText("0");
        txt_TongDoanhThu.setBorder(null);
        txt_TongDoanhThu.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TongDoanhThuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_TongDoanhThuLayout = new javax.swing.GroupLayout(pnl_TongDoanhThu);
        pnl_TongDoanhThu.setLayout(pnl_TongDoanhThuLayout);
        pnl_TongDoanhThuLayout.setHorizontalGroup(
            pnl_TongDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_TongDoanhThuLayout.createSequentialGroup()
                .addGroup(pnl_TongDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_TongDoanhThuLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(txt_TongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_TongDoanhThuLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(lbl_TongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        pnl_TongDoanhThuLayout.setVerticalGroup(
            pnl_TongDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_TongDoanhThuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_TongDoanhThu)
                .addGap(41, 41, 41)
                .addComponent(txt_TongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

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
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        chk_TatCa.setBackground(new java.awt.Color(199, 210, 213));
        chk_TatCa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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

        btn_Top5SPDTCN.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_Top5SPDTCN.setText("Top 5 Sản phẩm có doanh thu cao nhất");
        btn_Top5SPDTCN.setBorder(null);
        btn_Top5SPDTCN.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_Top5SPDTCNActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btn_Top5SPDTTN.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_Top5SPDTTN.setText("Top 5 sản phẩm có doanh thu thấp nhất");
        btn_Top5SPDTTN.setBorder(null);
        btn_Top5SPDTTN.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_Top5SPDTTNActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btn_LamMoi.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btn_LamMoi.setText("Làm mới");
        btn_LamMoi.setBorder(null);
        btn_LamMoi.addActionListener(new java.awt.event.ActionListener() {
            public synchronized void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_LamMoiActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        lbl_ThangLap.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_ThangLap.setText("Tháng");

        cmb_ThangLap.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmb_ThangLap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmb_ThangLap.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    cmb_ThangLapItemStateChanged(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        lbl_NamLap.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_NamLap.setText("Năm");

        cmb_NamLap.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmb_NamLap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        cmb_NamLap.addItemListener(new java.awt.event.ItemListener() {
            public synchronized void itemStateChanged(java.awt.event.ItemEvent evt) {
                try {
                    cmb_NamLapItemStateChanged(evt);
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
                .addGap(100, 100, 100)
                .addComponent(pnl_TongSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(pnl_TongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_KichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(cmb_MauSac, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cmb_KichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cmb_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(48, 48, 48)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btn_Top5SPDTCN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_Top5SPDTTN, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                                    .addComponent(btn_LamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lbl_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lbl_TuNgay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lbl_DenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(dch_TuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dch_DenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(chk_TatCa)))
                            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_ThangLap, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmb_ThangLap, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_NamLap, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmb_NamLap, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_ThongTinLayout.setVerticalGroup(
            pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lbl_KichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(pnl_TongSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(cmb_KichThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btn_Top5SPDTCN, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(lbl_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                            .addComponent(lbl_ThangLap, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(cmb_ThangLap, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                            .addComponent(lbl_NamLap, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(cmb_NamLap, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(cmb_MauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btn_Top5SPDTTN, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbl_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmb_PhanLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btn_LamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_ThongTinLayout.createSequentialGroup()
                                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_TuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(dch_TuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(pnl_ThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_DenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(dch_DenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(3, 3, 3)
                                        .addComponent(chk_TatCa))))))
                    .addGroup(pnl_ThongTinLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(pnl_TongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
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

        javax.swing.GroupLayout pnl_NutChucNangLayout = new javax.swing.GroupLayout(pnl_NutChucNang);
        pnl_NutChucNang.setLayout(pnl_NutChucNangLayout);
        pnl_NutChucNangLayout.setHorizontalGroup(
            pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_NutChucNangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_BieuDo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_XuatThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                .addGap(60, 60, 60))
        );
        pnl_NutChucNangLayout.setVerticalGroup(
            pnl_NutChucNangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_NutChucNangLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(btn_XuatThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btn_BieuDo, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnl_DanhSachSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_ThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

    private synchronized void txt_TongDoanhThuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TongDoanhThuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TongDoanhThuActionPerformed

    private synchronized void btn_XuatThongKeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_XuatThongKeMouseEntered

        btn_XuatThongKe.setBackground(new Color(0x9EDDFF));
        btn_XuatThongKe.setForeground(new Color(0x141E46));
    }//GEN-LAST:event_btn_XuatThongKeMouseEntered

    private synchronized void btn_XuatThongKeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_XuatThongKeMouseExited

        btn_XuatThongKe.setBackground(UIManager.getColor("Menu.background"));
        btn_XuatThongKe.setForeground(UIManager.getColor("Menu.foreground"));
    }//GEN-LAST:event_btn_XuatThongKeMouseExited

    private synchronized void cmb_KichThuocItemStateChanged(java.awt.event.ItemEvent evt) throws ParseException, RemoteException {//GEN-FIRST:event_cmb_KichThuocItemStateChanged
        if (chk_TatCa.isSelected()) {
            loadDanhSachSanPham();

        } else if (!chk_TatCa.isSelected()) {
            loadDanhSachSanPhamTheoThoiGian();
        }
    }//GEN-LAST:event_cmb_KichThuocItemStateChanged

    private synchronized void cmb_MauSacItemStateChanged(java.awt.event.ItemEvent evt) throws ParseException, RemoteException {//GEN-FIRST:event_cmb_MauSacItemStateChanged
        if (chk_TatCa.isSelected()) {
            loadDanhSachSanPham();
        } else if (!chk_TatCa.isSelected()) {
            loadDanhSachSanPhamTheoThoiGian();
        }
    }//GEN-LAST:event_cmb_MauSacItemStateChanged

    private synchronized void cmb_PhanLoaiItemStateChanged(java.awt.event.ItemEvent evt) throws ParseException, RemoteException {//GEN-FIRST:event_cmb_PhanLoaiItemStateChanged
        if (chk_TatCa.isSelected()) {
            loadDanhSachSanPham();
        } else if (!chk_TatCa.isSelected()) {
            loadDanhSachSanPhamTheoThoiGian();
        }
    }//GEN-LAST:event_cmb_PhanLoaiItemStateChanged

    private synchronized void dch_TuNgayPropertyChange(java.beans.PropertyChangeEvent evt) throws ParseException, RemoteException {//GEN-FIRST:event_dch_TuNgayPropertyChange
        if (!dieuKienTuNgay()) {
            return;
        }
        if (chk_TatCa.isSelected()) {
            loadDanhSachSanPham();
        } else if (!chk_TatCa.isSelected()) {
            loadDanhSachSanPhamTheoThoiGian();
        }
        activeTatCa = false;
        activeTKTheoNgay = true;
        activeTKDTCao = false;
        activeTKDTThap = false;
    }//GEN-LAST:event_dch_TuNgayPropertyChange

    private synchronized void dch_DenNgayPropertyChange(java.beans.PropertyChangeEvent evt) throws ParseException, RemoteException {//GEN-FIRST:event_dch_DenNgayPropertyChange
        if (!dieuKienDenNgay()) {
            return;
        }
        if (chk_TatCa.isSelected()) {
            loadDanhSachSanPham();
        } else if (!chk_TatCa.isSelected()) {
            loadDanhSachSanPhamTheoThoiGian();
        }
        activeTatCa = false;
        activeTKTheoNgay = true;
        activeTKDTCao = false;
        activeTKDTThap = false;
    }//GEN-LAST:event_dch_DenNgayPropertyChange

    private synchronized void chk_TatCaItemStateChanged(java.awt.event.ItemEvent evt) throws ParseException, RemoteException {//GEN-FIRST:event_chk_TatCaItemStateChanged
        if (chk_TatCa.isSelected()) {
            loadLaiGiaTriBanDau();
            loadDanhSachSanPham();

        } else if (!chk_TatCa.isSelected()) {
            loadDanhSachSanPhamTheoThoiGian();
        }
        activeTatCa = true;
        activeTKTheoNgay = false;
        activeTKDTCao = false;
        activeTKDTThap = false;
    }//GEN-LAST:event_chk_TatCaItemStateChanged

    private synchronized void btn_XuatThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XuatThongKeActionPerformed
        try {
            ArrayList<SanPham> listSP = getGiaTriTrongBang();
            xuatBaoCaoThongKe(listSP);

        } catch (ParseException ex) {
            Logger.getLogger(ManHinh_NV_ThongKeDoanhThu.class.getName()).log(Level.SEVERE, null, ex);
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
            new ManHinh_NV_BieuDoThongKeDoanhThu().setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(ManHinh_NV_ThongKeSanPham.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }//GEN-LAST:event_btn_BieuDoActionPerformed

    private synchronized void btn_Top5SPDTTNActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_Top5SPDTTNActionPerformed
        loadLaiGiaTriBanDau();

        tblTop5SanPhamDoanhThuThapNhat();
        activeTatCa = false;
        activeTKTheoNgay = false;
        activeTKDTCao = false;
        activeTKDTThap = true;
    }//GEN-LAST:event_btn_Top5SPDTTNActionPerformed

    private synchronized void btn_Top5SPDTCNActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_Top5SPDTCNActionPerformed
        loadLaiGiaTriBanDau();

        tblTop5SanPhamDoanhThuCaoNhat();

        activeTatCa = false;
        activeTKTheoNgay = false;
        activeTKDTCao = true;
        activeTKDTThap = false;

    }//GEN-LAST:event_btn_Top5SPDTCNActionPerformed

    private synchronized void btn_LamMoiActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {//GEN-FIRST:event_btn_LamMoiActionPerformed
        loadLaiGiaTriBanDau();
        loadDanhSachSanPham();

    }//GEN-LAST:event_btn_LamMoiActionPerformed

    private synchronized void cmb_ThangLapItemStateChanged(java.awt.event.ItemEvent evt) throws RemoteException {//GEN-FIRST:event_cmb_ThangLapItemStateChanged
        cmb_KichThuoc.setSelectedIndex(0);
        cmb_MauSac.setSelectedIndex(0);
        cmb_PhanLoai.setSelectedIndex(0);
        dch_TuNgay.setDate(Calendar.getInstance().getTime());
        dch_DenNgay.setDate(Calendar.getInstance().getTime());
        chk_TatCa.setSelected(false);
        loadDanhSachSanPhamTheoThangNam();
    }//GEN-LAST:event_cmb_ThangLapItemStateChanged

    private synchronized void cmb_NamLapItemStateChanged(java.awt.event.ItemEvent evt) throws RemoteException {//GEN-FIRST:event_cmb_NamLapItemStateChanged
        cmb_KichThuoc.setSelectedIndex(0);
        cmb_MauSac.setSelectedIndex(0);
        cmb_PhanLoai.setSelectedIndex(0);
        dch_TuNgay.setDate(Calendar.getInstance().getTime());
        dch_DenNgay.setDate(Calendar.getInstance().getTime());
        chk_TatCa.setSelected(false);
        loadDanhSachSanPhamTheoThangNam();
    }//GEN-LAST:event_cmb_NamLapItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_BieuDo;
    private javax.swing.JButton btn_LamMoi;
    private javax.swing.JButton btn_Top5SPDTCN;
    private javax.swing.JButton btn_Top5SPDTTN;
    private javax.swing.JButton btn_XuatThongKe;
    private javax.swing.JCheckBox chk_TatCa;
    private javax.swing.JComboBox<String> cmb_KichThuoc;
    private javax.swing.JComboBox<String> cmb_MauSac;
    private javax.swing.JComboBox<String> cmb_NamLap;
    private javax.swing.JComboBox<String> cmb_PhanLoai;
    private javax.swing.JComboBox<String> cmb_ThangLap;
    private com.toedter.calendar.JDateChooser dch_DenNgay;
    private com.toedter.calendar.JDateChooser dch_TuNgay;
    private javax.swing.JLabel lbl_DenNgay;
    private javax.swing.JLabel lbl_KichThuoc;
    private javax.swing.JLabel lbl_MauSac;
    private javax.swing.JLabel lbl_NamLap;
    private javax.swing.JLabel lbl_PhanLoai;
    private javax.swing.JLabel lbl_ThangLap;
    private javax.swing.JLabel lbl_TieuDe;
    private javax.swing.JLabel lbl_TongDoanhThu;
    private javax.swing.JLabel lbl_TongSanPhamBan;
    private javax.swing.JLabel lbl_TuNgay;
    private javax.swing.JPanel pnl_DanhSachSanPham;
    private javax.swing.JPanel pnl_NV_TieuDe;
    private javax.swing.JPanel pnl_NutChucNang;
    private javax.swing.JPanel pnl_ThongTin;
    private javax.swing.JPanel pnl_TongDoanhThu;
    private javax.swing.JPanel pnl_TongSanPham;
    private javax.swing.JScrollPane scr_DanhSachSanPham;
    private javax.swing.JTable tbl_SanPham;
    private javax.swing.JTextField txt_TongDoanhThu;
    private javax.swing.JTextField txt_TongSanPhamBan;
    // End of variables declaration//GEN-END:variables
}
