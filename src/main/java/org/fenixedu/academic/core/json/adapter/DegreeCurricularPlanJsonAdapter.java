package org.fenixedu.academic.core.json.adapter;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(DegreeCurricularPlan.class)
public class DegreeCurricularPlanJsonAdapter implements JsonViewer<DegreeCurricularPlan> {
    @Override
    public JsonElement view(DegreeCurricularPlan curricularPlan, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("name", curricularPlan.getName());
        object.addProperty("fullname", curricularPlan.getPresentationName());
        object.addProperty("externalId", curricularPlan.getExternalId());
        return object;
    }
}
