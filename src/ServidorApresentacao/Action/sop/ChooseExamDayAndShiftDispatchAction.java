/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2003/03/19
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.Util;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamDayAndShiftDispatchAction extends DispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);

		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);

		ArrayList horas = Util.getHours();
		session.setAttribute("horas", horas);

		ArrayList daysOfMonth = Util.getDaysOfMonth();
		session.setAttribute("daysOfMonth", daysOfMonth);

		ArrayList monthsOfYear = Util.getMonthsOfYear();
		session.setAttribute("monthsOfYear", monthsOfYear);

		ArrayList years = Util.getYears();
		session.setAttribute("yearsList", years);

		return mapping.findForward("Show Choose Form");

	}

	public ActionForward choose(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);

		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);

		DynaValidatorForm chooseDayAndShiftForm = (DynaValidatorForm) form;

		Integer day = new Integer((String) chooseDayAndShiftForm.get("day"));
		Integer month = new Integer((String) chooseDayAndShiftForm.get("month"));
		Integer year = new Integer((String) chooseDayAndShiftForm.get("year"));
		Integer beginning = new Integer((String) chooseDayAndShiftForm.get("beginning"));

		return mapping.findForward("View Exams");
	}

}