package org.fenixedu.core.ui.student;

import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(StudentGroup.class)
public class StudentGroupJsonAdapter implements JsonViewer<StudentGroup> {

    @Override
    public JsonElement view(StudentGroup studentGroup, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("externalId", studentGroup.getExternalId());
        object.addProperty("groupNumber", studentGroup.getGroupNumber());
        if (studentGroup.getShift() != null) {
            object.addProperty("shiftId", studentGroup.getShift().getExternalId());
        }
        return object;
    }
}