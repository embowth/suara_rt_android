package rw.rt.com.mykomplek;

/**
 * Created by embowth on 01/02/2018.
 */

public class ItemListDataLayanan {

    private int id;
    private String nama_lengkap;
    private String nomor_ktp;
    private String tanggal;
    private String status;

    public ItemListDataLayanan(int id, String nama_lengkap, String nomor_ktp, String tanggal, String status) {
        this.id = id;
        this.nama_lengkap = nama_lengkap;
        this.nomor_ktp = nomor_ktp;
        this.tanggal = tanggal;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getNomor_ktp() {
        return nomor_ktp;
    }

    public void setNomor_ktp(String nomor_ktp) {
        this.nomor_ktp = nomor_ktp;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
