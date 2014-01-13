package net.sourceforge.fenixedu.presentationTier.Action.research.result.publication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "researcher", path = "/publications/search")
@Forwards(value = { @Forward(name = "SearchPublication", path = "/researcher/result/publications/searchPublication.jsp") })
public class SearchPublicationsAction extends FenixDispatchAction {
    public ActionForward prepareSearchPublication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("SearchPublication");
    }
}