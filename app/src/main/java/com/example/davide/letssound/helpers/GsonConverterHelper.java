package com.example.davide.letssound.helpers;

import com.example.davide.letssound.models.SoundTrack;
import com.example.davide.letssound.gsonDeserializer.SoundTrackDeserializer;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by davide on 16/07/16.
 */
public class GsonConverterHelper {

    public static GsonConverterFactory getGsonConverter() {
        return GsonConverterFactory.create(new GsonBuilder()
                .registerTypeAdapter(new TypeToken<ArrayList<SoundTrack>>() {}.getType(), new SoundTrackDeserializer())
                .create());
    }


}
