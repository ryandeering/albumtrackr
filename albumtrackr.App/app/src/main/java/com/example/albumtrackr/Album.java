package com.example.albumtrackr;

// MODEL CLASS - NAME WILL BE CHANGED
public class Album
{
    private Integer id;
    private String artist;
    private String name;
    private String thumbnail;


    public Album(Integer id, String artist, String name, String thumbnail) {
        this.id = id;
        this.artist = artist;
        this.name = name;
        this.thumbnail = thumbnail;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }



    public String toString()
    {
        return artist + name;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
