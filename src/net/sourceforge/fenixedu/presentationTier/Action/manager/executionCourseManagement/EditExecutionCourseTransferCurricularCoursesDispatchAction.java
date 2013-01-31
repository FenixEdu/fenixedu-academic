/*
 * Created 2004/10/24
 * 
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurricularCourseByID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionCourseByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.TransferCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionDegreesByExecutionPeriodId;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author Luis Cruz
 * @version 1.1, Oct 24, 2004
 * @since 1.1
 * 
 */
@Mapping(
		module = "manager",
		path = "/editExecutionCourseTransferCurricularCourses",
		input = "...",
		attribute = "executionCourseTransferCurricularCourseForm",
		formBean = "executionCourseTransferCurricularCourseForm",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "completedTransfer", path = "/executionCourseManagement.do?method=firstPage"),
		@Forward(name = "showPage", path = "/manager/executionCourseManagement/transferCurricularCourse.jsp") })
public class EditExecutionCourseTransferCurricularCoursesDispatchAction extends FenixDispatchAction {

	public ActionForward prepareTransferCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixServiceException, FenixFilterException {

		IUserView userView = UserView.getUser();

		Integer executionCourseId = new Integer(RequestUtils.getAndSetStringToRequest(request, "executionCourseId"));
		Integer curricularCourseId = new Integer(RequestUtils.getAndSetStringToRequest(request, "curricularCourseId"));
		Integer executionPeriodId = new Integer(RequestUtils.getAndSetStringToRequest(request, "executionPeriodId"));

		InfoExecutionCourse infoExecutionCourse = ReadExecutionCourseByOID.run(executionCourseId);
		request.setAttribute("infoExecutionCourse", infoExecutionCourse);

		InfoCurricularCourse infoCurricularCourse = ReadCurricularCourseByID.run(curricularCourseId);
		request.setAttribute("infoCurricularCourse", infoCurricularCourse);

		List executionDegrees = ReadExecutionDegreesByExecutionPeriodId.run(executionPeriodId);
		Collection executionDegreesLabelValueList = RequestUtils.buildExecutionDegreeLabelValueBean(executionDegrees);
		request.setAttribute("executionDegrees", executionDegreesLabelValueList);

		List curricularYears = RequestUtils.buildCurricularYearLabelValueBean();
		request.setAttribute("curricularYears", curricularYears);

		return mapping.findForward("showPage");
	}

	public ActionForward selectExecutionDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixServiceException, FenixFilterException {

		DynaActionForm dynaActionForm = (DynaActionForm) form;

		IUserView userView = UserView.getUser();

		RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
		RequestUtils.getAndSetStringToRequest(request, "curricularCourseId");
		Integer executionPeriodId = new Integer(RequestUtils.getAndSetStringToRequest(request, "executionPeriodId"));

		// String destinationExecutionDegreeIdString =
		// RequestUtils.getAndSetStringToRequest(request,
		// "destinationExecutionDegreeId");
		// String curricularYearString =
		// RequestUtils.getAndSetStringToRequest(request, "curricularYear");
		String destinationExecutionDegreeIdString = (String) dynaActionForm.get("destinationExecutionDegreeId");
		String curricularYearString = (String) dynaActionForm.get("curricularYear");

		if (destinationExecutionDegreeIdString != null && curricularYearString != null
				&& destinationExecutionDegreeIdString.length() > 0 && curricularYearString.length() > 0
				&& StringUtils.isNumeric(destinationExecutionDegreeIdString) && StringUtils.isNumeric(curricularYearString)) {
			Integer destinationExecutionDegreeId = new Integer(destinationExecutionDegreeIdString);
			Integer curricularYear = new Integer(curricularYearString);

			List executionCourses =
					(List) ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear.run(
							destinationExecutionDegreeId, executionPeriodId, curricularYear);
			request.setAttribute("executionCourses", executionCourses);
		}

		return prepareTransferCurricularCourse(mapping, form, request, response);
	}

	public ActionForward transferCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixServiceException, FenixFilterException {

		DynaActionForm dynaActionForm = (DynaActionForm) form;

		IUserView userView = UserView.getUser();

		Integer executionCourseId = new Integer(RequestUtils.getAndSetStringToRequest(request, "executionCourseId"));
		Integer curricularCourseId = new Integer(RequestUtils.getAndSetStringToRequest(request, "curricularCourseId"));
		RequestUtils.getAndSetStringToRequest(request, "executionPeriodId");

		String destinationExecutionDegreeIdString = (String) dynaActionForm.get("destinationExecutionDegreeId");
		String curricularYearString = (String) dynaActionForm.get("curricularYear");
		String destinationExecutionCourseIdString = (String) dynaActionForm.get("destinationExecutionCourseId");

		if (destinationExecutionDegreeIdString != null && curricularYearString != null
				&& destinationExecutionCourseIdString != null && destinationExecutionDegreeIdString.length() > 0
				&& curricularYearString.length() > 0 && destinationExecutionCourseIdString.length() > 0
				&& StringUtils.isNumeric(destinationExecutionDegreeIdString) && StringUtils.isNumeric(curricularYearString)
				&& StringUtils.isNumeric(destinationExecutionCourseIdString)) {
			Integer destinationExecutionCourseId = new Integer(destinationExecutionCourseIdString);

			TransferCurricularCourse.run(executionCourseId, curricularCourseId, destinationExecutionCourseId);
		}

		return mapping.findForward("completedTransfer");
	}

}