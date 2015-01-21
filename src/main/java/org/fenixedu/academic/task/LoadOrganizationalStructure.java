package org.fenixedu.academic.task;

import java.util.Locale;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.AggregateUnit;
import org.fenixedu.academic.domain.organizationalStructure.CountryUnit;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.SchoolUnit;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class LoadOrganizationalStructure extends CustomTask {

	@Override
	public void runTask() throws Exception {
		final CountryUnit countryUnit = getCountryUnit(Country.readDefault().getName());
        final UniversityUnit universityUnit =
                createUniversityUnit(countryUnit, "Starfleet Univeristy",
                        "SU");
        final SchoolUnit institutionUnit =
                createSchoolUnit(universityUnit, "Starflet Academy", "SA");
        Bennu.getInstance().setInstitutionUnit(institutionUnit);
        final AggregateUnit serviceUnits = createAggregateUnit(institutionUnit, "Services");
        //createServiceUnits(serviceUnits);
        final AggregateUnit departmentUnits = createAggregateUnit(institutionUnit, "Departments");
        //createDepartmentUnits(departmentUnits);
        final AggregateUnit degreeUnits = createAggregateUnit(institutionUnit, "Degrees");
        //createDegreeUnits(degreeUnits);
	}
	
	private CountryUnit getCountryUnit(final String countryUnitName) {
        for (final Party party : Bennu.getInstance().getPartysSet()) {
            if (party.isCountryUnit()) {
                final CountryUnit countryUnit = (CountryUnit) party;
                if (countryUnit.getName().equalsIgnoreCase(countryUnitName)) {
                    return countryUnit;
                }
            }
        }
        return null;
    }
	
	private UniversityUnit createUniversityUnit(final CountryUnit countryUnit, final String universityName,
            final String universityAcronym) {
        return UniversityUnit.createNewUniversityUnit(new MultiLanguageString(Locale.getDefault(), universityName), null,
                null, universityAcronym, new YearMonthDay(), null, countryUnit, null, null, false, null);
    }
	
	private SchoolUnit createSchoolUnit(final UniversityUnit universityUnit, final String universityName,
            final String universityAcronym) {
        return SchoolUnit.createNewSchoolUnit(new MultiLanguageString(Locale.getDefault(), universityName), null, null,
                universityAcronym, new YearMonthDay(), null, universityUnit, null, null, Boolean.FALSE, null);
    }
	
	private AggregateUnit createAggregateUnit(final Unit parentUnit, final String unitName) {
        return AggregateUnit.createNewAggregateUnit(new MultiLanguageString(Locale.getDefault(), unitName), null, null, null,
                new YearMonthDay(), null, parentUnit,
                AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE), null, null, Boolean.FALSE,
                null);
    }

}
