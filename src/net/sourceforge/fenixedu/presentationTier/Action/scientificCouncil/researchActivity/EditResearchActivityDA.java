package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.PageContainerBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class EditResearchActivityDA extends FenixDispatchAction {
    
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PageContainerBean pageContainerBean = (PageContainerBean) getRenderedObject("pageContainerBean");
	
	if(pageContainerBean == null) {
	    pageContainerBean = new PageContainerBean();
	}
	
	RenderUtils.invalidateViewState();
	return setObjects(mapping, form, request, response, pageContainerBean);
    }
 
    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PageContainerBean pageContainerBean = (PageContainerBean) getRenderedObject("pageContainerBean");
	RenderUtils.invalidateViewState();
	request.setAttribute("pageContainerBean", pageContainerBean);
	return mapping.findForward("show-research-activity-edit");
    }
    
    public ActionForward goToPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PageContainerBean pageContainerBean = 
	    (PageContainerBean) getRenderedObject("page");
	RenderUtils.invalidateViewState();
	return setObjects(mapping, form, request, response, pageContainerBean);
    }
    
    public ActionForward back(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PageContainerBean pageContainerBean = (PageContainerBean) getRenderedObject("back");
	pageContainerBean.setSelected(null);
	RenderUtils.invalidateViewState();
	return setObjects(mapping, form, request, response, pageContainerBean);
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PageContainerBean pageContainerBean = (PageContainerBean) getRenderedObject("pageContainerBean");
	request.setAttribute("pageContainerBean", pageContainerBean);
	return mapping.findForward("show-research-activity-edit");
    }

    protected ActionForward setObjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, PageContainerBean pageContainerBean) {
	pageContainerBean.setObjects(getObjects());
	request.setAttribute("pageContainerBean", pageContainerBean);
	return mapping.findForward("show-research-activity-list");
    }
    
    protected abstract List getObjects();
}
