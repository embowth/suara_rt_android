package rw.rt.com.mykomplek;

/**
 * Created by embowth on 09/02/2018.
 */

public class ItemListKandidat {

    private int id;
    private String url;
    private String nama;
    private String visi;
    private String misi;

    public ItemListKandidat(int id, String url, String nama, String visi, String misi) {
        this.id = id;
        this.url = url;
        this.nama = nama;
        this.visi = visi;
        this.misi = misi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getVisi() {
        return visi;
    }

    public void setVisi(String visi) {
        this.visi = visi;
    }

    public String getMisi() {
        return misi;
    }

    public void setMisi(String misi) {
        this.misi = misi;
    }
}
