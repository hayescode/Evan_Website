package com.lynn.lynn.models.Images;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "images")
public class Images {

    @Id
    @GeneratedValue
    private int id;
    private String path;
    private String title;
    private String caption;

    public Images() {}

    public Images(String aPath, String aTitle, String aCaption) {
        this.path = aPath;
        this.title = aTitle;
        this.caption = aCaption;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
