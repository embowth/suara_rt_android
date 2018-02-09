package rw.rt.com.mykomplek;

/**
 * Created by embowth on 09/02/2018.
 */

public class ItemTotalSuara {

    private int id;
    private String nama;
    private String total_suara;

    public ItemTotalSuara(int id, String nama, String total_suara) {
        this.id = id;
        this.nama = nama;
        this.total_suara = total_suara;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTotal_suara() {
        return total_suara;
    }

    public void setTotal_suara(String total_suara) {
        this.total_suara = total_suara;
    }
}
