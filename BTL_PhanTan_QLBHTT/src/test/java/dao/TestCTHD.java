package dao;

import fit.iuh.dao.impl.Dao_CTHD;
import fit.iuh.dao.impl.Dao_HoaDon;
import fit.iuh.entity.CTHD;
import fit.iuh.entity.HoaDon;
import fit.iuh.entity.SanPham;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestCTHD {
    private Dao_CTHD dao_cthd;
    private Dao_HoaDon dao_hoaDon;
    EntityManager em = Persistence.createEntityManagerFactory("JPADemo_SQL").createEntityManager();
    EntityTransaction tx = em.getTransaction();

    @BeforeAll
    public void init() throws RemoteException {
        dao_cthd = new Dao_CTHD();
        dao_hoaDon = new Dao_HoaDon();
    }
    @Test
    public void testGetAllCTHD() throws RemoteException {
        ArrayList<CTHD> listCTHD = dao_cthd.getAllCTHD(1);
        for (CTHD cthd : listCTHD){
            System.out.println(cthd.getSanPham().getTenSP());
        }
    }
    @Test
    public void testThemCTHD() throws RemoteException {
        SanPham sp = em.find(SanPham.class, 1);
        HoaDon hd = dao_hoaDon.getHoaDonTheoMa(1);
        CTHD cthd = new CTHD(sp, hd, 2);
        Boolean result = dao_cthd.themCTHD(cthd);
        System.out.println(result);
    }
    @Test
    public void testTinhThanhTienSanPham() throws RemoteException {
        double thanhTien = dao_cthd.tinhThanhTienSanPham(1, 1);
        System.out.println(thanhTien);
    }
    @Test
    public void testGetTongDoanhThu() throws RemoteException {
        double doanhThu = dao_cthd.getTongDoanhThu(1, "10", "2023");
        System.out.println(doanhThu);
    }
    @Test
    public void testGetSoLuongSanPhamBanDuoc() throws RemoteException {
        int soLuong = dao_cthd.getSoLuongSanPhamBanDuoc(1, "10", "2023");
        System.out.println(soLuong);
    }
    @Test
    public void testGetDoanhThuSanPhamBanDuoc() throws RemoteException {
        double doanhThu = dao_cthd.getDoanhThuSanPhamBanDuoc(1, "10", "2023");
        System.out.println(doanhThu);
    }

}
