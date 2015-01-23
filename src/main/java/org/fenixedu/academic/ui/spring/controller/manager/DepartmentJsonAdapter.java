package org.fenixedu.academic.ui.spring.controller.manager;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(Department.class)
public class DepartmentJsonAdapter implements JsonViewer<Department> {
    @Override
    public JsonElement view(Department department, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("name", department.getName());
        object.addProperty("code", department.getCode());
        object.addProperty("active", department.getActive());
        object.addProperty("realName", department.getRealName());
        object.addProperty("realNameEn", department.getRealNameEn());
        object.addProperty("externalId", department.getExternalId());
        return object;
    }
}
