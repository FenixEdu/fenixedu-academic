package net.sourceforge.fenixedu.dataTransferObject.person;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public class SimpleSearchPersonWithStudentBean implements Serializable {

    private String name;

    private String email;

    private String username;

    private String documentIdNumber;

    private IDDocumentType idDocumentType;

    private String roleType;

    private DegreeType degreeType;

    private Integer degreeId;

    private Integer departmentId;

    private Boolean activePersons;

    private Integer studentNumber;

    public SimpleSearchPersonWithStudentBean() {
	super();
    }

    public Boolean getActivePersons() {
	return activePersons;
    }

    public void setActivePersons(Boolean activePersons) {
	this.activePersons = activePersons;
    }

    public Integer getDegreeId() {
	return degreeId;
    }

    public void setDegreeId(Integer degreeId) {
	this.degreeId = degreeId;
    }

    public DegreeType getDegreeType() {
	return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
	this.degreeType = degreeType;
    }

    public Integer getDepartmentId() {
	return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
	this.departmentId = departmentId;
    }

    public String getDocumentIdNumber() {
	return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
	this.documentIdNumber = documentIdNumber;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public IDDocumentType getIdDocumentType() {
	return idDocumentType;
    }

    public void setIdDocumentType(IDDocumentType idDocumentType) {
	this.idDocumentType = idDocumentType;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getRoleType() {
	return roleType;
    }

    public void setRoleType(String roleType) {
	this.roleType = roleType;
    }

    public Integer getStudentNumber() {
	return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
	this.studentNumber = studentNumber;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

}
