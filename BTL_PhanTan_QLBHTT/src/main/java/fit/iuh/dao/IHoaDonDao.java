package fit.iuh.dao;

import fit.iuh.entity.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public interface IHoaDonDao extends Remote {

    public ArrayList<HoaDon> getAllHoaDon() throws RemoteException;
    public Boolean themHoaDon(HoaDon hd) throws RemoteException;
    public double tongTienHoaDon(long maHD) throws RemoteException;
    public HoaDon getHoaDonTheoMa(long maHD) throws RemoteException;
    public ArrayList<SanPham> thongKeDanhSachSanPhamVoiSoLuongBanDuocByTieuChi(String mauSac, String phanLoai, String kichThuoc) throws RemoteException;
    public ArrayList<SanPham> thongKeTop5SPDTCN() throws RemoteException;
    public ArrayList<SanPham> thongKeTop5SPDTTN() throws RemoteException;
    public ArrayList<HoaDon> getAllHoaDonTheoNgay(String tuNgay, String denNgay) throws ParseException, RemoteException;
    public  ArrayList<SanPham> thongKeDanhSachSanPhamVoiSoLuongBanDuocByTieuChiByTime(String mauSac, String phanLoai, String kichThuoc, String tuNgay, String denNgay) throws ParseException, RemoteException;
    public  ArrayList<SanPham> thongKeDanhSachSanPhamTheoThangNam(String thangLap, String namLap) throws RemoteException;
    public ArrayList<KhachHang> thongKeThongTinKhachHangDaMuaHang() throws RemoteException;
    public double getThanhTienKhachHangMua(long maKH) throws RemoteException;
    public int getSoLuongKhachHangMua(long maKH) throws RemoteException;
    public int getSoLuongKhachHang() throws RemoteException;
    public ArrayList<KhachHang> thongKeThongTinTop5KhachHangDTCaoNhat() throws RemoteException;
    public ArrayList<KhachHang> thongKeThongTinTop5KhachHangThuongXuyenMuaHang() throws RemoteException;
    public ArrayList<KhachHang> thongKeThongTinTop5KhachHangSLNhieuNhat() throws RemoteException;
    public int getSoLuongHoaDonKhachHangMua(long maKH) throws RemoteException;

    public HoaDon getHoaDon() throws RemoteException;

    public Map<Integer, Double> thongKeDoanhThuTheoThangCuaNam(String nam) throws RemoteException;
}
