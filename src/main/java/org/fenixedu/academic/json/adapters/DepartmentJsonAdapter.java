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

import java.util.Locale;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.YearMonthDay;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(Department.class)
public class DepartmentJsonAdapter implements JsonAdapter<Department> {
    @Override
    public JsonElement view(Department department, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("id", department.getExternalId());
        object.addProperty("unitId", department.getDepartmentUnit().getExternalId());
        object.addProperty("name", department.getName());
        object.addProperty("code", department.getCode());
        object.addProperty("acronym", department.getAcronym());
        object.addProperty("active", department.getActive());
        object.add(
                "realName",
                ctx.view(new LocalizedString().with(Locale.UK, department.getRealNameEn()).with(new Locale("pt"),
                        department.getRealName())));
        object.addProperty("externalId", department.getExternalId());
        return object;
    }

    @Override
    public Department create(JsonElement departmentJson, JsonBuilder ctx) {
        JsonObject jsonObject = departmentJson.getAsJsonObject();
        Department department = new Department();
        department.setRootDomainObject(Bennu.getInstance());
        department.setCompetenceCourseMembersGroup(DynamicGroup.get("managers"));
        department.setActive(jsonObject.get("active") == null ? false : jsonObject.get("active").getAsBoolean());
        department.setCode(jsonObject.get("code").getAsString());
        department.setName(jsonObject.get("name").getAsString());
        LocalizedString realname = ctx.create(jsonObject.get("realName"), LocalizedString.class);
        department.setRealName(realname.getContent(new Locale("pt")));
        department.setRealNameEn(realname.getContent(Locale.UK));

        Unit departmentParent =
                Bennu.getInstance().getInstitutionUnit().getSubUnits().stream().filter(x -> x.getName().equals("Departments"))
                .findAny().orElse(Bennu.getInstance().getInstitutionUnit());

        DepartmentUnit.createNewInternalDepartmentUnit(department.getNameI18n(), null, null, department.getAcronym(),
                new YearMonthDay(), null, departmentParent,
                AccountabilityType.readByType(AccountabilityTypeEnum.ACADEMIC_STRUCTURE), null, department, null, false, null);

        return department;
    }

    @Override
    public Department update(JsonElement departmentJson, Department department, JsonBuilder ctx) {
        JsonObject jsonObject = departmentJson.getAsJsonObject();
        department.setActive(jsonObject.get("active").getAsBoolean());
        department.setCode(jsonObject.get("code").getAsString());
        department.setName(jsonObject.get("name").getAsString());
        LocalizedString realname = ctx.create(jsonObject.get("realName"), LocalizedString.class);
        department.setRealName(realname.getContent(new Locale("pt")));
        department.setRealNameEn(realname.getContent(Locale.UK));
        DepartmentUnit departmentUnit = department.getDepartmentUnit();
        departmentUnit.setPartyName(department.getNameI18n());
        departmentUnit.setAcronym(department.getCode());
        return department;
    }
}
