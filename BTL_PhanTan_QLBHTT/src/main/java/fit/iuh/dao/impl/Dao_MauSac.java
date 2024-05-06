/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fit.iuh.dao.impl;

import fit.iuh.dao.IMauSacDao;
import fit.iuh.entity.MauSac;
import fit.iuh.entity.SanPham;
import jakarta.persistence.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Dao_MauSac extends UnicastRemoteObject implements IMauSacDao {

    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final EntityTransaction et;

    public Dao_MauSac() throws RemoteException {
        super();
        emf = Persistence.createEntityManagerFactory("JPADemo_SQL");
        em = emf.createEntityManager();
        et = em.getTransaction();
    }

    @Override
    public ArrayList<MauSac> getAllMauSac() throws RemoteException{
        ArrayList<MauSac> listMauSac = new ArrayList<>();
        String  sql = "select ms from MauSac ms";
        try {
            et.begin();
            listMauSac = (ArrayList<MauSac>) em.createQuery(sql).getResultList();
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return listMauSac;
    }
    @Override
    public MauSac getDLMauSacTheoMa(long maMS) throws RemoteException{
        MauSac mauSac = null;
        try {
            et.begin();
            mauSac = em.find(MauSac.class, maMS);
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return mauSac;
    }

    @Override
    public MauSac getMauSacTheoTen(String mauSac) throws RemoteException{
        MauSac mauSac1 = null;
        try {
            et.begin();
            mauSac1 = em.createQuery("select ms from MauSac ms where ms.mauSac = :mauSac", MauSac.class)
                    .setParameter("mauSac", mauSac).getResultList().stream().findFirst().orElse(null);
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return mauSac1;
    }

    @Override
    public boolean themDLMauSac(MauSac mauSac) throws RemoteException{
        try {
            et.begin();
            em.persist(mauSac);
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhatDLMauSac(MauSac mauSac) throws RemoteException{
        try {
            et.begin();
            em.merge(mauSac);
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean xoaDLMauSac(long maMauSac) throws RemoteException{
        String sql = "delete from MauSac ms where ms.maMauSac = :maMauSac";
        try {
            et.begin();
            em.createQuery(sql).setParameter("maMauSac", maMauSac).executeUpdate();
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return false;
    }
    public void close(){
        em.close();
        emf.close();
    }
}
