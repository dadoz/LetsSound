package com.application.letssound.gsonDeserializer;

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

public class SoundTrackURLDeserializer implements JsonDeserializer {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Log.e("DESERIALIZER URL - TAG ", json.toString());
        return json.getAsJsonObject().get("link").getAsString();
    }
}
