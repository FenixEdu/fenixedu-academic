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
package org.fenixedu.academic.bootstrap;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.bootstrap.FenixBootstrapper.SchoolSetupSection;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.Installation;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.PartyContactType;
import org.fenixedu.academic.domain.contacts.PartyContactValidationState;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.PartyType;
import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestCategory;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.bootstrap.AdminUserBootstrapper.AdminUserSection;
import org.fenixedu.bennu.core.bootstrap.BootstrapError;
import org.fenixedu.bennu.core.bootstrap.annotations.Bootstrap;
import org.fenixedu.bennu.core.bootstrap.annotations.Bootstrapper;
import org.fenixedu.bennu.core.bootstrap.annotations.Field;
import org.fenixedu.bennu.core.bootstrap.annotations.Section;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.portal.domain.PortalBootstrapper;
import org.fenixedu.bennu.portal.domain.PortalBootstrapper.PortalSection;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.commons.i18n.LocalizedString.Builder;
import org.fenixedu.spaces.domain.SpaceClassification;

import com.google.common.collect.Lists;
import com.google.gson.JsonParser;

import pt.ist.standards.geographic.Planet;

@Bootstrapper(sections = { SchoolSetupSection.class, PortalSection.class, AdminUserSection.class }, name = "bootstrapper.name",
        bundle = Bundle.APPLICATION, after = PortalBootstrapper.class)
public class FenixBootstrapper {

    final static Locale PT = new Locale("pt");
    final static Locale EN = new Locale("en");

    @Bootstrap
    public static List<BootstrapError> boostrap(final SchoolSetupSection schoolSetupSection, final PortalSection portalSection,
            final AdminUserSection adminSection) {

        if (Planet.getEarth().getByAlfa3(schoolSetupSection.getCountryCode()) == null
                || !Planet.getEarth().getByAlfa3(schoolSetupSection.getCountryCode()).alpha3
                        .equals(schoolSetupSection.getCountryCode())) {
            return singletonList(new BootstrapError(SchoolSetupSection.class, "getCountryCode", "bootstrapper.error.contry",
                    Bundle.APPLICATION));
        }

        createManagerUser(adminSection, schoolSetupSection);
        createAcademicSpaceClassifications();
        createPartyTypeEnums();
        createAccountabilityTypeEnums();
        createCountries(schoolSetupSection);
        createCurricularYearsAndSemesters();
        createDistrictAndDistrictSubdivision();
        createOrganizationalStructure();

//        EvaluationSeason normalSeason = createEvaluationSeason("EN", "RS", "NORMAL", true, false, false, false);
//        EvaluationConfiguration.getInstance().setDefaultEvaluationSeason(normalSeason);
//        createEvaluationSeason("MN", "GI", "IMPROVEMENT", false, true, false, false);
//        createEvaluationSeason("AE", "SA", "SPECIAL_AUTHORIZATION", false, false, true, false);
//        createEvaluationSeason("EE", "SS", "SPECIAL_SEASON", false, false, false, true);
        // new CreateExecutionYears().doIt();
        // new CreateResources().doIt();

        new CreateOrganizationalStructure().doIt(portalSection, schoolSetupSection);

        // new CreateDegrees().doIt(process);
        // new CreateCurricularPeriods().doIt();
        // new CreateCurricularStructure().doIt();
        // new CreateExecutionCourses().doIt();
        // new CreateEvaluations().doIt();

        createEmptyDegreeAndEmptyDegreeCurricularPlan();
        createDefaultRegistrationProtocol();
        Installation installation = Installation.getInstance();
        installation.setInstituitionEmailDomain(schoolSetupSection.getSchoolEmailDomain());
        installation.setInstituitionURL(schoolSetupSection.getSchoolURL());

        if (Bennu.getInstance().getRootClassificationSet().isEmpty()) {
            Builder schoolSpaces = new LocalizedString.Builder();
            CoreConfiguration.supportedLocales().stream().forEach(l -> schoolSpaces.with(l, SpaceUtils.SCHOOL_SPACES));
            SpaceClassification sc = new SpaceClassification("1", schoolSpaces.build());

            Builder campus = new LocalizedString.Builder();
            CoreConfiguration.supportedLocales().stream().forEach(l -> campus.with(l, SpaceUtils.CAMPUS));
            sc.addChildren(new SpaceClassification("1.1", campus.build()));

            Builder building = new LocalizedString.Builder();
            CoreConfiguration.supportedLocales().stream().forEach(l -> building.with(l, SpaceUtils.BUILDING));
            sc.addChildren(new SpaceClassification("1.2", building.build()));

            Builder floor = new LocalizedString.Builder();
            CoreConfiguration.supportedLocales().stream().forEach(l -> floor.with(l, SpaceUtils.FLOOR));
            sc.addChildren(new SpaceClassification("1.3", floor.build()));

            Builder roomSubdivision = new LocalizedString.Builder();
            CoreConfiguration.supportedLocales().stream().forEach(l -> roomSubdivision.with(l, SpaceUtils.ROOM_SUBDIVISION));
            sc.addChildren(new SpaceClassification("1.4", roomSubdivision.build()));

            Builder room = new LocalizedString.Builder();
            CoreConfiguration.supportedLocales().stream().forEach(l -> room.with(l, SpaceUtils.ROOM));
            sc.addChildren(new SpaceClassification("1.4", room.build()));
        }

        createDefaultServiceRequestTypes();

        return Lists.newArrayList();
    }

