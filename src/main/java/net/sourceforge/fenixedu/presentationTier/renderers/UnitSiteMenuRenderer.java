package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.cms.TemplatedSection;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class UnitSiteMenuRenderer extends SiteMenuRenderer {

    @Override
    protected Collection<? extends CmsContent> getInitialEntries(Site site) {
        List<CmsContent> contents = new ArrayList<>();
        contents.addAll(getDefaultEntries(site));
        Section targetSection = getTargetSection(site);
        if (targetSection != null) {
            contents.addAll(targetSection.getOrderedSubSections());
        }
        return contents;
    }

    protected List<TemplatedSection> getDefaultEntries(Site site) {
        return site.getTemplate().getOrderedSections();
    }

    protected Section getTargetSection(Site site) {
        MultiLanguageString name = getTargetSectionName();
        for (Section section : site.getAssociatedSectionSet()) {
            if (equalInAnyLanguage(section.getName(), name)) {
                return section;
            }
        }
        return null;
    }

    protected MultiLanguageString i18n(String pt, String en) {
        return new MultiLanguageString(Language.pt, pt).with(Language.en, en);
    }

    protected abstract MultiLanguageString getTargetSectionName();

    protected boolean equalInAnyLanguage(MultiLanguageString target, MultiLanguageString sub) {
        return target.equalInAnyLanguage(sub);
    }

}