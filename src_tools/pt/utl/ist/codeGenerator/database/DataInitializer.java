package pt.utl.ist.codeGenerator.database;

import net.sourceforge.fenixedu._development.MetadataManager;
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
            SuportePersistenteOJB.fixDescriptors();
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

}
