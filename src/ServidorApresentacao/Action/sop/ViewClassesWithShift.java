package ServidorApresentacao.Action.sop;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoShift;
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
			DynaValidatorForm shiftForm = (DynaValidatorForm) form;
			String name = (String)shiftForm.get("name");
			
			InfoShift infoShift = getInfoShift (name, request);
			
			Object[] args = {infoShift};
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
	private InfoShift getInfoShift(String name, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		List infoShiftList = (List) session.getAttribute(SessionConstants.INFO_SHIFTS_EXECUTION_COURSE_KEY);
		
		if (infoShiftList == null){
			throw new IllegalStateException("A sessão não está válida");
		}
		
		Iterator listIterator = infoShiftList.iterator();
		while (listIterator.hasNext()) {
			InfoShift infoShift = (InfoShift) listIterator.next();
			
			if (infoShift.getNome().equalsIgnoreCase(name)){
				return infoShift; 
			}
		}
		throw new IllegalStateException("O turno pretendido não está em sessão!");
	}
}
