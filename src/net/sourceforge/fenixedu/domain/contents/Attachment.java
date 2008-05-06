package net.sourceforge.fenixedu.domain.contents;

import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.util.MultiLanguageString;

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

    public Site getSite() {
	Section section = null;
	Item item = getParent(Item.class);
	
	if (item != null) {
	    section = item.getSection();
	} else {
	    section = getParent(Section.class);
	}
	return section == null ? null : section.getSite();
    }
}
