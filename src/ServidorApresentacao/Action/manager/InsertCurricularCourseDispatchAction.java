/*
 * Created on 8/Ago/2003
 */
package ServidorApresentacao.Action.manager;

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
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoDepartmentCourse;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CurricularCourseType;

/**
 * @author lmac1
 */

public class InsertCurricularCourseDispatchAction extends FenixDispatchAction {


	public ActionForward prepareInsert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {

				HttpSession session = request.getSession(false);
				UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
				
				
				GestorServicos manager = GestorServicos.manager();
				List result = null;
				try {
						result = (List) manager.executar(userView, "ReadAllDepartmentCourses", null);
				} catch (FenixServiceException e) {
					throw new FenixActionException(e);
				}
				
//			creation of bean of InfoDepartmentCourses for use in jsp
				ArrayList departmentCoursesList = new ArrayList();
				if(result != null) {
					departmentCoursesList.add(new LabelValueBean("", ""));
					InfoDepartmentCourse infoDepartmentCourse;
					Iterator iter = result.iterator();
					String label, value;
					while(iter.hasNext()) {
						infoDepartmentCourse = (InfoDepartmentCourse) iter.next();
						value = infoDepartmentCourse.getCode() + "-" + infoDepartmentCourse.getName();
						label = infoDepartmentCourse.getCode() + " - " + infoDepartmentCourse.getName();
						departmentCoursesList.add(new LabelValueBean(label, value));
					}
					request.setAttribute("departmentCoursesList", departmentCoursesList);
				}
				
//				request.setAttribute("degreeId", degreeId);
//				request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanId);
				return mapping.findForward("insertCurricularCourse");
	}


	public ActionForward insert(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
		
		Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
    	
		DynaActionForm dynaForm = (DynaValidatorForm) form;
		String type = (String) dynaForm.get("type");
		String mandatory = (String) dynaForm.get("mandatory");
		String basic = (String) dynaForm.get("basic");
		String departmentCourse = (String) dynaForm.get("departmentCourse");
		
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
		infoDegreeCurricularPlan.setIdInternal(degreeCurricularPlanId);
		
		InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
		if(basic.compareTo("") != 0)
			infoCurricularCourse.setBasic(new Boolean(basic));
		infoCurricularCourse.setCode((String) dynaForm.get("code"));
		infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		if(mandatory.compareTo("") != 0)
			infoCurricularCourse.setMandatory(new Boolean(mandatory));
		infoCurricularCourse.setName((String) dynaForm.get("name"));
		if(type.compareTo("") != 0)
			infoCurricularCourse.setType(new CurricularCourseType(new Integer(type)));
		
		Object args[] = { infoCurricularCourse };
							
		GestorServicos manager = GestorServicos.manager();
		List serviceResult = null;
		
		try {
				serviceResult = (List) manager.executar(userView, "InsertCurricularCourseAtDegreeCurricularPlan", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		if(serviceResult != null) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = new ActionError("message.existingCurricularCourse", serviceResult.get(0), serviceResult.get(1));
			actionErrors.add("message.existingCurricularCourse", error);			
			saveErrors(request, actionErrors);
		}
		return mapping.findForward("readDegreeCurricularPlan");
	}			
}