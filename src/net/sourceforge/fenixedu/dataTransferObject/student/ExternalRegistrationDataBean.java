/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.student.ExternalRegistrationData;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExternalRegistrationDataBean implements Serializable {

    private DomainReference<Unit> institution;

    private DomainReference<ExternalRegistrationData> externalRegistrationData;

    private String institutionName;

    private String coordinatorName;

    public ExternalRegistrationDataBean(ExternalRegistrationData externalRegistrationData) {
	super();
	this.externalRegistrationData = new DomainReference<ExternalRegistrationData>(
		externalRegistrationData);
	this.institution = (externalRegistrationData.getInstitution() == null) ? null
		: new DomainReference<Unit>(externalRegistrationData.getInstitution());
	setCoordinatorName(externalRegistrationData.getCoordinatorName());
    }

    public String getCoordinatorName() {
	return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
	this.coordinatorName = coordinatorName;
    }

    public Unit getInstitution() {
	return (institution == null) ? null : institution.getObject();
    }

    public void setInstitution(Unit institution) {
	this.institution = (institution == null) ? null : new DomainReference<Unit>(institution);
    }

    public UnitName getInstitutionUnitName() {
	return (institution == null) ? null : institution.getObject().getUnitName();
    }

    public void setInstitutionUnitName(UnitName institutionUnitName) {
	this.institution = (institution == null) ? null : new DomainReference<Unit>(institutionUnitName
		.getUnit());
    }

    public String getInstitutionName() {
	return institutionName;
    }

    public void setInstitutionName(String institutionName) {
	this.institutionName = institutionName;
    }

    public ExternalRegistrationData getExternalRegistrationData() {
	return externalRegistrationData.getObject();
    }

}
