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
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Ana e Ricardo
 */
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

	InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);

	InfoCurricularYear curricularYearObj = (InfoCurricularYear) request.getAttribute(PresentationConstants.CURRICULAR_YEAR);

	Integer curricularYear = curricularYearObj.getYear();

	InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

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
	InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request.getAttribute(PresentationConstants.EXECUTION_COURSE);

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

	InfoExam infoExam = (InfoExam) ReadExamByOID.run(examID);
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