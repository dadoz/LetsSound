package com.application.letssound.deserializer;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class SoundTrackURLDeserializer implements JsonDeserializer {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Log.e("DESERIALIZER URL - TAG ", json.toString());
        return json.getAsJsonObject().get("link").getAsString();
    }
}
