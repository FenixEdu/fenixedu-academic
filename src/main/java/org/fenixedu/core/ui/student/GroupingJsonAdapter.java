package org.fenixedu.core.ui.student;

import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;

import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonUtils;
import org.fenixedu.bennu.core.json.JsonViewer;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(Grouping.class)
public class GroupingJsonAdapter implements JsonViewer<Grouping> {

    @Override
    public JsonElement view(Grouping grouping, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        object.addProperty("externalId", grouping.getExternalId());
        object.addProperty("name", grouping.getName());
        object.addProperty("description", grouping.getProjectDescription());
        object.addProperty("enrolmentBeginDay", grouping.getEnrolmentBeginDayDateDateTime().toString(timeFormatter));
        object.addProperty("enrolmentEndDay", grouping.getEnrolmentEndDayDateDateTime().toString(timeFormatter));
        object.addProperty("shiftType", grouping.getShiftType() == null ? "" : grouping.getShiftType().toString());
        object.addProperty("atomicEnrolmentPolicy", grouping.getEnrolmentPolicy().getType() == EnrolmentGroupPolicyType.ATOMIC);
        object.addProperty("differentiatedCapacity", grouping.getDifferentiatedCapacity());
        object.addProperty("minimumGroupCapacity", grouping.getMinimumCapacity());
        object.addProperty("maximumGroupCapacity", grouping.getMaximumCapacity());
        object.addProperty("idealGroupCapacity", grouping.getIdealCapacity());
        object.addProperty("maxGroupNumber", grouping.getGroupMaximumNumber());
        JsonUtils.put(object, "executionCourses", ctx.view(grouping.getExecutionCourses()));

        return object;
    }
}