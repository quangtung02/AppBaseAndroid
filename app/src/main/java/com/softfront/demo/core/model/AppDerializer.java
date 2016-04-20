package com.softfront.demo.core.model;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by nguyen.quang.tung on 4/14/2016.
 */
public class AppDerializer implements JsonDeserializer<AppResponse> {
    private Gson gson;

    public AppDerializer() {
        BooleanSerializer booleanSerializer = new BooleanSerializer();
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Boolean.class, booleanSerializer)
                .registerTypeAdapter(boolean.class, booleanSerializer)
                .create();
    }

    @Override
    public AppResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        AppResponse<Object> appResponse = gson.fromJson(json, typeOfT);
        JsonElement jsonElement = jsonObject.get("data");
        if (jsonElement != null && !jsonElement.isJsonArray()) {
            Object object = gson.fromJson(jsonElement, appResponse.getData().getClass());
            appResponse.setData(object);
        }
        return appResponse;
    }
}
