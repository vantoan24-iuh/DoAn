/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fit.iuh.dao.impl;

import fit.iuh.dao.IPhieuDatHangDao;
import fit.iuh.entity.*;
import fit.iuh.entity.PhieuDatHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phant
 */
public class Dao_PhieuDatHang extends UnicastRemoteObject implements IPhieuDatHangDao {

    EntityManager em = null;
    EntityTransaction tx = null;

    public Dao_PhieuDatHang() throws RemoteException{
        super();
        em = Persistence.createEntityManagerFactory("JPADemo_SQL").createEntityManager();
        tx = em.getTransaction();
    }

    @Override
    public List<PhieuDatHang> getAllPhieuDatHang() throws RemoteException{
        String query = "Select pdt from PhieuDatHang pdt";
        List<PhieuDatHang> list = null;
        try {
            tx.begin();
            list = em.createQuery(query, PhieuDatHang.class).getResultList();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return list;

    }
    @Override
    public boolean themPhieuDatHang(PhieuDatHang pdt) throws RemoteException {
        String query = "INSERT INTO PhieuDatHang (maKH, maNV, ngayLap) VALUES (?, ?, ?)";
        try {
            tx.begin();
            em.createNativeQuery(query)
                    .setParameter(1, pdt.getKhachHang().getMaKH())
                    .setParameter(2, pdt.getNhanVien().getMaNV())
                    .setParameter(3, pdt.getNgayLap())
                    .executeUpdate();
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error while committing transaction: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    /*
    *@param tham số: mã phieu dat hang
    */
    @Override
    public boolean xoaPhieuDatHang(long maPhieuDatHang) throws RemoteException{

        String url = "DELETE FROM PhieuDatHang pd WHERE pd.maPhieuDat = :maPhieuDatHang";
        try {
            tx.begin();
//            PhieuDatHang pdt = em.find(PhieuDatHang.class, maPhieuDatHang);
//            em.remove(pdt);
            em.createQuery(url).setParameter("maPhieuDatHang", maPhieuDatHang).executeUpdate();
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy thông tin phiếu đặt hàng theo mã phiếu đặt hàng
     */
    @Override
    public PhieuDatHang getPDTTheoMa(long maPDT) throws RemoteException{
        try {
            tx.begin();
            PhieuDatHang pdt = em.find(PhieuDatHang.class, maPDT);
            tx.commit();
            return pdt;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy thông tin phiếu đặt hàng theo mã khách hàng
     */
    @Override
    public PhieuDatHang getPDTTheoMaKH(long maKhachHang) throws RemoteException{

//        String url = "Select p from PhieuDatHang p where p.khachHang.maKH = :maKHachHang";
//        try {
//            tx.begin();
//            PhieuDatHang pdt = em.createQuery(url, PhieuDatHang.class).setParameter("maKHachHang", maKhachHang)
//                    .getResultList()
//                    .stream()
//                    .findFirst().orElse(null);
//            tx.commit();
//            return pdt;
//        } catch (Exception e) {
//            tx.rollback();
//            e.printStackTrace();
//        }
//
//        return null;
        String url = "Select p from PhieuDatHang p join p.khachHang kh where kh.maKH = :maKHachHang";
            List<PhieuDatHang> resultList = em.createQuery(url, PhieuDatHang.class)
                    .setParameter("maKHachHang", maKhachHang)
                    .getResultList();
            if (resultList.isEmpty()) {
                // Xử lý khi không tìm thấy kết quả, ví dụ: trả về null hoặc thông báo lỗi
                return null;
            } else {
                // Xử lý khi tìm thấy kết quả
                return resultList.get(0); // Trả về phần tử đầu tiên trong danh sách kết quả
            }
    }

}
