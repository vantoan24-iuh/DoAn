create database QLBHTT_DB_PT
go
use QLBHTT_DB_PT
go

 --DỮ LIỆU----
 SET IDENTITY_INSERT dbo.PhanLoai ON
GO
 --Phân Loại--
 insert into PhanLoai (maPhanLoai, tenPhanLoai) values (1, N'Áo');
 insert into PhanLoai (maPhanLoai, tenPhanLoai) values (2, N'Quần');
 insert into PhanLoai (maPhanLoai, tenPhanLoai) values (3, N'Nón');
 insert into PhanLoai (maPhanLoai, tenPhanLoai) values (4, N'Giày');
 insert into PhanLoai (maPhanLoai, tenPhanLoai) values (5, N'Thắt Lưng');
 go
 SET IDENTITY_INSERT dbo.PhanLoai OFF


GO
 SET IDENTITY_INSERT dbo.KichThuoc ON
go
 --Kích thước--
insert into KichThuoc (maKichThuoc,tenKichThuoc) values (1,N'S');
insert into KichThuoc (maKichThuoc,tenKichThuoc)values (2,N'M');
insert into KichThuoc (maKichThuoc,tenKichThuoc)values (3,N'L');
insert into KichThuoc (maKichThuoc,tenKichThuoc)values (4,N'XL');
insert into KichThuoc (maKichThuoc,tenKichThuoc)values (5,N'XXL');
go
 SET IDENTITY_INSERT dbo.KichThuoc OFF
GO

 SET IDENTITY_INSERT dbo.MauSac ON
go
--Màu sắc--
insert into MauSac (maMauSac,tenMauSac) values (1,N'Trắng');
insert into MauSac (maMauSac,tenMauSac) values (2,N'Đen');
insert into MauSac (maMauSac,tenMauSac) values (3,N'Xanh');
insert into MauSac (maMauSac,tenMauSac) values (4,N'Nâu');
insert into MauSac (maMauSac,tenMauSac) values (5,N'Vàng');
insert into MauSac (maMauSac,tenMauSac) values (6,N'Bạch kim');
insert into MauSac (maMauSac,tenMauSac) values (7,N'Xanh Lam');
insert into MauSac (maMauSac,tenMauSac) values (8,N'Hồng');
insert into MauSac (maMauSac,tenMauSac) values (9,N'Nâu');
insert into MauSac (maMauSac,tenMauSac) values (10,N'Đỏ');
go
 SET IDENTITY_INSERT dbo.MauSac OFF
GO

 SET IDENTITY_INSERT dbo.ChatLieu ON
go
--Chất liệu---
insert into ChatLieu (maChatLieu, tenChatLieu) values  (1,N'Cotton');
insert into ChatLieu (maChatLieu, tenChatLieu) values (2,N'Vải gió dù');
insert into ChatLieu (maChatLieu, tenChatLieu) values (3,N'Vải Len');
insert into ChatLieu (maChatLieu, tenChatLieu) values (4,N'Vải Viscose');
insert into ChatLieu (maChatLieu, tenChatLieu) values (5,N'Vải Nỉ');
insert into ChatLieu (maChatLieu, tenChatLieu) values (6,N'Da');
go
 SET IDENTITY_INSERT dbo.ChatLieu OFF
GO

 SET IDENTITY_INSERT dbo.KhachHang ON
