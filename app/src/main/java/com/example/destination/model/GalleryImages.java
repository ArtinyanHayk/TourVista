package com.example.destination.model;

import android.net.Uri;

import java.net.URI;

public class GalleryImages {
    public Uri pickUri;


    public GalleryImages() {
    }

    public GalleryImages(Uri pickUri) {
        this.pickUri = pickUri;

    }

    public Uri getPickUri() {
        return pickUri;
    }

    public void setPickUri(Uri pickUri) {
        this.pickUri = pickUri;
    }



}

