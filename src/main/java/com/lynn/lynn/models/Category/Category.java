package com.lynn.lynn.models.Category;

import com.lynn.lynn.models.Images.Images;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue
    private int id;
    private String category;

    @OneToMany
    @JoinColumn(name = "category_id")
    private List<Images> images = new ArrayList<>();

    public Category() {}

    public Category(String aCategory) {
        this.category = aCategory;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Images> getImages() {
        return images;
    }
}
