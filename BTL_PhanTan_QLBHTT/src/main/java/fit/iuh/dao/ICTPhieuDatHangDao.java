/**
 * @ (#) ICTPhieuDatHangDao.java      4/11/2024
 * <p>
 * Copyright (c) 2024 IUH. All rights reserved
 */

package fit.iuh.dao;

import fit.iuh.entity.CTPhieuDatHang;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/*
 * @description:
 * @author: Sinh Phan Tien
 * @date: 4/11/2024
 */
public interface ICTPhieuDatHangDao extends Remote {
    public ArrayList<CTPhieuDatHang> getAllCTPhieuDatHang(long maPhieuDatHang) throws RemoteException;
    public boolean themCTPDT(CTPhieuDatHang ctpdt) throws RemoteException;
    public boolean xoaCTPhieuDatHang(long maPhieuDatHang) throws RemoteException;
}
