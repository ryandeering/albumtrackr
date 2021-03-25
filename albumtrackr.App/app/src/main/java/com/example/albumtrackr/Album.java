package com.example.albumtrackr;

// MODEL CLASS - NAME WILL BE CHANGED
public class Album
{
    private Integer id;
    private String artist;
    private String name;
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
