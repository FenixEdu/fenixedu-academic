package org.fenixedu.academic.json.adapters;

import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOfficeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.AggregateUnit;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitClassification;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(AdministrativeOffice.class)
public class AdministrativeOfficeJsonAdapter implements JsonAdapter<AdministrativeOffice> {
    @Override
    public JsonElement view(AdministrativeOffice administrativeOffice, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.add("name", ctx.view(administrativeOffice.getName()));
        object.addProperty("id", administrativeOffice.getExternalId());
        object.add("space", ctx.view(administrativeOffice.getCampus()));
        object.add("coordinator", ctx.view(administrativeOffice.getCoordinator()));
        return object;
    }

    @Override
    public AdministrativeOffice create(JsonElement officeJson, JsonBuilder ctx) {
        JsonObject jsonObject = officeJson.getAsJsonObject();
        AdministrativeOffice office = new AdministrativeOffice();
        new AdministrativeOfficeServiceAgreementTemplate(office);
        office.setName(LocalizedString.fromJson(jsonObject.get("name")));
        office.setAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);

        Space campus = FenixFramework.getDomainObject(jsonObject.get("space").getAsJsonObject().get("id").getAsString());
        if (!Space.getTopLevelSpaces().contains(campus)) {
            throw new DomainException("label.not.topLevel");
        }
        office.setCampus(campus);
        office.setCoordinator(FenixFramework.getDomainObject(jsonObject.get("coordinator").getAsJsonObject().get("id")
                .getAsString()));

        Unit servicesParent =
                Bennu.getInstance().getInstitutionUnit().getSubUnits().stream()
                        .filter(x -> x instanceof AggregateUnit && ((AggregateUnit) x).getName().equals("Services")).findAny()
                        .orElse(Bennu.getInstance().getInstitutionUnit());
        Unit unit =
                Unit.createNewUnit(MultiLanguageString.fromLocalizedString(office.getName()), null, null, null,
                        new YearMonthDay(), null, servicesParent,
                        AccountabilityType.readByType(AccountabilityTypeEnum.ADMINISTRATIVE_STRUCTURE), null,
                        UnitClassification.CENTRAL_ORG, office, false, office.getCampus());
        return office;
    }

    @Override
    public AdministrativeOffice update(JsonElement officeJson, AdministrativeOffice office, JsonBuilder ctx) {
        JsonObject jsonObject = officeJson.getAsJsonObject();
        office.setName(LocalizedString.fromJson(jsonObject.get("name")));
        office.setCampus(FenixFramework.getDomainObject(jsonObject.get("space").getAsJsonObject().get("id").getAsString()));
        office.setCoordinator(FenixFramework.getDomainObject(jsonObject.get("coordinator").getAsJsonObject().get("id")
                .getAsString()));
        return office;
    }
}
