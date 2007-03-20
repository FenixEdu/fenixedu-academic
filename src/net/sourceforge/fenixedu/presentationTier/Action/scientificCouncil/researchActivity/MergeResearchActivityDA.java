package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.MergeResearchActivityPageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class MergeResearchActivityDA extends FenixDispatchAction {
    
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MergeResearchActivityPageContainerBean mergeResearchActivityPageContainerBean = (MergeResearchActivityPageContainerBean) getRenderedObject("mergeList");
	
	if(mergeResearchActivityPageContainerBean == null) {
	    mergeResearchActivityPageContainerBean = getNewBean();
	} else {
	    RenderUtils.invalidateViewState();
	}

	return setObjects(mapping, form, request, response, mergeResearchActivityPageContainerBean);
    }
    
    public ActionForward prepare2(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MergeResearchActivityPageContainerBean mergeResearchActivityPageContainerBean = (MergeResearchActivityPageContainerBean) getRenderedObject("mergeListNotVisible");
	RenderUtils.invalidateViewState();

	return setObjects(mapping, form, request, response, mergeResearchActivityPageContainerBean);
    }
    
    public ActionForward addToMergeList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MergeResearchActivityPageContainerBean mergeResearchActivityPageContainerBean = (MergeResearchActivityPageContainerBean) getRenderedObject("mergeListNotVisible");
	RenderUtils.invalidateViewState();
	mergeResearchActivityPageContainerBean.addSelected();
	
	return setObjects(mapping, form, request, response, mergeResearchActivityPageContainerBean);
    }
    
    public ActionForward removeFromMergeList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MergeResearchActivityPageContainerBean mergeResearchActivityPageContainerBean = (MergeResearchActivityPageContainerBean) getRenderedObject("mergeList");
	RenderUtils.invalidateViewState();
	mergeResearchActivityPageContainerBean.removeSelected();
	return setObjects(mapping, form, request, response, mergeResearchActivityPageContainerBean);
    }
    
    
    public ActionForward goToPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MergeResearchActivityPageContainerBean researchActivityPageContainerBean = 
	    (MergeResearchActivityPageContainerBean) getRenderedObject("page");
	RenderUtils.invalidateViewState();
	return setObjects(mapping, form, request, response, researchActivityPageContainerBean);
    }

    protected ActionForward setObjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, MergeResearchActivityPageContainerBean mergeResearchActivityPageContainerBean) {
	mergeResearchActivityPageContainerBean.setObjects(getObjects());
	request.setAttribute("mergeBean", mergeResearchActivityPageContainerBean);
	return mapping.findForward("show-research-activity-merge-page");
    }
    
    public ActionForward prepareResearchActivityMerge(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return pageTwo(mapping, form, request, response, "mergeList", true);
    }
    
    public ActionForward invalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return pageTwo(mapping, form, request, response, "scientificJournal", false);
    }
    
    private ActionForward pageTwo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, String id, boolean invalidate) {
	MergeResearchActivityPageContainerBean researchActivityPageContainerBean = 
	    (MergeResearchActivityPageContainerBean) getRenderedObject(id);
	if(invalidate) {
	    RenderUtils.invalidateViewState();
	}
	request.setAttribute("mergeList", researchActivityPageContainerBean);
	return mapping.findForward("show-research-activity-merge-list");
    }

    
    public ActionForward mergeResearchActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	MergeResearchActivityPageContainerBean researchActivityPageContainerBean = 
	    (MergeResearchActivityPageContainerBean) getRenderedObject("scientificJournal");
	
	executeService(request, getServiceName(), new Object[] { researchActivityPageContainerBean });
	
	return setObjects(mapping, form, request, response, getNewBean());
    }
    
    public ActionForward back(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MergeResearchActivityPageContainerBean researchActivityPageContainerBean = 
	    (MergeResearchActivityPageContainerBean) getRenderedObject("scientificJournal");
	return setObjects(mapping, form, request, response, researchActivityPageContainerBean);
    }


    protected abstract List getObjects();
    
    protected abstract MergeResearchActivityPageContainerBean getNewBean();
    
    protected abstract String getServiceName();

}
