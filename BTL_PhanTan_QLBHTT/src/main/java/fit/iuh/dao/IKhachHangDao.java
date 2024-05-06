/*
 * @ (#) IKhachHangDao.java     1.0   4/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */
package fit.iuh.dao;

import fit.iuh.entity.KhachHang;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/*
 * @description:
 * @author: Le Tan Phat
 * @code: 21004661
 * @date: 4/13/2024
 * @version:  1.0
 */
public interface IKhachHangDao extends Remote {
    public ArrayList<KhachHang> getAllKhachHang() throws RemoteException;
    public void themKhachHang(KhachHang kh) throws RemoteException;
    public void capNhatKhachHang(KhachHang kh) throws RemoteException;
    public ArrayList<KhachHang> timKiemKhachHang(long maKhachHang,String tenKhachHang,String soDienThoai, String email) throws RemoteException;
    public KhachHang getKhachHangTheoMa(long maKH) throws RemoteException;
    public boolean kiemTraKhachHangDaTonTai(String email, String sdt) throws RemoteException;
}
