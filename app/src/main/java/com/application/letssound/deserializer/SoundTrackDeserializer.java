package com.application.letssound.deserializer;

import android.util.Log;

import com.application.letssound.models.SoundTrack;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by davide on 16/07/16.
 */
public class SoundTrackDeserializer implements JsonDeserializer {
    @Override
    public ArrayList<SoundTrack> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Log.e("DESERIALIZER - TAG ", json.toString());
        return new Gson().fromJson(json.getAsJsonObject().get("items"), new TypeToken<ArrayList<SoundTrack>>() {}.getType());
    }
}
