package pt.utl.ist.codeGenerator.database;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PlanetUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixframework.FenixFrameworkInitializer;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DataInitializer {

    public static void main(String[] args) {

	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig(FenixFrameworkInitializer.CONFIG_PATH));

	RootDomainObject.init();

	try {
	    Transaction.withTransaction(false, new jvstm.TransactionalCommand() {
		public void doIt() {
		    try {
			initialize();
		    } catch (Exception e) {
			e.printStackTrace();
			throw new Error("Found exception while processing script: " + e, e);
		    }
		}
	    });
	} catch (Exception ex) {
	    ex.printStackTrace();
	}

	System.out.println("Initialization complete.");
	System.exit(0);
    }

    private static void initialize() {
	createRoles();
	createCurricularYearsAndSemesters();
	createCountries();
	createManagerUser();
	createPartyTypeEnums();
	createAccountabilityTypeEnums();
	createOrganizationalStructure();
    }

    private static void createRoles() {
	new Role(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE, "/academicAdminOffice", "/index.do", "portal.academicAdminOffice");
	new Role(RoleType.ADIST_INSTITUCIONAL_PROJECTS_MANAGER, "/", "/.do", "portal.");
	new Role(RoleType.ADIST_PROJECTS_MANAGER, "/", "/.do", "portal.");
	new Role(RoleType.ADMINISTRATOR, "/", "/.do", "portal.");
	new Role(RoleType.ALUMNI, "/alumin", "/index.do", "portal.alumni");
	new Role(RoleType.BOLONHA_MANAGER, "/bolonhaManager", "/index.do", "portal.bolonhaManager");
	new Role(RoleType.CANDIDATE, "/candidate", "/index.do", "portal.candidate");
	new Role(RoleType.CMS_MANAGER, "/CMSManager", "/index.do", "portal.CMSManager");
	new Role(RoleType.COORDINATOR, "/coordinator", "/index.do", "portal.coordinator");
	new Role(RoleType.CREDITS_MANAGER, "/facultyAdmOffice", "/index.do", "portal.credits");
	new Role(RoleType.DEGREE_ADMINISTRATIVE_OFFICE, "/degreeAdministrativeOffice", "/index.do", "portal.degreeAdministrativeOffice");
	new Role(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER, "/degreeAdministrativeOffice", "/index.do", "portal.degreeAdministrativeOfficeSuperUser");
	new Role(RoleType.DELEGATE, "/delegate", "/index.do", "portal.delegate");
	new Role(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE, "/departmentAdmOffice", "/index.do", "portal.departmentAdmOffice");
	new Role(RoleType.DEPARTMENT_CREDITS_MANAGER, "/departmentAdmOffice", "/index.do", "portal.credits.department");
	new Role(RoleType.DEPARTMENT_MEMBER, "/departmentMember", "/index.do", "portal.departmentMember");
	new Role(RoleType.DIRECTIVE_COUNCIL, "/directiveCouncil", "/index.do", "portal.directiveCouncil");
	new Role(RoleType.EMPLOYEE, "/employee", "/index.do", "portal.employee");
	new Role(RoleType.ERASMUS, "/", "/.do", "portal.");
	new Role(RoleType.EXAM_COORDINATOR, "/examCoordination", "/index.do", "portal.examCoordinator");
	new Role(RoleType.EXTERNAL_SUPERVISOR, "/externalSupervisor", "/welcome.do", "portal.externalSupervisor");
	new Role(RoleType.GEP, "/gep", "/index.do", "portal.gep");
	new Role(RoleType.GRANT_OWNER, "/grantOwner", "/index.do", "portal.grantOwner");
	new Role(RoleType.GRANT_OWNER_MANAGER, "/facultyAdmOffice", "/index.do", "portal.facultyAdmOffice");
	new Role(RoleType.HTML_CAPABLE_SENDER, "/messaging", "/index.do", "portal.messaging");
	new Role(RoleType.IDENTIFICATION_CARD_MANAGER, "/identificationCardManager", "/index.do", "portal.identificationCardManager");
	new Role(RoleType.INSTITUCIONAL_PROJECTS_MANAGER, "/institucionalProjectsManagement", "/institucionalProjectIndex.do", "portal.institucionalProjectsManager");
	new Role(RoleType.INTERNATIONAL_RELATION_OFFICE, "/internationalRelatOffice", "/index.do", "portal.internRelationOffice");
	new Role(RoleType.ISTID_INSTITUCIONAL_PROJECTS_MANAGER, "/istidInstitutionalProjectsManagement", "/istidInstitutionalProjectIndex.do", "portal.istidInstitutionalProjectsManager");
	new Role(RoleType.ISTID_PROJECTS_MANAGER, "/istidProjectsManagement", "/istidProjectIndex.do", "portal.istidProjectsManager");
	new Role(RoleType.IT_PROJECTS_MANAGER, "/itProjectsManagement", "/itProjectIndex.do", "portal.itProjectsManager");
	new Role(RoleType.LIBRARY, "/library", "/index.do", "portal.library");
	new Role(RoleType.MANAGER, "/manager", "/index.do", "portal.manager");
	new Role(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE, "/masterDegreeAdministrativeOffice", "/index.do", "portal.masterDegree");
	new Role(RoleType.MASTER_DEGREE_CANDIDATE, "/masterDegreeCandidate", "/index.do", "portal.candidate");
	new Role(RoleType.MESSAGING, "/messaging", "/index.do", "portal.messaging");
	new Role(RoleType.NAPE, "/nape", "/index.do", "portal.nape");
	new Role(RoleType.OPERATOR, "/operator", "/index.do", "portal.operator");
	new Role(RoleType.PARKING_MANAGER, "/parkingManager", "/index.do", "portal.parkingManager");
	new Role(RoleType.PEDAGOGICAL_COUNCIL, "/pedagogicalCouncil", "/index.do", "portal.PedagogicalCouncil");
	new Role(RoleType.PERSON, "/person", "/index.do", "portal.person");
	new Role(RoleType.PERSONNEL_SECTION, "/personnelSection", "/index.do", "portal.personnelSection");
	new Role(RoleType.PROJECTS_MANAGER, "/projectsManagement", "/index.do", "portal.projectsManager");
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
	new Country("Portugal", "Portuguesa", "pt");
    }

    private static void createManagerUser() {
	final Person person = new Person();
	person.setName("Fenix System Administrator");
	person.addPersonRoles(Role.getRoleByRoleType(RoleType.PERSON));
	person.addPersonRoles(Role.getRoleByRoleType(RoleType.MANAGER));
	person.setIsPassInKerberos(Boolean.FALSE);
	final User user = person.getUser();
	final Login login = user.readUserLoginIdentification();
	login.setActive(Boolean.TRUE);
	LoginAlias.createNewCustomLoginAlias(login, "admin");
	login.openLoginIfNecessary(RoleType.MANAGER);
    }

    private static void createPartyTypeEnums() {
	for (final PartyTypeEnum partyTypeEnum : PartyTypeEnum.values()) {
	    new PartyType(partyTypeEnum);
	}
    }

    private static void createAccountabilityTypeEnums() {
	for (final AccountabilityTypeEnum accountabilityTypeEnum : AccountabilityTypeEnum.values()) {
	    new AccountabilityType(accountabilityTypeEnum, new MultiLanguageString(Language.getDefaultLanguage(), accountabilityTypeEnum.getName()));
	}
    }

    private static void createOrganizationalStructure() {
	final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
	final PlanetUnit planetUnit = PlanetUnit.createNewPlanetUnit(new MultiLanguageString(Language.getDefaultLanguage(),
		"Earth"), null, null, "E", new YearMonthDay(), null, null, null, null, false, null);
	rootDomainObject.setEarthUnit(planetUnit);

	createCountryUnits(rootDomainObject, planetUnit);
    }

    private static void createCountryUnits(final RootDomainObject rootDomainObject, final PlanetUnit planetUnit) {
	for (final Country country : rootDomainObject.getCountrysSet()) {
	    CountryUnit.createNewCountryUnit(new MultiLanguageString(Language.getDefaultLanguage(), country.getName()), null, null,
		    country.getCode(), new YearMonthDay(), null, planetUnit, null, null, false, null);
	}
    }

}