go
--Khách hàng--
insert into KhachHang (maKH, hoTen, sdt, email, gioiTinh) values (1, N'Nguyễn Văn Anh', '0367494954', 'zkyeu1232@gmail.com','Nam');
insert into KhachHang (maKH, hoTen, sdt, email, gioiTinh) values (2, N'Bùi Thị Linh', '0775214548', 'phansinh0402@gmail.com', N'Nữ');
insert into KhachHang (maKH, hoTen, sdt, email, gioiTinh) values (3, N'Trần Văn Tài', '0967494204', 'sinhphan2003@gmail.com','Nam');
insert into KhachHang (maKH, hoTen, sdt, email, gioiTinh) values (4, N'Trần Thị Cẩm Sương', '0917458632', 'thicamsuong@gmail.com',N'Nữ');
insert into KhachHang (maKH, hoTen, sdt, email, gioiTinh) values (5, N'Bách Ngọc Luân', '0367852142', 'ngocluan@gmail.com','Nam');
insert into KhachHang (maKH, hoTen, sdt, email, gioiTinh) values (6, N'Đinh Thế Tài', '0364618255', 'thetai02@gmail.com','Nam');
insert into KhachHang (maKH, hoTen, sdt, email, gioiTinh) values (7, N'Phan Thị Ngọc Châu', '0969452361', 'phanchau@gmail.com',N'Nữ');
insert into KhachHang (maKH, hoTen, sdt, email, gioiTinh) values (8, N'Nguyễn Hữu Tín', '0721365451', 'huutin11@gmail.com','Nam');
insert into KhachHang (maKH, hoTen, sdt, email, gioiTinh) values (9, N'Trần Phương', '0965124897', 'phuong03@gmail.com',N'Nữ');
insert into KhachHang (maKH, hoTen, sdt, email, gioiTinh) values (10, N'Nguyễn Cẩm Tú', '0362198524', 'camthu66@gmail.com',N'Nữ');
go
 SET IDENTITY_INSERT dbo.KhachHang OFF
GO

 SET IDENTITY_INSERT dbo.NhaCungCap ON
go
--Nhà cung cấp
insert into NhaCungCap (maNCC, tenNCC, diaChi, sdt,email) values (1,N'Công Ty TNHH PARADOX Việt Nam',N'995 Nguyễn Trãi, Phường 14, Quận 5, TP. Hồ Chí Minh','0344800808','info@paradoxworldwide.com');
insert into NhaCungCap (maNCC, tenNCC, diaChi, sdt,email) values (2,N'Công Ty TNHH Tm & May Mặc Thời Trang S.A',N'Hương Giang, Cư xá Bắc Hải, 10, Thành phố Hồ Chí Minh','0783970874','yatuan99@gmail.com');
insert into NhaCungCap (maNCC, tenNCC, diaChi, sdt,email) values (3,N'Công ty TNHH May áo khoác Ann',N'68 đường C2, P 13, Q Tân Bình, TP HCM','0918567109','tranyenngoc9x@gmail.com');
insert into NhaCungCap (maNCC, tenNCC, diaChi, sdt,email) values (4,N'Công ty TNHH May Thêu Giày An Phước',N'100/11-12 An Dương Vương, P.9, Q.5, Tp.Hồ Chí Minh','0383835005','maydodongphuc@anphuoc.com.vn');
insert into NhaCungCap (maNCC, tenNCC, diaChi, sdt,email) values (5,N'Công ty TNHH May Dony',N'142/4 Bàu Cát 2, Phường 12, Quận Tân Bình, TP.HCM','0938842123','TrangphucDony@gmail.com');
insert into NhaCungCap (maNCC, tenNCC, diaChi, sdt,email) values (6,N'Công Ty TNHH HADES',N'45 Phan Chu Trinh, P. Bến Thành, Quận 1, TP. Hồ Chí Minh','0367301102','support@hades.vn');
go
 SET IDENTITY_INSERT dbo.NhaCungCap OFF
GO

 SET IDENTITY_INSERT dbo.NhanVien ON
