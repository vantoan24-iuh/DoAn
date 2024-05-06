/**
 * @ (#) TestDaoChatLieu.java      4/11/2024
 * <p>
 * Copyright (c) 2024 IUH. All rights reserved
 */

package dao;

import fit.iuh.dao.impl.Dao_ChatLieu;
import fit.iuh.entity.ChatLieu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;

/*
 * @description:
 * @author: Sinh Phan Tien
 * @date: 4/11/2024
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestDaoChatLieu {
    private Dao_ChatLieu daoChatLieu;
    @BeforeAll
    public void init() throws RemoteException {
        daoChatLieu = new Dao_ChatLieu();
    }

    @Test
    public void testGetAllChatLieu() throws RemoteException {
        daoChatLieu.getAllChatLieu().forEach(System.out::println);
    }

    @Test
    public void testThemDLChatLieu() throws RemoteException {
        ChatLieu chatLieu = new ChatLieu();
        chatLieu.setChatLieu("NiLon");
        boolean check = daoChatLieu.themDLChatLieu(chatLieu);
        Assertions.assertTrue(check);
    }

    @Test
    public void testXoaDLChatLieu()throws RemoteException{
        boolean check = daoChatLieu.xoaDLChatLieu(6);
        Assertions.assertTrue(check);
    }

    @Test
    public void testCapNhatDLChatLieu()throws RemoteException{
        ChatLieu chatLieu = daoChatLieu.getDLChatLieuTheoMa(7);
        chatLieu.setChatLieu("NiSen");
        boolean check = daoChatLieu.catNhatDLChatLieu(chatLieu);
        Assertions.assertTrue(check);
    }

    @Test
    public void testGetDLChatLieuTheoMa()throws RemoteException{
        ChatLieu chatLieu = daoChatLieu.getDLChatLieuTheoMa(1);
        System.out.println(chatLieu);
    }

    @Test
    public void testGetChatLieuTheoTen()throws RemoteException{
        ChatLieu chatLieu = daoChatLieu.getChatLieuTheoTen("Da");
        System.out.println(chatLieu);
    }


}
