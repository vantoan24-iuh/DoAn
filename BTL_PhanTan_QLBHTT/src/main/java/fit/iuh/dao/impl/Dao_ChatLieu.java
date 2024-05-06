/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fit.iuh.dao.impl;

import fit.iuh.dao.IChatLieuDao;
import fit.iuh.entity.ChatLieu;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author phant
 */
public class Dao_ChatLieu extends UnicastRemoteObject implements IChatLieuDao {

    private EntityManager em = null;
    private EntityTransaction tx = null;

    public Dao_ChatLieu() throws RemoteException {
        super();
        em = Persistence.createEntityManagerFactory("JPADemo_SQL").createEntityManager();
        tx = em.getTransaction();
    }

    /**
     * Lấy tất cả chất liệu trong database
     *
     * @return
     */
    @Override
    public ArrayList<ChatLieu> getAllChatLieu() throws RemoteException {
        String query = "select cl from ChatLieu cl";
        List<ChatLieu> list = null;
        try {
            tx.begin();
            list = em.createQuery(query, ChatLieu.class).getResultList();
            tx.commit();
            return (ArrayList<ChatLieu>) list;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Thêm chất liệu vào database
     *
     * @param chatLieu
     */
    @Override
    public boolean themDLChatLieu(ChatLieu chatLieu) throws RemoteException{
        try {
            tx.begin();
            em.persist(chatLieu);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa dữ liệu Chất Liệu trên database
     *
     * @param maChatLieu
     */
    @Override
    public boolean xoaDLChatLieu(long maChatLieu)throws RemoteException {
        try {
            tx.begin();
            ChatLieu cl = em.find(ChatLieu.class, maChatLieu);
            em.remove(cl);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Cập nhật dữ liệu Chất Liệu trên database
     *
     * @param chatLieu
     */
    @Override
    public boolean catNhatDLChatLieu(ChatLieu chatLieu) throws RemoteException{
        try {
            tx.begin();
            em.merge(chatLieu);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tìm dữ liệu Chất Liệu theo mã trên database
     *
     * @param id
     */
    @Override
    public ChatLieu getDLChatLieuTheoMa(long id) throws RemoteException {
        try {
            tx.begin();
            ChatLieu cl = em.find(ChatLieu.class, id);
            tx.commit();
            return cl;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy dữ liệu Chất liệu theo tên
     *
     * @param tenChatLieu
     * @return
     */
    @Override
    public ChatLieu getChatLieuTheoTen(String tenChatLieu) throws RemoteException{

        String sql = "select cl from ChatLieu cl where cl.chatLieu = :tenChatLieu";
        try {
            tx.begin();
            ChatLieu cl = em.createQuery(sql, ChatLieu.class).setParameter("tenChatLieu", tenChatLieu).getResultList().stream().findFirst().orElse(null);;
            tx.commit();
            return cl;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Tạo tự động mã
     * @return
     */
//    public String taoMaChatLieu() {
//        Connection con = Connect.getInstance().getConnection();
//        String url = "select top 1 maChatLieu from ChatLieu order by maChatLieu desc";
//
//        try {
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(url);
//            if(rs.next()) {
//                String maChatLieu = rs.getString(1);
//                int so = Integer.parseInt(maChatLieu.substring(4));
//                so++;
//                String maChatLieuMoi = so + "";
//                while(maChatLieuMoi.length() < 4) {
//                    maChatLieuMoi = "0" +maChatLieuMoi;
//
//                }
//                return "CL" + maChatLieuMoi;
//            } else {
//                return "CL0001";
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
