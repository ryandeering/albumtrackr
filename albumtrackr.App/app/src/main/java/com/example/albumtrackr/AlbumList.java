package com.example.albumtrackr;

import java.util.ArrayList;
import java.util.List;

public class AlbumList {

    private Integer id;
    private List<Album> albums;
    private String created;
    private String username;
    private String name;
    private String description;
    private Integer stars;

    public AlbumList(Integer id, String username, String name, String description, String created) {
        this.id = id;
        this.albums = new ArrayList<Album>();
        this.created = created;
        this.username = username;
        this.name = name;
        this.description = description;
        this.stars = 0;
    }

    public AlbumList(){

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

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }




}
