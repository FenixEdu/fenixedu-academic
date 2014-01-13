package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.accessControl.NoOneGroup;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class GenericApplicationLetterOfRecomentation extends GenericApplicationLetterOfRecomentation_Base {

    public GenericApplicationLetterOfRecomentation(GenericApplicationRecomentation recomentation, String displayName,
            String fileName, byte[] content) {
        super();
        init(fileName, displayName, content, new NoOneGroup());
        setRecomentation(recomentation);
        sendEmailForRecommendationUploadNotification();
    }

    @Atomic
    public void deleteFromApplication() {
        delete();
    }

    @Override
    protected void disconnect() {
        setRecomentation(null);
        super.disconnect();
    }

    public void sendEmailForRecommendationUploadNotification() {
        final String subject =
                BundleUtil.getStringFromResourceBundle("resources.CandidateResources",
                        "label.application.recomentation.upload.notification.email.subject");
        final String body =
                BundleUtil.getStringFromResourceBundle("resources.CandidateResources",
                        "label.application.recomentation.upload.notification.email.body", getRecomentation().getName(),
                        getRecomentation().getInstitution());

        new Message(Bennu.getInstance().getSystemSender(), getRecomentation().getGenericApplication().getEmail(), subject, body);
    }

    @Deprecated
    public boolean hasRecomentation() {
        return getRecomentation() != null;
    }

}
