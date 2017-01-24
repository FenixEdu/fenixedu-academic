package org.fenixedu.academic.json.adapters;

import org.fenixedu.academic.domain.student.StudentStatute;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(StudentStatute.class)
public class StudentStatuteJsonAdapter implements JsonViewer<StudentStatute> {

    @Override
    public JsonElement view(StudentStatute studentStatute, JsonBuilder ctx) {

        JsonObject statuteType = new JsonObject();
        statuteType.addProperty("id", studentStatute.getType().getExternalId());
        statuteType.addProperty("name", studentStatute.getType().getName().getContent());

        JsonObject object = new JsonObject();
        object.add("type", statuteType);
        object.addProperty("comment", studentStatute.getComment());

        return object;
    }
}