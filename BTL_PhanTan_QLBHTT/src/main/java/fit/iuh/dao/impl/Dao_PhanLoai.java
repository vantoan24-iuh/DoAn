/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fit.iuh.dao.impl;

import fit.iuh.dao.IPhanLoaiDao;
import fit.iuh.entity.PhanLoai;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author phant
 */
public class Dao_PhanLoai extends UnicastRemoteObject implements IPhanLoaiDao {
    private EntityManager em = null;
    private EntityTransaction et = null;

    public Dao_PhanLoai() throws RemoteException {
        super();
        em = Persistence.createEntityManagerFactory("JPADemo_SQL").createEntityManager();
        et = em.getTransaction();
    }
    @Override
    public ArrayList<PhanLoai> getAllPhanLoai() throws RemoteException{
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPADemo_SQL");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        ArrayList<PhanLoai> listPhanLoai = new ArrayList<>();
        String url = "Select * from PhanLoai";

        try {
            et.begin();
            listPhanLoai = (ArrayList<PhanLoai>) em.createNativeQuery(url, PhanLoai.class).getResultList();
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return listPhanLoai;
    }
    @Override
    public ArrayList<PhanLoai> getAllPhanLoaiCuaPhuKien() throws RemoteException{
        EntityManager em = Persistence.createEntityManagerFactory("JPADemo_SQL").createEntityManager();
        ArrayList<PhanLoai> listPhanLoai = new ArrayList<>();
        String url = "Select * from PhanLoai WHERE maPhanLoai NOT IN ('1', '2')";
        try {
            listPhanLoai = (ArrayList<PhanLoai>) em.createNativeQuery(url, PhanLoai.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return listPhanLoai;
        }
        return listPhanLoai;
    }
    @Override
    public PhanLoai getDLPhanLoaiSPTheoMa(long maPL) throws RemoteException{
        return em.find(PhanLoai.class, maPL);
    }
    
    /**
     * lấy thông tin phân loại sản phảm theo tên
     * @param tenPhanLoai
     * @return 
     */
    @Override
    public PhanLoai getPhanLoaiTheoTen(String tenPhanLoai)throws RemoteException{
        String sql = "select pl from PhanLoai pl where pl.loaiSanPham = :tenPhanLoai";
        try {
            et.begin();
            PhanLoai phanLoai = em.createQuery(sql, PhanLoai.class)
                    .setParameter("tenPhanLoai", tenPhanLoai)
                    .getResultList().stream().findFirst().orElse(null);
            et.commit();
            return phanLoai;
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Thêm loai sản phẩm
     * @param phanLoai 
     */
    @Override
    public void themLoaiSanPham(PhanLoai phanLoai) throws RemoteException{
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.merge(phanLoai);
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Xóa dữ liệu loại sản phẩm trên database
     * @param maPhanLoai
     */
    @Override
    public void xoaPhanLoaiSanPham(long maPhanLoai) throws RemoteException{
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            PhanLoai phanLoai = em.find(PhanLoai.class, maPhanLoai);
            if (phanLoai == null) {
                return;
            }
            em.remove(phanLoai);
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
    }
    
    /**
     * Cập nhật dữ liệu phân loại trên database
     * @param phanLoai
     */
    @Override
    public void catNhatLoaiSanPham(PhanLoai phanLoai)throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.merge(phanLoai);
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
    }

}
