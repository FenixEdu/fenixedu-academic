package org.fenixedu.academic.json.adapters;

import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(ShiftType.class)
public class ShiftTypeJsonAdapter implements JsonViewer<ShiftType> {
    @Override
    public JsonElement view(ShiftType shiftType, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("fullName", shiftType.getFullNameTipoAula());
        object.addProperty("shortName", shiftType.getSiglaTipoAula());
        object.addProperty("name", shiftType.getName());
        return object;
    }
}