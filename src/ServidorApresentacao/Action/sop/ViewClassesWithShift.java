package ServidorApresentacao.Action.sop;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class ViewClassesWithShift extends Action {
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		try {
			//DynaValidatorForm shiftForm = (DynaValidatorForm) form;
			//String name = (String) shiftForm.get("name");
			String name = request.getParameter("name");
			InfoShift infoShift = getInfoShift(name, request);
			Object[] args = { infoShift };
			List infoClasses =
				(List) ServiceUtils.executeService(
					SessionUtils.getUserView(request),
					"ReadClassesWithShiftService",
					args);
					
			if (infoClasses == null || infoClasses.isEmpty()) {
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(
					"message.shift.no.classes",
					new ActionError("message.shift.no.classes", name));
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}
			
			Collections.sort(infoClasses, new BeanComparator("nome"));
			request.setAttribute("classesWithShift", infoClasses);
			
			
			return mapping.findForward("sucess");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * Method getInfoShift.
	 * @param name
	 * @param request
	 * @return InfoShift
	 */
	private InfoShift getInfoShift(String name, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(false);
		List infoShiftList =
			(List) request.getAttribute(
				SessionConstants.INFO_SHIFTS_EXECUTION_COURSE_KEY);
		InfoShift infoShift = null;
		if (infoShiftList == null) {
			InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(
					SessionConstants.INFO_EXECUTION_PERIOD_KEY);
			infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
			String infoExecutionCourseCode = request.getParameter("ecCode");
			
			if (infoExecutionCourseCode == null) {
				throw new IllegalStateException("A sessão não está válida");
			} else {
				ShiftKey shiftKey = new ShiftKey();
				infoExecutionCourse.setSigla(infoExecutionCourseCode);
				shiftKey.setInfoExecutionCourse(infoExecutionCourse);
				shiftKey.setShiftName(name);
				Object args[] = { shiftKey };
				
				infoShift = (InfoShift) ServiceUtils.executeService(SessionUtils.getUserView(request),"LerTurno", args);
			}

		}else{
			Iterator listIterator = infoShiftList.iterator();
			while (listIterator.hasNext() && infoShift == null) {
				InfoShift infoShiftAux = (InfoShift) listIterator.next();

				if (infoShiftAux.getNome().equalsIgnoreCase(name)) {
					infoShift = infoShiftAux;
				}
			}
		}
		
		if (infoShift == null)
			throw new IllegalStateException("O turno pretendido não está em sessão!");
		else
			return infoShift;
	}
}
