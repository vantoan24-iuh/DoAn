package fit.iuh.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "SanPham")
@NamedQueries({
        @NamedQuery(name = "getAllSanPham", query = "SELECT sp FROM SanPham sp"),
        @NamedQuery(name = "getAllQuaNAo", query = "SELECT sp FROM SanPham sp JOIN FETCH sp.phanLoai pl where pl.maPhanLoai in (1,2)"),
        @NamedQuery(name = "getAllPhuKien", query = "SELECT sp FROM SanPham sp where sp.phanLoai.maPhanLoai not in (1,2)"),
        @NamedQuery(name = "timKiemQuanAo",
                query = "SELECT sp FROM SanPham sp WHERE sp.phanLoai.maPhanLoai IN (1, 2) AND (:maSPCheck = '' OR sp.maSP = :maSP) AND " +
                        "sp.tenSP LIKE :tenSP AND sp.phanLoai.loaiSanPham LIKE :loaiSanPham AND sp.nhaCungCap.tenNCC LIKE :tenNCC " +
                        "AND sp.mauSac.mauSac LIKE :mauSac AND sp.chatLieu.chatLieu LIKE :chatLieu AND sp.kichThuoc.kichThuoc LIKE :kichThuoc"
        ),
        @NamedQuery(name = "timKiemPhuKien",
                query = "SELECT sp FROM SanPham sp WHERE sp.phanLoai.maPhanLoai NOT IN (1, 2) AND (:maSPCheck = '' OR sp.maSP = :maSP) AND " +
                        "sp.tenSP LIKE :tenSP AND sp.phanLoai.loaiSanPham LIKE :loaiSanPham AND sp.nhaCungCap.tenNCC LIKE :tenNCC " +
                        "AND sp.mauSac.mauSac LIKE :mauSac AND sp.chatLieu.chatLieu LIKE :chatLieu AND sp.kichThuoc.kichThuoc LIKE :kichThuoc"
        ),
//
        @NamedQuery(name = "getSanPhamTheoMa", query = "SELECT sp FROM SanPham sp WHERE sp.maSP = :maSP"),
        @NamedQuery(name = "getAllSanPhamTheoTieuChi", query = "SELECT sp FROM SanPham sp where sp.phanLoai.loaiSanPham LIKE  :maPhanLoai AND sp.mauSac.mauSac LIKE :mauSac AND sp.kichThuoc.kichThuoc LIKE :kichThuoc"),
        @NamedQuery(name = "getAllSanPhamHetHang", query = "SELECT sp FROM SanPham sp where sp.phanLoai.loaiSanPham LIKE  :maPhanLoai AND sp.mauSac.mauSac LIKE :mauSac AND sp.kichThuoc.kichThuoc LIKE :kichThuoc AND sp.soLuong = 0"),
        @NamedQuery(name="getSanPhamBanChay", query = "SELECT sp.maSP,  SUM(cthd.soLuong) FROM SanPham sp JOIN  CTHD cthd ON sp.maSP = cthd.sanPham.maSP GROUP BY sp.maSP ORDER BY SUM(cthd.soLuong)  DESC"),
        @NamedQuery(name="getSanPhamBanCham", query = "SELECT sp.maSP,  SUM(cthd.soLuong) FROM SanPham sp JOIN  CTHD cthd ON sp.maSP = cthd.sanPham.maSP GROUP BY sp.maSP ORDER BY SUM(cthd.soLuong)  asc "),
        @NamedQuery(name="getSoLuongSPTheoMaPL", query = "SELECT sp.phanLoai.maPhanLoai, COUNT(sp) FROM SanPham sp GROUP BY sp.phanLoai.maPhanLoai ORDER BY sp.phanLoai.maPhanLoai"),


        @NamedQuery(name="getAllSanPhamTheoNgay", query = "SELECT sp FROM SanPham sp where sp.ngayNhap >= :ngayNhap AND sp.ngayNhap <= :ngayKetThuc"),

})
public class SanPham implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long maSP;

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String tenSP;
    private int soLuong;
    private double giaNhap;
    private double giaBan;

    @Column(columnDefinition = "date", nullable = false)
    private Date ngayNhap;

    @Column(nullable = false)
    private String hinhAnh;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "maChatLieu")
    private ChatLieu chatLieu;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "maKichThuoc")
    private KichThuoc kichThuoc;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "maMauSac")
    private MauSac mauSac;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "maPhanLoai")
    private PhanLoai phanLoai;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "maNhaCungCap")
    private NhaCungCap nhaCungCap;

