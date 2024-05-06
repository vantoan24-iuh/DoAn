package fit.iuh.dao.impl;

import fit.iuh.dao.ISanPhamDao;
import fit.iuh.entity.PhanLoai;
import fit.iuh.entity.SanPham;
import jakarta.persistence.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Dao_SanPham extends UnicastRemoteObject implements ISanPhamDao {
    private EntityManager em;

    public Dao_SanPham() throws RemoteException {
        super();
        em = Persistence.createEntityManagerFactory("JPADemo_SQL")
                .createEntityManager();
    }

    public Dao_SanPham(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public ArrayList<SanPham> getAllSanPham() throws RemoteException {
        return (ArrayList<SanPham>) em.createNamedQuery("getAllSanPham", SanPham.class)
                .getResultList();
    }

    @Override
    public ArrayList<SanPham> getAllQuanAo() throws RemoteException {
        return (ArrayList<SanPham>) em.createNamedQuery("getAllQuaNAo", SanPham.class)
                .getResultList();
    }

    @Override
    public ArrayList<SanPham> getAllPhuKien() throws RemoteException {
        return (ArrayList<SanPham>) em.createNamedQuery("getAllPhuKien", SanPham.class)
                .getResultList();
    }

    @Override
    public boolean themSanPham(SanPham sanPham) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sanPham);
            tx.commit();
            return true;
        } catch (RollbackException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause != null) {
                System.err.println("Cause: " + cause);
            }
            if (tx.isActive()) {
                tx.rollback();
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean xoaSanPham(long masp) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SanPham sanPham = em.find(SanPham.class, masp);

//            em.detach(sanPham);
            em.remove(sanPham);

//            tx.commit();
            return true;

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhatSanPham(SanPham sanPham) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sanPham);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public ArrayList<SanPham> timKiemQuanAo(long maSP, String tenSP, String tenPhanLoai, String tenNCC, String tenMauSac, String tenChatLieu, String tenKichThuoc) throws RemoteException {
        return (ArrayList<SanPham>) em.createNamedQuery("timKiemQuanAo", SanPham.class)
                .setParameter("maSPCheck", maSP == 0 ? "" : String.valueOf(maSP))
                .setParameter("maSP", maSP)
                .setParameter("tenSP", "%" + tenSP + "%")
                .setParameter("loaiSanPham", "%" + tenPhanLoai + "%")
                .setParameter("tenNCC", "%" + tenNCC + "%")
                .setParameter("mauSac", "%" + tenMauSac + "%")
                .setParameter("chatLieu", "%" + tenChatLieu + "%")
                .setParameter("kichThuoc", "%" + tenKichThuoc + "%")
                .getResultList();
    }

    @Override
    public ArrayList<SanPham> timKiemPhuKien(long maSP, String tenSP, String tenPhanLoai, String tenNCC, String tenMauSac, String tenChatLieu, String tenKichThuoc) throws RemoteException {

        return (ArrayList<SanPham>) em.createNamedQuery("timKiemPhuKien", SanPham.class)
                .setParameter("maSPCheck", maSP == 0 ? "" : String.valueOf(maSP))
                .setParameter("maSP", maSP)
                .setParameter("tenSP", "%" + tenSP + "%")
                .setParameter("loaiSanPham", "%" + tenPhanLoai + "%")
                .setParameter("tenNCC", "%" + tenNCC + "%")
                .setParameter("mauSac", "%" + tenMauSac + "%")
                .setParameter("chatLieu", "%" + tenChatLieu + "%")
                .setParameter("kichThuoc", "%" + tenKichThuoc + "%")
                .getResultList();
    }

    @Override
    public SanPham getSanPhamTheoMa(long maSP) throws RemoteException {
        return em.createNamedQuery("getSanPhamTheoMa", SanPham.class)
                .setParameter("maSP", maSP)
                .getSingleResult();
    }

    @Override
    public ArrayList<SanPham> getAllSanPhamTheoTieuChi(String maPhanLoai, String maMauSac, String maKichThuoc) throws RemoteException {
        return (ArrayList<SanPham>) em.createNamedQuery("getAllSanPhamTheoTieuChi", SanPham.class)
                .setParameter("maPhanLoai", "%" + maPhanLoai + "%")
                .setParameter("mauSac", "%" + maMauSac + "%")
                .setParameter("kichThuoc", "%" + maKichThuoc + "%")
                .getResultList();
    }

    @Override
    public ArrayList<SanPham> getAllSanPhamHetHang(String maPhanLoai, String maMauSac, String maKichThuoc) throws RemoteException {
        return (ArrayList<SanPham>) em.createNamedQuery("getAllSanPhamHetHang", SanPham.class)
                .setParameter("maPhanLoai", "%" + maPhanLoai + "%")
                .setParameter("mauSac", "%" + maMauSac + "%")
                .setParameter("kichThuoc", "%" + maKichThuoc + "%")
                .getResultList();
    }

    @Override
    public ArrayList<SanPham> getSanPhamBanChay() throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        String query = "SELECT sp.maSP,  SUM(cthd.soLuong) FROM SanPham sp JOIN  CTHD cthd ON sp.maSP = cthd.sanPham.maSP GROUP BY sp.maSP ORDER BY SUM(cthd.soLuong)  DESC";
        List<SanPham> list = new ArrayList<>();
        try {
            List<Object[]> results = em.createQuery(query, Object[].class).setMaxResults(5).getResultList();
            for (Object[] result : results) {
                SanPham sanPham = em.find(SanPham.class, (Long) result[0]);
                list.add(sanPham);
            }
            return (ArrayList<SanPham>) list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<SanPham> getSanPhamBanCham() throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        String query = "SELECT sp.maSP,  SUM(cthd.soLuong) FROM SanPham sp JOIN  CTHD cthd ON sp.maSP = cthd.sanPham.maSP GROUP BY sp.maSP ORDER BY SUM(cthd.soLuong)  ASC";
        List<SanPham> list = new ArrayList<>();
        try {
            List<Object[]> results = em.createQuery(query, Object[].class).setMaxResults(5).getResultList();
            for (Object[] result : results) {
                SanPham sanPham = em.find(SanPham.class, (Long) result[0]);
                list.add(sanPham);
            }
            return (ArrayList<SanPham>) list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<SanPham> getSoLuongSPTheoMaPL() throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        String query = "SELECT sp.phanLoai.maPhanLoai, COUNT(sp) FROM SanPham sp GROUP BY sp.phanLoai.maPhanLoai ORDER BY sp.phanLoai.maPhanLoai";
        List<SanPham> list = new ArrayList<>();
        try {
            List<Object[]> results = em.createQuery(query, Object[].class).getResultList();
            for (Object[] result : results) {
                PhanLoai phanLoai = em.find(PhanLoai.class, (Long) result[0]);
                Long soLuong = (Long) result[1];
                SanPham sanPham = new SanPham(soLuong.intValue(), phanLoai);
                list.add(sanPham);
            }
            return (ArrayList<SanPham>) list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<SanPham> getAllSanPhamTheoNgay(String tuNgay, String denNgay) throws RemoteException {
        Date ngayNhap = java.sql.Date.valueOf(tuNgay);
        Date ngayKetThuc = java.sql.Date.valueOf(denNgay);
        return (ArrayList<SanPham>) em.createNamedQuery("getAllSanPhamTheoNgay", SanPham.class)
                .setParameter("ngayNhap", ngayNhap)
                .setParameter("ngayKetThuc", ngayKetThuc)
                .getResultList();
    }

    @Override
    public void giamSoLuongSanPham(SanPham sp) throws RemoteException {
        String query = "UPDATE SanPham SET soLuong = soLuong - ? WHERE maSP = ?";
        System.out.println(sp.getMaSP() + " : " + sp.getSoLuong());


        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.clear();
            int updatedQuantity1 = em.find(SanPham.class, sp.getMaSP()).getSoLuong();
            System.out.println("Số lượng trc  khi cập nhật: " + updatedQuantity1);
            em.createNativeQuery(query)
                    .setParameter(1, sp.getSoLuong())
                    .setParameter(2, sp.getMaSP())
                    .executeUpdate();

            tx.commit();

            em.clear();
            // Kiểm tra lại số lượng sau khi cập nhật
            int updatedQuantity = em.find(SanPham.class, sp.getMaSP()).getSoLuong();
            System.out.println("Số lượng sau khi cập nhật: " + updatedQuantity);

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
//        System.out.println(sp.getMaSP() + " : " + sp.getSoLuong());
//        int updatedQuantity1 = em.find(SanPham.class, sp.getMaSP()).getSoLuong();
//        System.out.println("Số lượng trc  khi cập nhật: " + updatedQuantity1);
//
//        EntityTransaction tx = em.getTransaction();
//        try {
//            tx.begin();
//
//           SanPham spUpdate = em.find(SanPham.class, sp.getMaSP());
//            System.out.println("sp tim update: "+ spUpdate);
//           if(spUpdate!=null) {
//                spUpdate.setSoLuong(spUpdate.getSoLuong() - sp.getSoLuong());
//                em.merge(spUpdate);
//           }
//            tx.commit();
//
//            // Kiểm tra lại số lượng sau khi cập nhật
//            int updatedQuantity = em.find(SanPham.class, sp.getMaSP()).getSoLuong();
//            System.out.println("Số lượng sau khi cập nhật: " + updatedQuantity);
//
//        } catch (Exception e) {
//            if (tx.isActive()) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        }
//        System.out.println(sp.getMaSP() + " : " + sp.getSoLuong());
//
//        EntityTransaction tx = em.getTransaction();
//        try {
//            tx.begin();
//
//            // Tạo một bản sao của đối tượng SanPham từ cơ sở dữ liệu
//            SanPham spUpdate = em.find(SanPham.class, sp.getMaSP());
//            if (spUpdate != null) {
//                // Tạo một bản sao của đối tượng SanPham để thay đổi
//                SanPham spToBeUpdated = new SanPham(spUpdate.getMaSP(), spUpdate.getTenSP(), spUpdate.getSoLuong());
//                spToBeUpdated.setSoLuong(spUpdate.getSoLuong() - sp.getSoLuong());
//
//                // Cập nhật đối tượng trong phiên làm việc (session) của EntityManager
//                em.merge(spToBeUpdated);
//            }
//            tx.commit();
//
//            // Kiểm tra lại số lượng sau khi cập nhật
//            int updatedQuantity = em.find(SanPham.class, sp.getMaSP()).getSoLuong();
//            System.out.println("Số lượng sau khi cập nhật: " + updatedQuantity);
//
//        } catch (Exception e) {
//            if (tx.isActive()) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        }
    }


    @Override
    public void tangSoLuongSanPham(long maSP, int soLuong) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        String url = "UPDATE SanPham SET soLuong = soLuong + ? WHERE maSP = ?";

        try {
            tx.begin();
            em.createNativeQuery(url)
                    .setParameter(1, soLuong)
                    .setParameter(2, maSP)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

}
