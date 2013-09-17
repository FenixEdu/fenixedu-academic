package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class IRSDeclarationRequest extends IRSDeclarationRequest_Base {

    static final private int FIRST_VALID_YEAR = 2006;

    protected IRSDeclarationRequest() {
        super();
    }

    public IRSDeclarationRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setYear(bean.getYear());
    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
        if (bean.getYear() == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.SchoolRegistrationDeclarationRequest.year.cannot.be.null");
        }

        if (new YearMonthDay(bean.getYear(), 1, 1).isBefore(new YearMonthDay(FIRST_VALID_YEAR, 1, 1))) {
            throw new DomainException("IRSDeclarationRequest.only.available.after.first.valid.year");
        }
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.IRS_DECLARATION;
    }

    @Override
    final public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    final public void setYear(Integer year) {
        throw new DomainException("error.serviceRequests.documentRequests.IRSDeclarationRequest.cannot.modify.year");
    }

    @Override
    final public EventType getEventType() {
        return null;
    }

    @Override
    protected boolean hasFreeDeclarationRequests() {
        return false;
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

}
