package dao;

import fit.iuh.dao.impl.*;
import fit.iuh.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestHoaDon {
    EntityManager em = Persistence.createEntityManagerFactory("JPADemo_SQL").createEntityManager();
    EntityTransaction tx = em.getTransaction();
    private Dao_HoaDon dao_hoaDon;
    private Dao_PhanLoai dao_phanLoai;
    private Dao_MauSac dao_mauSac;
    private Dao_KichThuoc dao_kichThuoc;

    @BeforeAll
    public void init() throws RemoteException {
        dao_hoaDon = new Dao_HoaDon();
        dao_mauSac = new Dao_MauSac();
        dao_kichThuoc = new Dao_KichThuoc();
        dao_phanLoai = new Dao_PhanLoai();
    }

    @Test
    public void testGetAllHoaDon() throws RemoteException{
        ArrayList<HoaDon> dsHoaDon = dao_hoaDon.getAllHoaDon();
        for (HoaDon hd : dsHoaDon) {
            System.out.println(hd);
        }
    }

    @Test
    public void testThemHoaDon() throws ParseException, RemoteException {
        NhanVien nv = em.find(NhanVien.class,1);
        KhachHang kh = em.find(KhachHang.class,1);
        Date date = new Date();
        HoaDon hd = new HoaDon(kh, nv, date);
        System.out.println(dao_hoaDon.themHoaDon(hd));
    }
    @Test
    public void testTongTienHoaDon() throws RemoteException{
        Double tongTien = dao_hoaDon.tongTienHoaDon(1);
        System.out.println(tongTien);
    }

    @Test
    public void testGetHoaDonTheoMa() throws RemoteException{
        HoaDon hd = dao_hoaDon.getHoaDonTheoMa(1);
        System.out.println(hd);
    }

    @Test
    public void testThongKeTop5SPDTCN() throws RemoteException{
        ArrayList<SanPham> dsSanPham = dao_hoaDon.thongKeTop5SPDTCN();
        dsSanPham.forEach(System.out::println);
    }
    @Test
    public void testThongKeDanhSachSanPhamVoiSoLuongBanDuocByTieuChi() throws RemoteException{
        String mauSac = "";
        String phanLoai = "";
        String kichThuoc = "XL";
        ArrayList<SanPham> dsSanPham = dao_hoaDon.thongKeDanhSachSanPhamVoiSoLuongBanDuocByTieuChi(mauSac, phanLoai, kichThuoc);
        for (SanPham sp : dsSanPham) {
            System.out.println(sp);
        }
    }

    @Test
    public void testThongKeHdTheoThangNam() throws RemoteException {
        String namLap = "2023";
        Map<Integer, Double> dsHoaDon = dao_hoaDon.thongKeDoanhThuTheoThangCuaNam(namLap);
        dsHoaDon.entrySet().forEach(System.out::println);
    }

    @Test
    public void testThongKeDanhSachSanPhamVoiSoLuongBanDuocByTieuChiByTime() throws ParseException, RemoteException {
        String mauSac = "";
        String phanLoai = "";
        String kichThuoc = "XL";

        String tuNgay = "2023-10-19";
        String denNgay = "2023-10-31";

        ArrayList<SanPham> dsSanPham = dao_hoaDon.thongKeDanhSachSanPhamVoiSoLuongBanDuocByTieuChiByTime(mauSac, phanLoai, kichThuoc, tuNgay, denNgay);
        for (SanPham sp : dsSanPham) {
            System.out.println(sp);
        }
    }

    @Test
    public void testThongKeDanhSachSanPhamTheoThangNam() throws RemoteException{
        String thangLap = "10";
        String namLap = "2023";
        ArrayList<SanPham> dsSanPham = dao_hoaDon.thongKeDanhSachSanPhamTheoThangNam(thangLap, namLap);
        for (SanPham sp : dsSanPham) {
            System.out.println(sp);
        }
    }
    @Test
    public void testGetAllHoaDonTheoNgay() throws ParseException, RemoteException {
        String tuNgay = "2023-10-19";
        String denNgay = "2023-10-31";
        ArrayList<HoaDon> dsHoaDon = dao_hoaDon.getAllHoaDonTheoNgay(tuNgay, denNgay);
        for (HoaDon hd : dsHoaDon) {
            System.out.println(hd);
        }
    }

    @Test
    public void testThongKeThongTinKhachHangDaMuaHang() throws RemoteException{
        ArrayList<KhachHang> list = dao_hoaDon.thongKeThongTinKhachHangDaMuaHang();
        for (KhachHang kh : list) {
            System.out.println(kh);
        }
    }

    @Test
    public void testGetThanhTienKhachHangMua()throws RemoteException {
        double thanhTien = dao_hoaDon.getThanhTienKhachHangMua(1);
        System.out.println(thanhTien);
    }

    @Test
    public void testGetSoLuongKhachHangMua() throws RemoteException{
        int soLuong = dao_hoaDon.getSoLuongKhachHangMua(1);
        System.out.println(soLuong);
    }
    @Test
    public void testGetSoLuongKhachHang() throws RemoteException{
        int soLuong = dao_hoaDon.getSoLuongKhachHang();
        System.out.println(soLuong);
    }

    @Test
    public void testGetSoLuongHoaDonKhachHangMua()throws RemoteException {
        int soLuong = dao_hoaDon.getSoLuongHoaDonKhachHangMua(1);
        System.out.println(soLuong);
    }

    @Test
    public void testthongKeThongTinTop5KhachHangDTCaoNhat() throws RemoteException{
        ArrayList<KhachHang> list = dao_hoaDon.thongKeThongTinTop5KhachHangDTCaoNhat();
        list.forEach(System.out::println);
    }

    @Test
    public void testthongKeThongTinTop5KhachHangThuongXuyenMuaHang()throws RemoteException {
        ArrayList<KhachHang> list = dao_hoaDon.thongKeThongTinTop5KhachHangThuongXuyenMuaHang();
        list.forEach(System.out::println);
    }

    @Test
    public void testthongKeThongTinTop5KhachHangSLNhieuNhat() throws RemoteException{
        ArrayList<KhachHang> list = dao_hoaDon.thongKeThongTinTop5KhachHangSLNhieuNhat();
        list.forEach(System.out::println);
    }

    @Test
    public void testthongKeTop5SPDTTN() throws RemoteException{
        ArrayList<SanPham> dsSanPham = dao_hoaDon.thongKeTop5SPDTTN();
        dsSanPham.forEach(System.out::println);
    }


}
