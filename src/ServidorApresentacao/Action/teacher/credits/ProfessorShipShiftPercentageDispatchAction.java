
/*
 * Created on 15/Mai/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.teacher.credits;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.CreditsView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoShiftProfessorship;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class ProfessorShipShiftPercentageDispatchAction
	extends DispatchAction {

	public ActionForward show(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		DynaValidatorForm professorshipShiftPercentageForm =
			(DynaValidatorForm) form;

		IUserView userView = SessionUtils.getUserView(request);

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		Integer idInternal =
			(Integer) professorshipShiftPercentageForm.get("objectCode");
		infoExecutionCourse.setIdInternal(idInternal);

		InfoTeacher infoTeacher = new InfoTeacher();
		Integer teacherOID =
			new Integer(
				(String) professorshipShiftPercentageForm.get("teacherOID"));
		infoTeacher.setIdInternal(teacherOID);

		Object args[] = { infoTeacher, infoExecutionCourse };

		List infoShiftPercentageList =
			(List) ServiceUtils.executeService(
				userView,
				"ReadTeacherExecutionCourseShiftsPercentage",
				args);
		
		CreditsView creditsView =
			(CreditsView) ServiceUtils.executeService(
				userView,
				"ReadCreditsTeacher",
				new Object[]{teacherOID});
		request.setAttribute("creditsView", creditsView);

		Collections.sort(
			infoShiftPercentageList,
			new BeanComparator("shift.tipo.tipo"));
		
		request.setAttribute(
			"infoShiftPercentageList",
			infoShiftPercentageList);
		return mapping.findForward("showTable");
	}

	public ActionForward accept(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		DynaValidatorForm professorshipShiftPercentageForm =
			(DynaValidatorForm) form;

		IUserView userView = SessionUtils.getUserView(request);
		InfoTeacher infoTeacher = new InfoTeacher();
		Integer teacherOID =
			new Integer(
				(String) professorshipShiftPercentageForm.get("teacherOID"));
		infoTeacher.setIdInternal(teacherOID);
		
		ActionErrors actionErrors = new ActionErrors();
		List infoTeacherShiftPercentageList =
			processForm((DynaActionForm) form, request, actionErrors);
		if (!actionErrors.isEmpty()) {
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}
		Object[] args =
			{
				infoTeacher,
				getInfoExecutionCourse((DynaActionForm) form),
				infoTeacherShiftPercentageList };
		List shiftWithErrors =
			(List) ServiceUtils.executeService(
				userView,
				"AcceptTeacherExecutionCourseShiftPercentage",
				args);

		if (shiftWithErrors.size() > 0) {
			actionErrors = new ActionErrors();

			Iterator iterator = shiftWithErrors.listIterator();
			while (iterator.hasNext()) {
				InfoShift infoShift = (InfoShift) iterator.next();
				actionErrors.add(
					"shiftPercentage",
					new ActionError(
						"errors.ShiftPercentage",
						infoShift.getNome()));
			}

			saveErrors(request, actionErrors);

			// TODO Para que é que se está a fazer isto?
			request.setAttribute(
				"objectCode",
				getInfoExecutionCourse((DynaActionForm) form).getIdInternal());

			return mapping.getInputForward();
		}
		ActionForward forward = getForward(mapping, teacherOID);
		return forward;
	}

	private ActionForward getForward(ActionMapping mapping, Integer teacherOID) {
		ActionForward forward = new ActionForward ();
		ActionForward acceptSuccess = mapping.findForward("acceptSuccess");
		String path = acceptSuccess.getPath();
		
		if (path.indexOf('?') == -1) {
			path += "?";
		}else {
			path += "&amp;";
		}
		forward.setPath(path + "teacherOID=" + teacherOID);
		forward.setContextRelative(acceptSuccess.getContextRelative());
		forward.setName(acceptSuccess.getName());
		
		return forward;
	}

	private InfoExecutionCourse getInfoExecutionCourse(DynaActionForm form) {
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		infoExecutionCourse.setIdInternal((Integer) form.get("objectCode"));
		return infoExecutionCourse;
	}

	private List processForm(
		DynaActionForm form,
		HttpServletRequest request,
		ActionErrors actionErrors) {
		List infoTeacherShiftPercentageList = new ArrayList();

		InfoShiftProfessorship infoTeacherShiftPercentage =
			new InfoShiftProfessorship();

		Integer[] shiftProfessorships =
			(Integer[]) form.get("shiftProfessorships");

		DecimalFormatSymbols defaultDecimalFormats = new DecimalFormatSymbols();
		System.out.println(defaultDecimalFormats.getDecimalSeparator());
		for (int i = 0; i < shiftProfessorships.length; i++) {
			Integer shiftInternalCode = shiftProfessorships[i];
			if (shiftInternalCode != null) {
				Double percentage = null;
				String percentageStr =
					request.getParameter("percentage_" + shiftInternalCode);

				try {
					percentage = new Double(Double.parseDouble(percentageStr));

				} catch (NumberFormatException e) {
					if (percentageStr != null && !percentageStr.equals("")) {
						ActionError actionError =
							new ActionError(
								"error.double.format",
								percentageStr);
						actionErrors.add("error.double.format", actionError);
					}
				}

				if (percentage != null) {
					if (percentage.doubleValue() < 0) {
						ActionError actionError =
							new ActionError(
								"error.non.positive",
								percentageStr);
						actionErrors.add("error.non.positive", actionError);
					}
					infoTeacherShiftPercentage =
						new InfoShiftProfessorship();
					infoTeacherShiftPercentage.setPercentage(percentage);

					InfoShift infoShift = new InfoShift();
					infoShift.setIdInternal(shiftInternalCode);
					infoTeacherShiftPercentage.setInfoShift(infoShift);
					infoTeacherShiftPercentageList.add(
						infoTeacherShiftPercentage);
				}
			}
		}

		return infoTeacherShiftPercentageList;
	}
}