go
--Nhân Viên
insert into NhanVien (maNV, hoTen, chuVu, email, sdt, diaChi, gioiTinh, trangThai) values (1,N'Lê Tấn Phát',N'Quản Lý','phat172003@gmail.com','0925365999',N'Long An', N'Nam',1);
insert into NhanVien (maNV, hoTen, chuVu, email, sdt, diaChi, gioiTinh, trangThai) values (2,N'Phan Tiên Sinh',N'Nhân Viên','zatos1232@gmail.com','0367494904',N'Phú Yên', N'Nam',1);
insert into NhanVien (maNV, hoTen, chuVu, email, sdt, diaChi, gioiTinh, trangThai) values (3,N'Ngô Văn Toàn',N'Nhân Viên','ngovantoan0986@gmail.com','0364581248',N'Phú Yên', N'Nam',1);
go
 SET IDENTITY_INSERT dbo.NhanVien OFF
GO
-- Tài khoản
insert into TaiKhoan (tenTaiKhoan, matKhau, phanQuyen, maNV, trangThai) values ('Admin','admin', N'Quản Lý',1,1);
insert into TaiKhoan (tenTaiKhoan, matKhau, phanQuyen, maNV, trangThai) values ('NV002','1111', N'Nhân Viên',2,1);
insert into TaiKhoan (tenTaiKhoan, matKhau, phanQuyen, maNV, trangThai) values ('NV003','1111', N'Nhân Viên',3,1);
go

 SET IDENTITY_INSERT dbo.SanPham ON
go
--Sản phẩm
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap)
values (1,N'Áo Hoodie Unisex',45,165000,160000,'2023-10-15','SP0001.jpg',5,2,1,1,6);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (2,N'Nón xoè teelab', 30, 150000,120000,'2023-10-16','SP0002.jpg',2,5,4,3,1);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (3,N'Giày thời trang nike',70,600000,550000,'2023-10-16','SP0003.jpg',6,4,3,4,4);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (4,N'Áo Thun teelab TS173',30,225000,205000,'2023-10-16','SP0004.jpg',1,2,1,1,6);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (5,N'Thắt lưng nam da cá sấu',30,300000,250000,'2023-10-25','SP0005.jpg',6,4,3,5,1);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (6,N'Quần jean đen rách gối',40,225000,215000,'2023-10-26','SP0006.jpg',6,4,2,2,1);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (7,N'Áo khoác gió chống nước Unisex',35,350000,250000,'2023-10-26','SP0007.jpg',2,4,3,1,2);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (8,N'Giày thể thao vải',45,500000,450000,'2023-10-26','SP0008.jpg',6,5,4,4,4);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (9,N'Áo khoác Kaki Nam ',25,200000,170000,'2023-10-27','SP0009.jpg',1,5,2,1,3);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (10,N'Nón Lưỡi chai nike',50,70000,65000,'2023-10-27','SP0010.jpg',2,4,3,3,2);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (11,N'Thắt lưng vải dù',30,65000,60000,'2023-10-29','SP0011.jpg',3,3,2,5,1);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (12,N'Quần short nam lưng thun',50,95000,93000,'2023-10-29','SP0012.jpg',5,3,7,2,5);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (13,N'Á Thun Cotton K1',70,130000,140000,'2023-11-03','SP0013.jpg',1,3,2,1,6);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (14,N'Quần Jean Nam trơn cá tính',15,220000,210000,'2023-11-03','SP0014.jpg',6,2,4,2,5);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (15,N'Áo sơ mi nam dài tay',50,200000,190000,'2023-11-04','SP0015.jpg',4,2,2,1,1);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (16,N'Áo sweater Nam',25,210000,180000,'2023-11-04','SP0016.jpg',5,1,1,1,6);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (17,N'Áo khoác Kaki Nam',25,200000,170000,'2023-11-12','SP0017.jpg',1,5,5,1,3);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (18,N'Áo polo thời trang Nam',15,185000,180000,'2023-11-12','SP0018.jpg',1,1,1,1,2);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (19,N'Quần thun Jean cá tính',15,150000,145000,'2023-11-12','SP0019.jpg',6,2,2,2,6);
insert into dbo.sanPham (maSP,tenSP,soLuong,giaBan,giaNhap, ngayNhap, hinhAnh, maChatLieu, maKichThuoc, maMauSac, maPhanLoai, maNhaCungCap) values (20,N'Quần thun Jean cá tính',20,150000,145000,'2023-12-13','SP0020.jpg',6,3,3,2,6);
go
 SET IDENTITY_INSERT dbo.SanPham OFF
