package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.List;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public abstract class UnitSiteMenuRenderer extends SiteMenuRenderer {

    @Override
    protected boolean isTopSection(Section section) {
        Section target = getTargetSection(section.getSite());
        return target == null ? super.isTopSection(section) : target.hasAssociatedSections(section);
    }
    
    @Override
    protected List<Section> getSections(Site site) {
        List<Section> sections = getBaseSections(site);
        
        Section section = getTargetSection(site);
        if (section != null) {
            sections.addAll(section.getOrderedSubSections());
        }
        
        return sections;
    }

    private Section getTargetSection(Site site) {
        MultiLanguageString name = getTargetSectionName();
        
        for (Section section : site.getTopLevelSections()) {
            if (equalInAnyLanguage(section.getName(), name)) {
                return section;
            }
        }
        
        return null;
    }

    protected abstract List<Section> getBaseSections(Site site);
    protected abstract MultiLanguageString getTargetSectionName();
    
    protected MultiLanguageString i18n(String pt, String en) {
        MultiLanguageString mls = new MultiLanguageString();
        mls.setContent(Language.pt, pt);
        mls.setContent(Language.en, en);
        
        return mls;
    }
    
    protected boolean equalInAnyLanguage(MultiLanguageString target, MultiLanguageString sub) {
        for (Language language : sub.getAllLanguages()) {
            String text = sub.getContent(language);
            String targetText = target.getContent(language);
            
            if (targetText == null) {
                continue;
            }
            
            if (text.equalsIgnoreCase(targetText)) {
                return true;
            }
        }
        
        return false;
    }
    
}
