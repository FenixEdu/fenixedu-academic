package net.sourceforge.fenixedu.presentationTier.renderers.functionalities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlList;
import net.sourceforge.fenixedu.renderers.components.HtmlListItem;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

public class SiteMenuRenderer extends MenuRenderer {

    private String sectionUrl;
    
    public String getSectionUrl() {
        return this.sectionUrl;
    }

    public void setSectionUrl(String sectionUrl) {
        this.sectionUrl = sectionUrl;
    }

    @Override
    protected HtmlComponent renderComponent(Layout layout, Object object, Class type) {
        HtmlList list = (HtmlList) super.renderComponent(layout, object, type);
        
        FunctionalityContext context = (FunctionalityContext) object;
        
        Site currentSite = getCurrentSite();
        if (currentSite != null) {
            addSiteSections(context, currentSite, currentSite.getAssociatedSections(null), list);
        }
        
        return list;
    }

    private void addSiteSections(FunctionalityContext context, Site site, List<Section> sections, HtmlList list) {
        List<Section> orderedSections = new ArrayList<Section>(sections);
        Collections.sort(orderedSections, Section.COMPARATOR_BY_ORDER);
        
        HtmlListItem item = list.createItem();
        for (Section section : orderedSections) {
            if (! section.isVisible(context)) {
                continue;
            }
            
            HtmlComponent nameComponent = createSectionNameComponnet(site, section);
            
            // TODO: make a better design and add configuration to this renderer
            if (section.getSuperiorSection() != null) {
                nameComponent.setStyle("margin-left: 1em;");
            }
            
            item.addChild(nameComponent);
            
            if (isSelectedSection(section)) {
                List<Section> subSections = site.getAssociatedSections(section);
                
                if (subSections != null && !subSections.isEmpty()) {
                    HtmlList subList = new HtmlList();

                    addSiteSections(context, site, subSections, subList);
                    item.addChild(subList);
                }
            }
        }
    }

    private boolean isSelectedSection(Section current) {
        Section selectedSection = getSelectedSection();
        
        if (selectedSection == null) {
            return false;
        }
        
        for (Section section = selectedSection; section != null; section = section.getSuperiorSection()) {
            if (section == current) {
                return true;
            }
        }
        
        return false;
    }

    private Section getSelectedSection() {
        HttpServletRequest request = getContext().getViewState().getRequest();
        String idValue = request.getParameter("sectionID");
        
        if (idValue == null) {
            return null;
        }
        else {
            Integer sectionId = new Integer(idValue);
            return RootDomainObject.getInstance().readSectionByOID(sectionId);
        }
    }

    private HtmlComponent createSectionNameComponnet(Site site, Section section) {
        HtmlLink link = new HtmlLink();
        link.setUrl(getSectionUrl());
        
        link.setParameter("sectionID", section.getIdInternal());
        
        // TODO: remove this dependency
        link.setParameter("executionCourseID", ((ExecutionCourseSite) site).getExecutionCourse().getIdInternal());
        
        link.setBody(new HtmlText(section.getName().getContent()));
        
        return link;
    }

    private Site getCurrentSite() {
        HttpServletRequest request = getContext().getViewState().getRequest();
        String idValue = request.getParameter("executionCourseID");
        
        if (idValue == null) {
            Object otherIdValue = request.getAttribute("executionCourseID");
            
            if (otherIdValue != null) {
                idValue = otherIdValue.toString();
            }
        }
        
        Integer executionCourseId = new Integer(idValue);
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseId);
        
        if (executionCourse == null) {
            return null;
        }
        else {
            return executionCourse.getSite();
        }
    }

}