GO

go

 SET IDENTITY_INSERT dbo.HoaDon ON
go
----Them gia tri vao table hoa don
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (1,1,1,'2023-10-19');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (2,2,2,'2023-10-26');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (3,3,3,'2023-10-30');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (4,4,1,'2023-10-30');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (5,5,1,'2023-10-31');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (6,6,3,'2023-11-01');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (7,7,1,'2023-11-03');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (8,8,3,'2023-11-03');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (9,9,1,'2023-11-04');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (10,2,1,'2023-11-04');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (13,1,1,'2024-01-04');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (14,4,1,'2024-02-04');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (15,5,2,'2024-03-04');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (16,8,3,'2024-05-04');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (17,9,2,'2024-07-04');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (18,2,2,'2024-04-04');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (19,3,1,'2024-10-04');
insert into HoaDon (maHD, maKH, maNV,ngayNhap) values (20,10,2,'2024-12-04');

go
 SET IDENTITY_INSERT dbo.HoaDon OFF
GO

----Them gia tri vao table chi tiet hoa don
insert into CTHD (maHD, maSP, soLuong) values (1,4,2);
    insert into CTHD(maHD, maSP, soLuong) values (1,2,2);
    insert into CTHD(maHD, maSP, soLuong) values (1,3,2);

    insert into CTHD(maHD, maSP, soLuong) values (2,5,2);
    insert into CTHD(maHD, maSP, soLuong) values (2,4,1);

    insert into CTHD(maHD, maSP, soLuong) values (3,8,1);			 
    insert into CTHD(maHD, maSP, soLuong) values (3,12,2);
    insert into CTHD(maHD, maSP, soLuong) values (3,1,1);
    insert into CTHD(maHD, maSP, soLuong) values (3,3,1);

    insert into CTHD(maHD, maSP, soLuong) values (4,7,1);
    insert into CTHD(maHD, maSP, soLuong) values (4,6,2);

    insert into CTHD(maHD, maSP, soLuong) values (5,10,1);
    insert into CTHD(maHD, maSP, soLuong) values (5,11,1);

    insert into CTHD(maHD, maSP, soLuong) values (6,11,1);
    insert into CTHD(maHD, maSP, soLuong) values (6,2,2);

    insert into CTHD(maHD, maSP, soLuong) values (7,10,1);
    insert into CTHD(maHD, maSP, soLuong) values (7,13,2);

    insert into CTHD(maHD, maSP, soLuong) values (8,4,3);
    insert into CTHD(maHD, maSP, soLuong) values (8,12,1);

    insert into CTHD(maHD, maSP, soLuong) values (9,9,1);
    insert into CTHD(maHD, maSP, soLuong) values (9,7,1);

    insert into CTHD(maHD, maSP, soLuong) values (10,4,2);
    insert into CTHD(maHD, maSP, soLuong) values (10,6,1);
    
    insert into CTHD(maHD, maSP, soLuong)  values (11,6,1);

	insert into CTHD(maHD, maSP, soLuong)  values (13, 1, 3), (13, 2, 4);
	insert into CTHD(maHD, maSP, soLuong)  values (14, 19, 2), (14, 12, 3);
	insert into CTHD(maHD, maSP, soLuong)  values (15,16,3), (15,8,2);
	insert into CTHD(maHD, maSP, soLuong)  values (16,6,1);
	insert into CTHD(maHD, maSP, soLuong)  values (17,10,1), (17,9,1);
	insert into CTHD(maHD, maSP, soLuong)  values (18,6,2);
	insert into CTHD(maHD, maSP, soLuong)  values (19,13,1);
	insert into CTHD(maHD, maSP, soLuong)  values (20,7,2);
	
