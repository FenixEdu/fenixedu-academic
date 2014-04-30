package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class FindPersonBean implements Serializable {
    private static final long serialVersionUID = -7868952167229025567L;

    private RoleType roleType;
    private DegreeType degreeType;
    private Degree degree;
    private Department department;
    private String name;
    private Boolean viewPhoto;

    public FindPersonBean() {

    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public DegreeType getDegreeType() {
        if (!RoleType.STUDENT.equals(roleType)) {
            return null;
        }
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getViewPhoto() {
        return viewPhoto;
    }

    public void setViewPhoto(Boolean viewPhoto) {
        this.viewPhoto = viewPhoto;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public String getDepartmentExternalId() {
        if (department != null && RoleType.TEACHER.equals(roleType)) {
            return department.getExternalId();
        }
        return null;
    }

    public String getDegreeExternalId() {
        if (degree != null && RoleType.STUDENT.equals(roleType)) {
            return degree.getExternalId();
        }
        return null;
    }

}
