/**
 * @ (#) IChatLieuDao.java      4/10/2024
 * <p>
 * Copyright (c) 2024 IUH. All rights reserved
 */

package fit.iuh.dao;

import fit.iuh.entity.CTPhieuDatHang;
import fit.iuh.entity.ChatLieu;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/*
 * @description:
 * @author: Sinh Phan Tien
 * @date: 4/10/2024
 */
public interface IChatLieuDao extends Remote {
    public ArrayList<ChatLieu> getAllChatLieu() throws RemoteException;
    public boolean themDLChatLieu(ChatLieu chatLieu) throws RemoteException;
    public boolean xoaDLChatLieu(long maChatLieu) throws RemoteException;
    public boolean catNhatDLChatLieu(ChatLieu chatLieu) throws RemoteException;
    public ChatLieu getDLChatLieuTheoMa(long id) throws RemoteException;
    public ChatLieu getChatLieuTheoTen(String tenChatLieu) throws RemoteException;

}
