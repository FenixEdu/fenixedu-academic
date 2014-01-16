package pt.utl.ist.codeGenerator.database;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Instalation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ResourceAllocationRole;
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

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.standards.geographic.Planet;
import pt.utl.ist.codeGenerator.database.CreateTestData.CreateOrganizationalStructure;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Installer {

    final static Locale PT = new Locale("pt");
    final static Locale EN = new Locale("en");

    public static class InstallationProcess {
        public InstallationProcess(String countryCode, String adminName, String adminUsername, String adminPass,
                String universityName, String universityAcronym, String schoolName, String schoolAcronym, String email,
                String instituitionURL, String instituitionEmailDomain, String instalationName, String instalationDomain) {
            super();
            this.countryCode = countryCode;

            pt.ist.standards.geographic.Country country = Planet.getEarth().getByAlfa3(countryCode);
            if (country == null) {
                throw new RuntimeException("Country with code " + countryCode + " does not exist.");
            }

            this.adminName = adminName;
            this.adminUsername = adminUsername;
            this.adminPass = adminPass;
            this.universityName = universityName;
            this.universityAcronym = universityAcronym;
            this.schoolName = schoolName;
            this.schoolAcronym = schoolAcronym;
            this.email = email;
            this.instituitionURL = instituitionURL;
            this.instituitionEmailDomain = instituitionEmailDomain;
            this.instalationName = instalationName;
            this.instalationDomain = instalationDomain;
        }

        public InstallationProcess() {
            // TODO Auto-generated constructor stub
        }

        public String countryCode;
        protected Country country;

        public String adminName;
        public String adminUsername;
        public String adminPass;
        protected Person person;

        public String universityName;
        public String universityAcronym;

        public String schoolName;
        public String schoolAcronym;
        public String email;

        public String instituitionURL;
        protected String instituitionDomain;
        public String instituitionEmailDomain;
        public String instalationName;
        public String instalationDomain;
    }

    private static String readValue(String prompt, String def) {
        //  prompt the user to enter their name
        System.out.print(prompt + " [" + def + "]: ");

        //  open up standard input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String value = null;

        //  read the username from the command-line; need to use try/catch with the
        //  readLine() method
        try {
            value = br.readLine();
        } catch (IOException ioe) {
            System.out.println("IO error trying to read your name!");
            System.exit(1);
        }
        if (value.equals("")) {
            value = def;
        }
        return value;
    }

    private static String readPassword(String prompt) {
        Console console = System.console();

        System.out.print(prompt + ": ");
        char[] passwordChars = console.readPassword();

        return new String(passwordChars);
    }

    public static final Logger logger = LoggerFactory.getLogger(Installer.class);

    public static void main(String[] args) {
        initialize();
        System.exit(0);
    }

    @Atomic(mode = TxMode.WRITE)
    private static void initialize() {
        System.out.println("");
        System.out.println("##############################################################################");
        System.out.println("                                                                              ");
        System.out.println("                   XXXXXXX                                                    ");
        System.out.println("                  + XXXXX +                                                   ");
        System.out.println("                +++++ X +++++                                                 ");
        System.out.println("               +++++++ +++++++                                                ");
        System.out.println("              . +++++ . +++++ .            FenixEdu™                          ");
        System.out.println("            ..... + ..... + .....          Installation                       ");
        System.out.println("           ....... ....... .......                                            ");
        System.out.println("            .....   .....   .....                                             ");
        System.out.println("              .       .       .                                               ");
        System.out.println("                                                                              ");
        System.out.println("##############################################################################");

        InstallationProcess process = new InstallationProcess();

        System.out.println("");
        System.out.println("This process will guide you in installing FenixEdu in your School.");
        System.out.println("First in what country are you at?");

        Language.setDefaultLocale(Locale.getDefault());
        while (true) {
            String countryCode = readValue("Country (Three Letter ISO 3166-1)", "USA").toUpperCase();

            pt.ist.standards.geographic.Country country = Planet.getEarth().getByAlfa3(countryCode);
            if (country != null) {
                System.out.println("Using " + country.getLocalizedName(EN));
                process.countryCode = countryCode;
                break;
            } else {
                System.out.println("There isn't a country with '" + countryCode + "' code.");
            }
        }

        System.out.println("");
        System.out.println("Let's setup your school. You need to set up your university and school name.");

        process.universityName = readValue("University Name", "Example University");
        process.universityAcronym = readValue("University Acronym", "EU");
        process.schoolName = readValue("School Name", "Example Engineering School");
        process.schoolAcronym = readValue("School Acronym", "EES");

        System.out.println("");
        System.out.println("Next we need to setup the domain information about your applicatinos.");
        process.instituitionDomain = readValue("School Domain", "ees.example.edu");
        process.instituitionURL = readValue("School URL", "http://" + process.instituitionDomain);
        process.instituitionEmailDomain = readValue("School Email Domain", process.instituitionDomain);
        process.instalationName = readValue("Installation Name", process.schoolAcronym + " FenixEdu");
        process.instalationDomain = readValue("Installation Domain", "fenixedu." + process.instituitionDomain);

        System.out.println("");
        System.out.println("Now we need to create an administrator account.");
        process.adminUsername = readValue("Username", System.getProperty("user.name"));
        process.adminName = readValue("Name", "FenixEdu Administrator");
        process.email = readValue("Email", process.adminUsername + "@" + process.instituitionEmailDomain);

        String password = null;
        while (true) {
            password = readPassword("Password");
            if (password.equals("")) {
                System.out.println("Error: Empty password not allowed.\n");
                continue;
            }
            String password2 = readPassword("Password (again)");
            if (!password.equals(password2)) {
                System.out.println("Error: Passwords do not match.\n");
            } else {
                break;
            }
        }

        process.adminPass = password;
        System.out.println("");
        System.out.println("Starting install process...");
        installFenixEdu(process);
        System.out.println("Installation Complete.");
    }

    public static void installFenixEdu(InstallationProcess process) {
        createRoles();
        createManagerUser(process);
        createPartyTypeEnums();
        createAccountabilityTypeEnums();
        createCountries(process);
        createCurricularYearsAndSemesters();
        createDistrictAndDistrictSubdivision();
        createOrganizationalStructure();

        //new CreateExecutionYears().doIt();
        //new CreateResources().doIt();

        new CreateOrganizationalStructure().doIt(process);

        //new CreateDegrees().doIt(process);
        //new CreateCurricularPeriods().doIt();
        //new CreateCurricularStructure().doIt();
        //new CreateExecutionCourses().doIt();
        //new CreateEvaluations().doIt();

        createEmptyDegreeAndEmptyDegreeCurricularPlan();
        CreateFunctionallityTree.doIt();
        Instalation inst = new Instalation();
        inst.setInstalationDomain(process.instalationDomain);
        inst.setInstalationName(process.instalationName);
        inst.setInstituitionEmailDomain(process.instituitionEmailDomain);
        inst.setInstituitionURL(process.instituitionURL);
    }

    private static void createEmptyDegreeAndEmptyDegreeCurricularPlan() {
//        EmptyDegree.init();
//        EmptyDegree.getInstance().setAdministrativeOffice(CreateTestData.administrativeOffice);
//        EmptyDegreeCurricularPlan.init();

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
        new Role(RoleType.DEVELOPER, "/person", "/index.do", "portal.developer");
    }

    private static void createCurricularYearsAndSemesters() {
        new CurricularYear(Integer.valueOf(1), 2);
        new CurricularYear(Integer.valueOf(2), 2);
        new CurricularYear(Integer.valueOf(3), 2);
        new CurricularYear(Integer.valueOf(4), 2);
        new CurricularYear(Integer.valueOf(5), 2);
    }

    private static void createCountries(InstallationProcess process) {
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

            final MultiLanguageString countryName = new MultiLanguageString(Language.pt, localizedNamePT);
            countryName.append(new MultiLanguageString(Language.en, localizedNameEN));

            final String code = metaData.alpha2;
            final String threeLetterCode = metaData.alpha3;

            final Country country =
                    new Country(countryName, new MultiLanguageString(Language.pt, nationalityPT).append(new MultiLanguageString(
                            Language.en, nationalityEN)), code, threeLetterCode);
            if (threeLetterCode.equals(process.countryCode)) {
                defaultCountry = country;
            }
        }

        defaultCountry.setDefaultCountry(Boolean.TRUE);
        process.country = defaultCountry;
    }

    private static void createDistrictAndDistrictSubdivision() {

    }

    private static void createManagerUser(InstallationProcess process) {
        Bennu bennu = Bennu.getInstance();
        final User user = new User(process.adminUsername);
        final Person person = new Person(user);
        person.setName(process.adminName);
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.PERSON));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.MANAGER));
        person.setRootDomainObject(bennu);
        person.setCountry(process.country);
        person.setCountryOfBirth(process.country);
        user.changePassword(process.adminPass);
        process.person = person;

        EmailAddress.createEmailAddress(person, process.email, PartyContactType.PERSONAL, true, true, true, true, true, true);
        for (PartyContact partyContact : person.getPartyContactsSet()) {
            partyContact.setValid();
            partyContact.getPartyContactValidation().setState(PartyContactValidationState.VALID);
        }
        Authenticate.mock(user);
        PersistentAcademicAuthorizationGroup group = new PersistentAcademicAuthorizationGroup(AcademicOperationType.MANAGE_AUTHORIZATIONS, new HashSet<AcademicProgram>(), new HashSet<AdministrativeOffice>());
        group.addMember(person);
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