    private static EvaluationSeason createEvaluationSeason(final String ptCode, final String enCode, final String nameKey,
            final boolean normal, final boolean improvement, final boolean specialAuthorization, final boolean special) {
        final LocalizedString acronym = new LocalizedString.Builder().with(Locale.forLanguageTag("pt-PT"), ptCode)
                .with(Locale.forLanguageTag("en-GB"), enCode).build();
        return new EvaluationSeason(acronym, BundleUtil.getLocalizedString(Bundle.ENUMERATION, nameKey), normal, improvement,
                specialAuthorization, special);
    }

    private static void createDefaultRegistrationProtocol() {
        LocalizedString description =
                LocalizedString.fromJson(new JsonParser().parse("{\"pt-PT\":\"Normal\",\"en-GB\":\"Normal\"}"));

        RegistrationProtocol registrationProtocol = new RegistrationProtocol("NORMAL", description, true, true, true, false,
                false, false, false, false, false, true, false);
    }

    public static class CreateOrganizationalStructure {
        public void doIt(final PortalSection portalSection, final SchoolSetupSection schoolSetupSection) {
            final Unit countryUnit = getCountryUnit(Country.readDefault().getName());
            final Unit universityUnit = createUniversityUnit(countryUnit, schoolSetupSection.getUniversityName(),
                    schoolSetupSection.getUniversityAcronym());
            final Unit institutionUnit =
                    createSchoolUnit(universityUnit, portalSection.getOrganizationName(), schoolSetupSection.getSchoolAcronym());
            Bennu.getInstance().setInstitutionUnit(institutionUnit);

            final Unit serviceUnits = createAggregateUnit(institutionUnit, "Services");
            // createServiceUnits(serviceUnits);
            final Unit departmentUnits = createAggregateUnit(institutionUnit, "Departments");
            // createDepartmentUnits(departmentUnits);
            final Unit degreeUnits = createAggregateUnit(institutionUnit, "Degrees");
            // createDegreeUnits(degreeUnits);
        }

        private Unit getCountryUnit(final String countryUnitName) {
            for (final Party party : Bennu.getInstance().getPartysSet()) {
                if (party.isCountryUnit()) {
                    final Unit countryUnit = (Unit) party;
                    if (countryUnit.getName().equalsIgnoreCase(countryUnitName)) {
                        return countryUnit;
                    }
                }
            }
            return null;
        }

