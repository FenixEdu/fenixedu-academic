package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Random;

import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class GenericApplicationRecomentation extends GenericApplicationRecomentation_Base {

    public GenericApplicationRecomentation(GenericApplication application, String title, String name, String institution,
            String email) {
        setRootDomainObject(Bennu.getInstance());
        final String confirmationCode =
                Hashing.sha512()
                        .hashString(
                                getEmail() + System.currentTimeMillis() + hashCode()
                                        + new Random(System.currentTimeMillis()).nextGaussian(), Charsets.UTF_8).toString();
        setConfirmationCode(confirmationCode);
        setEmail(email);
        setGenericApplication(application);
        setInstitution(institution);
        setTitle(title);
        setName(name);
        setRequestTime(new DateTime());
        sendEmailForRecommendation();
    }

    @Atomic
    public void sendEmailForRecommendation() {
        final String subject =
                BundleUtil.getStringFromResourceBundle("resources.CandidateResources",
                        "label.application.recomentation.email.subject", getGenericApplication().getName());
        final String body =
                BundleUtil
                        .getStringFromResourceBundle("resources.CandidateResources",
                                "label.application.recomentation.email.body", getTitle(), getName(), getGenericApplication()
                                        .getName(),
                                getGenericApplication().getGenericApplicationPeriod().getTitle().getContent(),
                                generateConfirmationLink());

        new Message(getRootDomainObject().getSystemSender(), getEmail(), subject, body);
    }

    private String generateConfirmationLink() {
        return FenixConfigurationManager.getConfiguration().getGenericApplicationEmailRecommendationLink()
                + getConfirmationCode() + "&recommendationExternalId=" + getExternalId();
    }

    @Deprecated
    public boolean hasGenericApplication() {
        return getGenericApplication() != null;
    }

    @Deprecated
    public boolean hasLetterOfRecomentation() {
        return getLetterOfRecomentation() != null;
    }

}
