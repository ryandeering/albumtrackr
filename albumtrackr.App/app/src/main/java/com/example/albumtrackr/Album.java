package com.example.albumtrackr;

// MODEL CLASS - NAME WILL BE CHANGED
public class Album
{
    private Integer id;
    private String artist;
    private String name;

    public Album(Integer id, String artist, String name, String thumbnail) {
        this.id = id;
        this.artist = artist;
        this.name = name;
        this.thumbnail = thumbnail;
        lists = new Object(); //temp
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

    public Object getLists() {
        return lists;
    }

    public void setLists(Object lists) {
        this.lists = lists;
    }

    private String thumbnail;
    private Object lists;







    public String toString()
    {
        return "The artist album and name should be: Grimes' Halfaxa." + "RESPONSE: " + artist + name;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
