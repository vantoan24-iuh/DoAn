package fit.iuh.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TaiKhoan")
@NamedQueries(value = {
        @NamedQuery(name = "TaiKhoan.getAll", query = "SELECT tk FROM TaiKhoan tk"),
        @NamedQuery(name = "TaiKhoan.getTrangThai", query = "SELECT tk FROM TaiKhoan tk where tk.trangThai = true"),
        @NamedQuery(name = "TaiKhoan.kiemTraTaiKhoan", query = "SELECT tk FROM TaiKhoan tk where tk.tenTaiKhoan = :tenTaiKhoan and tk.matKhau = :matKhau and tk.trangThai = true"),
        @NamedQuery(name = "TaiKhoan.getMatKhau", query = "SELECT tk.matKhau FROM TaiKhoan tk where tk.tenTaiKhoan = :ten"),
        @NamedQuery(name = "TaiKhoan.getTaiKhoanByMaNV", query = "SELECT tk FROM TaiKhoan tk where tk.nhanVien.maNV = :maNV"),
        @NamedQuery(name = "TaiKhoan.updateMatKhau", query = "UPDATE TaiKhoan tk SET tk.matKhau = :matKhau where tk.nhanVien.maNV = :maNV")

})
public class TaiKhoan implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "nvarchar(100)")
    private String tenTaiKhoan;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String matKhau;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String phanQuyen;

    @Id
    @OneToOne
    @JoinColumn(name = "maNV")
    private NhanVien nhanVien;

    @Column(nullable = false, columnDefinition = "bit")
    private boolean trangThai;

//    public TaiKhoan() {
//        // TODO Auto-generated constructor stub
//    }
//
//    public TaiKhoan(String tenTaiKhoan, String matKhau, String phanQuyen, NhanVien nhanVien, boolean trangThai) {
//        this.tenTaiKhoan = tenTaiKhoan;
//        this.matKhau = matKhau;
//        this.phanQuyen = phanQuyen;
//        this.nhanVien = nhanVien;
//        this.trangThai = trangThai;
//
//    }
//
//    public String getTenTaiKhoan() {
//        return tenTaiKhoan;
//    }
//
//    public void setTenTaiKhoan(String tenTaiKhoan) {
//        this.tenTaiKhoan = tenTaiKhoan;
//    }
//
//    public String getMatKhau() {
//        return matKhau;
//    }
//
//    public void setMatKhau(String matKhau) {
//        this.matKhau = matKhau;
//    }
//
//    public String getPhanQuyen() {
//        return phanQuyen;
//    }
//
//    public void setPhanQuyen(String phanQuyen) {
//        this.phanQuyen = phanQuyen;
//    }
//
//    public NhanVien getNhanVien() {
//        return nhanVien;
//    }
//
//    public void setNhanVien(NhanVien nhanVien) {
//        this.nhanVien = nhanVien;
//    }
//
//    public boolean isTrangThai() {
//        return trangThai;
//    }
//
//    public void setTrangThai(boolean trangThai) {
//        this.trangThai = trangThai;
//    }
//
//    @Override
//    public String toString() {
//        return "TaiKhoan{" + "tenTaiKhoan=" + tenTaiKhoan + ", matKhau=" + matKhau + ", phanQuyen=" + phanQuyen + ", nhanVien=" + nhanVien + ", trangThai=" + trangThai + '}';
//    }

}