go


-- Mot so truy van

Select * from NhanVien where maNV like '%%' and hoTen like '%%' and sdt like '%%' and email like '%%' and chucVu like '%%' and diaChi like '%%' and trangThai = 0

--Lấy sản phẩm theo các tiêu chí
select * from SanPham sp JOIN PhanLoai pl ON sp.maPhanLoai = pl.maPhanLoai 
						 JOIN MauSac ms ON sp.maMauSac = ms.maMauSac
						 JOIN KichThuoc kt ON sp.maKichThuoc = kt.maKichThuoc
where pl.maPhanLoai like '%%' and ms.maMauSac like '%%'	and kt.maKichThuoc like '%%' and sp.soLuong = 0		 

--Lấy sản phẩm quần áo
select * from SanPham sp JOIN PhanLoai pl ON sp.maPhanLoai = pl.maPhanLoai 
where pl.maPhanLoai like '%PL0001%' or pl.maPhanLoai like '%PL0002%'

--Lấy sản phẩm phụ kiện
select * from SanPham sp JOIN PhanLoai pl ON sp.maPhanLoai = pl.maPhanLoai 
where pl.maPhanLoai like '%PL0003%' or pl.maPhanLoai like '%PL0004%' or pl.maPhanLoai like '%PL0005%'

--Lấy top 5 sản phẩm bán chạy
select top 5 sp.maSP, SUM(cthd.soLuong) as tongSoLuongBan from SanPham sp 
                                     JOIN CTHD cthd ON sp.maSP = cthd.maSP
                                      JOIN HoaDon hd ON cthd.maHD = hd.maHD
									  group by sp.maSP
                    order by tongSoLuongBan asc
--where sp.maSP = 'SP0004'
                    
--Lấy hóa đơn theo mã
select * from CTHD cthd  JOIN SanPham sp ON cthd.maSP = sp.maSP
where cthd.maHD = 'HD0001'

--Tính thành tiền của từng sản phẩm ứng với mỗi hóa đơn
select sp.maSP, sp.tenSP, cthd.soLuong, cthd.soLuong*sp.giaBan as thanhTien from CTHD cthd  JOIN SanPham sp ON cthd.maSP = sp.maSP
where cthd.maHD = 'HD0003' and cthd.maSP = 'SP0001'

--Tính tổng tiền của hóa đơn 
select cthd.maSP,sp.tenSP,hd.maHD, SUM( cthd.soLuong*sp.giaBan) as tongTien from HoaDon hd 
				JOIN  CTHD cthd ON hd.maHD = cthd.maHD 
				JOIN SanPham sp ON cthd.maSP = sp.maSP
where hd.maHD = 'HD0001'
group by hd.maHD,cthd.maSP,sp.tenSP

--lấy danh sách hóa đơn từ ngày này đến ngày kia
Select * from HoaDon where ngayLap >='04-04-2023' and ngayLap <= '05-04-2023'

select * from PhanLoai

SELECT COUNT(CASE WHEN pl.tenPhanLoai = N'Áo' THEN 1 END) AS SoLuongAo, 
                    COUNT(CASE WHEN pl.tenPhanLoai = N'Quần' THEN 1 END) AS SoLuongQuan, 
                    COUNT(CASE WHEN pl.tenPhanLoai = N'Nón' THEN 1 END) AS SoLuongNon, 
					COUNT(CASE WHEN pl.tenPhanLoai = N'Giày' THEN 1 END) AS SoLuongGiay, 
					COUNT(CASE WHEN pl.tenPhanLoai = N'Thắt Lưng' THEN 1 END) AS SoLuongThatLung
                    FROM SanPham sp JOIN PhanLoai pl ON sp.maPhanLoai = pl.maPhanLoai 
					group by pl.maPhanLoai

SELECT maPhanLoai, COUNT(*) AS SoLuongSanPham
FROM SanPham
GROUP BY maPhanLoai
ORDER BY maPhanLoai;

