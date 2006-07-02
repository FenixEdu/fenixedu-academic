package net.sourceforge.fenixedu.domain.person;

import java.util.ArrayList;
import java.util.List;

public enum RoleType {

    PERSON,

    STUDENT,

    TEACHER,
    
    DEPARTMENT_MEMBER,

    TIME_TABLE_MANAGER,

    MASTER_DEGREE_CANDIDATE,

    MASTER_DEGREE_ADMINISTRATIVE_OFFICE,

    TREASURY,

    COORDINATOR,

    EMPLOYEE,

    MANAGEMENT_ASSIDUOUSNESS,

    MANAGER,

    DEGREE_ADMINISTRATIVE_OFFICE,

    CREDITS_MANAGER,

    DEPARTMENT_CREDITS_MANAGER,

    ERASMUS,

    DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER,

    SCIENTIFIC_COUNCIL,

    ADMINISTRATOR,

    OPERATOR,

    SEMINARIES_COORDINATOR,

    WEBSITE_MANAGER,

    GRANT_OWNER,

    GRANT_OWNER_MANAGER,

    DEPARTMENT_ADMINISTRATIVE_OFFICE,

    GEP,

    DIRECTIVE_COUNCIL,

    DELEGATE,

    FIRST_TIME_STUDENT,

    PROJECTS_MANAGER,

    INSTITUCIONAL_PROJECTS_MANAGER,    

    BOLONHA_MANAGER,

    CMS_MANAGER,

    SPACE_MANAGER,
    
    RESEARCHER,
    
    ALUMNI,
    
    MESSAGING,

    PEDAGOGICAL_COUNCIL,
    
    CANDIDATE;

    public String getName() {
        return name();    
    }
    
    public static List<RoleType> getRolesImportance(){
        List<RoleType> rolesImportance = new ArrayList<RoleType>();
        rolesImportance.add(RoleType.TEACHER);
        rolesImportance.add(RoleType.EMPLOYEE);
        rolesImportance.add(RoleType.STUDENT);
        rolesImportance.add(RoleType.GRANT_OWNER);
        rolesImportance.add(RoleType.INSTITUCIONAL_PROJECTS_MANAGER);
        rolesImportance.add(RoleType.PROJECTS_MANAGER);
        rolesImportance.add(RoleType.MASTER_DEGREE_CANDIDATE);
        rolesImportance.add(RoleType.CANDIDATE);
        rolesImportance.add(RoleType.ALUMNI);
        rolesImportance.add(RoleType.PERSON);
        return rolesImportance;
    }

}
