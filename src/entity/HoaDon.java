package entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HoaDon {

    Integer idHoaDon;
    Integer idNhanVien;
    Integer idKhachHang;
    String trangThai;
    String ngayMua;
    Integer tien;

    public HoaDon() {
    }

    public HoaDon(Integer idHoaDon, Integer idNhanVien, Integer idKhachHang, String trangThai, String ngayMua, Integer tien) {
        this.idHoaDon = idHoaDon;
        this.idNhanVien = idNhanVien;
        this.idKhachHang = idKhachHang;
        this.trangThai = trangThai;
        this.ngayMua = ngayMua;
        this.tien = tien;
    }

    public Integer getIdHoaDon() {
        return idHoaDon;
    }

    public void setIdHoaDon(Integer idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public Integer getIdNhanVien() {
        return idNhanVien;
    }

    public void setIdNhanVien(Integer idNhanVien) {
        this.idNhanVien = idNhanVien;
    }

    public Integer getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(Integer idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(String ngayMua) {
        this.ngayMua = ngayMua;
    }

    public Integer getTien() {
        return tien;
    }

    public void setTien(Integer tien) {
        this.tien = tien;
    }

    @Override
    public String toString() {
        return "HoaDon{" + "idHoaDon=" + idHoaDon + ", idNhanVien=" + idNhanVien + ", idKhachHang=" + idKhachHang + ", trangThai=" + trangThai + ", ngayMua=" + ngayMua + ", tien=" + tien + '}';
    }

}
