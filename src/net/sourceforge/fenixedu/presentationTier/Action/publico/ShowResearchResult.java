package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowResearchResult extends FenixDispatchAction {

	public ActionForward showPatent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String resultOID = request.getParameter("result");
		ResearchResult result = (ResearchResult) RootDomainObject.readDomainObjectByOID(ResearchResult.class, Integer.valueOf(resultOID));
		request.setAttribute("result",result);
		putHomePageOnRequest(request);
		return mapping.findForward("showResult");
	}
	
	public ActionForward showPublication(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String resultOID = request.getParameter("result");
		ResearchResultPublication result = (ResearchResultPublication) RootDomainObject.readDomainObjectByOID(ResearchResult.class, Integer.valueOf(resultOID));
		request.setAttribute("result",result);
		request.setAttribute("resultPublicationType", result.getClass().getSimpleName());
		putHomePageOnRequest(request);
		return mapping.findForward("showResult");
	}

	private void putHomePageOnRequest(HttpServletRequest request) {
		String homepageID = request.getParameter("homepageID");
		if(homepageID!=null) {
			Homepage homepage = (Homepage) RootDomainObject.readDomainObjectByOID(Homepage.class, Integer.valueOf(homepageID));
			request.setAttribute("homepage", homepage);
		}
	}
}
