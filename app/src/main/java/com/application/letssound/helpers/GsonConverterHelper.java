package com.application.letssound.helpers;

import com.application.letssound.gsonDeserializer.SoundTrackURLDeserializer;
import com.application.letssound.models.SoundTrack;
import com.application.letssound.gsonDeserializer.SoundTrackDeserializer;
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
                .registerTypeAdapter(new TypeToken<String>() {}.getType(), new SoundTrackURLDeserializer())
                .create());
    }


}
