package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.dataTransferObject.student.ExternalRegistrationDataBean;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExternalRegistrationData extends ExternalRegistrationData_Base {

    public ExternalRegistrationData(Registration registration) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setRegistration(registration);
    }

    public void edit(ExternalRegistrationDataBean externalRegistrationDataBean) {

        Unit institution = externalRegistrationDataBean.getInstitution();
        if (institution == null && !StringUtils.isEmpty(externalRegistrationDataBean.getInstitutionName())) {
            institution = UnitUtils.readExternalInstitutionUnitByName(externalRegistrationDataBean.getInstitutionName());
            if (institution == null) {
                institution = Unit.createNewNoOfficialExternalInstitution(externalRegistrationDataBean.getInstitutionName());
            }
        }

        setInstitution(institution);
        setCoordinatorName(externalRegistrationDataBean.getCoordinatorName());
    }

    public void delete() {
        setRootDomainObject(null);
        setInstitution(null);
        setRegistration(null);
        super.deleteDomainObject();
    }
    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCoordinatorName() {
        return getCoordinatorName() != null;
    }

    @Deprecated
    public boolean hasInstitution() {
        return getInstitution() != null;
    }

}
