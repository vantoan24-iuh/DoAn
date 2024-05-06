package fit.iuh.dao;

import fit.iuh.entity.MauSac;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IMauSacDao extends Remote {
    public ArrayList<MauSac> getAllMauSac() throws RemoteException;
    public MauSac getDLMauSacTheoMa(long maMS) throws RemoteException;
    public MauSac getMauSacTheoTen(String tenMauSac) throws RemoteException;
    public boolean themDLMauSac(MauSac mauSac) throws RemoteException;
    public boolean capNhatDLMauSac(MauSac mauSac) throws RemoteException;
    public boolean xoaDLMauSac(long maMauSac) throws RemoteException;


}
