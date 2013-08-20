package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.util.BundleUtil;

public class GenericApplication extends GenericApplication_Base {

    public GenericApplication(final GenericApplicationPeriod period, final String email) {
        super();
        final RootDomainObject rdo = RootDomainObject.getInstance();
        setRootDomainObject(rdo);
        setGenericApplicationPeriod(period);
        setApplicationNumber(period.generateApplicationNumber());
        if (email == null || email.isEmpty()) {
            throw new NullPointerException("error.email.cannot.be.null");
        }
        setEmail(email);
        sendEmailForApplication();
        setIdDocumentType(IDDocumentType.IDENTITY_CARD);
        final Unit institutionUnit = rdo.getInstitutionUnit();
        if (institutionUnit != null) {
            setNationality(institutionUnit.getCountry());
        }
    }

    public void sendEmailForApplication() {
        final String subject =
                BundleUtil.getStringFromResourceBundle("resources.CandidateResources", "label.application.email.subject",
                        getGenericApplicationPeriod().getTitle().getContent());
        final String body =
                BundleUtil.getStringFromResourceBundle("resources.CandidateResources", "label.application.email.body",
                        getApplicationNumber(), generateConfirmationLink());
        new Message(getRootDomainObject().getSystemSender(), getEmail(), subject, body);
    }

    private String generateConfirmationLink() {
        final String confirmationCode =
                DigestUtils.sha512Hex(getEmail() + System.currentTimeMillis() + hashCode()
                        + new Random(System.currentTimeMillis()).nextGaussian());
        setConfirmationCode(confirmationCode);
        return PropertiesManager.getProperty("generic.application.email.confirmation.link") + confirmationCode
                + "&applicationExternalId=" + getExternalId();
    }

}
