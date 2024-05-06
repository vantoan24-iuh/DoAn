package fit.iuh.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NhaCungCap")
@NamedQueries(value = {
        @NamedQuery(name = "NhaCungCap.getAll", query = "SELECT ncc FROM NhaCungCap ncc"),
        @NamedQuery(name = "NhaCungCap.findNhaCungCap", query = "SELECT ncc FROM NhaCungCap ncc WHERE (:maNVCheck = '' OR ncc.maNCC = :maNVParam) and  ncc.tenNCC like :tenNCC and ncc.sdt like :sdt and ncc.email like :email"),
        @NamedQuery(name = "NhaCungCap.findTenNhaCungCap", query = "SELECT ncc FROM NhaCungCap ncc WHERE ncc.tenNCC = :tenNCC"),
        @NamedQuery(name = "NhaCungCap.findNhaCungCapTheoMa", query = "SELECT ncc FROM NhaCungCap ncc WHERE ncc.maNCC = :maNCC"),

})
public class NhaCungCap implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long maNCC;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String tenNCC;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String diaChi;

    @Column(columnDefinition = "nvarchar(100)", name = "sdt", nullable = false)
    private String sdt;

    @Column(name = "email", columnDefinition = "nvarchar(100)", nullable = false)
    private String email;

    public NhaCungCap(String tenNCC, String diaChi, String sdt, String email) {
        this.tenNCC = tenNCC;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.email = email;
    }

//    private String auto_ID(){
//        Dao_NhaCungCap daoNhaCungCap = new Dao_NhaCungCap();
//        String idPrefix = daoNhaCungCap.taoMaNhaCungCap();
//
//        return idPrefix;
//    }
//
//    public NhaCungCap(String tenNhaCungCap, String diaChi, String sdt, String email) {
//        this.maNCC = auto_ID();
//        this.tenNCC = tenNhaCungCap;
//        this.diaChi = diaChi;
//        this.sdt = sdt;
//        this.email = email;
//    }
//
//    public NhaCungCap() {
//    }
//    public NhaCungCap(String maNC) {
//              this.maNCC = maNCC;
//
//    }
//    public NhaCungCap(String maNCC, String tenNCC, String diaChi, String sdt, String email) {
//        this.maNCC = maNCC;
//        this.tenNCC = tenNCC;
//        this.diaChi = diaChi;
//        this.sdt = sdt;
//        this.email = email;
//    }
//
//    public String getMaNCC() {
//        return maNCC;
//    }
//
//    public void setMaNCC(String maNCC) {
//        this.maNCC = maNCC;
//    }
//
//    public String getTenNCC() {
//        return tenNCC;
//    }
//
//    public void setTenNCC(String tenNCC) {
//        this.tenNCC = tenNCC;
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
//    public String getSdt() {
//        return sdt;
//    }
//
//    public void setSdt(String sdt) {
//        this.sdt = sdt;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
}
