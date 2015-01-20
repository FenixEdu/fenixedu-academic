package org.fenixedu.academic.json.adapters;

import org.fenixedu.academic.domain.Shift;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ShiftShortJsonAdapter implements JsonViewer<Shift> {
    @Override
    public JsonElement view(Shift shift, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("shortName", shift.getNome());
        object.addProperty("name", shift.getPresentationName());
        object.addProperty("externalId", shift.getExternalId());
        return object;
    }
}