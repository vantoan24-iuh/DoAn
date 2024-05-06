package fit.iuh.dao;

import fit.iuh.entity.NhaCungCap;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface INhaCungCapDao extends Remote {
    public ArrayList<NhaCungCap> getAllNhaCungCap() throws RemoteException;

    public boolean themNhaCungCap(NhaCungCap nhaCungCap) throws RemoteException;
    public boolean xoaNhaCungCap(Long maNCC) throws RemoteException;
    public boolean capNhatNhaCungCap(NhaCungCap ncc) throws RemoteException;

    //    String maNCC, String tenNCC, String sdt, String email

    public List<NhaCungCap> timKiemNhaCungCap(Long maNCC, String tenNCC, String sdt, String email ) throws RemoteException;

    public NhaCungCap  getNhaCungCapTheoTen(String tenNCC) throws RemoteException;

    public NhaCungCap  getNhaCungCapTheoMa(Long maNCC) throws RemoteException;

//    taoMaNhaCungCap không viết


}
