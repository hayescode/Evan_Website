package com.lynn.lynn.models.Data;

import com.lynn.lynn.models.Images.Images;
import org.springframework.data.repository.CrudRepository;

public interface ImagesDAO extends CrudRepository<Images, Integer> {
    Images[] findByCategoryId(int category_id);
}
