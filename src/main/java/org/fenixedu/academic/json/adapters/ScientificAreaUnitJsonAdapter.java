package org.fenixedu.academic.json.adapters;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.domain.organizationalStructure.ScientificAreaUnit;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.adapters.DomainObjectViewer;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(ScientificAreaUnit.class)
public class ScientificAreaUnitJsonAdapter implements JsonAdapter<ScientificAreaUnit> {
    @Override
    public JsonElement view(ScientificAreaUnit scientificAreaUnit, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("id", scientificAreaUnit.getExternalId());
        object.addProperty("name", scientificAreaUnit.getPartyPresentationName());
        object.add("localizedName", ctx.view(scientificAreaUnit.getPartyName().toLocalizedString()));
        object.addProperty("acronym", scientificAreaUnit.getAcronym());
        object.add("degree", ctx.view(scientificAreaUnit.getDegree()));
        object.add("competenceCourses", ctx.view(scientificAreaUnit.getCompetenceCourseGroupUnits()));
        object.add("departmentsId", ctx.view(scientificAreaUnit.getParentUnits(), DomainObjectViewer.class));
        return object;
    }

    @Override
    public ScientificAreaUnit create(JsonElement scientificAreaUnitJson, JsonBuilder ctx) {
        JsonObject jsonObject = scientificAreaUnitJson.getAsJsonObject();
        return ScientificAreaUnit.createNewInternalScientificArea(
                MultiLanguageString.fromLocalizedString(LocalizedString.fromJson(jsonObject.get("localizedName"))),
                null,
                null,
                jsonObject.get("acronym").getAsString(),
                new YearMonthDay(),
                null,
                ((Department) FenixFramework.getDomainObject(jsonObject.get("departmentId").getAsJsonArray().get(0).getAsJsonObject().get("id")
                        .getAsString())).getDepartmentUnit(), AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE), null,
                null, false, null);
    }

    @Override
    public ScientificAreaUnit update(JsonElement scientificAreaUnitJson, ScientificAreaUnit scientificAreaUnit, JsonBuilder ctx) {
        JsonObject jsonObject = scientificAreaUnitJson.getAsJsonObject();
        scientificAreaUnit.setAcronym(jsonObject.get("acronym").getAsString());
        scientificAreaUnit.setPartyName(MultiLanguageString.fromLocalizedString(LocalizedString.fromJson(jsonObject
                .get("localizedName"))));
        jsonObject
                .get("departmentsId")
                .getAsJsonArray()
                .forEach(
                        departmentId -> {
                            DepartmentUnit departmentUnit =
                                    ((DepartmentUnit) FenixFramework.getDomainObject(departmentId.getAsJsonObject().get("id")
                                            .getAsString()));
                            if (!scientificAreaUnit.getParentUnits().contains(departmentUnit)) {
                                scientificAreaUnit.addParentUnit(departmentUnit,
                                        AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
                            }
                        });
        return scientificAreaUnit;
    }
}
