package pt.utl.ist.codeGenerator.database;

import java.util.Locale;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.District;
import net.sourceforge.fenixedu.domain.DistrictSubdivision;
import net.sourceforge.fenixedu.domain.EmptyDegree;
import net.sourceforge.fenixedu.domain.EmptyDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactValidation;
import net.sourceforge.fenixedu.domain.contacts.PartyContactValidationState;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PlanetUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.YearMonthDay;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.domain.User;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.utl.ist.codeGenerator.database.CreateTestData.CreateCurricularPeriods;
import pt.utl.ist.codeGenerator.database.CreateTestData.CreateCurricularStructure;
import pt.utl.ist.codeGenerator.database.CreateTestData.CreateDegrees;
import pt.utl.ist.codeGenerator.database.CreateTestData.CreateEvaluations;
import pt.utl.ist.codeGenerator.database.CreateTestData.CreateExecutionCourses;
import pt.utl.ist.codeGenerator.database.CreateTestData.CreateExecutionYears;
import pt.utl.ist.codeGenerator.database.CreateTestData.CreateManagerUser;
import pt.utl.ist.codeGenerator.database.CreateTestData.CreateOrganizationalStructure;
import pt.utl.ist.codeGenerator.database.CreateTestData.CreateResources;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DataInitializer {

    public static void main(String[] args) {

        initialize();

        System.out.println("Initialization complete.");
        System.exit(0);
    }

    @Atomic(mode = TxMode.WRITE)
    private static void initialize() {
        RootDomainObject.ensureRootDomainObject();
        RootDomainObject.initialize();
        Language.setDefaultLocale(Locale.getDefault());

        createRoles();
        createManagerUser();
        createPartyTypeEnums();
        createAccountabilityTypeEnums();
        createCountries();
        createCurricularYearsAndSemesters();
        createDistrictAndDistrictSubdivision();
        createOrganizationalStructure();

        new CreateManagerUser().doIt();
        new CreateExecutionYears().doIt();
        new CreateResources().doIt();
        new CreateOrganizationalStructure().doIt();
        new CreateDegrees().doIt();
        new CreateCurricularPeriods().doIt();
        new CreateCurricularStructure().doIt();
        new CreateExecutionCourses().doIt();
        new CreateEvaluations().doIt();

        createEmptyDegreeAndEmptyDegreeCurricularPlan();

        Portal portal = new Portal();
        portal.setName(new MultiLanguageString(Language.pt, "FenixEdu").append(new MultiLanguageString(Language.en, "FenixEdu")));
        RootDomainObject rdo = RootDomainObject.getInstance();
        portal.setRootDomainObject(rdo);
        rdo.setRootPortal(portal);

    }

    private static void createEmptyDegreeAndEmptyDegreeCurricularPlan() {
        EmptyDegree.init();
        EmptyDegree.getInstance().setAdministrativeOffice(CreateTestData.administrativeOffice);
        EmptyDegreeCurricularPlan.init();
    }

    private static void createRoles() {
        new Role(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE, "/academicAdminOffice", "/index.do", "portal.academicAdminOffice");
        new Role(RoleType.ALUMNI, "/alumin", "/index.do", "portal.alumni");
        new Role(RoleType.BOLONHA_MANAGER, "/bolonhaManager", "/index.do", "portal.bolonhaManager");
        new Role(RoleType.CANDIDATE, "/candidate", "/index.do", "portal.candidate");
        new Role(RoleType.CMS_MANAGER, "/CMSManager", "/index.do", "portal.CMSManager");
        new Role(RoleType.COORDINATOR, "/coordinator", "/index.do", "portal.coordinator");
        new Role(RoleType.CREDITS_MANAGER, "/facultyAdmOffice", "/index.do", "portal.credits");
        new Role(RoleType.DEGREE_ADMINISTRATIVE_OFFICE, "/degreeAdministrativeOffice", "/index.do",
                "portal.degreeAdministrativeOffice");
        new Role(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER, "/degreeAdministrativeOffice", "/index.do",
                "portal.degreeAdministrativeOfficeSuperUser");
        new Role(RoleType.DELEGATE, "/delegate", "/index.do", "portal.delegate");
        new Role(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE, "/departmentAdmOffice", "/index.do", "portal.departmentAdmOffice");
        new Role(RoleType.DEPARTMENT_CREDITS_MANAGER, "/departmentAdmOffice", "/index.do", "portal.credits.department");
        new Role(RoleType.DEPARTMENT_MEMBER, "/departmentMember", "/index.do", "portal.departmentMember");
        new Role(RoleType.DIRECTIVE_COUNCIL, "/directiveCouncil", "/index.do", "portal.directiveCouncil");
        new Role(RoleType.EMPLOYEE, "/employee", "/index.do", "portal.employee");
        new Role(RoleType.EXAM_COORDINATOR, "/examCoordination", "/index.do", "portal.examCoordinator");
        new Role(RoleType.EXTERNAL_SUPERVISOR, "/externalSupervisor", "/welcome.do", "portal.externalSupervisor");
        new Role(RoleType.GEP, "/gep", "/index.do", "portal.gep");
        new Role(RoleType.GRANT_OWNER, "/grantOwner", "/index.do", "portal.grantOwner");
        new Role(RoleType.HTML_CAPABLE_SENDER, "/messaging", "/index.do", "portal.messaging");
        new Role(RoleType.IDENTIFICATION_CARD_MANAGER, "/identificationCardManager", "/index.do",
                "portal.identificationCardManager");
        new Role(RoleType.INTERNATIONAL_RELATION_OFFICE, "/internationalRelatOffice", "/index.do", "portal.internRelationOffice");
        new Role(RoleType.LIBRARY, "/library", "/index.do", "portal.library");
        new Role(RoleType.MANAGER, "/manager", "/index.do", "portal.manager");
        new Role(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE, "/masterDegreeAdministrativeOffice", "/index.do",
                "portal.masterDegree");
        new Role(RoleType.MASTER_DEGREE_CANDIDATE, "/masterDegreeCandidate", "/index.do", "portal.candidate");
        new Role(RoleType.MESSAGING, "/messaging", "/index.do", "portal.messaging");
        new Role(RoleType.NAPE, "/nape", "/index.do", "portal.nape");
        new Role(RoleType.OPERATOR, "/operator", "/index.do", "portal.operator");
        new Role(RoleType.PARKING_MANAGER, "/parkingManager", "/index.do", "portal.parkingManager");
        new Role(RoleType.PEDAGOGICAL_COUNCIL, "/pedagogicalCouncil", "/index.do", "portal.PedagogicalCouncil");
        new Role(RoleType.PERSON, "/person", "/index.do", "portal.person");
        new Role(RoleType.PERSONNEL_SECTION, "/personnelSection", "/index.do", "portal.personnelSection");
        new Role(RoleType.PUBLIC_RELATIONS_OFFICE, "/publicRelations", "/index.do", "portal.publicRelations");
        new Role(RoleType.RECTORATE, "/rectorate", "/index.do", "portal.rectorate");
        new Role(RoleType.RESEARCHER, "/researcher", "/index.do", "portal.researcher");
        new Role(RoleType.RESIDENCE_MANAGER, "/residenceManagement", "/index.do", "portal.residenceManagement");
        new ResourceAllocationRole("/resourceAllocationManager", "/paginaPrincipal.jsp", "portal.resourceAllocationManager");
        new Role(RoleType.RESOURCE_MANAGER, "/resourceManager", "/index.do", "portal.resourceManager");
        new Role(RoleType.SCIENTIFIC_COUNCIL, "/scientificCouncil", "/index.do", "portal.scientificCouncil");
        new Role(RoleType.SEMINARIES_COORDINATOR, "/teacher", "/seminariesIndex.do", "portal.seminariesCoordinator");
        new Role(RoleType.SPACE_MANAGER, "/SpaceManager", "/index.do", "portal.SpaceManager");
        new Role(RoleType.SPACE_MANAGER_SUPER_USER, "/spaceManagerSuperUser", "/index.do", "portal.spaceManagerSuperUser");
        new Role(RoleType.STUDENT, "/student", "/index.do", "portal.student");
        new Role(RoleType.TEACHER, "/teacher", "/index.do", "portal.teacher");
        new Role(RoleType.TREASURY, "/treasury", "/index.do", "portal.treasury");
        new Role(RoleType.TUTORSHIP, "/pedagogicalCouncil", "/index.do", "portal.PedagogicalCouncil");
        new Role(RoleType.WEBSITE_MANAGER, "/webSiteManager", "/index.do", "portal.webSiteManager");
    }

    private static void createCurricularYearsAndSemesters() {
        new CurricularYear(Integer.valueOf(1), 2);
        new CurricularYear(Integer.valueOf(2), 2);
        new CurricularYear(Integer.valueOf(3), 2);
        new CurricularYear(Integer.valueOf(4), 2);
        new CurricularYear(Integer.valueOf(5), 2);
    }

    private static void createCountries() {
        final MultiLanguageString localizedNamePortugal =
                new MultiLanguageString(Language.pt, Country.PORTUGAL).append(new MultiLanguageString(Language.en,
                        Country.PORTUGAL));
        final String code = "PT";
        final String threeLetterCode = "PRT";

        final Country countryDefault =
                new Country(localizedNamePortugal,
                        new MultiLanguageString(Language.pt, Country.NATIONALITY_PORTUGUESE).append(new MultiLanguageString(
                                Language.en, "PORTUGUESE")), code, threeLetterCode);
        countryDefault.setDefaultCountry(Boolean.TRUE);

        new Country(localizedNamePortugal,
                new MultiLanguageString(Language.pt, "PORTUGUESA NATURAL DOS ACORES").append(new MultiLanguageString(Language.en,
                        "NATURAL PORTUGUESE THE AZORES")), code, threeLetterCode);
        new Country(localizedNamePortugal,
                new MultiLanguageString(Language.pt, "PORTUGUESA NATURAL DA MADEIRA").append(new MultiLanguageString(Language.en,
                        "PORTUGUESE NATURAL MADEIRA")), code, threeLetterCode);
        new Country(localizedNamePortugal,
                new MultiLanguageString(Language.pt, "PORTUGUESA NATURAL DO ESTRANGEIRO").append(new MultiLanguageString(
                        Language.en, "NATURAL PORTUGUESE OF FOREIGN")), code, threeLetterCode);
        new Country(localizedNamePortugal,
                new MultiLanguageString(Language.pt, "PORTUGUESA NATURAL DE MACAU E TIMOR LESTE").append(new MultiLanguageString(
                        Language.en, "NATURAL PORTUGUESE MACAU AND EAST TIMOR")), code, threeLetterCode);
    }

    private static void createDistrictAndDistrictSubdivision() {
        District district = new District("01", "Aveiro");
        district = new District("06", "Coimbra");
        district = new District("07", "Evora");

        district = new District("08", "Faro");
        new DistrictSubdivision("081", "Se (Faro)", district);
        new DistrictSubdivision("081", "Sao Pedro", district);

        district = new District("11", "Lisboa");
        district = new District("31", "Ilha da Madeira (Madeira)");
        district = new District("32", "Ilha de Porto Santo (Madeira)");
        district = new District("43", "Ilha Terceira (Acores)");
        district = new District("99", "Estrangeiro");
    }

    private static void createManagerUser() {
        RootDomainObject rdo = RootDomainObject.getInstance();
        final Person person = new Person();
        person.setName("Fenix System Administrator");
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.PERSON));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.MANAGER));
        final User user = person.getUser();
        final Login login = user.readUserLoginIdentification();
        login.setActive(Boolean.TRUE);
        LoginAlias.createNewCustomLoginAlias(login, "admin");
        login.openLoginIfNecessary(RoleType.MANAGER);
        login.setRootDomainObject(rdo);
        person.setRootDomainObject(rdo);
        for (PartyContact partyContact : person.getPartyContactsSet()) {
            PhysicalAddress add = (PhysicalAddress) partyContact;
            add.setValid();
            add.getPartyContactValidation().setState(PartyContactValidationState.VALID);
        }
        final IUserView mock = new Authenticate().mock(person, "https://fenix.ist.utl.pt");
        UserView.setUser(mock);
