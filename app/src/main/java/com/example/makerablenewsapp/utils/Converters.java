package com.example.makerablenewsapp.utils;

import androidx.room.TypeConverter;

import com.example.makerablenewsapp.Models.Source;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class Converters {
    @TypeConverter
    public static Source fromString(String value) {
        Type sourceType = new TypeToken<Source>() {}.getType();
        return new Gson().fromJson(value, sourceType);
    }

    @TypeConverter
    public static String fromSource(Source source) {
        return new Gson().toJson(source);
    }
}
