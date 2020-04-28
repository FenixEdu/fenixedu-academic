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

import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.schedule.shiftCapacity.ShiftCapacity;
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
        object.addProperty("shortName", shift.getNome());
        object.addProperty("name", shift.getPresentationName());
        object.addProperty("externalId", shift.getExternalId());
        object.add("lessons", ctx.view(shift.getLessonsOrderedByWeekDayAndStartTime()));
        object.addProperty("capacity", ShiftCapacity.getTotalCapacity(shift));
        object.add("shiftTypes", ctx.view(shift.getTypes()));

        return object;
    }
}