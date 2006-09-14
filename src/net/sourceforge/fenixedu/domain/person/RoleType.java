package net.sourceforge.fenixedu.domain.person;

import java.util.ArrayList;
import java.util.List;

public enum RoleType {

    PERSON("Person"),

    STUDENT("Registration"),

    TEACHER("Teacher"),
    
    DEPARTMENT_MEMBER("Department Member"),

    TIME_TABLE_MANAGER("Timetable Management"),

    MASTER_DEGREE_CANDIDATE("Master Degree Candidate"),

    MASTER_DEGREE_ADMINISTRATIVE_OFFICE("Master Degree Administrative Office"),

    TREASURY("Treasury"),

    COORDINATOR("Coordinator"),

    EMPLOYEE("Employee"),

    MANAGEMENT_ASSIDUOUSNESS("Assiduousness Management"),

    MANAGER("Management"),

    DEGREE_ADMINISTRATIVE_OFFICE("Degree Administrative Office"),

    CREDITS_MANAGER("Credits Management"),

    DEPARTMENT_CREDITS_MANAGER("Department Credits Management"),

    ERASMUS("Erasmus"),

    DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER("Degree Administrative Office (Super User)"),

    SCIENTIFIC_COUNCIL("Scientific Council"),

    ADMINISTRATOR("Administration"),

    OPERATOR("Operator"),

    SEMINARIES_COORDINATOR("Seminaries Coordination"),

    WEBSITE_MANAGER("Website Management"),

    GRANT_OWNER("Grant Owner"),

    GRANT_OWNER_MANAGER("Grant Owner Management"),

    DEPARTMENT_ADMINISTRATIVE_OFFICE("Department Administrative Office"),

    GEP("Planning and Studies Office"),

    DIRECTIVE_COUNCIL("Directive Council"),

    DELEGATE("Delegate"),

    FIRST_TIME_STUDENT("First time student"),

    PROJECTS_MANAGER("Projects Management"),

    INSTITUCIONAL_PROJECTS_MANAGER("Institutional Projects Management"),    

    BOLONHA_MANAGER("Bolonha Process Management"),

    CMS_MANAGER("Content Management"),

    SPACE_MANAGER("Space Management"),
    
    RESEARCHER("Researcher"),
    
    ALUMNI("Alumni"),
    
    MESSAGING("Messaging"),

    PEDAGOGICAL_COUNCIL("Pedagogical Council"),
    
    CANDIDATE("Candidate"),

    EXAM_COORDINATOR("Exam Coordinator");
    
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
        rolesImportance.add(RoleType.EXAM_COORDINATOR);
        return rolesImportance;
    }

    private String defaultLabel=null;

    private RoleType(String defaultLabel)
    {
    	setDefaultLabel(defaultLabel);
    }
    
    public String getDefaultLabel()
    {
    	return defaultLabel;
    }
    
    private void setDefaultLabel(String defaultLabel)
    {
    	this.defaultLabel=defaultLabel;
    }
}

