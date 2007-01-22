package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreateExternalCurricularCourseBean implements Serializable {

    private DomainReference<Unit> parentUnit;
    private String name;
    private String code;
    private boolean enrolStudent;
    private CreateExternalEnrolmentBean externalEnrolmentBean;
    
    
    public CreateExternalCurricularCourseBean(final Unit parentUnit) {
	setParentUnit(parentUnit);
	setExternalEnrolmentBean(new CreateExternalEnrolmentBean());
    }
    
    public Unit getParentUnit() {
	return (this.parentUnit != null) ? this.parentUnit.getObject() : null;
    }

    public void setParentUnit(Unit parentUnit) {
	this.parentUnit = (parentUnit != null) ? new DomainReference<Unit>(parentUnit) : null;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnrolStudent() {
        return enrolStudent;
    }

    public void setEnrolStudent(boolean enrolStudent) {
        this.enrolStudent = enrolStudent;
    }

    public CreateExternalEnrolmentBean getExternalEnrolmentBean() {
        return externalEnrolmentBean;
    }

    public void setExternalEnrolmentBean(CreateExternalEnrolmentBean externalEnrolmentBean) {
        this.externalEnrolmentBean = externalEnrolmentBean;
    }
}
