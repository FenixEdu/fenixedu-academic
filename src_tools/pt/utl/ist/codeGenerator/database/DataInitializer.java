package pt.utl.ist.codeGenerator.database;

import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class DataInitializer {

    private static ISuportePersistente persistentSupport = null;

    public static void main(String[] args) {
        try {
            persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
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
        DomainFactory.makeRole(RoleType.PERSON, "/person", "/index.do", "portal.person");
        DomainFactory.makeRole(RoleType.STUDENT, "/student", "/index.do", "portal.student");
        DomainFactory.makeRole(RoleType.TEACHER, "/teacher", "/index.do", "portal.teacher");
        DomainFactory.makeRole(RoleType.TIME_TABLE_MANAGER, "/sop", "/paginaPrincipal.jsp", "portal.sop");
        DomainFactory.makeRole(RoleType.MASTER_DEGREE_CANDIDATE, "/candidato", "/index.do", "portal.candidate");
        DomainFactory.makeRole(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE, "/posGraduacao", "/index.do", "portal.masterDegree");
        DomainFactory.makeRole(RoleType.TREASURY, "/treasury", "/index.do", "portal.treasury");
        DomainFactory.makeRole(RoleType.COORDINATOR, "/coordinator", "/index.do", "portal.coordinator");
        DomainFactory.makeRole(RoleType.EMPLOYEE, "/employee", "/index.do", "portal.employee");
        DomainFactory.makeRole(RoleType.MANAGEMENT_ASSIDUOUSNESS, "/managementAssiduousness", "/index.do", "portal.managementAssiduousness");
        DomainFactory.makeRole(RoleType.MANAGER, "/manager", "/index.do", "portal.manager");
        DomainFactory.makeRole(RoleType.CREDITS_MANAGER, "/facultyAdmOffice", "/index.do", "portal.credits");
        DomainFactory.makeRole(RoleType.DEPARTMENT_CREDITS_MANAGER, "/departmentAdmOffice", "/index.do", "portal.credits.department");
        DomainFactory.makeRole(RoleType.GRANT_OWNER, "/grantOwner", "", "");
        DomainFactory.makeRole(RoleType.SEMINARIES_COORDINATOR, "/teacher", "/seminariesIndex.jsp", "portal.seminariesCoordinator");
        DomainFactory.makeRole(RoleType.OPERATOR, "/operator", "/index.do", "portal.operator");
        DomainFactory.makeRole(RoleType.WEBSITE_MANAGER, "/webSiteManager", "/index.do", "portal.webSiteManager");
        DomainFactory.makeRole(RoleType.GRANT_OWNER_MANAGER, "/facultyAdmOffice", "/index.do", "portal.facultyAdmOffice");
        DomainFactory.makeRole(RoleType.DEPARTMENT_MEMBER, "/departmentAdmOffice", "/index.do", "portal.departmentAdmOffice");
        DomainFactory.makeRole(RoleType.GEP, "/gep", "/index.do", "portal.gep");
        DomainFactory.makeRole(RoleType.DIRECTIVE_COUNCIL, "/directiveCouncil", "/index.do", "portal.directiveCouncil");
        DomainFactory.makeRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE, "/degreeAdministrativeOffice", "/index.do", "portal.degreeAdministrativeOffice");
        DomainFactory.makeRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER, "/degreeAdministrativeOffice", "/index.do", "portal.degreeAdministrativeOfficeSuperUser");
        DomainFactory.makeRole(RoleType.DELEGATE, "/delegate", "/index.do", "portal.delegate");
        DomainFactory.makeRole(RoleType.FIRST_TIME_STUDENT, "/schoolRegistration", "/index.do", "portal.schoolRegistration");
        DomainFactory.makeRole(RoleType.PROJECTS_MANAGER, "/projectsManagement", "/index.do", "portal.projectsManager");
        DomainFactory.makeRole(RoleType.INSTITUCIONAL_PROJECTS_MANAGER, "/institucionalProjectsManagement", "/institucionalProjectIndex.do", "portal.institucionalProjectsManager");
    }

    private static void createCurricularYearsAndSemesters() {
        DomainFactory.makeCurricularYear(Integer.valueOf(1), 2);
        DomainFactory.makeCurricularYear(Integer.valueOf(2), 2);
        DomainFactory.makeCurricularYear(Integer.valueOf(3), 2);
        DomainFactory.makeCurricularYear(Integer.valueOf(4), 2);
        DomainFactory.makeCurricularYear(Integer.valueOf(5), 2);
    }

}
