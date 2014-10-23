package org.fenixedu.core.ui.student;

import org.fenixedu.academic.domain.Shift;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(Shift.class)
public class ShiftJsonAdapter implements JsonViewer<Shift> {
    @Override
    public JsonElement view(Shift shift, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("name", shift.getPresentationName());
        object.addProperty("externalId", shift.getExternalId());
        object.add("lessons", ctx.view(shift.getLessonsOrderedByWeekDayAndStartTime()));
        object.addProperty("capacity", shift.getLotacao());
        if (shift.getShiftGroupingProperties() != null && shift.getShiftGroupingProperties().getCapacity() != null) {
            object.addProperty("groupCapacity", shift.getShiftGroupingProperties().getCapacity());
        }

        return object;
    }
}