package net.sourceforge.fenixedu.domain.contents;

import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Attachment extends Attachment_Base {

    protected Attachment() {
        super();
    }

    public Attachment(FileContent fileContent) {
        this();
        setFile(fileContent);
    }

    @Override
    protected void disconnect() {
        if (hasFile()) {
            getFile().delete();
        }
        super.disconnect();
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString(getFile().getDisplayName());
    }

    @Override
    public void setNormalizedName(final MultiLanguageString normalizedName) {
        // unable to optimize because we cannot track changes to name correctly.
        // don't call super.setNormalizedName() !
    }

    @Override
    public boolean isParentAccepted(Container parent) {
        return parent instanceof Item || parent instanceof Section || parent instanceof AnnouncementBoard;
    }

    private Section getSection() {
        final Item item = getUniqueParentContainer(Item.class);
        return item == null ? getUniqueParentContainer(Section.class) : item.getSection();
    }

    public Site getSite() {
        final Section section = getSection();
        if (section != null) {
            return section.getSite();
        }
        final AnnouncementBoard announcementBoard = getAnnouncementBoard();
        return announcementBoard == null ? null : announcementBoard.getSite();
    }

    private AnnouncementBoard getAnnouncementBoard() {
        for (final Node node : getParentsSet()) {
            final Container parent = node.getParent();
            if (parent instanceof AnnouncementBoard) {
                return (AnnouncementBoard) parent;
            }
        }
        return null;
    }

    @Override
    public void delete() {
        logRemoveFile();
        setFile(null);
        super.delete();
    }

    protected void logRemoveFile() {
        Site site = getSite();
        if (site != null) {
            site.logRemoveFile(this);
        }

    }

    public void logEditFile() {
        Site site = getSite();
        if (site != null) {
            site.logEditFile(this);
        }
    }

    public void logEditFileToItem() {
        Site site = getSite();
        Section section = getSection();
        if (site != null && section != null) {
            site.logEditFileToItem(this, section);
        }
    }

    public void logItemFilePermittedGroup() {
        Site site = getSite();
        Section section = getSection();
        if (site != null && section != null) {
            site.logItemFilePermittedGroup(this, section);
        }

    }

    @Deprecated
    public boolean hasFile() {
        return getFile() != null;
    }

}