        private Unit createUniversityUnit(final Unit countryUnit, final String universityName, final String universityAcronym) {
            return Unit.createNewUnit(PartyType.of(PartyTypeEnum.UNIVERSITY),
                    new LocalizedString(Locale.getDefault(), universityName), universityAcronym, countryUnit,
                    AccountabilityType.readByType(AccountabilityTypeEnum.GEOGRAPHIC));
        }

        private Unit createAggregateUnit(final Unit parentUnit, final String unitName) {
            return Unit.createNewUnit(PartyType.of(PartyTypeEnum.AGGREGATE_UNIT),
                    new LocalizedString(Locale.getDefault(), unitName), null, parentUnit,
                    AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
        }

        private Unit createSchoolUnit(final Unit universityUnit, final String universityName, final String universityAcronym) {
            return Unit.createNewUnit(PartyType.of(PartyTypeEnum.SCHOOL),
                    new LocalizedString(Locale.getDefault(), universityName), universityAcronym, universityUnit,
                    AccountabilityType.readByType(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE));
        }
    }

    private static void createEmptyDegreeAndEmptyDegreeCurricularPlan() {
//        EmptyDegree.init();
//        EmptyDegree.getInstance().setAdministrativeOffice(CreateTestData.administrativeOffice);
//        EmptyDegreeCurricularPlan.init();

    }

    private static void createCurricularYearsAndSemesters() {
        new CurricularYear(Integer.valueOf(1), 2);
        new CurricularYear(Integer.valueOf(2), 2);
        new CurricularYear(Integer.valueOf(3), 2);
        new CurricularYear(Integer.valueOf(4), 2);
        new CurricularYear(Integer.valueOf(5), 2);
        new CurricularYear(Integer.valueOf(6), 2);
    }

    private static Country createCountries(final SchoolSetupSection schoolSection) {
        Country defaultCountry = null;
        for (pt.ist.standards.geographic.Country metaData : Planet.getEarth().getPlaces()) {
            String localizedNamePT = null;
            try {
                localizedNamePT = metaData.getLocalizedName(PT);
            } catch (MissingResourceException e) {
            }

            String localizedNameEN = null;
            try {
                localizedNameEN = metaData.getLocalizedName(EN);
            } catch (MissingResourceException e) {
            }

            if (localizedNameEN == null && localizedNamePT == null) {
                continue;
            }

            if (localizedNamePT == null) {
                localizedNamePT = localizedNameEN;
            }

            if (localizedNameEN == null) {
                localizedNameEN = localizedNamePT;
            }
            String nationalityPT = null;
            try {
                nationalityPT = metaData.getNationality(PT);
            } catch (MissingResourceException e) {
            }

            String nationalityEN = null;
            try {
                nationalityEN = metaData.getNationality(EN);
            } catch (MissingResourceException e) {
            }

            if (nationalityPT == null) {
                if (nationalityEN == null) {
                    nationalityPT = localizedNamePT;
                } else {
                    nationalityPT = nationalityEN;
                }
            }

            if (nationalityEN == null) {
                if (nationalityPT == null) {
                    nationalityEN = localizedNameEN;
                } else {
                    nationalityEN = nationalityPT;
                }
            }

            final LocalizedString countryName = new LocalizedString(PT, localizedNamePT).with(EN, localizedNameEN);
            final LocalizedString nationality = new LocalizedString(PT, nationalityPT).with(EN, nationalityEN);

            final String code = metaData.alpha2;
            final String threeLetterCode = metaData.alpha3;

            final Country country = new Country(countryName, nationality, code, threeLetterCode);
            if (StringUtils.equals(threeLetterCode, schoolSection.getCountryCode().toUpperCase())) {
                defaultCountry = country;
            }
        }

        defaultCountry.setDefaultCountry(Boolean.TRUE);
        return defaultCountry;
    }

    private static void createDistrictAndDistrictSubdivision() {

    }

