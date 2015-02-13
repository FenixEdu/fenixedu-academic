/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
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