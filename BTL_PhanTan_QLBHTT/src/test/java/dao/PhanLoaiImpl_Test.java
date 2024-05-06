package dao;/*
 * @ (#) PhanLoaiImpl_Test.java     1.0   4/13/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

import fit.iuh.dao.impl.Dao_PhanLoai;
import fit.iuh.entity.PhanLoai;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;

/*
 * @description:
 * @author: Le Tan Phat
 * @code: 21004661
 * @date: 4/13/2024
 * @version:  1.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhanLoaiImpl_Test {
    private Dao_PhanLoai phanLoaiImpl;

    @BeforeAll
    public void setUp() throws RemoteException {
        phanLoaiImpl = new Dao_PhanLoai();
    }

    @Test
    public void testGetAllPhanLoai() throws RemoteException {
        phanLoaiImpl.getAllPhanLoai().forEach(System.out::println);
    }

    @Test
    public void testgetAllPhanLoaiCuaPhuKien() throws RemoteException {
        phanLoaiImpl.getAllPhanLoaiCuaPhuKien().forEach(System.out::println);
    }

    @Test
    public void testgetDLPhanLoaiSPTheoMa() throws RemoteException {
        System.out.println(phanLoaiImpl.getDLPhanLoaiSPTheoMa(3));
    }

    @Test
    public void testgetPhanLoaiTheoTen() throws RemoteException {
        System.out.println(phanLoaiImpl.getPhanLoaiTheoTen("Nón"));
    }

    @Test
    public void testthemLoaiSanPham() throws RemoteException {
        phanLoaiImpl.themLoaiSanPham(new PhanLoai(6, "Ca vat"));
    }

    @Test
    public void testXoaPhanLoaiSanPham() throws RemoteException {
        phanLoaiImpl.xoaPhanLoaiSanPham(6);
    }

    @Test
    public void testCapNhatPhanLoaiSanPham() throws RemoteException {
        phanLoaiImpl.catNhatLoaiSanPham(new PhanLoai(5, "Ca vat"));
    }
}

