package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class ShowResearchResult extends FenixDispatchAction {

	public ActionForward showPatent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String resultOID = request.getParameter("resultId");
		ResearchResult result = (ResearchResult) RootDomainObject.readDomainObjectByOID(ResearchResult.class, Integer.valueOf(resultOID));
		request.setAttribute("result",result);
		putSiteOnRequest(request);
		return mapping.findForward("showResult");
	}
	
	public ActionForward showPublication(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String resultOID = request.getParameter("resultId");
		ResearchResultPublication result = (ResearchResultPublication) RootDomainObject.readDomainObjectByOID(ResearchResult.class, Integer.valueOf(resultOID));
		request.setAttribute("result",result);
		request.setAttribute("resultPublicationType", result.getClass().getSimpleName());
		putSiteOnRequest(request);
		return mapping.findForward("showResult");
	}

	abstract protected void putSiteOnRequest(HttpServletRequest request);
	
	
}
