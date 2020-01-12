package com.infinitemakerz.heartdiseaseprediction;

import com.google.gson.annotations.SerializedName;

public class Pojo {
    @SerializedName("isHeart")
    private String isHeart;
    @SerializedName("CHECK")
    private String CHECK;

    public String getCHECK()
    {
        return CHECK;
    }

    public String getIsHeart()
    {
        return isHeart;
    }
}
