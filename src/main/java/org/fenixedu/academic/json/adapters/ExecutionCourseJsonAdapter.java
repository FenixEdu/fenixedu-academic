package org.fenixedu.academic.json.adapters;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(ExecutionCourse.class)
public class ExecutionCourseJsonAdapter implements JsonViewer<ExecutionCourse> {

    @Override
    public JsonElement view(ExecutionCourse executionCourse, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("externalId", executionCourse.getExternalId());
        object.addProperty("name", executionCourse.getName());
        object.addProperty("site", executionCourse.getSiteUrl());

        return object;
    }
}