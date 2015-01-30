package org.fenixedu.academic.json.adapters;

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(Lesson.class)
public class LessonJsonAdapter implements JsonViewer<Lesson> {

    @Override
    public JsonElement view(Lesson lesson, JsonBuilder ctx) {
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
        JsonObject object = new JsonObject();
        object.addProperty("dayOfWeek", lesson.getDiaSemana().getDiaSemanaString());
        object.addProperty("beginTime", lesson.getBeginHourMinuteSecond().toString(timeFormatter));
        object.addProperty("endTime", lesson.getEndHourMinuteSecond().toString(timeFormatter));
        if (lesson.getRoomOccupation() != null && lesson.getRoomOccupation().getRoom() != null) {
            object.addProperty("room", lesson.getRoomOccupation().getRoom().getPresentationName());
        }
        return object;
    }
}