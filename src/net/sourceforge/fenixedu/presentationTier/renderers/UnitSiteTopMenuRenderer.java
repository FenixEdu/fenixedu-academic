package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MenuEntry;
import net.sourceforge.fenixedu.presentationTier.renderers.functionalities.MenuRenderer;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * This is a specific renderer used to create the side menu of a UnitSite. This
 * renderer is very similar to the standard Site renderer except that it uses
 * the subsections of a specific top level section.
 * 
 * <p>
 * <ul>
 * <li>Top
 * <ul>
 * <li>A
 * <li>B
 * </ul>
 * <li>C
 * </ul>
 * If the site contains a section structure like the one presented then only the
 * subsections of top (A and B) are presented.
 * 
 * @author cfgi
 */
public class UnitSiteTopMenuRenderer extends UnitSiteMenuRenderer {

    @Override
    protected MultiLanguageString getTargetSectionName() {
	return i18n("Topo", "Top");
    }

    @Override
    protected List<Section> getBaseSections(Site site) {
	return new ArrayList<Section>();
    }

    @Override
    protected List<MenuEntry> getDefaultEntries(Site site) {
	return new ArrayList<MenuEntry>();
    }

    @Override
    protected String getPath(FilterFunctionalityContext context, Content content) {
	List<String> subPaths = new ArrayList<String>();
	subPaths.add(Content.normalize(getTargetSectionName().getContent()));
	return MenuRenderer.findPathFor(context.getRequest().getContextPath(), content, context, subPaths);
    }
    
    @Override
    protected boolean allowsSubMenus() {
	return false;
    }
}