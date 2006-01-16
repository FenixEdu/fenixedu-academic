package net.sourceforge.fenixedu.presentationTier.Action.manager.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.cms.messaging.MailingList;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.components.state.LifeCycleConstants;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class SearchAction extends DispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SearchBean bean = new SearchBean();
        bean.setDate(new Date());
        
        Collection c;
        try {
            c = (Collection) ServiceUtils.executeService(null, "ReadAllDomainObjects", new Object[] { MailingList.class });
        }
        catch (Exception e) {
            c = null;
            e.printStackTrace();
        }
        
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
        ViewState viewState = (ViewState) request.getAttribute(LifeCycleConstants.VIEWSTATE_PARAM_NAME);
        return (SearchBean) viewState.getMetaObject().getObject();
    }
}
