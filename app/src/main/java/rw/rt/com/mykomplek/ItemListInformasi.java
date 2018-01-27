package rw.rt.com.mykomplek;

import android.widget.TextView;

/**
 * Created by embowth on 25/01/2018.
 */

public class ItemListInformasi {

    private int id;
    private String judul;
    private String sender;
    private String post;

    //constructor

    public ItemListInformasi(int id, String judul, String sender, String post) {
        this.id = id;
        this.judul = judul;
        this.sender = sender;
        this.post = post;
    }

    //getter setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
