package com.example.albumtrackr;

public class Star {

    private int id;
    private String username;
    private int AlbumListId;

    public Star(int id, String username, int AlbumListId) {
        this.id = id;
        this.username = username;
        this.AlbumListId = AlbumListId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAlbumListId() {
        return AlbumListId;
    }

    public void setAlbumListId(int albumListId) {
        AlbumListId = albumListId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
