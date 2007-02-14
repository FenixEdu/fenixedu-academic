package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.net.MalformedURLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

public class DegreeSiteVisualizationDA extends SiteVisualizationDA {

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("degree-description");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Degree degree = getDegree(request);
        if (degree != null) {
            request.setAttribute("degree", degree);
        }
        
        setPageLanguage(request);
        return super.execute(mapping, actionForm, request, response);
    }

    private void setPageLanguage(HttpServletRequest request) {
        Boolean inEnglish;
        
        String inEnglishParameter = request.getParameter("inEnglish");
        if (inEnglishParameter == null) {
            inEnglish = (Boolean) request.getAttribute("inEnglish");
        }
        else {
            inEnglish = new Boolean(inEnglishParameter);
        }
        
        if (inEnglish == null) {
            inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
        }
        
        request.setAttribute("inEnglish", inEnglish);
    }
    
    public Degree getDegree(HttpServletRequest request) {
        String parameter = request.getParameter("degreeID");
        
        if (parameter == null) {
            return null;
        }
        
        try {
            Integer oid = new Integer(parameter);
            return RootDomainObject.getInstance().readDegreeByOID(oid);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        Degree degree = getDegree(request);
        
        if (degree == null) {
            return null;
        }

        if (degree.getSigla() == null) {
            return null;
        }
        
        try {
            return RequestUtils.absoluteURL(request, "/" + degree.getSigla().toLowerCase()).toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }
    
}