    static void createManagerUser(final AdminUserSection adminSection, final SchoolSetupSection schoolSetupSection) {
        User adminUser = User.findByUsername(adminSection.getAdminUsername());
        final Person person = new Person(adminUser.getProfile());
        RoleType.grant(RoleType.SCIENTIFIC_COUNCIL, adminUser);
        RoleType.grant(RoleType.SPACE_MANAGER, adminUser);
        RoleType.grant(RoleType.SPACE_MANAGER_SUPER_USER, adminUser);
        RoleType.grant(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE, adminUser);
        RoleType.grant(RoleType.BOLONHA_MANAGER, adminUser);
        person.setCountry(Country.readDefault());
        person.setCountryOfBirth(Country.readDefault());

        EmailAddress.createEmailAddress(person, adminSection.getAdminUserEmail(), PartyContactType.PERSONAL, true, true, true,
                true);
        for (PartyContact partyContact : person.getPartyContactsSet()) {
            partyContact.setValid();
            partyContact.getPartyContactValidation().setState(PartyContactValidationState.VALID);
        }
        Authenticate.mock(adminUser, "TODO: CHANGE ME");
        AcademicOperationType.MANAGE_AUTHORIZATIONS.grant(adminUser);
//        AcademicOperationType.MANAGE_ACADEMIC_CALENDARS.grant(adminUser);
    }

    private static void createAcademicSpaceClassifications() {
        LocalizedString.Builder campusNameBuilder = new LocalizedString.Builder();
        CoreConfiguration.supportedLocales().stream().forEach(l -> campusNameBuilder.with(l, "Campus"));
        new SpaceClassification("1", campusNameBuilder.build());
    }

    private static void createPartyTypeEnums() {
        for (final PartyTypeEnum partyTypeEnum : PartyTypeEnum.values()) {
            new PartyType(partyTypeEnum);
        }
    }

    private static void createAccountabilityTypeEnums() {
        for (final AccountabilityTypeEnum accountabilityTypeEnum : AccountabilityTypeEnum.values()) {
            new AccountabilityType(accountabilityTypeEnum,
                    new LocalizedString(Locale.getDefault(), accountabilityTypeEnum.getName()));
        }
    }

    private static void createOrganizationalStructure() {
        final Bennu rootDomainObject = Bennu.getInstance();

        final Unit planetUnit = Unit.createNewUnit(PartyType.of(PartyTypeEnum.PLANET),
                new LocalizedString(Locale.getDefault(), "Earth"), "E", null, null);

        rootDomainObject.setEarthUnit(planetUnit);

        createCountryUnits(rootDomainObject, planetUnit);
    }

    private static void createCountryUnits(final Bennu rootDomainObject, final Unit planetUnit) {
        final AccountabilityType accountabilityType = AccountabilityType.readByType(AccountabilityTypeEnum.GEOGRAPHIC);
        for (final Country country : Country.readDistinctCountries()) {
            Unit.createNewUnit(PartyType.of(PartyTypeEnum.COUNTRY), country.getLocalizedName(), country.getCode(), planetUnit,
                    accountabilityType);
        }
    }

