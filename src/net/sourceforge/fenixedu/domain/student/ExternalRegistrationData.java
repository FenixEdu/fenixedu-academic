package net.sourceforge.fenixedu.domain.student;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.dataTransferObject.student.ExternalRegistrationDataBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExternalRegistrationData extends ExternalRegistrationData_Base {

    public ExternalRegistrationData(Registration registration) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setRegistration(registration);
    }

    public void edit(ExternalRegistrationDataBean externalRegistrationDataBean) {

	Unit institution = externalRegistrationDataBean.getInstitution();
	if (institution == null
		&& !StringUtils.isEmpty(externalRegistrationDataBean.getInstitutionName())) {
	    institution = UnitUtils.readExternalInstitutionUnitByName(externalRegistrationDataBean
		    .getInstitutionName());
	    if (institution == null) {
		institution = Unit.createNewExternalInstitution(externalRegistrationDataBean
			.getInstitutionName());
	    }
	}

	setInstitution(institution);
	setCoordinatorName(externalRegistrationDataBean.getCoordinatorName());
    }
}
