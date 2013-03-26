package net.sourceforge.fenixedu.presentationTier.Action.manager.strutsVsJSF;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriods;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/someStrutsPage", input = "/someStrutsPage.do", attribute = "strutsExampleForm",
        formBean = "strutsExampleForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showListExecutionPeriods", path = "/manager/somePageInStrutsListExecutionPeriods.jsp"),
        @Forward(name = "showFirstPage", path = "/manager/somePageInStruts.jsp") })
public class StrutsExampleDsipatchAction extends FenixDispatchAction {

    public ActionForward showFirstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("showFirstPage");

    }

    public ActionForward showExecutionPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Collection infoExecutionPeriods = ReadExecutionPeriods.run();

        request.setAttribute("executionPeriods", infoExecutionPeriods);

        return mapping.findForward("showListExecutionPeriods");

    }

}