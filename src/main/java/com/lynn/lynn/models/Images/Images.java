package com.lynn.lynn.models.Images;

import com.lynn.lynn.models.Category.Category;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "images")
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String path;

    @ManyToOne
    private Category category;

    public Images() {}

    public Images(String aPath) {
        this.path = aPath;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
