package dao;/*
 * @ (#) KhachHangImpl_Test.java     1.0   4/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

import fit.iuh.dao.impl.Dao_KhachHang;
import fit.iuh.entity.KhachHang;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;
import java.util.ArrayList;

/*
 * @description:
 * @author: Le Tan Phat
 * @code: 21004661
 * @date: 4/13/2024
 * @version:  1.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KhachHangImpl_Test {
    private Dao_KhachHang khachHangDao;

    @BeforeAll
    public void setUp() throws RemoteException {
        khachHangDao = new Dao_KhachHang();
    }

    @Test
    public void testAddKhachHang() throws RemoteException {
        KhachHang khachHang = new KhachHang("Le Tan Phat", "0123453", "phat@gmail.com", "Nam");
        khachHangDao.themKhachHang(khachHang);
    }

    @Test
    public void testGetAllKhachHang() throws RemoteException {
        khachHangDao.getAllKhachHang().forEach(System.out::println);
    }

    @Test
    public void testUpdateKhachHang() throws RemoteException {
        KhachHang khachHang = khachHangDao.getKhachHangTheoMa(11);
        khachHang.setEmail("hihi@gmail.com");
//        KhachHang khachHang = new KhachHang("Le Tan Phat", "0123453", "hihi@gmail.com", "Nam");
        khachHangDao.capNhatKhachHang(khachHang);
    }

    @Test
    public void testTimKiemKhachHang() throws RemoteException {
        ArrayList<KhachHang> khachHangs = khachHangDao.timKiemKhachHang(11, "Le", "0123453", "hihi");
        if (khachHangs == null || khachHangs.isEmpty() ){
            System.out.println("Không tìm thấy khách hàng");
            return;
        }
        khachHangs.forEach(System.out::println);
    }

    @Test
    public void testGetKhachHangByID() throws RemoteException {
        KhachHang khachHang = khachHangDao.getKhachHangTheoMa(11);
        System.out.println(khachHang);
    }

    @Test
    public void testKiemTraTonTai() throws RemoteException {
        boolean check = khachHangDao.kiemTraKhachHangDaTonTai("a@gmail.com", "0123453");
        System.out.println(check);
    }
}
