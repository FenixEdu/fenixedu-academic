package ServidorApresentacao.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoShift;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author jpvl
 */
public class ViewClassesWithShift extends FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		super.execute(mapping, form, request, response);
			
		try {
			//DynaValidatorForm shiftForm = (DynaValidatorForm) form;
			//String name = (String) shiftForm.get("name");
			//String name = request.getParameter("name");
			InfoShift infoShift = getInfoShift(request);
			
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
					new ActionError("message.shift.no.classes", infoShift.getNome()));
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
	private InfoShift getInfoShift(HttpServletRequest request) throws Exception {
		Integer shiftOID = new Integer(request.getParameter(SessionConstants.SHIFT_OID));

		IUserView userView =
			(IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);

		Object args[] = { shiftOID };
		InfoShift infoShift = (InfoShift) ServiceManagerServiceFactory.executeService(
				userView,
				"ReadShiftByOID",
				args);
		
		if (infoShift == null){
			throw new IllegalStateException("O turno pretendido não existe");}
		
			return infoShift;
	}
}
