/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fit.iuh.dao.impl;

import fit.iuh.dao.IKhachHangDao;
import fit.iuh.entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


/**
 * @author ACER
 */
public class Dao_KhachHang extends UnicastRemoteObject implements IKhachHangDao {

    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final EntityTransaction et;

    public Dao_KhachHang() throws RemoteException {
        super();
        emf = Persistence.createEntityManagerFactory("JPADemo_SQL");
        em = emf.createEntityManager();
        et = em.getTransaction();
    }

    @Override
    public ArrayList<KhachHang> getAllKhachHang() throws RemoteException {
        return (ArrayList<KhachHang>) em.createQuery("SELECT kh FROM KhachHang kh").getResultList();
    }

    public void themKhachHang(KhachHang kh) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.merge(kh);
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void capNhatKhachHang(KhachHang kh) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            KhachHang khInDBS = em.find(KhachHang.class, kh.getMaKH());
            if (khInDBS == null) {
                return;
            }
            khInDBS = em.merge(kh);
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<KhachHang> timKiemKhachHang(long maKhachHang, String tenKhachHang, String soDienThoai, String email) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        ArrayList<KhachHang> listKhachHang = new ArrayList<>();
        String sql = "select kh from KhachHang kh where (:maKHCheck = '' or kh.maKH = :maKH) and kh.hoTen like :hoTen and kh.sdt like :sdt and kh.email like :email";
        try {
            et.begin();
            listKhachHang = (ArrayList<KhachHang>) em.createQuery(sql, KhachHang.class)
                    .setParameter("maKHCheck", maKhachHang == 0 ? "" : String.valueOf(maKhachHang))
                    .setParameter("maKH", maKhachHang)
                    .setParameter("hoTen", "%" + tenKhachHang + "%")
                    .setParameter("sdt", "%" + soDienThoai + "%")
                    .setParameter("email", "%" + email + "%")
                    .getResultList();
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return listKhachHang;
    }

    /**
     * Lấy thông tin khách hàng theo mã
     *
     * @param maKH
     * @return
     */
    @Override
    public KhachHang getKhachHangTheoMa(long maKH) throws RemoteException {
        return em.find(KhachHang.class, maKH);
    }

    @Override
    public boolean kiemTraKhachHangDaTonTai(String email, String sdt) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        String sql = "select kh from KhachHang kh where kh.email = :email or kh.sdt = :sdt";
        try {
            et.begin();
            ArrayList<KhachHang> listKhachHang = (ArrayList<KhachHang>) em.createQuery(sql, KhachHang.class)
                    .setParameter("email", email)
                    .setParameter("sdt", sdt)
                    .getResultList();
            et.commit();
            return listKhachHang.size() > 0;
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tạo tự động mã
     * @return
     */
//    public String taoMaKhachHang() {
//        Connection con = Connect.getInstance().getConnection();
//        String url = "select top 1 maKH from KhachHang order by maKH desc";
//
//        try {
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(url);
//            if(rs.next()) {
//                String maKH = rs.getString(1);
//                int so = Integer.parseInt(maKH.substring(4));
//                so++;
//                String maKHMoi = so + "";
//                while(maKHMoi.length() < 4) {
//                    maKHMoi = "0" +maKHMoi;
//
//                }
//                return "KH" + maKHMoi;
//            } else {
//                return "KH0001";
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
