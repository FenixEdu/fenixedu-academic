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
package org.fenixedu.academic.domain.degreeStructure;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class CycleTypes {

    private final ImmutableSet<CycleType> types;

    public CycleTypes(JsonElement json) {
        ImmutableSet.Builder<CycleType> builder = ImmutableSet.builder();
        for (JsonElement el : json.getAsJsonArray()) {
            builder.add(CycleType.valueOf(el.getAsString()));
        }
        this.types = builder.build();
    }

    public CycleTypes(Collection<CycleType> types) {
        this.types = ImmutableSet.copyOf(types);
    }

    public CycleTypes() {
        this.types = ImmutableSet.of();
    }

    public Collection<CycleType> getTypes() {
        return types;
    }

    public JsonElement toJson() {
        JsonArray array = new JsonArray();
        types.stream().map(CycleType::name).map(JsonPrimitive::new).forEach(type -> array.add(type));
        return array;
    }

}
