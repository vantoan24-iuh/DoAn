package testDao;

import fit.iuh.dao.impl.Dao_NhaCungCap;
import fit.iuh.dao.impl.Dao_TaiKhoan;
import fit.iuh.entity.NhaCungCap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class testNhaCungCap {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPADemo_SQL");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    private Dao_NhaCungCap nhaCungCapDao;

    @BeforeAll
    public void setUp()throws RemoteException {
        nhaCungCapDao = new Dao_NhaCungCap(em);
    }

    @Test
    public void testGetAllNhaCungCap()throws RemoteException {
        System.out.println(nhaCungCapDao.getAllNhaCungCap());
    }

    @Test
    public void addNhaCungCap() throws RemoteException{
        NhaCungCap nhaCungCap = new fit.iuh.entity.NhaCungCap();
        nhaCungCap.setDiaChi("Dia chi 2");
        nhaCungCap.setSdt("0123456789");
        nhaCungCap.setEmail("john@gamil.com");
        nhaCungCap.setTenNCC("HCM");

        boolean checked = nhaCungCapDao.themNhaCungCap(nhaCungCap);
    }

    @Test
    public void deleteNhaCungCap()throws RemoteException {
        boolean checked = nhaCungCapDao.xoaNhaCungCap(8L);
    }

    @Test
    public void testUpDate() throws RemoteException{
        NhaCungCap nhaCungCap = new NhaCungCap();
        nhaCungCap.setDiaChi("Dia chi 3");
        nhaCungCap.setSdt("0123456789");
        nhaCungCap.setEmail("111@gmail.com");
        nhaCungCap.setTenNCC("HCM");
        boolean checked = nhaCungCapDao.capNhatNhaCungCap(nhaCungCap);
    }

    @Test
    public void testFindById()throws RemoteException {
        System.out.println(nhaCungCapDao.timKiemNhaCungCap(7L,null,null,null));
    }

    @Test
    public void testFindByName()throws RemoteException {
        System.out.println(nhaCungCapDao.getNhaCungCapTheoTen("HCM"));
    }

    @Test
    public void testFindByMa()throws RemoteException {
        System.out.println(nhaCungCapDao.getNhaCungCapTheoMa(7L));
    }

    @AfterAll
    public void tearDown() {
        em.close();
        emf.close();
    }

}
