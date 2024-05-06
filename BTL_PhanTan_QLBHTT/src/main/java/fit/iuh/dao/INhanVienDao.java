/*
 * @ (#) INhanVienDao.java     1.0   4/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */
package fit.iuh.dao;

import fit.iuh.entity.NhanVien;

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
public interface INhanVienDao extends Remote {
    public ArrayList<NhanVien> getAllNhanVien() throws RemoteException;
    public void themNhanVien(NhanVien nv) throws RemoteException;
    public void xoaNhanVien(long manv) throws RemoteException;
    public void capNhatNhanVien(NhanVien nv) throws RemoteException;
    public ArrayList<NhanVien> timKiemNhanVien(long maNV, String tenNV, String sdt, String email, String chucVu, String diaChi, boolean trangThai) throws RemoteException;
    public NhanVien getNhanVienTheoTen(String tenNV) throws RemoteException;
    public NhanVien getNhanVienTheoMa(long maNV) throws RemoteException;
    public ArrayList<NhanVien> getAllNhanVienConHoaDong() throws RemoteException;
    public NhanVien getNhanVienTheoEmail(String email) throws RemoteException;
}