select  sum(cthd.soLuong) as soLuongBanDuoc from HoaDon hd join CTHD cthd on hd.maHD=cthd.maHD
					join SanPham sp on cthd.maSP=sp.maSP
						join PhanLoai pl on pl.maPhanLoai=sp.maPhanLoai 
						join KichThuoc kc on kc.maKichThuoc=sp.maKichThuoc
						join MauSac ms on ms.maMauSac=sp.maMauSac
						join ChatLieu cl on cl.maChatLieu=sp.maChatLieu
						join NhaCungCap ncc on ncc.maNCC=sp.maNhaCungCap
	where cthd.maSP= 'SP0004'
	group by cthd.maSP

select sp.maSP from HoaDon hd join CTHD cthd on hd.maHD=cthd.maHD
                   				join SanPham sp on cthd.maSP=sp.maSP                    			
               		group by sp.maSP

select cthd.maSP from HoaDon hd join CTHD cthd on hd.maHD=cthd.maHD
                   						join SanPham sp on cthd.maSP=sp.maSP
                   						join PhanLoai pl on pl.maPhanLoai=sp.maPhanLoai 
                   						join KichThuoc kt on kt.maKichThuoc=sp.maKichThuoc
                    					join MauSac ms on ms.maMauSac=sp.maMauSac
                    					join ChatLieu cl on cl.maChatLieu=sp.maChatLieu
                    					join NhaCungCap ncc on ncc.maNCC=sp.maNhaCungCap
      	where ms.tenMauSac like '%%' and kt.tenKichThuoc like '%%' and pl.tenPhanLoai like '%%' and hd.ngayLap >= '2023-9-10' and  hd.ngayLap <= '2023-10-15'
		group by cthd.maSP;
-- Lấy doanh thu theo tháng
select  MONTH(hd.ngayLap) as thang, sum(sp.giaBan*cthd.soLuong) as doanhThu from HoaDon hd join CTHD cthd on hd.maHD=cthd.maHD
                   						join SanPham sp on cthd.maSP=sp.maSP
		group by MONTH(hd.ngayLap);
-- Lấy doanh thu theo tháng
select  sum(sp.giaBan*cthd.soLuong) as doanhThu from HoaDon hd join CTHD cthd on hd.maHD=cthd.maHD
                   						join SanPham sp on cthd.maSP=sp.maSP
		
--Lấy dữ liệu doanh thu theo tháng
-- Dùng With để tạo ra bảng dữ liệu tạm thời, dùng UNION để lưu dữ liệu tạm thời
WITH AllMonths AS (
    SELECT 1 AS thang
    UNION SELECT 2
    UNION SELECT 3
    UNION SELECT 4
    UNION SELECT 5
    UNION SELECT 6
    UNION SELECT 7
    UNION SELECT 8
    UNION SELECT 9
    UNION SELECT 10
    UNION SELECT 11
    UNION SELECT 12
)
SELECT allM.thang, SUM(sp.giaBan*cthd.soLuong) AS doanhThu
FROM AllMonths allM
LEFT JOIN HoaDon hd ON MONTH(hd.ngayNhap) = allM.thang AND YEAR(hd.ngayNhap) = 2024
LEFT JOIN CTHD cthd ON hd.maHD = cthd.maHD
LEFT JOIN SanPham sp ON cthd.maSP = sp.maSP
GROUP BY allM.thang


select * from TaiKhoan tk join NhanVien nv on tk.maNV = nv.maNV
where nv.email = 'zatos1232@gmail.com' or nv.sdt = ''

select * from TaiKhoan where maNV = 'NV002'

