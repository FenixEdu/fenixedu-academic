package pt.utl.ist.codeGenerator.database;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class DataInitializer {

    private static final ISuportePersistente persistentSupport = null;

    public static void main(String[] args) {
        try {
            PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentSupport.iniciarTransaccao();
            initialize();
            persistentSupport.confirmarTransaccao();
        } catch (Exception ex) {
            ex.printStackTrace();

            try {
                persistentSupport.cancelarTransaccao();
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
        new Role(RoleType.DEPARTMENT_MEMBER, "/departmentAdmOffice", "/index.do", "portal.departmentAdmOffice");
        new Role(RoleType.GEP, "/gep", "/index.do", "portal.gep");
        new Role(RoleType.DIRECTIVE_COUNCIL, "/directiveCouncil", "/index.do", "portal.directiveCouncil");
        new Role(RoleType.DEGREE_ADMINISTRATIVE_OFFICE, "/degreeAdministrativeOffice", "/index.do", "portal.degreeAdministrativeOffice");
        new Role(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER, "/degreeAdministrativeOffice", "/index.do", "portal.degreeAdministrativeOfficeSuperUser");
        new Role(RoleType.DELEGATE, "/delegate", "/index.do", "portal.delegate");
        new Role(RoleType.FIRST_TIME_STUDENT, "/schoolRegistration", "/index.do", "portal.schoolRegistration");
        new Role(RoleType.PROJECTS_MANAGER, "/projectsManagement", "/index.do", "portal.projectsManager");
        new Role(RoleType.INSTITUCIONAL_PROJECTS_MANAGER, "/institucionalProjectsManagement", "/institucionalProjectIndex.do", "portal.institucionalProjectsManager");
    }

    private static void createCurricularYearsAndSemesters() {
        new CurricularYear(Integer.valueOf(1), 2);
        new CurricularYear(Integer.valueOf(2), 2);
        new CurricularYear(Integer.valueOf(3), 2);
        new CurricularYear(Integer.valueOf(4), 2);
        new CurricularYear(Integer.valueOf(5), 2);
    }

}
