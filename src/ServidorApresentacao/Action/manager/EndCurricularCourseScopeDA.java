/*
 * Created on 5/Set/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourseScope;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidArgumentsActionException;
import ServidorApresentacao.Action.exceptions.InvalidSituationActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;

/**
 * @author Fernanda Quitério
 * 27/10/2003
 * 
 */
public class EndCurricularCourseScopeDA extends FenixDispatchAction {

	public ActionForward prepareEnd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		Integer curricularCourseScopeId = new Integer(request.getParameter("curricularCourseScopeId"));
		InfoCurricularCourseScope oldInfoCurricularCourseScope = null;

		Object args[] = { curricularCourseScopeId };

		try {
			oldInfoCurricularCourseScope =
				(InfoCurricularCourseScope) ServiceUtils.executeService(userView, "ReadCurricularCourseScope", args);
		} catch (NonExistingServiceException ex) {
			throw new NonExistingActionException("message.nonExistingCurricularCourseScope", mapping.findForward("readCurricularCourse"));
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		if (oldInfoCurricularCourseScope.getBeginDate() != null)
			dynaForm.set("beginDate", Data.format2DayMonthYear(oldInfoCurricularCourseScope.getBeginDate().getTime(), "/"));

		request.setAttribute("infoCurricularCourseScope", oldInfoCurricularCourseScope);
		return mapping.findForward("endCurricularCourseScope");
	}

	public ActionForward end(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaValidatorForm) form;

		Integer oldCurricularCourseScopeId = new Integer(request.getParameter("curricularCourseScopeId"));

		InfoCurricularCourseScope newInfoCurricularCourseScope = new InfoCurricularCourseScope();
		newInfoCurricularCourseScope.setIdInternal(oldCurricularCourseScopeId);

		String beginDateString = (String) dynaForm.get("beginDate");
		String endDateString = (String) dynaForm.get("endDate");

		if (beginDateString.compareTo("") != 0) {
			Calendar beginDateCalendar = Calendar.getInstance();
			beginDateCalendar.setTime(Data.convertStringDate(beginDateString, "/"));
			newInfoCurricularCourseScope.setBeginDate(beginDateCalendar);
		}

		if (endDateString.compareTo("") != 0) {
			Calendar endDateCalendar = Calendar.getInstance();
			endDateCalendar.setTime(Data.convertStringDate(endDateString, "/"));
			newInfoCurricularCourseScope.setEndDate(endDateCalendar);
		}
		Object args[] = { newInfoCurricularCourseScope };

		try {
			ServiceUtils.executeService(userView, "EndCurricularCourseScope", args);
		} catch (NonExistingServiceException ex) {
			throw new NonExistingActionException(ex.getMessage(), mapping.findForward("readCurricularCourse"));
		} catch (InvalidArgumentsServiceException ex) {
			throw new InvalidArgumentsActionException("error.manager.wrongDates");
		} catch (InvalidSituationServiceException ex) {
			throw new InvalidSituationActionException("error.manager.invalidDate");
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		return mapping.findForward("readCurricularCourse");
	}
}