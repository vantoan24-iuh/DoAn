package fit.iuh.dao;


import fit.iuh.entity.SanPham;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ISanPhamDao extends Remote {

    public ArrayList<SanPham> getAllSanPham() throws RemoteException;
    public ArrayList<SanPham> getAllQuanAo() throws RemoteException;
    public ArrayList<SanPham> getAllPhuKien() throws RemoteException;

    public boolean themSanPham(SanPham sanPham) throws RemoteException;
    public boolean xoaSanPham(long masp) throws RemoteException;
    public boolean capNhatSanPham(SanPham sanPham) throws RemoteException;

    public ArrayList<SanPham> timKiemQuanAo(long maSP, String tenSP, String tenPhanLoai, String tenNCC, String tenMauSac, String tenChatLieu, String tenKichThuoc) throws RemoteException;
    public ArrayList<SanPham> timKiemPhuKien(long maSP, String tenSP, String tenPhanLoai, String tenNCC, String tenMauSac, String tenChatLieu, String tenKichThuoc) throws RemoteException;

    public SanPham getSanPhamTheoMa(long maSP) throws RemoteException;

    public ArrayList<SanPham> getAllSanPhamTheoTieuChi(String maPhanLoai, String maMauSac, String maKichThuoc) throws RemoteException;
    public ArrayList<SanPham> getAllSanPhamHetHang(String maPhanLoai, String maMauSac, String maKichThuoc) throws RemoteException;

    public ArrayList<SanPham>  getSanPhamBanChay() throws RemoteException;
    public ArrayList<SanPham>  getSanPhamBanCham() throws RemoteException;

    public ArrayList<SanPham>  getSoLuongSPTheoMaPL() throws RemoteException;
    public  ArrayList<SanPham> getAllSanPhamTheoNgay(String tuNgay, String denNgay) throws RemoteException;

    public void giamSoLuongSanPham(SanPham sp) throws RemoteException;
    public void tangSoLuongSanPham(long maSP, int soLuong) throws RemoteException;
}
