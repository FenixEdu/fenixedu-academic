/*
 * Created on 15/Mai/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.teacher.credits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoShiftPercentage;
import DataBeans.teacher.credits.InfoTeacherShiftPercentage;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class ProfessorShipShiftPercentageDispatchAction extends DispatchAction {

	public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		DynaValidatorForm professorshipShiftPercentageForm = (DynaValidatorForm) form;

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		Integer idInternal = (Integer) professorshipShiftPercentageForm.get("executionCourseInternalCode");
		infoExecutionCourse.setIdInternal(idInternal);

		HttpSession session = request.getSession();
		IUserView userView = SessionUtils.getUserView(request);
		InfoTeacher infoTeacher = (InfoTeacher) session.getAttribute(SessionConstants.INFO_TEACHER);

		Object args[] = { infoTeacher, infoExecutionCourse };

		List infoShiftPercentageList = (List) ServiceUtils.executeService(userView, "ReadTeacherExecutionCourseShiftsPercentage", args);

		//initializeForm(professorshipShiftPercentageForm, infoShiftPercentageList, infoTeacher);

		request.setAttribute("infoShiftPercentageList", infoShiftPercentageList);
		return mapping.findForward("showTable");
	}

	public ActionForward accept(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();

		IUserView userView = SessionUtils.getUserView(request);
		InfoTeacher infoTeacher = (InfoTeacher) session.getAttribute(SessionConstants.INFO_TEACHER);

		List infoTeacherShiftPercentageList = processForm((DynaActionForm) form, request);

		Object[] args = { infoTeacher, getInfoExecutionCourse((DynaActionForm) form), infoTeacherShiftPercentageList };
		List shiftWithErrors = (List) ServiceUtils.executeService(userView, "AcceptTeacherExecutionCourseShiftPercentage", args);

		if (shiftWithErrors.size() > 0) {
			ActionErrors actionErrors = new ActionErrors();

			Iterator iterator = shiftWithErrors.listIterator();
			while (iterator.hasNext()) {
				InfoShift infoShift = (InfoShift) iterator.next();
				actionErrors.add("shiftPercentage", new ActionError("errors.ShiftPercentage", infoShift.getNome()));
			}

			saveErrors(request, actionErrors);
			
			request.setAttribute("executionCourseInternalCode", getInfoExecutionCourse((DynaActionForm) form).getIdInternal());

			return mapping.getInputForward();
		}

		return mapping.findForward("acceptSuccess");
	}

	private InfoExecutionCourse getInfoExecutionCourse(DynaActionForm form) {
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		infoExecutionCourse.setIdInternal((Integer) form.get("executionCourseInternalCode"));
		return infoExecutionCourse;
	}

	/**
		 * @param professorshipShiftPercentageForm
		 * @param infoShiftPercentageList
		 */
	private void initializeForm(
		DynaValidatorForm professorshipShiftPercentageForm,
		List infoShiftPercentageList,
		InfoTeacher infoTeacher) {

		Integer[] shiftsInternalCodes = new Integer[infoShiftPercentageList.size()];

		for (int i = 0; i < infoShiftPercentageList.size(); i++) {
			InfoShiftPercentage infoShiftPercentage = (InfoShiftPercentage) infoShiftPercentageList.get(i);
			shiftsInternalCodes[i] = infoShiftPercentage.getShift().getIdInternal();

			List infoTeacherShiftPercentageList = infoShiftPercentage.getTeacherShiftPercentageList();
			Iterator iterator = infoTeacherShiftPercentageList.iterator();
			while (iterator.hasNext()) {
				InfoTeacherShiftPercentage infoTeacherShiftPercentage = (InfoTeacherShiftPercentage) iterator.next();
				if (infoTeacherShiftPercentage.getInfoProfessorship().getInfoTeacher().equals(infoTeacher)) {
					shiftsInternalCodes[i] = infoShiftPercentage.getShift().getIdInternal();
					break;
				}
			}
		}
		professorshipShiftPercentageForm.set("shiftProfessorships", shiftsInternalCodes);
	}

	private List processForm(DynaActionForm form, HttpServletRequest request) {
		List infoTeacherShiftPercentageList = new ArrayList();

		InfoTeacherShiftPercentage infoTeacherShiftPercentage = new InfoTeacherShiftPercentage();

		Integer[] shiftProfessorships = (Integer[]) form.get("shiftProfessorships");

		for (int i = 0; i < shiftProfessorships.length; i++) {
			Integer shiftInternalCode = shiftProfessorships[i];
			if (shiftInternalCode != null) {
				Double percentage = null;
				try {
					percentage = Double.valueOf(request.getParameter("percentage_" + shiftInternalCode));
				} catch (NumberFormatException e) {

				}

				if (percentage != null) {
					infoTeacherShiftPercentage = new InfoTeacherShiftPercentage();
					infoTeacherShiftPercentage.setPercentage(percentage);

					InfoShift infoShift = new InfoShift();
					infoShift.setIdInternal(shiftInternalCode);
					infoTeacherShiftPercentage.setInfoShift(infoShift);
					infoTeacherShiftPercentageList.add(infoTeacherShiftPercentage);
				}
			}
		}

		return infoTeacherShiftPercentageList;
	}
}
