package pt.utl.ist.codeGenerator.database;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentKind;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.StudentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class DataInitializer {

    public static void main(String[] args) {
        
    	MetadataManager.init("build/WEB-INF/classes/domain_model.dml");
        SuportePersistenteOJB.fixDescriptors();
        RootDomainObject.init();
        
    	ISuportePersistente persistentSupport = null;
        try {
        	persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentSupport.iniciarTransaccao();
            initialize();
            persistentSupport.confirmarTransaccao();
        } catch (Exception ex) {
            ex.printStackTrace();

            try {
            	if (persistentSupport != null) {
            		persistentSupport.cancelarTransaccao();
            	}
            } catch (ExcepcaoPersistencia e) {
                throw new Error(e);
            }
        }

        System.out.println("Initialization complete.");
        System.exit(0);
    }

    private static void initialize() {
    	createRoles();
        createCurricularYearsAndSemesters();
        createStudentKinds();
        createCountries();
    }

	private static void createRoles() {
        new Role(RoleType.PERSON, "/person", "/index.do", "portal.person");
        new Role(RoleType.STUDENT, "/student", "/index.do", "portal.student");
        new Role(RoleType.TEACHER, "/teacher", "/index.do", "portal.teacher");
        new Role(RoleType.TIME_TABLE_MANAGER, "/sop", "/paginaPrincipal.do", "portal.sop");
        new Role(RoleType.MASTER_DEGREE_CANDIDATE, "/candidato", "/index.do", "portal.candidate");
        new Role(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE, "/posGraduacao", "/index.do", "portal.masterDegree");
        new Role(RoleType.TREASURY, "/treasury", "/index.do", "portal.treasury");
        new Role(RoleType.COORDINATOR, "/coordinator", "/index.do", "portal.coordinator");
        new Role(RoleType.EMPLOYEE, "/employee", "/index.do", "portal.employee");
        new Role(RoleType.MANAGEMENT_ASSIDUOUSNESS, "/managementAssiduousness", "/index.do", "portal.managementAssiduousness");
        new Role(RoleType.MANAGER, "/manager", "/index.do", "portal.manager");
        new Role(RoleType.CREDITS_MANAGER, "/facultyAdmOffice", "/index.do", "portal.credits");
        new Role(RoleType.DEPARTMENT_CREDITS_MANAGER, "/departmentAdmOffice", "/index.do", "portal.credits.department");
        new Role(RoleType.GRANT_OWNER, "/grantOwner", "", "");
        new Role(RoleType.SEMINARIES_COORDINATOR, "/teacher", "/seminariesIndex.do", "portal.seminariesCoordinator");
        new Role(RoleType.OPERATOR, "/operator", "/index.do", "portal.operator");
        new Role(RoleType.WEBSITE_MANAGER, "/webSiteManager", "/index.do", "portal.webSiteManager");
        new Role(RoleType.GRANT_OWNER_MANAGER, "/facultyAdmOffice", "/index.do", "portal.facultyAdmOffice");
        new Role(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE, "/departmentAdmOffice", "/index.do", "portal.departmentAdmOffice");
        new Role(RoleType.GEP, "/gep", "/index.do", "portal.gep");
        new Role(RoleType.DIRECTIVE_COUNCIL, "/directiveCouncil", "/index.do", "portal.directiveCouncil");
        new Role(RoleType.DEGREE_ADMINISTRATIVE_OFFICE, "/degreeAdministrativeOffice", "/index.do", "portal.degreeAdministrativeOffice");
        new Role(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER, "/degreeAdministrativeOffice", "/index.do", "portal.degreeAdministrativeOfficeSuperUser");
        new Role(RoleType.DELEGATE, "/delegate", "/index.do", "portal.delegate");
        new Role(RoleType.PROJECTS_MANAGER, "/projectsManagement", "/index.do", "portal.projectsManager");
        new Role(RoleType.INSTITUCIONAL_PROJECTS_MANAGER, "/institucionalProjectsManagement", "/institucionalProjectIndex.do", "portal.institucionalProjectsManager");
        new Role(RoleType.DEPARTMENT_MEMBER, "/departmentMember", "/index.do", "portal.departmentMember");
        new Role(RoleType.BOLONHA_MANAGER, "/bolonhaManager", "/index.do", "portal.bolonhaManager");
        new Role(RoleType.SCIENTIFIC_COUNCIL, "/scientificCouncil", "/index.do", "portal.scientificCouncil");
        new Role(RoleType.SPACE_MANAGER, "/SpaceManager", "/index.do", "portal.SpaceManager");
        new Role(RoleType.RESEARCHER, "/researcher", "/index.do", "portal.researcher");
        new Role(RoleType.ALUMNI, "/alumin", "/index.do", "portal.alumni");
        new Role(RoleType.MESSAGING, "/messaging", "/index.do", "portal.messaging");
        new Role(RoleType.CANDIDATE, "/candidate", "/index.do", "portal.candidate");
        new Role(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE, "/academicAdminOffice", "/index.do", "portal.academicAdminOffice");
        new Role(RoleType.PARKING_MANAGER, "/parkingManager", "/index.do", "portal.parkingManager");
    }

    private static void createCurricularYearsAndSemesters() {
        new CurricularYear(Integer.valueOf(1), 2);
        new CurricularYear(Integer.valueOf(2), 2);
        new CurricularYear(Integer.valueOf(3), 2);
        new CurricularYear(Integer.valueOf(4), 2);
        new CurricularYear(Integer.valueOf(5), 2);
    }

    private static void createStudentKinds() {
    	new StudentKind(StudentType.NORMAL, 3, 7, 10);
    	new StudentKind(StudentType.WORKING_STUDENT, 0, 10, 10);
    	new StudentKind(StudentType.FOREIGN_STUDENT, 0, 10, 10);
    	new StudentKind(StudentType.EXTERNAL_STUDENT, 0, 10, 10);
    	new StudentKind(StudentType.OTHER, 0, 10, 10);
	}

    private static void createCountries() {
    	new Country("Portugal", "Portuguesa", "pt");
    	new Country("Angola", "Angolana", "ao");
    	new Country("Cabo Verde", "Cabo-Verdiana", "cv");
    	new Country("Guiné-Bissau", "Guineense-Bissau", "gw");
    	new Country("Moçambique", "Moçambicana", "mz");
    	new Country("São Tomé e Principe", "São Tomense", "st");
    	new Country("Brasil", "Brasileira", "br");
    	new Country("Bélgica", "Belga", "be");
    	new Country("Espanha", "Espanhola", "es");
    	new Country("França", "Francesa", "fr");
    	new Country("Holanda", "Holandesa", "nl");
    	new Country("Irlanda", "Irlandesa", "ie");
    	new Country("Itália", "Italiana", "it");
    	new Country("Luxemburgo", "Luxemburguesa", "lu");
    	new Country("Alemanha", "Alemã", "de");
    	new Country("Reino Unido", "Britânica", "uk");
    	new Country("Grécia", "Grega", "gr");
    	new Country("Finlândia", "Finlandesa", "fi");
    	new Country("Áustria", "Austréaca", "at");
    	new Country("Noruega", "Norueguesa", "no");
    	new Country("Suécia", "Sueca", "se");
    	new Country("Polónia", "Polaca", "pl");
    	new Country("Rússia", "Russa", "ru");
    	new Country("Roménia", "Romena", "ro");
    	new Country("Suiça", "Suiça", "ch");
    	new Country("África do Sul", "Sul-Africana", "za");
    	new Country("Marrocos", "Marroquina", "ma");
    	new Country("Républica do Zaire", "Zairense", "zr");
    	new Country("Quénia", "Queniana", "ke");
    	new Country("Líbia", "Líbia", "ly");
    	new Country("Zimbabué", "Zimbabué", "zw");
    	new Country("Palestina", "Palestina", "ps");
    	new Country("Irão", "Iraniana", "ir");
    	new Country("Paquistão", "Paquistanesa", "pk");
    	new Country("Austrália", "Australiana", "au");
    	new Country("Irlanda", "Irlandesa", "ie");
    	new Country("Estados Unidos da América", "Norte Americana", "us");
    	new Country("Paraguai", "Paraguaia", "py");
    	new Country("Canadá", "Canadiana", "ca");
    	new Country("Argentina", "Argentina", "ar");
    	new Country("Chile", "Chilena", "cl");
    	new Country("Equador", "Equatoriana", "ec");
    	new Country("Venezuela", "Venezuelana", "ve");
    	new Country("México", "Mexicana", "mx");
    	new Country("Bulgária", "Bulgara", "bg");
    	new Country("Colombia", "Colombiana", "co");
    	new Country("Eslováquia", "Eslovaca", "sk");
    	new Country("China", "Chinesa", "cn");
    	new Country("Timor Loro Sae", "Timorense", "tp");
    	new Country("Dinamarca", "Dinamarquesa", "dk");
    	new Country("Iraque", "Iraquiana", "iq");
    	new Country("Perú", "Peruana", "pe");
    	new Country("República Checa", "Checa", "cz");
    	new Country("Turquia", "Turca", "tk");
    	new Country("Sérvia e Montenegro", "Sérvia e Montenegro", "cs");
    	new Country("Jugoslávia", "Jugoslava", "yu");
    	new Country("Índia", "Indiana", "in");
    	new Country("Coreia", "Coreana", "kr");
    	new Country("Ucrânia", "Ucraniana", "ua");
    	new Country("Eslovénia", "Eslovena", "si");
    	new Country("Vietname", "Vietnamita", "vn");
    	new Country("Estónia", "Estónia", "ee");
    	new Country("Senegal", "Senegalesa", "sn");
    	new Country("Singapura", "Singapura", "sg");
    	new Country("Tailândia", "Tailandesa", "th");
    	new Country("Botswana", "Botswana", "bw");
    	new Country("Bolívia", "Boliviana", "bo");
    	new Country("Tunísia", "Tunisina", "tn");
    	new Country("Kuwait", "Kuwait", "kw");
    	new Country("Costa Rica", "Costariquenho", "cr");
    	new Country("Indonésia", "Indonésia", "id");
    	new Country("Japão", "Japonesa", "jp");
    	new Country("Israel", "Israelita", "il");
    	new Country("Guatemala", "Guatemalteco", "gp");
    	new Country("Egipto", "Egípcia", "eg");
    	new Country("Húngria", "Hungara", "hu");
    	new Country("Croácia", "Croata", "hr");
    	new Country("Maurícias", "Maurícias", "mu");
    	new Country("Malta", "Maltês", "mt");
    	new Country("Porto Rico", "Portoriquenha", "pt");
    	new Country("Chipre", "Cipriota", "cy");
    	new Country("Lituânia", "Lituana", "lt");
    	new Country("Letónia", "Letónia", "lv");
    	new Country("Malásia", "Malaia", "my");
    	new Country("Macau", "Macaense", "mc");
    	new Country("Macedónia", "Macedónia", "mk");
    	new Country("Coreia do Sul", "Coreana", "kr");
    	new Country("Mauritânia", "Mauritânia", "mr");
    	new Country("Hong Kong", "Hong Kong", "hk");
    }
}