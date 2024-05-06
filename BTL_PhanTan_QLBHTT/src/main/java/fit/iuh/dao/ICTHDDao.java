package fit.iuh.dao;

import fit.iuh.entity.CTHD;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

public interface ICTHDDao extends Remote {
    public ArrayList<CTHD> getAllCTHD(long maHD) throws RemoteException;
    public  Boolean themCTHD(CTHD cthd) throws RemoteException;
    public  double tinhThanhTienSanPham(long maHD, long maSP) throws RemoteException;
    public  double getTongDoanhThu(long maSP, String thangLap, String namLap) throws RemoteException;
    public  int getSoLuongSanPhamBanDuoc(long maSP, String thangLap, String namLap) throws RemoteException;
    public  double getDoanhThuSanPhamBanDuoc(long maSP, String thangLap, String namLap) throws RemoteException;

}
