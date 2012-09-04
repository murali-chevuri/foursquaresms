package com.apps.foursquare.sms.server;

import javax.jdo.annotations.Persistent;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Blob;

@Entity
public class MyImage {
	@Id
    @Persistent
    private String name;

    @Persistent
    Blob image;

    public MyImage() { }
    public MyImage(String name, Blob image) {
        this.name = name; 
        this.image = image;
    }

    // JPA getters and setters and empty contructor
    // ...
    public String getName() { return name;}
    public void setName(String name) { this.name = name;}
    public Blob getImage()              { return image; }
    public void setImage(Blob image)    { this.image = image; }
}