//    private String auto_ID(){
//            Dao_SanPham dao_SanPham = new Dao_SanPham();
//            String idPrefix = "SP";
//            int length = dao_SanPham.getAllSanPham().size();
//            String finalId = idPrefix + String.format("%04d", length + 1);
//            return finalId;
//        }
//
//    public SanPham() {
//        // TODO Auto-generated constructor stub
//    }


    public SanPham(String tenSP, int soLuong, double giaBan, double giaNhap, Date ngayNhap, String hinhAnh, ChatLieu chatLieu, KichThuoc kichThuoc, MauSac mauSac, PhanLoai phanLoai, NhaCungCap nhaCungCap) {
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.ngayNhap = ngayNhap;
        this.hinhAnh = hinhAnh;
        this.chatLieu = chatLieu;
        this.kichThuoc = kichThuoc;
        this.mauSac = mauSac;
        this.phanLoai = phanLoai;
        this.nhaCungCap = nhaCungCap;
    }

    public SanPham(int soLuong, PhanLoai phanLoai) {
        this.soLuong = soLuong;
        this.phanLoai = phanLoai;
    }

    public SanPham(long maSP, String tenSP, int soLuong, double giaNhap, double giaBan, Date ngayNhap, String hinhAnh, ChatLieu chatLieu, KichThuoc kichThuoc, MauSac mauSac, PhanLoai phanLoai, NhaCungCap nhaCungCap) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.ngayNhap = ngayNhap;
        this.hinhAnh = hinhAnh;
        this.chatLieu = chatLieu;
        this.kichThuoc = kichThuoc;
        this.mauSac = mauSac;
        this.phanLoai = phanLoai;
        this.nhaCungCap = nhaCungCap;
    }

    public SanPham(long maSP, String tenSP, int soLuong) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
    }

    //    public String getMaSP() {
//        return maSP;
//    }
//
//    public void setMaSP(String maSP) {
//        this.maSP = maSP;
//    }
//
//    public String getTenSP() {
//        return tenSP;
//    }
//
//    public void setTenSP(String tenSP) {
//        this.tenSP = tenSP;
//    }
//
//    public int getSoLuong() {
//        return soLuong;
//    }
//
//    public void setSoLuong(int soLuong) {
//        this.soLuong = soLuong;
//    }
//
//    public double getGiaNhap() {
//        return giaNhap;
//    }
//
//    public void setGiaNhap(double giaNhap) {
//        this.giaNhap = giaNhap;
//    }
//
//    public double getGiaBan() {
//        return giaBan;
//    }
//
//    public void setGiaBan(double giaBan) {
//        this.giaBan = giaBan;
//    }
//
//    public String getHinhAnh() {
//        return hinhAnh;
//    }
//
//    public void setHinhAnh(String hinhAnh) {
//        this.hinhAnh = hinhAnh;
//    }
//
//    public ChatLieu getChatLieu() {
//        return chatLieu;
//    }
//
//    public void setChatLieu(ChatLieu chatLieu) {
//        this.chatLieu = chatLieu;
//    }
//
//    public KichThuoc getKichThuoc() {
//        return kichThuoc;
//    }
//
//    public void setKichThuoc(KichThuoc kichThuoc) {
//        this.kichThuoc = kichThuoc;
//    }
//
//    public MauSac getMauSac() {
//        return mauSac;
//    }
//
//    public void setMauSac(MauSac mauSac) {
//        this.mauSac = mauSac;
//    }
//
//    public PhanLoai getPhanLoai() {
//        return phanLoai;
//    }
//
//    public void setPhanLoai(PhanLoai phanLoai) {
//        this.phanLoai = phanLoai;
//    }
//
//    public NhaCungCap getNhaCungCap() {
//        return nhaCungCap;
//    }
//
//    public void setNhaCungCap(NhaCungCap nhaCungCap) {
//        this.nhaCungCap = nhaCungCap;
//    }
//
//    public Date getNgayNhap() {
//        return ngayNhap;
//    }
//
//    public void setNgayNhap(Date ngayNhap) {
//        this.ngayNhap = ngayNhap;
//    }

    @Override
    public String toString() {
        return "SanPham{" + "maSP=" + maSP + ", tenSP=" + tenSP + ", soLuong=" + soLuong + ", giaNhap=" + giaNhap + ", giaBan=" + giaBan + ", ngayNhap=" + ngayNhap + ", hinhAnh=" + hinhAnh + ", chatLieu=" + chatLieu + ", kichThuoc=" + kichThuoc + ", mauSac=" + mauSac + ", phanLoai=" + phanLoai + ", nhaCungCap=" + nhaCungCap + '}';
    }


}
