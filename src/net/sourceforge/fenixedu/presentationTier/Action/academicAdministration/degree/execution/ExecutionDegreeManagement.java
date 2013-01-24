package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.degree.execution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/executionDegreeManagement", module = "academicAdministration")
@Forwards({ @Forward(name = "index", path = "/academicAdministration/degree/execution/index.jsp") })
public class ExecutionDegreeManagement extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	DegreeFilterBean bean = new DegreeFilterBean();
	
	request.setAttribute("bean", bean);
	
	return mapping.findForward("index");
    }

    public ActionForward chooseDegreeTypePostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	DegreeFilterBean bean = getBean();
	
	request.setAttribute("bean", bean);

	RenderUtils.invalidateViewState();
	return mapping.findForward("index");
    }

    public ActionForward chooseDegreePostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	DegreeFilterBean bean = getBean();
	request.setAttribute("bean", bean);

	if (bean.getDegree() != null) {
	    request.setAttribute("executionDegrees", ExecutionDegree.getAllByDegree(bean.getDegree()));
	}

	RenderUtils.invalidateViewState();
	return mapping.findForward("index");
    }

    public ActionForward prepareCreateNewExecutionDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return null;
    }

    private DegreeFilterBean getBean() {
	return getRenderedObject("bean");
    }

}
