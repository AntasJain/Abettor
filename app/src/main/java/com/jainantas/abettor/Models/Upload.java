package com.jainantas.abettor.Models;

public class Upload {
    private String Name;
    private String ImageUrl;
    private Upload(){

    }

    public Upload(String name, String imageUrl) {
        if(name.trim().equals("")){
            name="No Name";
        }
        Name=name;
        ImageUrl=imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
