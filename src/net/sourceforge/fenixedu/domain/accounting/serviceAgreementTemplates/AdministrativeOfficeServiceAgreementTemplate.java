package net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class AdministrativeOfficeServiceAgreementTemplate extends
        AdministrativeOfficeServiceAgreementTemplate_Base {

    private AdministrativeOfficeServiceAgreementTemplate() {
        super();
    }

    public AdministrativeOfficeServiceAgreementTemplate(AdministrativeOffice administrativeOffice) {
        this();
        init(administrativeOffice);
    }

    private void checkParameters(AdministrativeOffice administrativeOffice) {
        if (administrativeOffice == null) {
            throw new DomainException(
                    "error.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate.administrativeOffice.cannot.be.null");
        }

    }

    protected void init(AdministrativeOffice administrativeOffice) {
        checkParameters(administrativeOffice);
        super.setAdministrativeOffice(administrativeOffice);
    }
    
    @Override
    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
        throw new DomainException(
                "error.accounting.serviceAgreementTemplates.enclosing_type.cannot.modify.administrativeOffice");
    }

}
