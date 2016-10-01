package com.teacherimpact.teacherimpact.DataTransferObjects;

public class Favorite {
    private String favorite;

    public Favorite() {}

    public Favorite(String favorite) {
        this.favorite = favorite;
    }

    public String getId() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
}
