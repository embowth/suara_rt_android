package rw.rt.com.mykomplek;

/**
 * Created by embowth on 09/02/2018.
 */

public class ItemPemilihan {

    private int id;
    private String periode;
    private String waktu;

    public ItemPemilihan(int id, String periode, String waktu) {
        this.id = id;
        this.periode = periode;
        this.waktu = waktu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
