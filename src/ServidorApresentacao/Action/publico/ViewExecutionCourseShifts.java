/*
 * Created on 24/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.publico;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.comparators.ComparatorByLessonTypeForInfoShiftWithAssociatedInfoClassesAndInfoLessons;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.RequestUtils;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ViewExecutionCourseShifts extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		InfoExecutionCourse infoExecCourse =
			(InfoExecutionCourse) RequestUtils.getExecutionCourseFromRequest(
				request);

		HttpSession session = request.getSession(false);
		GestorServicos gestor = GestorServicos.manager();
		// Read shifts of execution course.
		// Just the shifts aren't enough... we also need to read the classes
		// associated to the shift and the classes associated with the shift.
		Object argsSelectShifts[] = { infoExecCourse };
		List infoShifts =
			(List) gestor.executar(
				null,
				"SelectExecutionShiftsWithAssociatedLessonsAndClasses",
				argsSelectShifts);

		if (infoShifts != null && !infoShifts.isEmpty()) {
		
			Collections.sort(
				infoShifts,
				new ComparatorByLessonTypeForInfoShiftWithAssociatedInfoClassesAndInfoLessons());
				
			//TODO: see how lists can be placed in request	
			session.setAttribute("publico.infoShifts", infoShifts);
		} else {
			
		}

		return mapping.findForward("Sucess");
	}

}
