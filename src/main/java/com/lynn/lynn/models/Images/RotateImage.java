package com.lynn.lynn.models.Images;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class RotateImage {
    public static int getExifTransformation(int orientation) {

        int degrees = 0;

        switch (orientation) {
            case 1:
                degrees = 0;
                break;
            case 2: //flip
                break;
            case 3:
                degrees = 180;
                break;
            case 4: //flip
                break;
            case 5: //flip
                break;
            case 6:
                degrees = 90;
                break;
            case 7: //flip
                break;
            case 8:
                degrees = 270;
                break;
        }

        return degrees;
    }
    public static BufferedImage rotate(BufferedImage img, int rotation)
    {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage newImage = new BufferedImage(w, h, img.getType());
        Graphics2D g2 = newImage.createGraphics();
        g2.rotate(Math.toRadians(rotation), w/2, h/2);
        g2.drawImage(img,null,0,0);
        return newImage;
    }
}
