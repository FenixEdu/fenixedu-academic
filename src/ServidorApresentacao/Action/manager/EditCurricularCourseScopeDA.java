/*
 * Created on 21/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoBranch;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularSemester;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */
public class EditCurricularCourseScopeDA extends FenixDispatchAction {

	public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		

		Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
		Integer curricularCourseScopeId = new Integer(request.getParameter("curricularCourseScopeId"));
		InfoCurricularCourseScope oldInfoCurricularCourseScope = null;

		Object args[] = { curricularCourseScopeId };
		

		try {
			oldInfoCurricularCourseScope = (InfoCurricularCourseScope) ServiceUtils.executeService(userView, "ReadCurricularCourseScope", args);
		} catch (NonExistingServiceException ex) {
			throw new NonExistingActionException("message.nonExistingCurricularCourseScope", mapping.findForward("readCurricularCourse"));
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		if (oldInfoCurricularCourseScope.getTheoreticalHours() != null)
			dynaForm.set("theoreticalHours", oldInfoCurricularCourseScope.getTheoreticalHours().toString());

		if (oldInfoCurricularCourseScope.getPraticalHours() != null)
			dynaForm.set("praticalHours", oldInfoCurricularCourseScope.getPraticalHours().toString());

		if (oldInfoCurricularCourseScope.getTheoPratHours() != null)
			dynaForm.set("theoPratHours", oldInfoCurricularCourseScope.getTheoPratHours().toString());

		if (oldInfoCurricularCourseScope.getLabHours() != null)
			dynaForm.set("labHours", oldInfoCurricularCourseScope.getLabHours().toString());

		if (oldInfoCurricularCourseScope.getCredits() != null)
			dynaForm.set("credits", oldInfoCurricularCourseScope.getCredits().toString());

		if (oldInfoCurricularCourseScope.getMaxIncrementNac() != null)
			dynaForm.set("maxIncrementNac", oldInfoCurricularCourseScope.getMaxIncrementNac().toString());

		if (oldInfoCurricularCourseScope.getMinIncrementNac() != null)
			dynaForm.set("minIncrementNac", oldInfoCurricularCourseScope.getMinIncrementNac().toString());

		if (oldInfoCurricularCourseScope.getWeigth() != null)
			dynaForm.set("weight", oldInfoCurricularCourseScope.getWeigth().toString());

		Object[] args1 = { degreeCurricularPlanId };
		List result = null;
		try {
			result = (List) ServiceUtils.executeService(userView, "ReadBranchesByDegreeCurricularPlan", args1);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		//	creation of bean of InfoBranches for use in jsp
		ArrayList branchesList = new ArrayList();
		if (result != null) {
			InfoBranch infoBranch;
			Iterator iter = result.iterator();
			String label, value;
			while (iter.hasNext()) {
				infoBranch = (InfoBranch) iter.next();
				value = infoBranch.getIdInternal().toString();
				label = infoBranch.getCode() + " - " + infoBranch.getName();
				branchesList.add(new LabelValueBean(label, value));
			}
			dynaForm.set("branchId", oldInfoCurricularCourseScope.getInfoBranch().getIdInternal().toString());
			request.setAttribute("branchesList", branchesList);

		}

		dynaForm.set("curricularSemesterId", oldInfoCurricularCourseScope.getInfoCurricularSemester().getIdInternal().toString());

		return mapping.findForward("editCurricularCourseScope");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaValidatorForm) form;

		Integer oldCurricularCourseScopeId = new Integer(request.getParameter("curricularCourseScopeId"));

		InfoCurricularCourseScope newInfoCurricularCourseScope = new InfoCurricularCourseScope();

		String curricularSemesterIdString = (String) dynaForm.get("curricularSemesterId");
		String branchIdString = (String) dynaForm.get("branchId");

		String theoreticalHoursString = (String) dynaForm.get("theoreticalHours");
		String praticalHoursString = (String) dynaForm.get("praticalHours");
		String theoPratHoursString = (String) dynaForm.get("theoPratHours");
		String labHoursString = (String) dynaForm.get("labHours");
		String maxIncrementNacString = (String) dynaForm.get("maxIncrementNac");
		String minIncrementNacString = (String) dynaForm.get("minIncrementNac");
		String weightString = (String) dynaForm.get("weight");
		String creditsString = (String) dynaForm.get("credits");

		Integer curricularSemesterId = new Integer(curricularSemesterIdString);

		InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester();
		infoCurricularSemester.setIdInternal(curricularSemesterId);
		newInfoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);

		Integer branchId = new Integer(branchIdString);

		InfoBranch infoBranch = new InfoBranch();
		infoBranch.setIdInternal(branchId);
		newInfoCurricularCourseScope.setInfoBranch(infoBranch);
		newInfoCurricularCourseScope.setIdInternal(oldCurricularCourseScopeId);

		if (theoreticalHoursString.compareTo("") != 0) {
			Double theoreticalHours = new Double(theoreticalHoursString);
			newInfoCurricularCourseScope.setTheoreticalHours(theoreticalHours);
		}

		if (praticalHoursString.compareTo("") != 0) {
			Double praticalHours = new Double(praticalHoursString);
			newInfoCurricularCourseScope.setPraticalHours(praticalHours);
		}

		if (theoPratHoursString.compareTo("") != 0) {
			Double theoPratHours = new Double(theoPratHoursString);
			newInfoCurricularCourseScope.setTheoPratHours(theoPratHours);
		}

		if (labHoursString.compareTo("") != 0) {
			Double labHours = new Double(labHoursString);
			newInfoCurricularCourseScope.setLabHours(labHours);
		}

		if (maxIncrementNacString.compareTo("") != 0) {
			Integer maxIncrementNac = new Integer(maxIncrementNacString);
			newInfoCurricularCourseScope.setMaxIncrementNac(maxIncrementNac);

		}

		if (minIncrementNacString.compareTo("") != 0) {
			Integer minIncrementNac = new Integer(minIncrementNacString);
			newInfoCurricularCourseScope.setMinIncrementNac(minIncrementNac);

		}

		if (weightString.compareTo("") != 0) {
			Integer weight = new Integer(weightString);
			newInfoCurricularCourseScope.setWeigth(weight);
		}

		if (creditsString.compareTo("") != 0) {
			Double credits = new Double(creditsString);
			newInfoCurricularCourseScope.setCredits(credits);
		}

		Object args[] = { newInfoCurricularCourseScope };
		

		try {
			ServiceUtils.executeService(userView, "EditCurricularCourseScope", args);
		} catch (NonExistingServiceException ex) {
			throw new NonExistingActionException(ex.getMessage(), mapping.findForward("readCurricularCourse"));
		} catch (ExistingServiceException e) {
			throw new ExistingActionException("message.manager.existing.curricular.course.scope");
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		
		request.setAttribute("infoCurricularCourseScope", newInfoCurricularCourseScope);

		return mapping.findForward("readCurricularCourse");
	}
}
