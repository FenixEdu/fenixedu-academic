package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.MergeResearchActivityPageContainerBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class MergeResearchActivityDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        MergeResearchActivityPageContainerBean mergeResearchActivityPageContainerBean = getRenderedObject("mergeList");

        if (mergeResearchActivityPageContainerBean == null) {
            mergeResearchActivityPageContainerBean = getNewBean();
        }

        RenderUtils.invalidateViewState();

        return setObjects(mapping, form, request, response, mergeResearchActivityPageContainerBean);
    }

    public ActionForward prepare2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        MergeResearchActivityPageContainerBean mergeResearchActivityPageContainerBean = getRenderedObject("mergeListNotVisible");
        RenderUtils.invalidateViewState();

        return setObjects(mapping, form, request, response, mergeResearchActivityPageContainerBean);
    }

    public ActionForward prepareMerge(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        MergeResearchActivityPageContainerBean mergeResearchActivityPageContainerBean =
                (MergeResearchActivityPageContainerBean) request.getAttribute("mergeBean");
        return setObjects(mapping, form, request, response, mergeResearchActivityPageContainerBean);
    }

    public ActionForward addToMergeList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        MergeResearchActivityPageContainerBean mergeResearchActivityPageContainerBean = getRenderedObject("mergeListNotVisible");
        RenderUtils.invalidateViewState();
        mergeResearchActivityPageContainerBean.addSelected();

        return setObjects(mapping, form, request, response, mergeResearchActivityPageContainerBean);
    }

    public ActionForward removeFromMergeList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        MergeResearchActivityPageContainerBean mergeResearchActivityPageContainerBean = getRenderedObject("mergeList");
        RenderUtils.invalidateViewState();
        mergeResearchActivityPageContainerBean.setSelected(null);
        return setObjects(mapping, form, request, response, mergeResearchActivityPageContainerBean);
    }

    public ActionForward goToPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        MergeResearchActivityPageContainerBean researchActivityPageContainerBean = getRenderedObject("page");
        RenderUtils.invalidateViewState();
        return setObjects(mapping, form, request, response, researchActivityPageContainerBean);
    }

    protected ActionForward setObjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, MergeResearchActivityPageContainerBean mergeResearchActivityPageContainerBean) {
        mergeResearchActivityPageContainerBean.setObjects(getObjects(mergeResearchActivityPageContainerBean));
        request.setAttribute("mergeBean", mergeResearchActivityPageContainerBean);
        return mapping.findForward("show-research-activity-merge-page");
    }

    public ActionForward prepareResearchActivityMerge(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return pageTwo(mapping, form, request, response, "mergeList", true);
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return pageTwo(mapping, form, request, response, "researchActivity", false);
    }

    private ActionForward pageTwo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String id, boolean invalidate) {
        MergeResearchActivityPageContainerBean researchActivityPageContainerBean = getRenderedObject(id);
        if (invalidate) {
            RenderUtils.invalidateViewState();
        }
        request.setAttribute("mergeList", researchActivityPageContainerBean);
        return mapping.findForward("show-research-activity-merge-list");
    }

    public ActionForward mergeResearchActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        MergeResearchActivityPageContainerBean researchActivityPageContainerBean = getRenderedObject("researchActivity");

        executeService(researchActivityPageContainerBean);

        researchActivityPageContainerBean.reset();
        return setObjects(mapping, form, request, response, researchActivityPageContainerBean);
    }

    public ActionForward back(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        MergeResearchActivityPageContainerBean researchActivityPageContainerBean = getRenderedObject("mergeListNotVisible");
        RenderUtils.invalidateViewState();
        return setObjects(mapping, form, request, response, researchActivityPageContainerBean);
    }

    protected abstract List getObjects(MergeResearchActivityPageContainerBean researchActivityPageContainerBean);

    protected abstract MergeResearchActivityPageContainerBean getNewBean();

    protected abstract void executeService(MergeResearchActivityPageContainerBean bean) throws NotAuthorizedException;

}
