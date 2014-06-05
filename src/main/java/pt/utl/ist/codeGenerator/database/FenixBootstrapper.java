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
package pt.utl.ist.codeGenerator.database;

import static java.util.Collections.singletonList;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Installation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.PersistentAcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.PartyContactValidationState;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PlanetUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.bootstrap.AdminUserBootstrapper.AdminUserSection;
import org.fenixedu.bennu.core.bootstrap.BootstrapError;
import org.fenixedu.bennu.core.bootstrap.annotations.Bootstrap;
import org.fenixedu.bennu.core.bootstrap.annotations.Bootstrapper;
import org.fenixedu.bennu.core.bootstrap.annotations.Field;
import org.fenixedu.bennu.core.bootstrap.annotations.Section;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.domain.PortalBootstrapper;
import org.fenixedu.bennu.portal.domain.PortalBootstrapper.PortalSection;
import org.joda.time.YearMonthDay;

import pt.ist.standards.geographic.Planet;
import pt.utl.ist.codeGenerator.database.CreateTestData.CreateOrganizationalStructure;
import pt.utl.ist.codeGenerator.database.FenixBootstrapper.SchoolSetupSection;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.collect.Lists;

@Bootstrapper(sections = { SchoolSetupSection.class, PortalSection.class, AdminUserSection.class }, name = "bootstrapper.name",
        bundle = Bundle.APPLICATION, after = PortalBootstrapper.class)
public class FenixBootstrapper {

    final static Locale PT = new Locale("pt");
    final static Locale EN = new Locale("en");

    @Bootstrap
    public static List<BootstrapError> boostrap(SchoolSetupSection schoolSetupSection, PortalSection portalSection,
            AdminUserSection adminSection) {

        if (Planet.getEarth().getByAlfa3(schoolSetupSection.getCountryCode()) == null) {
            return singletonList(new BootstrapError(SchoolSetupSection.class, "getCountryCode", "bootstrapper.error.contry",
                    Bundle.APPLICATION));
        }

        createRoles();
        createManagerUser(adminSection, schoolSetupSection);
        createPartyTypeEnums();
        createAccountabilityTypeEnums();
        createCountries(schoolSetupSection);
        createCurricularYearsAndSemesters();
        createDistrictAndDistrictSubdivision();
        createOrganizationalStructure();

        //new CreateExecutionYears().doIt();
        //new CreateResources().doIt();

        new CreateOrganizationalStructure().doIt(portalSection, schoolSetupSection);

        //new CreateDegrees().doIt(process);
        //new CreateCurricularPeriods().doIt();
        //new CreateCurricularStructure().doIt();
        //new CreateExecutionCourses().doIt();
        //new CreateEvaluations().doIt();

        createEmptyDegreeAndEmptyDegreeCurricularPlan();
        Installation installation = Installation.getInstance();
        installation.setInstituitionEmailDomain(schoolSetupSection.getSchoolEmailDomain());
        installation.setInstituitionURL(schoolSetupSection.getSchoolURL());

        return Lists.newArrayList();
    }

    private static void createEmptyDegreeAndEmptyDegreeCurricularPlan() {
//        EmptyDegree.init();
//        EmptyDegree.getInstance().setAdministrativeOffice(CreateTestData.administrativeOffice);
//        EmptyDegreeCurricularPlan.init();

    }

    private static void createRoles() {
        for (RoleType roleType : RoleType.values()) {
            new Role(roleType);
        }
    }

    private static void createCurricularYearsAndSemesters() {
        new CurricularYear(Integer.valueOf(1), 2);
        new CurricularYear(Integer.valueOf(2), 2);
        new CurricularYear(Integer.valueOf(3), 2);
        new CurricularYear(Integer.valueOf(4), 2);
        new CurricularYear(Integer.valueOf(5), 2);
    }

    private static Country createCountries(SchoolSetupSection schoolSection) {
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

            final MultiLanguageString countryName = new MultiLanguageString(MultiLanguageString.pt, localizedNamePT);
            countryName.append(new MultiLanguageString(MultiLanguageString.en, localizedNameEN));

            final String code = metaData.alpha2;
            final String threeLetterCode = metaData.alpha3;

            final Country country =
                    new Country(countryName,
                            new MultiLanguageString(MultiLanguageString.pt, nationalityPT).append(new MultiLanguageString(
                                    MultiLanguageString.en, nationalityEN)), code, threeLetterCode);
            if (StringUtils.equals(threeLetterCode, schoolSection.getCountryCode().toUpperCase())) {
                defaultCountry = country;
            }
        }

        defaultCountry.setDefaultCountry(Boolean.TRUE);
        return defaultCountry;
    }

    private static void createDistrictAndDistrictSubdivision() {

    }

    static void createManagerUser(AdminUserSection adminSection, SchoolSetupSection schoolSetupSection) {
        Bennu bennu = Bennu.getInstance();
        User adminUser = User.findByUsername(adminSection.getAdminUsername());
        final Person person = new Person(adminUser);
        person.setName(adminSection.getAdminName());
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.PERSON));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.MANAGER));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.PERSONNEL_SECTION));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.SPACE_MANAGER));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.SPACE_MANAGER_SUPER_USER));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.BOLONHA_MANAGER));
        person.setRootDomainObject(bennu);
        person.setCountry(Country.readDefault());
        person.setCountryOfBirth(Country.readDefault());

        EmailAddress.createEmailAddress(person, adminSection.getAdminUserEmail(), PartyContactType.PERSONAL, true, true, true,
                true, true, true);
        for (PartyContact partyContact : person.getPartyContactsSet()) {
            partyContact.setValid();
            partyContact.getPartyContactValidation().setState(PartyContactValidationState.VALID);
        }
        Authenticate.mock(adminUser);
        new PersistentAcademicAuthorizationGroup(AcademicOperationType.MANAGE_AUTHORIZATIONS, new HashSet<AcademicProgram>(),
                new HashSet<AdministrativeOffice>()).addMember(person);
        new PersistentAcademicAuthorizationGroup(AcademicOperationType.MANAGE_ACADEMIC_CALENDARS, new HashSet<AcademicProgram>(),
                new HashSet<AdministrativeOffice>()).addMember(person);
    }

    private static void createPartyTypeEnums() {
        for (final PartyTypeEnum partyTypeEnum : PartyTypeEnum.values()) {
            new PartyType(partyTypeEnum);
        }
    }

    private static void createAccountabilityTypeEnums() {
        for (final AccountabilityTypeEnum accountabilityTypeEnum : AccountabilityTypeEnum.values()) {
            new AccountabilityType(accountabilityTypeEnum, new MultiLanguageString(Locale.getDefault(),
                    accountabilityTypeEnum.getName()));
        }
    }

    private static void createOrganizationalStructure() {
        final Bennu rootDomainObject = Bennu.getInstance();
        final PlanetUnit planetUnit =
                PlanetUnit.createNewPlanetUnit(new MultiLanguageString(Locale.getDefault(), "Earth"), null, null, "E",
                        new YearMonthDay(), null, null, null, null, false, null);
        rootDomainObject.setEarthUnit(planetUnit);

        createCountryUnits(rootDomainObject, planetUnit);
    }

    private static void createCountryUnits(final Bennu rootDomainObject, final PlanetUnit planetUnit) {
        for (final Country country : Country.readDistinctCountries()) {
            CountryUnit.createNewCountryUnit(new MultiLanguageString(Locale.getDefault(), country.getName()), null, null,
                    country.getCode(), new YearMonthDay(), null, planetUnit, null, null, false, null);
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
