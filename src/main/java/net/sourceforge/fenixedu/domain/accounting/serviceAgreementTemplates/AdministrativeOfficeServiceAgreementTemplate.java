package net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class AdministrativeOfficeServiceAgreementTemplate extends AdministrativeOfficeServiceAgreementTemplate_Base {

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
        addAdministrativeOffice(administrativeOffice);
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice> getAdministrativeOffice() {
        return getAdministrativeOfficeSet();
    }

    @Deprecated
    public boolean hasAnyAdministrativeOffice() {
        return !getAdministrativeOfficeSet().isEmpty();
    }

}
