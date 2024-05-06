/*
 * @ (#) IPhanLoaiDao.java     1.0   4/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */
package fit.iuh.dao;

import fit.iuh.entity.PhanLoai;

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
public interface IPhanLoaiDao extends Remote {
    public ArrayList<PhanLoai> getAllPhanLoai() throws RemoteException;
    public ArrayList<PhanLoai> getAllPhanLoaiCuaPhuKien() throws RemoteException;
    public PhanLoai getDLPhanLoaiSPTheoMa(long maPL) throws RemoteException;
    public PhanLoai getPhanLoaiTheoTen(String tenPhanLoai) throws RemoteException;
    public void themLoaiSanPham(PhanLoai phanLoai) throws RemoteException;
    public void xoaPhanLoaiSanPham(long maPhanLoai) throws RemoteException;
    public void catNhatLoaiSanPham(PhanLoai phanLoai) throws RemoteException;

}
