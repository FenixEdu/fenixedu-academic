package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.net.MalformedURLException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.UnitSiteProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

public class UnitSiteVisualizationDA extends SiteVisualizationDA {

    public static final int ANNOUNCEMENTS_NUMBER = 3;
    public static final String ANNOUNCEMENTS_NAME = "Anúncios";
    public static final String EVENTS_NAME = "Eventos";
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Unit unit = getUnit(request);
    	
        if (unit != null) {
            request.setAttribute("unit", unit);
            request.setAttribute("site", unit.getSite());
        }
        
        request.setAttribute("announcementActionVariable", getMappingPath(mapping, "announcementsAction"));
        request.setAttribute("eventActionVariable", getMappingPath(mapping, "eventsAction"));
        request.setAttribute("announcementRSSActionVariable", getMappingPath(mapping, "announcementsRSSAction"));
        request.setAttribute("eventRSSActionVariable", getMappingPath(mapping, "eventsRSSAction"));
        request.setAttribute("siteContextParam", getContextParamName(request));
        request.setAttribute("siteContextParamValue", getContextParamValue(request));
        
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	return presentation(mapping, form, request, response);
    }
    
    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        Unit unit = getUnit(request);

        try {
            return RequestUtils.absoluteURL(request, UnitSiteProcessor.getUnitSitePath(unit)).toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }
    
    protected String getContextParamName(HttpServletRequest request) {
		return "unitID";
	}

	protected Object getContextParamValue(HttpServletRequest request) {
		return getUnit(request).getIdInternal();
	}

	protected String getMappingPath(ActionMapping mapping, String name) {
		ActionForward forward = mapping.findForward(name);
		
		return forward == null ? null : forward.getPath();
	}

    public ActionForward presentation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        Unit unit = getUnit(request);
        UnitSite site = unit.getSite();
        
        AnnouncementBoard announcementsBoard = null;
        AnnouncementBoard eventsBoard = null;
        
        for (AnnouncementBoard unitBoard : unit.getBoards()) {
            if (unitBoard.isPublicToRead() && unitBoard.getName().equals(ANNOUNCEMENTS_NAME)) {
                announcementsBoard = unitBoard;
            }

            if (unitBoard.isPublicToRead() && unitBoard.getName().equals(EVENTS_NAME)) {
                eventsBoard = unitBoard;
            }
        }
        
        if (announcementsBoard != null) {
            List<Announcement> announcements = announcementsBoard.getActiveAnnouncements();
            announcements = announcements.subList(0, Math.min(announcements.size(), ANNOUNCEMENTS_NUMBER));
            request.setAttribute("announcements", announcements);
        }
        
        if (eventsBoard != null) {
            List<Announcement> announcements = eventsBoard.getActiveAnnouncements();
            announcements = announcements.subList(0, Math.min(announcements.size(), ANNOUNCEMENTS_NUMBER));
            request.setAttribute("eventAnnouncements", announcements);
        }

        return mapping.findForward("frontPage-" + site.getLayout());
    }
    
    protected Unit getUnit(HttpServletRequest request) {
        Unit unit = (Unit) request.getAttribute("unit");
        
        if (unit == null) {
            Integer id = getIntegerFromRequest(request, getContextParamName(request));
            unit = (Unit) RootDomainObject.getInstance().readPartyByOID(id);
        }
        
        return unit;
    }

    public ActionForward organization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
    	return mapping.findForward("unit-organization");
    }
    
    public ActionForward subunits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
    	Unit unit = getUnit(request);
    	
    	SortedSet<Unit> subunits = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
    	for (Unit sub : unit.getSubUnits()) {
    		if (sub.hasSite()) {
    			subunits.add(sub);
    			
    			String siteUrl = getSiteUrl(mapping, sub);
    			request.setAttribute("viewSite" + sub.getIdInternal(), siteUrl);
    		}
    	}
    	
    	request.setAttribute("subunits", subunits);
    	return mapping.findForward("unit-subunits");
    }

	protected String getSiteUrl(ActionMapping mapping, Unit sub) {
		ActionForward forward = mapping.findForward("view" + sub.getClass().getSimpleName() + "Site");
		
		if (forward == null) {
			forward = mapping.findForward("viewUnitSite");
		}
		
		return String.format(forward.getPath(), sub.getIdInternal(), sub.getSite().getIdInternal());
	}

}
