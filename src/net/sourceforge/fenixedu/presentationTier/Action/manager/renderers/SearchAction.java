package net.sourceforge.fenixedu.presentationTier.Action.manager.renderers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SearchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SearchBean bean = new SearchBean();
        bean.setDate(new Date());
        
        request.setAttribute("bean", bean);
        return mapping.findForward("input");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SearchBean bean = getBean(request);

        List<FoundPerson> foundPeople = new ArrayList<FoundPerson>();
        
        foundPeople.add(new FoundPerson("José António", bean.getMinAge(), bean.getGender()));
        foundPeople.add(new FoundPerson("António José", bean.getMaxAge(), bean.getGender()));
        foundPeople.add(new FoundPerson("Anté Josónio", (bean.getMaxAge() + bean.getMinAge()) / 2, bean.getGender()));
        
        request.setAttribute("found", foundPeople);
        
        return mapping.findForward("results");
    }

    private SearchBean getBean(HttpServletRequest request) {
        ViewState viewState = (ViewState) RenderUtils.getViewState();
        return (SearchBean) viewState.getMetaObject().getObject();
    }
}
