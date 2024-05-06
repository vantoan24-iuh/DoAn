package fit.iuh.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NhanVien")
public class NhanVien implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maNV")
    private long maNV;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String hoTen;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String chuVu;

    @Column(columnDefinition = "nvarchar(100)", nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "nvarchar(100)", nullable = false, unique = true)
    private String sdt;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String diaChi;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String gioiTinh;

    @Column(columnDefinition = "bit")
    private boolean trangThai;


//    public String auto_ID() {
//        Dao_NhanVien daoNhanVien = new Dao_NhanVien();
//        String idPrefix = daoNhanVien.taoMaNhanVien();
//        return idPrefix;
//    }

    public NhanVien(String hoTen, String chuVu, String email, String sdt, String diaChi, String gioiTinh, boolean trangThai) {
        this.hoTen = hoTen;
        this.chuVu = chuVu;
        this.email = email;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.gioiTinh = gioiTinh;
        this.trangThai = trangThai;
    }

    public void setMaNV(long maNV) {
        this.maNV = maNV;
    }
//    public String getMaNV() {
//        return maNV;
//    }
//
//    public void setMaNV(String maNV) {
//        this.maNV = maNV;
//    }
//
//    public String getHoTen() {
//        return hoTen;
//    }
//
//    public void setHoTen(String hoTen) {
//        this.hoTen = hoTen;
//    }
//
//    public String getChuVu() {
//        return chuVu;
//    }
//
//    public void setChuVu(String chuVu) {
//        this.chuVu = chuVu;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getSdt() {
//        return sdt;
//    }
//
//    public void setSdt(String sdt) {
//        this.sdt = sdt;
//    }
//
//    public String getDiaChi() {
//        return diaChi;
//    }
//
//    public void setDiaChi(String diaChi) {
//        this.diaChi = diaChi;
//    }
//
//    public String getGioiTinh() {
//        return gioiTinh;
//    }
//
//    public void setGioiTinh(String gioiTinh) {
//        this.gioiTinh = gioiTinh;
//    }
//
//    public boolean isTrangThai() {
//        return trangThai;
//    }
//
//    public void setTrangThai(boolean trangThai) {
//        this.trangThai = trangThai;
//    }


}
