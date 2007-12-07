package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.presentationTier.renderers.functionalities.MenuRenderer;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * This is a specific renderer used to create the side menu of a UnitSite. This
 * renderer is very similar to the standard Site renderer except that it uses
 * the subsections of a specific top level section. 
 * 
 * <p>
 * <ul>
 *  <li>Side
 *  <ul>
 *      <li>A
 *      <li>B
 *  </ul>
 *  <li>C
 * </ul>
 * If the site contains a section structure like the one presented then only the
 * subsections of top (A and B) are presented.
 * 
 * @author cfgi
 */
public class UnitSiteSideMenuRenderer extends UnitSiteMenuRenderer {
    
    @Override
    protected MultiLanguageString getTargetSectionName() {
        return i18n("Lateral", "Side");
    }

    @Override
    protected List<Section> getBaseSections(Site site) {
    	UnitSite unitSite = (UnitSite) site;
    	
    	TreeSet<Section> treeSet = new TreeSet<Section>(Section.COMPARATOR_BY_ORDER);
    	//treeSet.addAll(unitSite.getIntroductionSections());
    	
    	List<Section> sections = new ArrayList<Section>();

    	sections.addAll(treeSet);
//        sections.addAll(site.getOrderedTemplateSections());
        
        return sections;
    }
    
    @Override
    protected SortedSet<Section> getTargetSubSections(Section section) {
    	//UnitSite unitSite = (UnitSite) section.getSite();
    	TreeSet<Section> treeSet = new TreeSet<Section>(Section.COMPARATOR_BY_ORDER);

    	for (Section subSection : section.getAssociatedSections()) {
//    		if (unitSite.hasIntroductionSections(subSection)) {
//    			continue;
//    		}
    		
    		treeSet.add(subSection);
    	}
    	
    	return treeSet;
    }
    
    @Override
    protected String getPath(FilterFunctionalityContext context, Content content) {
	List<String> subPaths = new ArrayList<String>();
	subPaths.add(getTargetSectionName().getContent());
	return MenuRenderer.findPathFor(context.getRequest().getContextPath(), content, context, isTemplatedContent((Site)context.getSelectedContainer(), content) ? Collections.EMPTY_LIST : subPaths);
    }
}