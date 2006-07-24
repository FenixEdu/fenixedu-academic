package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

public class CertificateRequestEvent extends CertificateRequestEvent_Base {

    protected CertificateRequestEvent() {
        super();
    }

    public CertificateRequestEvent(AdministrativeOffice administrativeOffice, EventType eventType,
            Person person, CertificateRequest certificateRequest) {
        this();
        init(administrativeOffice, eventType, person, certificateRequest);

    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person,
            CertificateRequest certificateRequest) {
        init(administrativeOffice, eventType, person);
        checkParameters(certificateRequest);
        super.setCertificateRequest(certificateRequest);

    }

    private void checkParameters(CertificateRequest certificateRequest) {
        if (certificateRequest == null) {
            throw new DomainException(
                    "error.accounting.events.serviceRequests.CertificateRequestEvent.certificateRequest.cannot.be.null");
        }

    }

    @Override
    public void setCertificateRequest(CertificateRequest certificateRequest) {
        throw new DomainException(
                "error.accounting.events.serviceRequests.CertificateRequestEvent.cannot.modify.certificateRequest");
    }

    @Override
    public Account getToAccount() {
        return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    protected PostingRule getPostingRule(DateTime whenRegistered) {
        return getAdministrativeOffice().getServiceAgreementTemplate()
                .findPostingRuleByEventTypeAndDate(getEventType(), whenRegistered);
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, List<EntryDTO> entryDTOs,
            PaymentMode paymentMode, DateTime whenRegistered) {
        return getPostingRule(whenRegistered).process(responsibleUser, entryDTOs, paymentMode,
                whenRegistered, this, getPerson().getAccountBy(AccountType.EXTERNAL), getToAccount());
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" (").appendLabel(
                getDegree().getDegreeType().name(), "enum").appendLabel(" - ").appendLabel(
                getDegree().getName()).appendLabel(" )");

        return labelFormatter;

    }

    private Degree getDegree() {
        return getCertificateRequest().getStudentCurricularPlan().getDegreeCurricularPlan().getDegree();
    }

    public Integer getNumberOfUnits() {
        return getCertificateRequest().getNumberOfUnits();
    }

    public Integer getNumberOfPages() {
        return getCertificateRequest().getNumberOfPages();
    }

    public boolean isUrgentRequest() {
        return getCertificateRequest().isUrgentRequest();

    }
}
