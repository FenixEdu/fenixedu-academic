/**
 * Project sop 
 * 
 * Package presentationTier.Action.sop
 * 
 * Created on 10/Out/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams.ReadExamByOID;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Ana e Ricardo
 */
@Mapping(
		module = "resourceAllocationManager",
		path = "/showExamsManagement",
		input = "/chooseDegreeAndYearContext.do?method=prepare",
		attribute = "examNewForm",
		formBean = "examNewForm",
		scope = "request",
		validate = false,
		parameter = "method")
@Forwards(value = { @Forward(name = "editExam", path = "/createExamNew.do?method=prepareForEdit&page=0"),
		@Forward(name = "viewExamsMap", path = "df.page.viewExamsMap"),
		@Forward(name = "deleteExam", path = "/showExamsManagement.do?method=view&page=0"),
		@Forward(name = "createExamByDay", path = "/chooseExecutionCourseForExams.do?method=prepare&page=0"),
		@Forward(name = "createExamByCourse", path = "/createExamNew.do?method=prepare&page=0&nextPage=showExamsManagement") })
@Exceptions(
		value = { @ExceptionHandling(
				type = net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams.ReadFilteredExamsMap.ExamsPeriodUndefined.class,
				key = "error.exams.period.undefined",
				handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
				scope = "request") })
public class ShowExamsManagement extends FenixContextDispatchAction {

	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws FenixActionException, FenixServiceException, FenixFilterException {

		InfoExamsMap infoExamsMap = getExamsMap(request);
		request.setAttribute(PresentationConstants.INFO_EXAMS_MAP, infoExamsMap);

		request.setAttribute(PresentationConstants.LABELLIST_CURRICULAR_YEARS, ContextUtils.createCurricularYearList());
		request.setAttribute(PresentationConstants.DEGREES, ContextUtils.createExecutionDegreeList(request));

		return mapping.findForward("viewExamsMap");
	}

	private InfoExamsMap getExamsMap(HttpServletRequest request) throws FenixServiceException, FenixFilterException {
		IUserView userView = getUserView(request);

		InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);

		InfoCurricularYear curricularYearObj = (InfoCurricularYear) request.getAttribute(PresentationConstants.CURRICULAR_YEAR);

		Integer curricularYear = curricularYearObj.getYear();

		InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

		List curricularYearsList = new ArrayList();
		curricularYearsList.add(curricularYear);

		Object[] args = { infoExecutionDegree, curricularYearsList, infoExecutionPeriod };
		InfoExamsMap infoExamsMap;

		infoExamsMap = (InfoExamsMap) ServiceUtils.executeService("ReadFilteredExamsMap", args);

		return infoExamsMap;
	}

	public ActionForward createByCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ContextUtils.setExecutionCourseContext(request);
		InfoExecutionCourse infoExecutionCourse =
				(InfoExecutionCourse) request.getAttribute(PresentationConstants.EXECUTION_COURSE);

		ContextUtils.setCurricularYearContext(request);

		request.setAttribute(PresentationConstants.EXECUTION_COURSE_KEY, infoExecutionCourse);
		request.setAttribute(PresentationConstants.EXECUTION_COURSE_OID, infoExecutionCourse.getIdInternal().toString());

		ContextUtils.setExecutionDegreeContext(request);

		return mapping.findForward("createExamByCourse");
	}

	public ActionForward createByDay(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ContextUtils.setCurricularYearContext(request);

		Integer day = new Integer(request.getParameter(PresentationConstants.DAY));
		Integer month = new Integer(request.getParameter(PresentationConstants.MONTH));
		Integer year = new Integer(request.getParameter(PresentationConstants.YEAR));
		request.setAttribute(PresentationConstants.DAY, day);
		request.setAttribute(PresentationConstants.MONTH, month);
		request.setAttribute(PresentationConstants.YEAR, year);

		ContextUtils.setExecutionDegreeContext(request);

		return mapping.findForward("createExamByDay");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		IUserView userView = getUserView(request);

		ContextUtils.setCurricularYearContext(request);
		ContextUtils.setExecutionCourseContext(request);
		ContextUtils.setExecutionDegreeContext(request);
		ContextUtils.setExecutionPeriodContext(request);

		Integer examID = new Integer(request.getParameter(PresentationConstants.EXAM_OID));

		InfoExam infoExam = ReadExamByOID.run(examID);
		request.setAttribute(PresentationConstants.EXAM, infoExam);
		request.setAttribute(PresentationConstants.EXAM_OID, infoExam.getIdInternal());
		return mapping.findForward("editExam");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionErrors actionErrors = new ActionErrors();

		IUserView userView = getUserView(request);

		ContextUtils.setCurricularYearContext(request);
		ContextUtils.setExecutionCourseContext(request);
		ContextUtils.setExecutionDegreeContext(request);
		ContextUtils.setExecutionPeriodContext(request);

		Integer examID = new Integer(request.getParameter(PresentationConstants.EXAM_OID));
		Object[] args = { null, examID };

		try {
			ServiceUtils.executeService("DeleteWrittenEvaluation", args);
		} catch (FenixServiceException exception) {
			actionErrors.add(exception.getMessage(), new ActionError(exception.getMessage()));
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}

		return mapping.findForward("deleteExam");
	}
}