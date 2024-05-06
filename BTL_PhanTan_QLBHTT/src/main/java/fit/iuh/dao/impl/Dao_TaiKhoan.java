/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fit.iuh.dao.impl;


import fit.iuh.dao.ITaiKhoanDao;
import fit.iuh.entity.TaiKhoan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * @author phant
 */
public class Dao_TaiKhoan extends UnicastRemoteObject implements ITaiKhoanDao {
    private EntityManager em;

    public Dao_TaiKhoan() throws RemoteException {
        super();
        em = Persistence.createEntityManagerFactory("JPADemo_SQL")
                .createEntityManager();
    }

    public Dao_TaiKhoan(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public ArrayList<TaiKhoan> getAllTaiKhoan() throws RemoteException{
        return (ArrayList<TaiKhoan>) em.createNamedQuery("TaiKhoan.getAll", TaiKhoan.class).getResultList();
    }

    @Override
    public ArrayList<TaiKhoan> getAllTaiKhoanConHoatDong() throws RemoteException{
        return (ArrayList<TaiKhoan>) em.createNamedQuery("TaiKhoan.getTrangThai", TaiKhoan.class).getResultList();
    }

    @Override
    public boolean themTaiKhoan(TaiKhoan taiKhoan) throws RemoteException{
        EntityTransaction tx = em.getTransaction();
        String query = "insert into TaiKhoan(tenTaiKhoan, matKhau, phanQuyen, maNV, trangThai) values(?, ?, ?, ?, ?)";
        try {
            tx.begin();
            em.createNativeQuery(query)
                    .setParameter(1, taiKhoan.getTenTaiKhoan())
                    .setParameter(2, taiKhoan.getMatKhau())
                    .setParameter(3, taiKhoan.getPhanQuyen())
                    .setParameter(4, taiKhoan.getNhanVien().getMaNV())
                    .setParameter(5, taiKhoan.isTrangThai())
                    .executeUpdate();
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean xoaTaiKhoan(Long maNv) throws RemoteException{
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TaiKhoan taiKhoan = em.find(TaiKhoan.class, maNv);
            taiKhoan.setTrangThai(false);
            em.merge(taiKhoan);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhatTaiKhoan(Long maNv) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TaiKhoan taiKhoan = em.find(TaiKhoan.class, maNv);
            taiKhoan.setTrangThai(true);
            em.merge(taiKhoan);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void doiMatKhauTaiKhoan(TaiKhoan taiKhoan) throws RemoteException{
        EntityTransaction tx = em.getTransaction();
        String query="update TaiKhoan tk set tk.matKhau = :matKhau where tk.tenTaiKhoan = :tenTaiKhoan";
        try {
            tx.begin();
            em.createQuery(query)
                    .setParameter("matKhau", taiKhoan.getMatKhau())
                    .setParameter("tenTaiKhoan", taiKhoan.getTenTaiKhoan())
                    .executeUpdate();
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public TaiKhoan dangNhapTaiKhoan(String tenTaiKhoan, String matKhau) throws RemoteException{
        return em.createNamedQuery("TaiKhoan.kiemTraTaiKhoan", TaiKhoan.class)
                .setParameter("tenTaiKhoan", tenTaiKhoan)
                .setParameter("matKhau", matKhau)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public String getMatKhau(String tenTK) throws RemoteException{
        return em.createNamedQuery("TaiKhoan.getMatKhau", String.class)
                .setParameter("ten", tenTK)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public TaiKhoan getTaiKhoanNV(Long maNV)throws RemoteException{
        return em.createNamedQuery("TaiKhoan.getTaiKhoanByMaNV", TaiKhoan.class)
                .setParameter("maNV", maNV)
                .getResultList().stream().findFirst().orElse(null);

    }

    @Override
    public boolean datLaiMatKhau(TaiKhoan taiKhoan, String matKhau) throws RemoteException{
        EntityTransaction tx = em.getTransaction();
        String query = "update TaiKhoan tk set tk.matKhau = :matKhau where tk.tenTaiKhoan = :tenTaiKhoan";
        try {
            tx.begin();
            em.createQuery(query)
                    .setParameter("matKhau", matKhau)
                    .setParameter("tenTaiKhoan", taiKhoan.getTenTaiKhoan())
                    .executeUpdate();
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }


//
//
//    public TaiKhoan getTaiKhoanNV(String maNVTim) {
//        daoNhanVien = new Dao_NhanVien();
//
//        Connection con = Connect.getInstance().getConnection();
//        PreparedStatement prestmt = null;
//        String url = "select * from TaiKhoan where maNV = ?";
//        try {
//            prestmt = con.prepareStatement(url);
//            prestmt.setString(1, maNVTim);
//
//            ResultSet rs = prestmt.executeQuery();
//            while(rs.next()) {
//                String maNV = rs.getString(4);
//                NhanVien nhanVien = daoNhanVien.getNhanVienTheoMa(maNV);
//
//                TaiKhoan taiKhoan = new TaiKhoan(rs.getString(1), rs.getString(2), rs.getString(3), nhanVien, rs.getBoolean(5));
//                return taiKhoan;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * Đặt lại mật khẩu tài khoản
//     * @param taiKhoan
//     */
//    public void datLaiMatKhau(TaiKhoan taiKhoan, String matKhau) {
//       Connection con = Connect.getInstance().getConnection();
//        PreparedStatement prestmt = null;
//        String url = "update TaiKhoan set matKhau = ? where tenTaiKhoan = ?";
//        try {
//            prestmt = con.prepareStatement(url);
//
//            prestmt.setString(1, matKhau);
//            prestmt.setString(2, taiKhoan.getTenTaiKhoan());
//            prestmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }  finally {
//            try {
//                 prestmt.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
