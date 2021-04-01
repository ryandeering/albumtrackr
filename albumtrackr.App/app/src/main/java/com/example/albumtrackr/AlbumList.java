package com.example.albumtrackr;

import java.util.ArrayList;
import java.util.List;

public class AlbumList {

    private Integer id;
    private List<Album> albums;
    private List<Star> stars;
    private String created;
    private String username;
    private String name;
    private String description;

    public AlbumList(Integer id, String username, String name, String description, String created, ArrayList<Album> albums, ArrayList<Star> stars) {
        this.id = id;
        this.albums = albums;
        this.created = created;
        this.username = username;
        this.name = name;
        this.description = description;
        this.stars = stars;
    }

    public AlbumList(Integer id, String username, String name, String description, String created) {
        this.id = id;
        this.albums = null;
        this.created = created;
        this.username = username;
        this.name = name;
        this.description = description;
        this.stars = null;
    }

    public AlbumList(){
        id = 0;
        username = "";
        name = "";
        description = "";
        stars = new ArrayList<Star>();
        albums = new ArrayList<Album>();
        }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Star> getStars() {
        return stars;
    }

    public void setStars(List<Star> stars) {
        this.stars = stars;
    }




}
