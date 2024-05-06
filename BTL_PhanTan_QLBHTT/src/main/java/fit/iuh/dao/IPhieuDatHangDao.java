/**
 * @ (#) IPhieuDatHangDao.java      4/10/2024
 * <p>
 * Copyright (c) 2024 IUH. All rights reserved
 */

package fit.iuh.dao;

import fit.iuh.entity.PhieuDatHang;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/*
 * @description:
 * @author: Sinh Phan Tien
 * @date: 4/10/2024
 */
public interface IPhieuDatHangDao extends Remote {
    public List<PhieuDatHang> getAllPhieuDatHang() throws RemoteException;
    public boolean themPhieuDatHang(PhieuDatHang pdt) throws RemoteException;
    public boolean xoaPhieuDatHang(long maPhieuDatHang) throws RemoteException;
    public PhieuDatHang getPDTTheoMa(long maPDT) throws RemoteException;
    public PhieuDatHang getPDTTheoMaKH(long maKhachHang) throws RemoteException;

}