>>>>>>> f2e10ad... First steps to make it possible to start fenix from zero.
    }

    private static void createPartyTypeEnums() {
        for (final PartyTypeEnum partyTypeEnum : PartyTypeEnum.values()) {
            new PartyType(partyTypeEnum);
        }
    }

    private static void createAccountabilityTypeEnums() {
        for (final AccountabilityTypeEnum accountabilityTypeEnum : AccountabilityTypeEnum.values()) {
            new AccountabilityType(accountabilityTypeEnum, new MultiLanguageString(Language.getDefaultLanguage(),
                    accountabilityTypeEnum.getName()));
        }
    }

    private static void createOrganizationalStructure() {
        final Bennu rootDomainObject = Bennu.getInstance();
        final PlanetUnit planetUnit =
                PlanetUnit.createNewPlanetUnit(new MultiLanguageString(Language.getDefaultLanguage(), "Earth"), null, null, "E",
                        new YearMonthDay(), null, null, null, null, false, null);
        rootDomainObject.setEarthUnit(planetUnit);

        createCountryUnits(rootDomainObject, planetUnit);
    }

    private static void createCountryUnits(final Bennu rootDomainObject, final PlanetUnit planetUnit) {
        for (final Country country : Country.readDistinctCountries()) {
            CountryUnit.createNewCountryUnit(new MultiLanguageString(Language.getDefaultLanguage(), country.getName()), null,
                    null, country.getCode(), new YearMonthDay(), null, planetUnit, null, null, false, null);
        }
    }

}
