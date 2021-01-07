package org.fenixedu.academic.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.academic.domain.student.StudentStatute;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;
import org.joda.time.LocalDate;

import java.util.function.Supplier;

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

        final LocalDate beginDate = getDate(studentStatute.getBeginDate(), () -> studentStatute.getBeginExecutionPeriod().getBeginLocalDate());
        final LocalDate endDate = getDate(studentStatute.getEndDate(), () -> studentStatute.getEndExecutionPeriod().getEndLocalDate());
        object.addProperty("beginDate", beginDate.toString("yyyy-MM-dd"));
        object.addProperty("endDate", endDate.toString("yyyy-MM-dd"));

        return object;
    }

    private LocalDate getDate(final LocalDate date, final Supplier<LocalDate> alternative) {
        return date == null ? alternative.get() : date;
    }

}