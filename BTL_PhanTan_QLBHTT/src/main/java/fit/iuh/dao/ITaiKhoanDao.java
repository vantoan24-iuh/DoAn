package fit.iuh.dao;


import fit.iuh.entity.TaiKhoan;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ITaiKhoanDao extends Remote {

    public ArrayList<TaiKhoan> getAllTaiKhoan() throws RemoteException;

    public ArrayList<TaiKhoan> getAllTaiKhoanConHoatDong() throws RemoteException;


    public boolean themTaiKhoan(TaiKhoan taiKhoan) throws RemoteException;

    public boolean xoaTaiKhoan(Long maNv) throws RemoteException;

    public boolean capNhatTaiKhoan(Long maNv) throws RemoteException;

    public void doiMatKhauTaiKhoan(TaiKhoan taiKhoan) throws RemoteException;

    public TaiKhoan dangNhapTaiKhoan(String tenTaiKhoan, String matKhau) throws RemoteException;


    public String getMatKhau(String tenTK) throws RemoteException;

    public TaiKhoan getTaiKhoanNV(Long maNV) throws RemoteException;

    public boolean datLaiMatKhau(TaiKhoan taiKhoan, String matKhau) throws RemoteException;














}
