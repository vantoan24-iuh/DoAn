/**
 * @ (#) ISendMail.java      4/19/2024
 * <p>
 * Copyright (c) 2024 IUH. All rights reserved
 */

package fit.iuh.dao;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * @description:
 * @author: Sinh Phan Tien
 * @date: 4/19/2024
 */
public interface ISendMail extends Remote {
    public void sendEmailMa(String toEmail, String tieuDe, String noiDung) throws RemoteException;
    public void guiHoaDonVeEmail(String toEmail, String tieuDe, String noiDung) throws RemoteException;
}
