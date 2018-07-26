package com.lynn.lynn.models.Images;

import java.io.File;

public class fileImages {

    public static File[] allImages() {
        String path = "C:\\Users\\Haze\\Projects\\lynn\\src\\main\\resources\\static\\images\\";
        File dir = new File(path);
        File[] files = dir.listFiles();
        return files;
    }

    public static Integer countImages() {
        String path = "C:\\Users\\Haze\\Projects\\lynn\\src\\main\\resources\\static\\images\\";
        File dir = new File(path);
        File[] files = dir.listFiles();
        Integer count = 0;
        for(int i = 0; i < files.length; i++) {
            count += 1;
        }
        return count;
    }

}