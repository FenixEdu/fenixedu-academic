package org.fenixedu.academic.ui.spring.controller.teacher;

import org.fenixedu.academic.dto.teacher.executionCourse.SearchExecutionCourseAttendsBean.StudentAttendsStateType;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(StudentAttendsStateType.class)
public class StudentAttendsStateTypeJsonAdapter implements JsonViewer<StudentAttendsStateType> {
    @Override
    public JsonElement view(StudentAttendsStateType state, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("name", state.getQualifiedName());
        return object;
    }

}
