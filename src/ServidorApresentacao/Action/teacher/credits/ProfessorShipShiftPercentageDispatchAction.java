/*
 * Created on 15/Mai/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.teacher.credits;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionCourse;
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

		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		Integer idInternal =
			(Integer) professorshipShiftPercentageForm.get(
				"executionCourseInternalCode");
		infoExecutionCourse.setIdInternal(idInternal);

		HttpSession session = request.getSession();

		IUserView userView = SessionUtils.getUserView(request);
		InfoTeacher infoTeacher =
			(InfoTeacher) session.getAttribute(SessionConstants.INFO_TEACHER);

		Object args[] = { infoTeacher, infoExecutionCourse };

		List infoShiftPercentageList =
			(List) ServiceUtils.executeService(
				userView,
				"ReadTeacherExecutionCourseShiftsPercentage",
				args);

		initializeForm(
			professorshipShiftPercentageForm,
			infoShiftPercentageList, infoTeacher);

		request.setAttribute(
			"infoShiftPercentageList",
			infoShiftPercentageList);
		System.out.println("AQUI::::::::::::::::::::::::::::::");
		return mapping.findForward("showTable");
	}

	/**
	 * @param professorshipShiftPercentageForm
	 * @param infoShiftPercentageList
	 */
	private void initializeForm(
		DynaValidatorForm professorshipShiftPercentageForm,
		List infoShiftPercentageList, InfoTeacher infoTeacher) {
			
		Integer [] shiftsInternalCodes = new Integer [infoShiftPercentageList.size()];
		Double [] percentages = new Double [infoShiftPercentageList.size()];
		
		for (int i = 0; i < infoShiftPercentageList.size(); i++){
			InfoShiftPercentage infoShiftPercentage =
				(InfoShiftPercentage) infoShiftPercentageList.get(i);
			percentages[i] = infoShiftPercentage.getAvailablePercentage();
			
			List infoTeacherShiftPercentageList = infoShiftPercentage.getTeacherShiftPercentageList();
			
			Iterator iterator = infoTeacherShiftPercentageList.iterator();
						
			while (iterator.hasNext()) {
				InfoTeacherShiftPercentage infoTeacherShiftPercentage = (InfoTeacherShiftPercentage) iterator.next();
				if (!infoTeacherShiftPercentage.getInfoProfesorShip().getInfoTeacher().equals(infoTeacher)){
					shiftsInternalCodes [i] = infoShiftPercentage.getShift().getIdInternal();
					break;
				}
			}
		}
		
		professorshipShiftPercentageForm.set("shiftProfessorships",shiftsInternalCodes);
		professorshipShiftPercentageForm.set("shiftProfessorshipsPercentages", percentages);
	}

	public ActionForward accept(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		return mapping.findForward("acceptSuccess");
	}
}
