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
            //commented out by jpereira - duplicate????
        	//SuportePersistenteOJB.fixDescriptors();
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
        new Role(RoleType.TIME_TABLE_MANAGER, "/sop", "/paginaPrincipal.jsp", "portal.sop");
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
        new Role(RoleType.SEMINARIES_COORDINATOR, "/teacher", "/seminariesIndex.jsp", "portal.seminariesCoordinator");
        new Role(RoleType.OPERATOR, "/operator", "/index.do", "portal.operator");
        new Role(RoleType.WEBSITE_MANAGER, "/webSiteManager", "/index.do", "portal.webSiteManager");
        new Role(RoleType.GRANT_OWNER_MANAGER, "/facultyAdmOffice", "/index.do", "portal.facultyAdmOffice");
        new Role(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE, "/departmentAdmOffice", "/index.do", "portal.departmentAdmOffice");
        new Role(RoleType.GEP, "/gep", "/index.do", "portal.gep");
        new Role(RoleType.DIRECTIVE_COUNCIL, "/directiveCouncil", "/index.do", "portal.directiveCouncil");
        new Role(RoleType.DEGREE_ADMINISTRATIVE_OFFICE, "/degreeAdministrativeOffice", "/index.do", "portal.degreeAdministrativeOffice");
        new Role(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER, "/degreeAdministrativeOffice", "/index.do", "portal.degreeAdministrativeOfficeSuperUser");
        new Role(RoleType.DELEGATE, "/delegate", "/index.do", "portal.delegate");
        new Role(RoleType.FIRST_TIME_STUDENT, "/schoolRegistration", "/index.do", "portal.schoolRegistration");
        new Role(RoleType.PROJECTS_MANAGER, "/projectsManagement", "/index.do", "portal.projectsManager");
        new Role(RoleType.INSTITUCIONAL_PROJECTS_MANAGER, "/institucionalProjectsManagement", "/institucionalProjectIndex.do", "portal.institucionalProjectsManager");
        new Role(RoleType.DEPARTMENT_MEMBER, "/departmentMember", "/index.do", "portal.departmentMember");
        new Role(RoleType.BOLONHA_MANAGER, "/bolonhaManager", "/index.do", "portal.bolonhaManager");
        new Role(RoleType.SCIENTIFIC_COUNCIL, "/scientificCouncil", "/index.do", "portal.scientificCouncil");
        new Role(RoleType.SPACE_MANAGER, "/SpaceManager", "/index.do", "portal.SpaceManager");
        new Role(RoleType.RESEARCHER, "/researcher", "/index.do", "portal.researcher");
        new Role(RoleType.ALUMNI, "/alumin", "/index.do", "portal.alumni");
        new Role(RoleType.MESSAGING, "/messaging", "/index.do", "portal.messaging");
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
    	new Country("Guin�-Bissau", "Guineense-Bissau", "gw");
    	new Country("Mo�ambique", "Mo�ambicana", "mz");
    	new Country("S�o Tom� e Principe", "S�o Tomense", "st");
    	new Country("Brasil", "Brasileira", "br");
    	new Country("B�lgica", "Belga", "be");
    	new Country("Espanha", "Espanhola", "es");
    	new Country("Fran�a", "Francesa", "fr");
    	new Country("Holanda", "Holandesa", "nl");
    	new Country("Irlanda", "Irlandesa", "ie");
    	new Country("It�lia", "Italiana", "it");
    	new Country("Luxemburgo", "Luxemburguesa", "lu");
    	new Country("Alemanha", "Alem�", "de");
    	new Country("Reino Unido", "Brit�nica", "uk");
    	new Country("Gr�cia", "Grega", "gr");
    	new Country("Finl�ndia", "Finlandesa", "fi");
    	new Country("�ustria", "Austr�aca", "at");
    	new Country("Noruega", "Norueguesa", "no");
    	new Country("Su�cia", "Sueca", "se");
    	new Country("Pol�nia", "Polaca", "pl");
    	new Country("R�ssia", "Russa", "ru");
    	new Country("Rom�nia", "Romena", "ro");
    	new Country("Sui�a", "Sui�a", "ch");
    	new Country("�frica do Sul", "Sul-Africana", "za");
    	new Country("Marrocos", "Marroquina", "ma");
    	new Country("R�publica do Zaire", "Zairense", "zr");
    	new Country("Qu�nia", "Queniana", "ke");
    	new Country("L�bia", "L�bia", "ly");
    	new Country("Zimbabu�", "Zimbabu�", "zw");
    	new Country("Palestina", "Palestina", "ps");
    	new Country("Ir�o", "Iraniana", "ir");
    	new Country("Paquist�o", "Paquistanesa", "pk");
    	new Country("Austr�lia", "Australiana", "au");
    	new Country("Irlanda", "Irlandesa", "ie");
    	new Country("Estados Unidos da Am�rica", "Norte Americana", "us");
    	new Country("Paraguai", "Paraguaia", "py");
    	new Country("Canad�", "Canadiana", "ca");
    	new Country("Argentina", "Argentina", "ar");
    	new Country("Chile", "Chilena", "cl");
    	new Country("Equador", "Equatoriana", "ec");
    	new Country("Venezuela", "Venezuelana", "ve");
    	new Country("M�xico", "Mexicana", "mx");
    	new Country("Bulg�ria", "Bulgara", "bg");
    	new Country("Colombia", "Colombiana", "co");
    	new Country("Eslov�quia", "Eslovaca", "sk");
    	new Country("China", "Chinesa", "cn");
    	new Country("Timor Loro Sae", "Timorense", "tp");
    	new Country("Dinamarca", "Dinamarquesa", "dk");
    	new Country("Iraque", "Iraquiana", "iq");
    	new Country("Per�", "Peruana", "pe");
    	new Country("Rep�blica Checa", "Checa", "cz");
    	new Country("Turquia", "Turca", "tk");
    	new Country("S�rvia e Montenegro", "S�rvia e Montenegro", "cs");
    	new Country("Jugosl�via", "Jugoslava", "yu");
    	new Country("�ndia", "Indiana", "in");
    	new Country("Coreia", "Coreana", "kr");
    	new Country("Ucr�nia", "Ucraniana", "ua");
    	new Country("Eslov�nia", "Eslovena", "si");
    	new Country("Vietname", "Vietnamita", "vn");
    	new Country("Est�nia", "Est�nia", "ee");
    	new Country("Senegal", "Senegalesa", "sn");
    	new Country("Singapura", "Singapura", "sg");
    	new Country("Tail�ndia", "Tailandesa", "th");
    	new Country("Botswana", "Botswana", "bw");
    	new Country("Bol�via", "Boliviana", "bo");
    	new Country("Tun�sia", "Tunisina", "tn");
    	new Country("Kuwait", "Kuwait", "kw");
    	new Country("Costa Rica", "Costariquenho", "cr");
    	new Country("Indon�sia", "Indon�sia", "id");
    	new Country("Jap�o", "Japonesa", "jp");
    	new Country("Israel", "Israelita", "il");
    	new Country("Guatemala", "Guatemalteco", "gp");
    	new Country("Egipto", "Eg�pcia", "eg");
    	new Country("H�ngria", "Hungara", "hu");
    	new Country("Cro�cia", "Croata", "hr");
    	new Country("Maur�cias", "Maur�cias", "mu");
    	new Country("Malta", "Malt�s", "mt");
    	new Country("Porto Rico", "Portoriquenha", "pt");
    	new Country("Chipre", "Cipriota", "cy");
    	new Country("Litu�nia", "Lituana", "lt");
    	new Country("Let�nia", "Let�nia", "lv");
    	new Country("Mal�sia", "Malaia", "my");
    	new Country("Macau", "Macaense", "mc");
    	new Country("Maced�nia", "Maced�nia", "mk");
    	new Country("Coreia do Sul", "Coreana", "kr");
    	new Country("Maurit�nia", "Maurit�nia", "mr");
    	new Country("Hong Kong", "Hong Kong", "hk");
    }
}