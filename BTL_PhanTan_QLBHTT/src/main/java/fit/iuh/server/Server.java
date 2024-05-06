/**
 * @ (#) Server.java      4/17/2024
 * <p>
 * Copyright (c) 2024 IUH. All rights reserved
 */

package fit.iuh.server;

import fit.iuh.dao.*;
import fit.iuh.dao.impl.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.registry.LocateRegistry;

/*
 * @description:
 * @author: Sinh Phan Tien
 * @date: 4/17/2024
 */
public class Server {
    public static final String URL = "rmi://localhost:1243/";

    public static void main(String[] args) {
        try {
            Context context = new InitialContext();

            //Interface
            IChatLieuDao chatLieuDao = new Dao_ChatLieu();
            IKichThuocDao kichThuocDao = new Dao_KichThuoc();
            ISanPhamDao sanPhamDao = new Dao_SanPham();
            IPhieuDatHangDao phieuDatHangDao = new Dao_PhieuDatHang();
            ICTPhieuDatHangDao ctPhieuDatHangDao = new Dao_CTPhieuDatHang();
            IHoaDonDao hoaDonDao = new Dao_HoaDon();
            ICTHDDao ctHoaDonDao = new Dao_CTHD();
            IKhachHangDao khachHangDao = new Dao_KhachHang();
            INhanVienDao nhanVienDao = new Dao_NhanVien();
            IMauSacDao mauSacDao = new Dao_MauSac();
            INhaCungCapDao nhaCungCapDao = new Dao_NhaCungCap();
            IPhanLoaiDao phanLoaiDao = new Dao_PhanLoai();
            ITaiKhoanDao taiKhoanDao = new Dao_TaiKhoan();
            ISendMail sendMailDao = new Dao_SendMail();

            LocateRegistry.createRegistry(1243);
            context.rebind(URL + "chatLieuDao", chatLieuDao);
            context.rebind(URL + "kichThuocDao", kichThuocDao);
            context.rebind(URL + "sanPhamDao", sanPhamDao);
            context.rebind(URL + "phieuDatHangDao", phieuDatHangDao);
            context.rebind(URL + "ctPhieuDatHangDao", ctPhieuDatHangDao);
            context.rebind(URL + "hoaDonDao", hoaDonDao);
            context.rebind(URL + "ctHoaDonDao", ctHoaDonDao);
            context.rebind(URL + "khachHangDao", khachHangDao);
            context.rebind(URL + "nhanVienDao", nhanVienDao);
            context.rebind(URL + "mauSacDao", mauSacDao);
            context.rebind(URL + "nhaCungCapDao", nhaCungCapDao);
            context.rebind(URL + "phanLoaiDao", phanLoaiDao);
            context.rebind(URL + "taikhoanDao", taiKhoanDao);
            context.rebind(URL + "sendMailDao", sendMailDao);

            System.out.println("Server is running...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
