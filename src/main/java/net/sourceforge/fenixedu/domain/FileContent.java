package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Attachment;
import pt.ist.fenixframework.FenixFramework;

public class FileContent extends FileContent_Base {

    public enum EducationalResourceType {
        EXERCISE("exercise"), SIMULATION("simulation"), QUESTIONNARIE("questionnaire"), FIGURE("figure"), SLIDE("slide"), TABLE(
                "table"), EXAM("exam"), TEST("test"), INFORMATIONS("informations"), MARKSHEET("marksheet"), PROJECT_SUBMISSION(
                "projectSubmission"), LABORATORY_GUIDE("laboratoryGuide"), DIDACTIL_TEXT("didactilText"),
        STUDY_BOOK("studyBook"), SITE_CONTENT("siteContent"), PROGRAM("program"), SUPPORT_TEXT("supportText");

        private String type;

        private EducationalResourceType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }

    protected FileContent() {
        super();
    }

    public FileContent(String filename, String displayName, byte[] content, Group group, EducationalResourceType type) {
        this();
        init(filename, displayName, content, group);
        setResourceType(type);
    }

    @Override
    public void delete() {
        Attachment attachment = getAttachment();
        if (attachment != null) {
            attachment.delete();
            setAttachment(null);
        }
        super.delete();
    }

    public static FileContent readByOID(String externalId) {
        return FenixFramework.getDomainObject(externalId);
    }

    public Site getSite() {
        return getAttachment().getSite();
    }

    private String processDisplayName(String name) {
        return name.replace('\\', '-').replace('/', '-');
    }

    @Override
    public void setDisplayName(String displayName) {
        super.setDisplayName(processDisplayName(displayName));
        final Attachment attachment = getAttachment();
        if (attachment != null) {
            attachment.logEditFileToItem();
        }
    }

    public void logEditFile() {
        final Attachment attachment = getAttachment();
        if (attachment != null) {
            attachment.logEditFile();
        }
    }

    public void logItemFilePermittedGroup() {
        final Attachment attachment = getAttachment();
        if (attachment != null) {
            attachment.logItemFilePermittedGroup();
        }
    }

    @Deprecated
    public boolean hasAttachment() {
        return getAttachment() != null;
    }

}