select top 5 hd.maKH,kh.hoTen,kh.sdt,SUM(cthd.soLuong) as SoLuongKHDaMua,SUM(cthd.soLuong*sp.giaBan) as thanhTien from HoaDon hd join CTHD cthd on hd.maHD=cthd.maHD 
                  														join KhachHang kh on kh.maKH=hd.maKH
                   																join SanPham sp on sp.maSP=cthd.maSP
                  						group by hd.maKH,kh.hoTen,kh.sdt
                 						order by SUM(cthd.soLuong) asc

select maKH , COUNT(*) as tongHD from HoaDon
where maKH = 1
group by maKH

select SUM(ct.soLuong*sp.giaBan) as thanhTien from HoaDon hd JOIN CTHD ct ON hd.maHD = ct.maHD
						JOIN SanPham sp ON ct.maSP = sp.maSP
						join PhanLoai pl on pl.maPhanLoai=sp.maPhanLoai 
                   		join KichThuoc kt on kt.maKichThuoc=sp.maKichThuoc
                    	join MauSac ms on ms.maMauSac=sp.maMauSac
                    	join ChatLieu cl on cl.maChatLieu=sp.maChatLieu
                    	join NhaCungCap ncc on ncc.maNCC=sp.maNhaCungCap
						where MONTH(hd.ngayLap) like '10' 
	

select sp.maSP, sp.tenSP, SUM(ct.soLuong*sp.giaBan) as thanhTien from HoaDon hd JOIN CTHD ct ON hd.maHD = ct.maHD
						JOIN SanPham sp ON ct.maSP = sp.maSP
						join PhanLoai pl on pl.maPhanLoai=sp.maPhanLoai 
                   		join KichThuoc kt on kt.maKichThuoc=sp.maKichThuoc
                    	join MauSac ms on ms.maMauSac=sp.maMauSac
                    	join ChatLieu cl on cl.maChatLieu=sp.maChatLieu
                    	join NhaCungCap ncc on ncc.maNCC=sp.maNhaCungCap
						where MONTH(hd.ngayNhap) like '%10%' and YEAR(hd.ngayNhap) like '%%'
						group by sp.maSP, sp.tenSP

select sum(cthd.soLuong*sp.giaBan) as doanhThu from HoaDon hd join CTHD cthd on hd.maHD=cthd.maHD
                					join SanPham sp on cthd.maSP=sp.maSP
                	where sp.maSP= 1 and MONTH(hd.ngayNhap) like '%10%' and YEAR(hd.ngayNhap) like '%%'
                			group by sp.maSP

Select * from PhieuDatHang where maKH = 'KH0006'

							select * from PhieuDatHang


select top 1 * from HoaDon
order by maHD desc

select * from SanPham
select * from HoaDon
select * from CTHD	

UPDATE SanPham SET soLuong = soLuong - 2 WHERE maSP = 9

select kh.* from KhachHang kh where kh.email = 'a@gmail.com' or kh.sdt = ''

ALTER TABLE KhachHang
ALTER COLUMN sdt nvarchar(100)  NULL;


-- xử lý dữ liệu trong database-----
select * from SanPham
select * from KhachHang
select * from NhanVien
select * from NhaCungCap
select * from KichThuoc
select * from MauSac
select * from PhanLoai
select * from ChatLieu
select * from HoaDon
select * from CTHD
select * from PhieuDatHang
select * from CTPhieuDatHang
select * from TaiKhoan
select sp.maSP from HoaDon hd join CTHD cthd on hd.maHD=cthd.maHD
              					join SanPham sp on cthd.maSP=sp.maSP
                					join PhanLoai pl on pl.maPhanLoai=sp.maPhanLoai 
               						join KichThuoc kt on kt.maKichThuoc=sp.maKichThuoc
              						join MauSac ms on ms.maMauSac=sp.maMauSac
               					join ChatLieu cl on cl.maChatLieu=sp.maChatLieu
               						join NhaCungCap ncc on ncc.maNCC=sp.maNhaCungCap
                	where MONTH(hd.ngayNhap) like '%10%' and YEAR(hd.ngayNhap) like '%2023%'
                			group by sp.maSP