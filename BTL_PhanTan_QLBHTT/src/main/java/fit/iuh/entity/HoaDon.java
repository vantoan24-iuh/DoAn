package fit.iuh.entity;


import java.io.Serial;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "HoaDon")
public class HoaDon implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "maHD")
	private long maHoaDon;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "maKH")
	private KhachHang khachHang;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "maNV")
	private NhanVien nhanVien;

	@Column(columnDefinition = "date",nullable = false)
	private Date ngayNhap;
	
//	public HoaDon() {
//		// TODO Auto-generated constructor stub
//	}
//
//        public String auto_ID() {
//            Dao_HoaDon daoHoaDon = new Dao_HoaDon();
//            String idPrefix = daoHoaDon.taoMaHoaDon();
//            return idPrefix;
//        }

        
	public HoaDon(KhachHang khachHang, NhanVien nhanVien, Date ngayNhap) {
		this.khachHang = khachHang;
		this.nhanVien = nhanVien;
		this.ngayNhap = ngayNhap;
	}

//	public String getMaHoaDon() {
//		return maHoaDon;
//	}
//
//	public void setMaHoaDon(String maHoaDon) {
//		this.maHoaDon = maHoaDon;
//	}
//
//	public KhachHang getKhachHang() {
//		return khachHang;
//	}
//
//	public void setKhachHang(KhachHang khachHang) {
//		this.khachHang = khachHang;
//	}
//
//	public NhanVien getNhanVien() {
//		return nhanVien;
//	}
//
//	public void setNhanVien(NhanVien nhanVien) {
//		this.nhanVien = nhanVien;
//	}
//
//	public Date getNgayNhap() {
//		return ngayNhap;
//	}
//
//	public void setNgayNhap(Date ngayNhap) {
//		this.ngayNhap = ngayNhap;
//	}
	
	
}
