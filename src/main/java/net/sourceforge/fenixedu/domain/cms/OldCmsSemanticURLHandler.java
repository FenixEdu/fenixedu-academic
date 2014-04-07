package net.sourceforge.fenixedu.domain.cms;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;

import org.fenixedu.bennu.portal.domain.MenuFunctionality;
import org.fenixedu.bennu.portal.servlet.SemanticURLHandler;

import pt.ist.fenixWebFramework.servlets.filters.RequestWrapperFilter;

public class OldCmsSemanticURLHandler implements SemanticURLHandler {

    private static final String SECTION_PATH = "/publico/viewGenericContent.do?method=viewSection";
    private static final String ITEM_PATH = "/publico/viewGenericContent.do?method=viewItem";
    private static final String NOT_FOUND_PATH = "/notFound.jsp";

    @Override
    public void handleRequest(MenuFunctionality functionality, HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        String path = request.getRequestURI().substring(request.getContextPath().length() + functionality.getFullPath().length());
        path = path.startsWith("/") ? path.substring(1) : path;
        String[] parts = path.split("/");
        SiteTemplateController controller = functionality.getSiteTemplate().getController();
        Site site = controller.selectSiteForPath(parts);
        int startIndex = controller.getTrailingPath(site, parts);

        CmsContent content = site == null ? null : findContent(site, null, site.getOrderedSections(), parts, startIndex);

        if (content instanceof TemplatedSectionInstance) {
            content = ((TemplatedSectionInstance) content).getSectionTemplate();
        }

        String dispatch = NOT_FOUND_PATH;

        if (content instanceof TemplatedSection) {
            TemplatedSection section = (TemplatedSection) content;
            dispatch = section.getCustomPath();
        } else if (content instanceof Section) {
            dispatch = SECTION_PATH;
        } else if (content instanceof Item) {
            dispatch = ITEM_PATH;
        }

        request.setAttribute("actual$site", site);
        request.setAttribute("actual$content", content);

        request.getRequestDispatcher(dispatch).forward(RequestWrapperFilter.getFenixHttpServletRequestWrapper(request), response);
    }

    private CmsContent findContent(Site site, CmsContent initial, List<? extends CmsContent> sections, String[] parts,
            int startIndex) {
        if (parts.length == startIndex) {
            if (initial == null) {
                return site.getInitialContent();
            }
            return initial;
        }

        for (CmsContent content : sections) {
            if (content.matchesPath(parts[startIndex]) && content.isAvailable() && content.getVisible()) {
                if (content instanceof Item || content instanceof TemplatedSection || content instanceof TemplatedSectionInstance) {
                    return content;
                } else {
                    Section section = (Section) content;
                    return findContent(site, section, section.getOrderedChildren(), parts, startIndex + 1);
                }
            }
        }

        // Nothing was found :\
        return null;
    }

    public static CmsContent getContent(HttpServletRequest request) {
        return (CmsContent) request.getAttribute("actual$content");
    }

    public static Site getSite(HttpServletRequest request) {
        return (Site) request.getAttribute("actual$site");
    }

    public static void selectSite(HttpServletRequest request, Site site) {
        request.setAttribute("actual$site", site);
    }
}
