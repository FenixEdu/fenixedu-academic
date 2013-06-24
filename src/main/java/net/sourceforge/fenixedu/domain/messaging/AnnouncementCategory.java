package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class AnnouncementCategory extends AnnouncementCategory_Base {

    public AnnouncementCategory() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public AnnouncementCategory(String namePt, String nameEn, AnnouncementCategoryType type) {
        this();
        this.setName(new MultiLanguageString(Language.pt, namePt).with(Language.en, nameEn));
        this.setType(type);
    }

}