    private static void createDefaultServiceRequestTypes() {

        // By default create all legacy ServiceRequestTypes as Inactive and as Services
        // -> Then configurate accordingly

        for (final AcademicServiceRequestType academicServiceRequestType : AcademicServiceRequestType.values()) {
            if (academicServiceRequestType == AcademicServiceRequestType.DOCUMENT) {
                continue;
            } else if (academicServiceRequestType == AcademicServiceRequestType.DIPLOMA_REQUEST) {
                ServiceRequestType.createLegacy(academicServiceRequestType.name(),
                        new LocalizedString(new Locale("PT", "pt"), academicServiceRequestType.getLocalizedName()), false,
                        academicServiceRequestType, DocumentRequestType.DIPLOMA_REQUEST, true, Boolean.FALSE, Boolean.FALSE,
                        Boolean.FALSE, ServiceRequestCategory.SERVICES);
                continue;
            } else if (academicServiceRequestType == AcademicServiceRequestType.DIPLOMA_SUPPLEMENT_REQUEST) {
                ServiceRequestType.createLegacy(academicServiceRequestType.name(),
                        new LocalizedString(new Locale("PT", "pt"), academicServiceRequestType.getLocalizedName()), false,
                        academicServiceRequestType, DocumentRequestType.DIPLOMA_SUPPLEMENT_REQUEST, true, Boolean.FALSE,
                        Boolean.FALSE, Boolean.FALSE, ServiceRequestCategory.SERVICES);
                continue;
            } else if (academicServiceRequestType == AcademicServiceRequestType.REGISTRY_DIPLOMA_REQUEST) {
                ServiceRequestType.createLegacy(academicServiceRequestType.name(),
                        new LocalizedString(new Locale("PT", "pt"), academicServiceRequestType.getLocalizedName()), false,
                        academicServiceRequestType, DocumentRequestType.REGISTRY_DIPLOMA_REQUEST, true, Boolean.FALSE,
                        Boolean.FALSE, Boolean.FALSE, ServiceRequestCategory.SERVICES);
                continue;
            }

            ServiceRequestType.createLegacy(academicServiceRequestType.name(),
                    new LocalizedString(new Locale("PT", "pt"), academicServiceRequestType.getLocalizedName()), false,
                    academicServiceRequestType, null, true, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
                    ServiceRequestCategory.SERVICES);
        }

        for (final DocumentRequestType documentRequestType : DocumentRequestType.values()) {

            if (documentRequestType == DocumentRequestType.DIPLOMA_REQUEST) {
                continue;
            } else if (documentRequestType == DocumentRequestType.DIPLOMA_SUPPLEMENT_REQUEST) {
                continue;
            } else if (documentRequestType == DocumentRequestType.REGISTRY_DIPLOMA_REQUEST) {
                continue;
            }

            // By default create all legacy ServiceRequestTypes as Services -> Then
            // configurate accordingly
            ServiceRequestType.createLegacy(documentRequestType.name(),
                    BundleUtil.getLocalizedString("resources.EnumerationResources",
                            "DocumentRequestType." + documentRequestType.name()),
                    false, AcademicServiceRequestType.DOCUMENT, documentRequestType, true, Boolean.FALSE, Boolean.FALSE,
                    Boolean.FALSE, ServiceRequestCategory.SERVICES);
        }
    }

    @Section(name = "bootstrapper.schoolSetup.name", description = "bootstrapper.schoolSetup.description",
            bundle = Bundle.APPLICATION)
    public static interface SchoolSetupSection {

        @Field(name = "bootstrapper.schoolSetup.universityName", hint = "bootstrapper.schoolSetup.universityName.hint", order = 1)
        public String getUniversityName();

        @Field(name = "bootstrapper.schoolSetup.universityAcronym", hint = "bootstrapper.schoolSetup.universityAcronym.hint",
                order = 2)
        public String getUniversityAcronym();

        @Field(name = "bootstrapper.schoolSetup.schoolAcronym", hint = "bootstrapper.schoolSetup.schoolAcronym.hint", order = 3)
        public String getSchoolAcronym();

        @Field(name = "bootstrapper.schoolSetup.country", hint = "bootstrapper.schoolSetup.country.hint", order = 4)
        public String getCountryCode();

        @Field(name = "bootstrapper.application.schoolDomain", hint = "bootstrapper.application.schoolDomain.hint", order = 5)
        public String getSchoolDomain();

        @Field(name = "bootstrapper.application.schoolUrl", hint = "bootstrapper.application.schoolUrl.hint", order = 6)
        public String getSchoolURL();

        @Field(name = "bootstrapper.application.schoolEmailDomain", hint = "bootstrapper.application.schoolEmailDomain.hint",
                order = 7)
        public String getSchoolEmailDomain();

    }

}
