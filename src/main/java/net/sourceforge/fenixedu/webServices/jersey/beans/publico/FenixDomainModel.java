/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.DomainEntity;
import pt.ist.fenixframework.dml.DomainModel;
import pt.ist.fenixframework.dml.DomainRelation;
import pt.ist.fenixframework.dml.Modifier;
import pt.ist.fenixframework.dml.Role;
import pt.ist.fenixframework.dml.Slot;
import pt.ist.fenixframework.dml.Slot.Option;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class FenixDomainModel {

    final JsonObject jsonObject = new JsonObject();

    public FenixDomainModel() {
        final JsonArray classes = new JsonArray();
        final JsonArray relations = new JsonArray();
        final DomainModel domainModel = FenixFramework.getDomainModel();
        for (final DomainClass domainClass : domainModel.getDomainClasses()) {
            addDomainClass(classes, domainClass);
        }
        for (final DomainRelation domainRelation : domainModel.getDomainRelations()) {
            addDomainRelation(relations, domainRelation);
        }
        jsonObject.add("classes", classes);
        jsonObject.add("relations", relations);
    }

    private static void addDomainClass(final JsonArray array, final DomainClass domainClass) {
        final JsonObject object = new JsonObject();
        object.addProperty("className", domainClass.getFullName());
        final DomainEntity superclass = domainClass.getSuperclass();
        if (superclass != null) {
            object.addProperty("superclassName", superclass.getFullName());
        }

        final JsonArray interfaces = new JsonArray();
        for (final Object oi : domainClass.getInterfacesNames()) {
            interfaces.add(new JsonPrimitive(oi.toString()));
        }
        object.add("interfaces", interfaces);

        final JsonArray modifiers = new JsonArray();
        for (final Modifier modifier : domainClass.getModifiers()) {
            modifiers.add(new JsonPrimitive(modifier.name()));
        }
        object.add("modifiers", modifiers);

        final JsonArray slots = new JsonArray();
        for (final Slot slot : domainClass.getSlotsList()) {
            addDomainClassSlot(slots, slot);
        }
        object.add("slots", slots);

        array.add(object);
    }

    private static void addDomainClassSlot(final JsonArray array, final Slot slot) {
        final JsonObject slotJsonObject = new JsonObject();
        slotJsonObject.addProperty("type", slot.getTypeName());
        slotJsonObject.addProperty("name", slot.getName());

        final JsonArray modifiers = new JsonArray();
        for (final Modifier modifier : slot.getModifiers()) {
            modifiers.add(new JsonPrimitive(modifier.name()));
        }
        slotJsonObject.add("modifiers", modifiers);

        final JsonArray options = new JsonArray();
        for (final Option option : slot.getOptions()) {
            options.add(new JsonPrimitive(option.name()));
        }
        slotJsonObject.add("options", options);

        array.add(slotJsonObject);
    }

    private static void addDomainRelation(final JsonArray array, final DomainRelation domainRelation) {
        final JsonObject object = new JsonObject();
        object.addProperty("name", domainRelation.getFullName());
        
        final JsonArray roles = new JsonArray();
        for (final Role role : domainRelation.getRoles()) {
            final JsonObject roleObject = new JsonObject();
            roleObject.addProperty("type", role.getType().getFullName());
            final String name = role.getName();
            if (name != null) {
                roleObject.addProperty("name", name);    
            }
            final JsonArray modifiers = new JsonArray();
            for (final Modifier modifier : role.getModifiers()) {
                modifiers.add(new JsonPrimitive(modifier.name()));
            }
            roleObject.add("modifiers", modifiers); 
            final int multiplicityLower = role.getMultiplicityLower();
            roleObject.add("multiplicityLower", new JsonPrimitive(Integer.toString(multiplicityLower)));
            final int multiplicityUpper = role.getMultiplicityUpper();
            roleObject.add("multiplicityUpper", new JsonPrimitive(multiplicityUpper < 0 ? "*" : Integer.toString(multiplicityUpper)));

            roles.add(roleObject);
        }
        object.add("roles", roles);

        array.add(object);
    }

    public String toJSONString() {
        return jsonObject.toString();
    }

}
