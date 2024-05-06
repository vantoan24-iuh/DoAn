/**
 * @ (#) IKichThuocDao.java      4/11/2024
 * <p>
 * Copyright (c) 2024 IUH. All rights reserved
 */

package fit.iuh.dao;

import fit.iuh.entity.KichThuoc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/*
 * @description:
 * @author: Sinh Phan Tien
 * @date: 4/11/2024
 */
public interface IKichThuocDao extends Remote {
    public ArrayList<KichThuoc> getAllKichThuoc() throws RemoteException;
    public KichThuoc getDLKichThuocTheoMa(long maKT) throws RemoteException;
    public KichThuoc getKichThuocTheoTen(String tenKichThuoc) throws RemoteException;
    public boolean themDLKichThuoc(KichThuoc kichThuoc) throws RemoteException;
    public boolean xoaDLKichThuoc(long maKichThuoc) throws RemoteException;
    public boolean capNhatDLKichThuoc(KichThuoc kichThuoc) throws RemoteException;
}
