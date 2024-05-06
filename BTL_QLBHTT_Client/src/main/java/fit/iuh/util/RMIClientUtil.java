/**
 * @ (#) RMIClientUtil.java      4/19/2024
 * <p>
 * Copyright (c) 2024 IUH. All rights reserved
 */

package fit.iuh.util;

import fit.iuh.dao.*;
import lombok.Getter;

import java.rmi.RemoteException;

/*
 * @description:
 * @author: Sinh Phan Tien
 * @date: 4/19/2024
 */
public class RMIClientUtil {
    public static final String URL = "rmi://localhost:1243/";
    private static IChatLieuDao chatLieuDao;
    private static IKichThuocDao kichThuocDao;
    private static ISanPhamDao sanPhamDao;
    private static IPhieuDatHangDao phieuDatHangDao;
    private static ICTPhieuDatHangDao ctPhieuDatHangDao;
    private static IHoaDonDao hoaDonDao;
    private static ICTHDDao ctHoaDonDao;
    private static IKhachHangDao khachHangDao;
    private static INhanVienDao nhanVienDao;
    private static IMauSacDao mauSacDao;
    private static INhaCungCapDao nhaCungCapDao;
    private static IPhanLoaiDao phanLoaiDao;
    private static ITaiKhoanDao taiKhoanDao;
    private static ISendMail sendMailDao;

    public static void connectToServer() {
        try {
            chatLieuDao = (IChatLieuDao) java.rmi.Naming.lookup(URL + "chatLieuDao");
            kichThuocDao = (IKichThuocDao) java.rmi.Naming.lookup(URL + "kichThuocDao");
            sanPhamDao = (ISanPhamDao) java.rmi.Naming.lookup(URL + "sanPhamDao");
            phieuDatHangDao = (IPhieuDatHangDao) java.rmi.Naming.lookup(URL + "phieuDatHangDao");
            ctPhieuDatHangDao = (ICTPhieuDatHangDao) java.rmi.Naming.lookup(URL + "ctPhieuDatHangDao");
            hoaDonDao = (IHoaDonDao) java.rmi.Naming.lookup(URL + "hoaDonDao");
            ctHoaDonDao = (ICTHDDao) java.rmi.Naming.lookup(URL + "ctHoaDonDao");
            khachHangDao = (IKhachHangDao) java.rmi.Naming.lookup(URL + "khachHangDao");
            nhanVienDao = (INhanVienDao) java.rmi.Naming.lookup(URL + "nhanVienDao");
            mauSacDao = (IMauSacDao) java.rmi.Naming.lookup(URL + "mauSacDao");
            nhaCungCapDao = (INhaCungCapDao) java.rmi.Naming.lookup(URL + "nhaCungCapDao");
            phanLoaiDao = (IPhanLoaiDao) java.rmi.Naming.lookup(URL + "phanLoaiDao");
            taiKhoanDao = (ITaiKhoanDao) java.rmi.Naming.lookup(URL + "taikhoanDao");
            sendMailDao = (ISendMail) java.rmi.Naming.lookup(URL + "sendMailDao");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws RemoteException {
//        IHoaDonDao daoHoaDon = getHoaDonDao();
//        daoHoaDon.getAllHoaDon().forEach(System.out::println);
//        ISanPhamDao daoSanPham = getSanPhamDao();
//        daoSanPham.getAllSanPham().forEach(System.out::println);
        INhanVienDao daoNhanVien = getNhanVienDao();
        daoNhanVien.getAllNhanVien().forEach(System.out::println);
    }

    public static IChatLieuDao getChatLieuDao() {
        if(chatLieuDao == null) {
            connectToServer();
        }
        return chatLieuDao;
    }

    public static IKichThuocDao getKichThuocDao() {
        if(kichThuocDao == null) {
            connectToServer();
        }
        return kichThuocDao;
    }

    public static ISanPhamDao getSanPhamDao() {
        if(sanPhamDao == null) {
            connectToServer();
        }
        return sanPhamDao;
    }

    public static IPhieuDatHangDao getPhieuDatHangDao() {
        if(phieuDatHangDao == null) {
            connectToServer();
        }
        return phieuDatHangDao;
    }

    public static ICTPhieuDatHangDao getCtPhieuDatHangDao() {
        if(ctPhieuDatHangDao == null) {
            connectToServer();
        }
        return ctPhieuDatHangDao;
    }

    public static IHoaDonDao getHoaDonDao() {
        if(hoaDonDao == null) {
            connectToServer();
        }
        return hoaDonDao;
    }

    public static ICTHDDao getCtHoaDonDao() {
        if(ctHoaDonDao == null) {
            connectToServer();
        }
        return ctHoaDonDao;
    }

    public static IKhachHangDao getKhachHangDao() {
        if(khachHangDao == null) {
            connectToServer();
        }
        return khachHangDao;
    }

    public static INhanVienDao getNhanVienDao() {
        if(nhanVienDao == null) {
            connectToServer();
        }
        return nhanVienDao;
    }

    public static IMauSacDao getMauSacDao() {
        if(mauSacDao == null) {
            connectToServer();
        }
        return mauSacDao;
    }

    public static INhaCungCapDao getNhaCungCapDao() {
        if(nhaCungCapDao == null) {
            connectToServer();
        }
        return nhaCungCapDao;
    }

    public static IPhanLoaiDao getPhanLoaiDao() {
        if(phanLoaiDao == null) {
            connectToServer();
        }
        return phanLoaiDao;
    }

    public static ITaiKhoanDao getTaiKhoanDao() {
        if(taiKhoanDao == null) {
            connectToServer();
        }
        return taiKhoanDao;
    }

    public static ISendMail getSendMailDao() {
        if(sendMailDao == null) {
            connectToServer();
        }
        return sendMailDao;
    }

}
