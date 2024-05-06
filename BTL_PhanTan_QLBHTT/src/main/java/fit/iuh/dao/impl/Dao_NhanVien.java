/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fit.iuh.dao.impl;


import fit.iuh.dao.INhanVienDao;
import fit.iuh.entity.NhanVien;
import jakarta.persistence.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author phant
 */
public class Dao_NhanVien extends UnicastRemoteObject implements INhanVienDao {

    private EntityManager em = null;
    private EntityTransaction et = null;

    public Dao_NhanVien() throws RemoteException {
        super();
        em = Persistence.createEntityManagerFactory("JPADemo_SQL").createEntityManager();
        et = em.getTransaction();
    }

    @Override
    public ArrayList<NhanVien> getAllNhanVien() throws RemoteException{
        try {
            et.begin();
            List<NhanVien> listNhanVien = em.createQuery("SELECT nv FROM NhanVien nv", NhanVien.class).getResultList();
            em.clear();
            et.commit();
            return (ArrayList<NhanVien>) listNhanVien;
        } catch (Exception e) {
            if (et != null && et.isActive()) {
                et.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ArrayList<NhanVien> getAllNhanVienConHoaDong() throws RemoteException{
        ArrayList<NhanVien> listNhanVien = new ArrayList<>();
        String url = "Select * from NhanVien where trangThai = 1";

        try {
            et.begin();
            listNhanVien = (ArrayList<NhanVien>) em.createNativeQuery(url, NhanVien.class).getResultList();
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return listNhanVien;
    }

    @Override
    public NhanVien getNhanVienTheoEmail(String email) throws RemoteException {
        String url = "Select nv from NhanVien nv where nv.email = :email";
        try {
            et.begin();
            NhanVien nv = em.createQuery(url, NhanVien.class).setParameter("email", email.trim())
                    .getResultList().stream().findFirst().orElse(null);
            et.commit();
            return nv;
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Thêm dữ liệu Nhân Viên vào database
     *
     * @param nv
     */
    @Override
    public void themNhanVien(NhanVien nv) throws RemoteException{
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(nv);
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Xóa dữ liệu Nhân viên trên database
     *
     * @param manv
     */
    @Override
    public void xoaNhanVien(long manv) throws RemoteException{
        EntityTransaction et = em.getTransaction();
        String query = "update NhanVien nv set nv.trangThai = false where maNV = :manv";
        try {
            et.begin();
            em.createQuery(query).setParameter("manv", manv).executeUpdate();
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật thông tin nhân viên
     *
     * @param nv
     */
    @Override
    public void capNhatNhanVien(NhanVien nv) throws RemoteException{
        String url = "update NhanVien set hoTen = ?, chuVu = ?, email = ?, sdt = ?, diaChi = ?, gioiTinh = ?, trangThai = ? where maNV = ?";
        try {
            et.begin();
            em.createNativeQuery(url)
                    .setParameter(1, nv.getHoTen())
                    .setParameter(2, nv.getChuVu())
                    .setParameter(3, nv.getEmail())
                    .setParameter(4, nv.getSdt())
                    .setParameter(5, nv.getDiaChi())
                    .setParameter(6, nv.getGioiTinh())
                    .setParameter(7, nv.isTrangThai())
                    .setParameter(8, nv.getMaNV())
                    .executeUpdate();
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Tìm kiếm nhân viên
     *
     * @param maNV
     * @param tenNV
     * @param sdt
     * @param email
     * @param chucVu
     * @param diaChi
     * @param trangThai
     * @return
     */
    @Override
    public ArrayList<NhanVien> timKiemNhanVien(long maNV, String tenNV, String sdt, String email, String chucVu, String diaChi, boolean trangThai) throws RemoteException{
        List<NhanVien> listNhanVienTim = null;

        try {
            et.begin();

            String queryString = "SELECT nv FROM NhanVien nv WHERE (:maNVCheck = '' OR nv.maNV = :maNVParam) " +
                    "AND nv.hoTen LIKE :tenNVParam " +
                    "AND nv.sdt LIKE :sdtParam " +
                    "AND nv.email LIKE :emailParam " +
                    "AND nv.chuVu LIKE :chucVuParam " +
                    "AND nv.diaChi LIKE :diaChiParam " +
                    "AND nv.trangThai = :trangThaiParam";

            listNhanVienTim = em.createQuery(queryString, NhanVien.class)
                    .setParameter("maNVCheck", maNV == 0 ? "" : String.valueOf(maNV))
                    .setParameter("maNVParam", maNV)
                    .setParameter("tenNVParam", "%" + tenNV + "%")
                    .setParameter("sdtParam", "%" + sdt + "%")
                    .setParameter("emailParam", "%" + email + "%")
                    .setParameter("chucVuParam", "%" + chucVu + "%")
                    .setParameter("diaChiParam", "%" + diaChi + "%")
                    .setParameter("trangThaiParam", trangThai)
                    .getResultList();

            et.commit();

            return (ArrayList<NhanVien>) listNhanVienTim;
        } catch (Exception e) {
            if (et != null && et.isActive()) {
                et.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public NhanVien getNhanVienTheoMa(long maNV) throws RemoteException{
        EntityManager em = Persistence.createEntityManagerFactory("JPADemo_SQL").createEntityManager();
        return em.find(NhanVien.class, maNV);
    }
    @Override
    public NhanVien getNhanVienTheoTen(String tenNV) throws RemoteException{
        String url = "Select nv from NhanVien nv where nv.hoTen = :hoTen";
        try {
            et.begin();
            NhanVien nv = em.createQuery(url, NhanVien.class).setParameter("hoTen", tenNV)
                    .getResultList().stream().findFirst().orElse(null);
            et.commit();
            return nv;
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return null;
    }


}
