package org.fenixedu.academic.json.adapters;

import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.CompetenceCourseGroupUnit;
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

@DefaultJsonAdapter(CompetenceCourseGroupUnit.class)
public class CompetenceCourseGroupUnitJsonAdapter implements JsonAdapter<CompetenceCourseGroupUnit> {

    @Override
    public CompetenceCourseGroupUnit create(JsonElement json, JsonBuilder ctx) {
        JsonObject jsonObject = json.getAsJsonObject();
        return (CompetenceCourseGroupUnit) CompetenceCourseGroupUnit.createNewInternalCompetenceCourseGroupUnit(
                MultiLanguageString.fromLocalizedString(LocalizedString.fromJson(jsonObject.get("localizedName"))),
                null,
                null,
                jsonObject.get("acronym").getAsString(),
                new YearMonthDay(),
                null,
                FenixFramework.getDomainObject(jsonObject.get("scientificAreaId").getAsJsonArray().get(0).getAsJsonObject()
                        .get("id").getAsString()),
                        AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE), null, null, false, null);
    }

    @Override
    public CompetenceCourseGroupUnit update(JsonElement competenceCourseGroupUnitJson,
            CompetenceCourseGroupUnit competenceCourseGroupUnit, JsonBuilder ctx) {
        JsonObject jsonObject = competenceCourseGroupUnitJson.getAsJsonObject();
        competenceCourseGroupUnit.setPartyName(MultiLanguageString.fromLocalizedString(LocalizedString.fromJson(jsonObject
                .get("localizedName"))));
        competenceCourseGroupUnit.setAcronym(jsonObject.get("acronym").getAsString());
        jsonObject
                .get("scientificAreaId")
                .getAsJsonArray()
                .forEach(
                        scientificAreaId -> {
                            ScientificAreaUnit scientificAreaUnit =
                                    ((ScientificAreaUnit) FenixFramework.getDomainObject(scientificAreaId.getAsJsonObject()
                                            .get("id").getAsString()));
                            if (!competenceCourseGroupUnit.getParentUnits().contains(scientificAreaUnit)) {
                                competenceCourseGroupUnit.addParentUnit(scientificAreaUnit,
                                        AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
                            }
                        });
        return competenceCourseGroupUnit;
    }

    @Override
    public JsonElement view(CompetenceCourseGroupUnit competenceCourseGroupUnit, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("id", competenceCourseGroupUnit.getExternalId());
        object.addProperty("name", competenceCourseGroupUnit.getPresentationName());
        object.add("localizedName", ctx.view(competenceCourseGroupUnit.getPartyName().toLocalizedString()));
        object.addProperty("acronym", competenceCourseGroupUnit.getAcronym());
        object.add("scientificAreaId", ctx.view(competenceCourseGroupUnit.getParentUnits(), DomainObjectViewer.class));
        return object;
    }
}
