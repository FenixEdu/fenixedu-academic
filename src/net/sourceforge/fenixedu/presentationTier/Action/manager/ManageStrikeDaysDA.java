package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.StrikeDay;
import net.sourceforge.fenixedu.domain.StrikeDayTask;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager",
	path = "/manageStrikeDays",
	input = "/manageStrikeDays.do?method=prepare",
	scope = "request",
	parameter = "method")
@Forwards(value = { @Forward(name = "manageStrikeDays", path = "/manager/manageStrikeDays.jsp") })
public class ManageStrikeDaysDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("manageStrikeDays");
    }

    public ActionForward deleteStrikeDay(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final StrikeDay strikeDay = getDomainObject(request, "strikeDayOid");
	strikeDay.delete();
	return prepare(mapping, form, request, response);
    }

    public ActionForward reportNow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	StrikeDayTask.reportStrikeDays();
	return prepare(mapping, form, request, response);
    }

}