package net.sourceforge.fenixedu.presentationTier.Action.publico.scientificalArea;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteVisualizationDA;

public class PublicScientificAreaSiteDA extends UnitSiteVisualizationDA {

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return presentation(mapping, form, request, response);
    }
}
