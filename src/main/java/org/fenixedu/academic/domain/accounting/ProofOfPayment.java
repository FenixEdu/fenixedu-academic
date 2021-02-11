package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;
import org.joda.time.DateTime;
import pt.ist.fenixframework.Atomic;

@DeclareMessageTemplate(id = "proof.of.payment.rejected.email",
        bundle = "resources.AccountingResources",
        description = "proof.of.payment.rejected.email.description",
        subject = "proof.of.payment.rejected.email.subject",
        text = "proof.of.payment.rejected.email.body",
        parameters = {
                @TemplateParameter(id = "uploadDate", description = "proof.of.payment.rejected.email.uploadDate"),
                @TemplateParameter(id = "eventDescription", description = "proof.of.payment.rejected.email.eventDescription")
        })
public class ProofOfPayment extends ProofOfPayment_Base {

    public ProofOfPayment(final Event event, final byte[] proofOfPaymentContent, final String contentType) {
        setBennu(Bennu.getInstance());
        setEvent(event);
        setUploadDate(new DateTime());
        new ProofOfPaymentFile(this, proofOfPaymentContent, contentType);
    }

    protected void delete() {
        getProofOfPaymentFile().delete();
        setEvent(null);
        setBennu(null);
        super.deleteDomainObject();
    }

    @Atomic
    public void markAsProcessed(final AccountingTransaction accountingTransaction) {
        if (accountingTransaction.getEvent() != getEvent()) {
            throw new Error("Event does not match: " + accountingTransaction.getEvent().getExternalId()
                    + " != " + getEvent().getExternalId());
        }
        setBennu(null);
        setVerificationDate(new DateTime());
        setVerificationUsername(Authenticate.getUser().getUsername());
        setAccountingTransactionDetail(accountingTransaction.getTransactionDetail());
    }

    @Atomic
    public void reject(final boolean notifyUser) {
        if (notifyUser) {
            final Person person = getEvent().getPerson();
            final String email = person == null ? null : person.getEmailForSendingEmails();
            if (email != null) {
                Message.fromSystem()
                        .singleTos(email)
                        .template("proof.of.payment.rejected.email")
                        .parameter("uploadDate", getUploadDate().toString("yyyy-MM-dd HH:mm"))
                        .parameter("eventDescription", getEvent().getDescription().toString())
                        .and()
                        .wrapped().send();
            }
        }
        setBennu(null);
        setVerificationDate(new DateTime());
        final User user = Authenticate.getUser();
        if (user != null) {
            setVerificationUsername(user.getUsername());
        }
        setAccountingTransactionDetail(null);
    }

}
