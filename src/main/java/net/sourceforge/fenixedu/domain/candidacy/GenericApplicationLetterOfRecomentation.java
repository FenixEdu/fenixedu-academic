package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.domain.accessControl.NoOneGroup;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class GenericApplicationLetterOfRecomentation extends GenericApplicationLetterOfRecomentation_Base {

    public GenericApplicationLetterOfRecomentation(GenericApplicationRecomentation recomentation, String displayName,
            String fileName, byte[] content) {
        super();
        final Collection<FileSetMetaData> metadata = Collections.emptySet();
        init(getVirtualPath(recomentation), fileName, displayName, metadata, content, new NoOneGroup());
        setRecomentation(recomentation);
        sendEmailForRecommendationUploadNotification();
    }

    protected VirtualPath getVirtualPath(final GenericApplicationRecomentation recomentation) {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode("GenericApplication", "GenericApplication"));
        filePath.addNode(new VirtualPathNode("GenericRecomentation" + recomentation.getExternalId(), recomentation.getName()));
        return filePath;
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

        new Message(getRootDomainObject().getSystemSender(), getRecomentation().getGenericApplication().getEmail(), subject, body);
    }

}
