package com.gzdb.utils;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.Serializable;


public class ImageItem implements Serializable {
    public String imageId;
    public String thumbnailPath;
    public String oosPath;
    public String imagePath;
    private Bitmap bitmap;
    public boolean isSelected = false;
    public boolean isUpdate = false;

    public String getOosPath() {
     return oosPath;
    }

    public void setOosPath(String oosPath) {
        this.oosPath = oosPath;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public Bitmap getBitmap() {
        if (bitmap == null) {
            try {
                bitmap = Bimp.revitionImageSize(imagePath);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
