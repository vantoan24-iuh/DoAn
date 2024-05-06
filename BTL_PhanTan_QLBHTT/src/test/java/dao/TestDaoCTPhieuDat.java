/**
 * @ (#) TestDaoCTPhieuDat.java      4/11/2024
 * <p>
 * Copyright (c) 2024 IUH. All rights reserved
 */

package dao;

import fit.iuh.dao.impl.Dao_CTPhieuDatHang;
import fit.iuh.dao.impl.Dao_PhieuDatHang;
import fit.iuh.entity.*;
import jakarta.persistence.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;

/*
 * @description:
 * @author: Sinh Phan Tien
 * @date: 4/11/2024
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestDaoCTPhieuDat {
    private Dao_CTPhieuDatHang daoCTPhieuDat;
    private Dao_PhieuDatHang daoPhieuDatHang;
    EntityManager em = Persistence.createEntityManagerFactory("JPADemo_SQL").createEntityManager();
    EntityTransaction tx = em.getTransaction();

    @BeforeAll
    public void init() throws RemoteException {
        daoCTPhieuDat = new Dao_CTPhieuDatHang();
        daoPhieuDatHang = new Dao_PhieuDatHang();
    }

    @Test
    public void testGetAllCTPhieuDatHang()throws RemoteException {
        daoCTPhieuDat.getAllCTPhieuDatHang(1).forEach(System.out::println);
    }

    @Test
    public void testThemCTPDT() throws RemoteException{
//        PhieuDatHang pdt = daoPhieuDatHang.getPDTTheoMa(2);
        KhachHang kh = em.find(KhachHang.class, 12);
        NhanVien nv = em.find(NhanVien.class, 1);
        PhieuDatHang pdt = new PhieuDatHang(kh, nv, java.sql.Date.valueOf("2024-04-11"));
        pdt.setMaPhieuDat(daoPhieuDatHang.getPDTTheoMaKH(kh.getMaKH()).getMaPhieuDat());
        System.out.println(pdt);
        SanPham sp = em.find(SanPham.class, 6);
        CTPhieuDatHang ctpdt = new CTPhieuDatHang(sp, pdt, 2);
        System.out.println(ctpdt);
        boolean check = daoCTPhieuDat.themCTPDT(ctpdt);
        Assertions.assertTrue(check);
    }

    @Test
    public void testXoaCTPhieuDatHang() throws RemoteException{
        boolean check = daoCTPhieuDat.xoaCTPhieuDatHang(2);
        Assertions.assertTrue(check);
    }
}
