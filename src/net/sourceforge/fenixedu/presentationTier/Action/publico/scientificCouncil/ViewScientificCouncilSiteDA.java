package net.sourceforge.fenixedu.presentationTier.Action.publico.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteVisualizationDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewScientificCouncilSiteDA extends UnitSiteVisualizationDA {

	@Override
	protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return presentation(mapping, form, request, response);
	}

	
}
