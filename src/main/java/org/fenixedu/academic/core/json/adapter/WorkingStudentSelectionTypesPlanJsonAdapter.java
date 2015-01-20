package org.fenixedu.academic.core.json.adapter;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.WorkingStudentSelectionType;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(WorkingStudentSelectionType.class)
public class WorkingStudentSelectionTypesPlanJsonAdapter implements JsonViewer<WorkingStudentSelectionType> {

    @Override
    public JsonElement view(WorkingStudentSelectionType type, JsonBuilder ctx) {

        JsonObject object = new JsonObject();
        object.addProperty("type", BundleUtil.getString(Bundle.ENUMERATION, type.getQualifiedName()));
        return object;
    }

}
