package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.PageContainerBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class PagesTestAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PageContainerBean pageContainerBean = getRenderedObject("page");
        if (pageContainerBean == null) {
            pageContainerBean = new PageContainerBean();
        } else {
            RenderUtils.invalidateViewState();
        }

        return setObjects(mapping, form, request, response, pageContainerBean);
    }

    public ActionForward doSomething(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PageContainerBean pageContainerBean = getRenderedObject("page");
        pageContainerBean.setSelected(null);
        RenderUtils.invalidateViewState();
        return setObjects(mapping, form, request, response, pageContainerBean);
    }

    private ActionForward setObjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, PageContainerBean pageContainerBean) {
        pageContainerBean.setObjects((List) rootDomainObject.getExecutionPeriodsSet());
        request.setAttribute("pagesBean", pageContainerBean);
        return mapping.findForward("showPage");
    }
}
