package net.sourceforge.fenixedu.presentationTier.Action.manager.extraCurricularActivities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/manageExtraCurricularActivities", module = "manager")
@Forwards({ @Forward(name = "index", path = "/manager/extraCurricularActivities/index.jsp"),
		@Forward(name = "tableInputStatus", path = "/manager/ectsComparabilityTables/tableInputStatus.jsp") })
public class ManageExtraCurricularActivityTypesDA extends FenixDispatchAction {
	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		RenderUtils.invalidateViewState();
		request.setAttribute("types", rootDomainObject.getExtraCurricularActivityTypeSet());
		return mapping.findForward("index");
	}
}
