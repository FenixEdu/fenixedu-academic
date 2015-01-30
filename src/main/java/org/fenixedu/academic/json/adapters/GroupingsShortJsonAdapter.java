package org.fenixedu.academic.json.adapters;

import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GroupingsShortJsonAdapter implements JsonViewer<Grouping> {

    @Override
    public JsonElement view(Grouping grouping, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("externalID", grouping.getExternalId());
        object.addProperty("name", grouping.getName());
        return object;
    }

}
