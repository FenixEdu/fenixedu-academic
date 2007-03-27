package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.PageContainerBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PagesTestAction extends FenixDispatchAction {
    
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PageContainerBean pageContainerBean = (PageContainerBean) getRenderedObject("page");
	if(pageContainerBean == null) {
	    pageContainerBean = new PageContainerBean();
	} else {
	    RenderUtils.invalidateViewState();
	}
	
	return setObjects(mapping, form, request, response, pageContainerBean);
    }
    
    public ActionForward doSomething(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PageContainerBean pageContainerBean = (PageContainerBean) getRenderedObject("page");
	System.out.println(pageContainerBean.getSelected());
	pageContainerBean.setSelected(null);
	RenderUtils.invalidateViewState();
	return setObjects(mapping, form, request, response, pageContainerBean);
    }
    
    private ActionForward setObjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, PageContainerBean pageContainerBean) {
	pageContainerBean.setObjects((List) rootDomainObject.getExecutionPeriods());
	request.setAttribute("pagesBean", pageContainerBean);
	return mapping.findForward("showPage");
    }
}
