package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Random;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.codec.digest.DigestUtils;

import pt.ist.fenixframework.Atomic;

public class GenericApplicationRecomentation extends GenericApplicationRecomentation_Base {

    public GenericApplicationRecomentation(GenericApplication application, String title, String name, String institution, String email) {
        setRootDomainObject(RootDomainObject.getInstance());
        final String confirmationCode =
                DigestUtils.sha512Hex(getEmail() + System.currentTimeMillis() + hashCode()
                        + new Random(System.currentTimeMillis()).nextGaussian());
        setConfirmationCode(confirmationCode);
        setEmail(email);
        setGenericApplication(application);
        setInstitution(institution);
        setTitle(title);
        setName(name);
        sendEmailForRecommendation();
    }

    @Atomic
    public void sendEmailForRecommendation() {
        final String subject =
                BundleUtil.getStringFromResourceBundle("resources.CandidateResources",
                        "label.application.recomentation.email.subject", getGenericApplication().getName());
        final String body =
                BundleUtil.getStringFromResourceBundle("resources.CandidateResources",
                        "label.application.recomentation.email.body", getTitle(), getName(), getGenericApplication().getName(), getGenericApplication()
                                .getGenericApplicationPeriod().getTitle().getContent(), generateConfirmationLink());

        new Message(getRootDomainObject().getSystemSender(), getEmail(), subject, body);
    }

    private String generateConfirmationLink() {
        return PropertiesManager.getProperty("generic.application.email.recommendation.link") + getConfirmationCode()
                + "&recommendationExternalId=" + getExternalId();
    }

}
