package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
@Mapping(path = "/chooseContext", module = "resourceAllocationManager", functionality = ExecutionPeriodDA.class)
@Forwards({ @Forward(name = "ShowChooseForm", path = "/resourceAllocationManager/chooseScheduleContext_bd.jsp"),
        @Forward(name = "ManageSchedules", path = "/resourceAllocationManager/manageSchedules_bd.jsp") })
public class ChooseContextDA extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("ShowChooseForm");
    }

    public ActionForward choosePostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("ShowChooseForm");
    }

    public ActionForward choosePostBackToContext(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("ManageSchedules");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("ManageSchedules");
    }
}