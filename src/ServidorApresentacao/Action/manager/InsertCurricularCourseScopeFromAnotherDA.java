/*
 * Created on 5/Set/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Calendar;
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
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularSemester;
import DataBeans.InfoExecutionPeriod;
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
import Util.Data;

/**
 * @author lmac1
 */
public class InsertCurricularCourseScopeFromAnotherDA extends FenixDispatchAction {

	public ActionForward prepareInsert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

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

		if (oldInfoCurricularCourseScope.getEctsCredits() != null)
			dynaForm.set("ectsCredits", oldInfoCurricularCourseScope.getEctsCredits().toString());

		if (oldInfoCurricularCourseScope.getMaxIncrementNac() != null)
			dynaForm.set("maxIncrementNac", oldInfoCurricularCourseScope.getMaxIncrementNac().toString());

		if (oldInfoCurricularCourseScope.getMinIncrementNac() != null)
			dynaForm.set("minIncrementNac", oldInfoCurricularCourseScope.getMinIncrementNac().toString());

		if (oldInfoCurricularCourseScope.getWeigth() != null)
			dynaForm.set("weight", oldInfoCurricularCourseScope.getWeigth().toString());

		if (oldInfoCurricularCourseScope.getBeginDate() != null)
			dynaForm.set("beginDate", Data.format2DayMonthYear(oldInfoCurricularCourseScope.getBeginDate().getTime(), "/"));

		dynaForm.set("branchId", oldInfoCurricularCourseScope.getInfoBranch().getIdInternal().toString());
		dynaForm.set("curricularSemesterId", oldInfoCurricularCourseScope.getInfoCurricularSemester().getIdInternal().toString());

		// obtain branches to show in jsp
		Object[] args1 = { degreeCurricularPlanId };
		List result = null;
		try {
				result = (List) ServiceUtils.executeService(userView, "ReadBranchesByDegreeCurricularPlan", args1);
		} catch (NonExistingServiceException ex) {
			throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", mapping.findForward("readDegree"));
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		if(result == null)
			throw new NonExistingActionException("message.insert.degreeCurricularCourseScope.error", mapping.findForward("readCurricularCourse"));

		//	creation of bean of InfoBranches for use in jsp
		ArrayList branchesList = new ArrayList();
		InfoBranch infoBranch;
		Iterator iter = result.iterator();
		String label, value;
		while (iter.hasNext()) {
			infoBranch = (InfoBranch) iter.next();
			value = infoBranch.getIdInternal().toString();
			label = infoBranch.getCode() + " - " + infoBranch.getName();
			branchesList.add(new LabelValueBean(label, value));
		}

		// obtain execution periods to show in jsp
		List infoExecutionPeriods = null;
		try {
			infoExecutionPeriods = (List) ServiceUtils.executeService(userView, "ReadExecutionPeriods", null);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (infoExecutionPeriods == null)
			throw new NonExistingActionException("message.insert.executionPeriods.error", mapping.findForward("readCurricularCourse"));

		List executionPeriodsLabels = new ArrayList();
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
		Iterator iterExecutionPeriods = infoExecutionPeriods.iterator();
		String labelExecutionPeriod, valueExecutionPeriod;
		while (iterExecutionPeriods.hasNext()) {
			infoExecutionPeriod = (InfoExecutionPeriod) iterExecutionPeriods.next();
			valueExecutionPeriod = Data.format2DayMonthYear(infoExecutionPeriod.getBeginDate(), "/");
			labelExecutionPeriod = Data.format2DayMonthYear(infoExecutionPeriod.getBeginDate(), "/");
			executionPeriodsLabels.add(new LabelValueBean(labelExecutionPeriod, valueExecutionPeriod));
		}

		request.setAttribute("executionPeriodsLabels", executionPeriodsLabels);		
		request.setAttribute("branchesList", branchesList);
		
		return mapping.findForward("insertCurricularCourseScope");
	}

	public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

		DynaActionForm dynaForm = (DynaValidatorForm) form;

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
		String ectsCreditsString = (String) dynaForm.get("ectsCredits");
		String beginDateString = (String) dynaForm.get("beginDate");

		Integer curricularSemesterId = new Integer(curricularSemesterIdString);

		InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester();
		infoCurricularSemester.setIdInternal(curricularSemesterId);
		newInfoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);

		Integer branchId = new Integer(branchIdString);

		InfoBranch infoBranch = new InfoBranch();
		infoBranch.setIdInternal(branchId);
		newInfoCurricularCourseScope.setInfoBranch(infoBranch);
		InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
		infoCurricularCourse.setIdInternal(new Integer(request.getParameter("curricularCourseId")));
		newInfoCurricularCourseScope.setInfoCurricularCourse(infoCurricularCourse);

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

		if (ectsCreditsString.compareTo("") != 0) {
			Double ectsCredits = new Double(ectsCreditsString);
			newInfoCurricularCourseScope.setEctsCredits(ectsCredits);
		}

		if (beginDateString.compareTo("") != 0) {
			Calendar beginDateCalendar = Calendar.getInstance();
			beginDateCalendar.setTime(Data.convertStringDate(beginDateString, "/"));
			newInfoCurricularCourseScope.setBeginDate(beginDateCalendar);
		}

		Object args[] = { newInfoCurricularCourseScope };
		try {
			ServiceUtils.executeService(userView, "InsertCurricularCourseScopeAtCurricularCourse", args);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e.getMessage(), e);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException);
		}

		request.setAttribute("infoCurricularCourseScope", newInfoCurricularCourseScope);

		return mapping.findForward("readCurricularCourse");
	}
}