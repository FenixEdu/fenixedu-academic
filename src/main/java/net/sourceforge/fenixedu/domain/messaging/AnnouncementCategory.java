package net.sourceforge.fenixedu.domain.messaging;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class AnnouncementCategory extends AnnouncementCategory_Base {

    public AnnouncementCategory() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public AnnouncementCategory(String namePt, String nameEn, AnnouncementCategoryType type) {
        this();
        this.setName(new MultiLanguageString(Language.pt, namePt).with(Language.en, nameEn));
        this.setType(type);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.messaging.Announcement> getAnnouncements() {
        return getAnnouncementsSet();
    }

    @Deprecated
    public boolean hasAnyAnnouncements() {
        return !getAnnouncementsSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

}
