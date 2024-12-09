package com.example.nerelicions;

import android.net.Uri;
public class Product {
    private String name;
    private Uri imageUri;
    private String size;  // Для размера

    // Конструктор, принимающий три параметра
    public Product(String name, Uri imageUri, String size) {
        this.name = name;
        this.imageUri = imageUri;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getSize() {
        return size;
    }
}

