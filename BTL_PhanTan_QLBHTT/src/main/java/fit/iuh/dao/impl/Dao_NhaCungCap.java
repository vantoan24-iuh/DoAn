package fit.iuh.dao.impl;

import fit.iuh.dao.INhaCungCapDao;
import fit.iuh.entity.NhaCungCap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Dao_NhaCungCap extends UnicastRemoteObject implements INhaCungCapDao {
    private EntityManager em;

    public Dao_NhaCungCap() throws RemoteException {
        super();
        em = Persistence.createEntityManagerFactory("JPADemo_SQL")
                .createEntityManager();
    }

    public Dao_NhaCungCap(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public ArrayList<NhaCungCap> getAllNhaCungCap() throws RemoteException{
        return (ArrayList<NhaCungCap>) em.createNamedQuery("NhaCungCap.getAll", NhaCungCap.class)
                .getResultList();
    }

    @Override
    public boolean themNhaCungCap(NhaCungCap nhaCungCap) throws RemoteException{
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(nhaCungCap);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean xoaNhaCungCap(Long maNCC) throws RemoteException{
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            NhaCungCap nhaCungCap = em.find(NhaCungCap.class, maNCC);
            em.remove(nhaCungCap);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhatNhaCungCap(NhaCungCap ncc) throws RemoteException{
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(ncc);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public List<NhaCungCap> timKiemNhaCungCap(Long maNCC, String tenNCC, String sdt, String email) throws RemoteException{
        return em.createNamedQuery("NhaCungCap.findNhaCungCap", NhaCungCap.class)
                .setParameter("maNVCheck", maNCC == 0 ? "" : String.valueOf(maNCC))
                .setParameter("maNVParam", maNCC)
                .setParameter("tenNCC", "%" + tenNCC+ "%")
                .setParameter("sdt", "%" + sdt+ "%")
                .setParameter("email", "%" +  email+ "%")
                .getResultList();


    }

    @Override
    public NhaCungCap getNhaCungCapTheoTen(String tenNCC) throws RemoteException{
        return em.createNamedQuery("NhaCungCap.findTenNhaCungCap", NhaCungCap.class)
                .setParameter("tenNCC", tenNCC)
                .getSingleResult();

    }

    @Override
    public NhaCungCap getNhaCungCapTheoMa(Long maNCC) throws RemoteException{
        return em.createNamedQuery("NhaCungCap.findNhaCungCapTheoMa", NhaCungCap.class)
                .setParameter("maNCC", String.valueOf(maNCC))
                .getSingleResult();
    }


}
