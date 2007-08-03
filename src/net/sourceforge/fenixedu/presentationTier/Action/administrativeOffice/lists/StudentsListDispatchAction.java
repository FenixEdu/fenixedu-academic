/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists.SearchStudents;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists.SearchStudents.SearchParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists.SearchStudents.SearchStudentPredicate;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ListInformationBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class StudentsListDispatchAction extends FenixDispatchAction {

	public ActionForward prepareByDegree(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("executionDegreeBean",
				new ExecutionDegreeListBean());
		request.setAttribute("listInformationBean", new ListInformationBean());

		return mapping.findForward("chooseExecutionDegree");
	}

	public ActionForward prepareByCurricularCourse(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException,
			FenixFilterException {

		request.setAttribute("executionDegreeBean",
				new ExecutionDegreeListBean());
		request.setAttribute("listInformationBean", new ListInformationBean());
		return mapping.findForward("chooseCurricularCourse");
	}

	public ActionForward chooseDegreePostBack(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) getRenderedObject();

		executionDegreeBean.setDegreeCurricularPlan(null);
		executionDegreeBean.setCurricularCourse(null);
		RenderUtils.invalidateViewState();
		request.setAttribute("executionDegreeBean", executionDegreeBean);
		request.setAttribute("listInformationBean", new ListInformationBean());

		return mapping.findForward("chooseExecutionDegree");
	}

	public ActionForward executionYearPostBack(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) getRenderedObject();

		executionDegreeBean.setDegreeCurricularPlan(null);
		executionDegreeBean.setDegree(null);
		executionDegreeBean.setCurricularCourse(null);
		RenderUtils.invalidateViewState();
		request.setAttribute("executionDegreeBean", executionDegreeBean);
		request.setAttribute("listInformationBean", new ListInformationBean());

		return mapping.findForward("chooseCurricularCourse");
	}

	public ActionForward chooseDegreeCurricularPlanPostBack(
			ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) {

		final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) getRenderedObject();
	//	executionDegreeBean.setExecutionYear(null);
		executionDegreeBean.setCurricularCourse(null);
		RenderUtils.invalidateViewState();
		request.setAttribute("listInformationBean", new ListInformationBean());
		request.setAttribute("executionDegreeBean", executionDegreeBean);

		return mapping.findForward("chooseExecutionDegree");
	}

	public ActionForward chooseDegreeCurricularByExecutionYearPlanPostBack(
			ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) {

		final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) getRenderedObject("executionYear");
		executionDegreeBean.setCurricularCourse(null);
		executionDegreeBean.setDegreeCurricularPlan(null);
		RenderUtils.invalidateViewState();
		request.setAttribute("executionDegreeBean", executionDegreeBean);
		request.setAttribute("listInformationBean", new ListInformationBean());

		return mapping.findForward("chooseCurricularCourse");
	}


	public ActionForward searchByDegree(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) getRenderedObject("executionDegree");

		ListInformationBean ingressionInformationBean = (ListInformationBean) getRenderedObject("chooseIngression");

		SearchParameters searchParameters = new SearchStudents.SearchParameters(ingressionInformationBean.getRegistrationAgreement(), ingressionInformationBean.getRegistrationStateType(), ingressionInformationBean.getWith_equivalence(),
				executionDegreeBean.getDegreeCurricularPlan(),executionDegreeBean.getExecutionYear());
				

			SearchStudentPredicate predicate = new SearchStudents.SearchStudentPredicate(searchParameters);

			Object[] args = { searchParameters, predicate };
		try {
			final List<StudentCurricularPlan> registrations = (List<StudentCurricularPlan>) ServiceUtils
					.executeService(getUserView(request),
							"SearchStudents", args);

		
//		Object[] args = { executionDegreeBean, ingressionInformationBean };
//
//		try {
//			final List<StudentCurricularPlan> registrations = (List<StudentCurricularPlan>) ServiceUtils
//					.executeService(getUserView(request),
//							"SearchStudentByCriteria", args);

			request.setAttribute("executionDegreeBean", executionDegreeBean);
			request.setAttribute("listInformationBean",
					ingressionInformationBean);
			request.setAttribute("studentCurricularPlanList", registrations);

		} catch (FenixServiceException e) {
			addActionMessage(request, e.getMessage());

		}
		return mapping.findForward("chooseExecutionDegree");
	}

	public ActionForward showActiveCurricularCourseScope(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException,
			FenixFilterException, FenixServiceException {

		final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) getRenderedObject("executionYear");
		final ListInformationBean listInformationBean = new ListInformationBean();

		Object[] args = {
				executionDegreeBean.getDegreeCurricularPlan().getIdInternal(),
				executionDegreeBean.getExecutionYear().getIdInternal() };

		final SortedSet<DegreeModuleScope> degreeModuleScopes = (SortedSet<DegreeModuleScope>) ServiceUtils
				.executeService(
						getUserView(request),
						"ReadCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear",
						args);

		if (degreeModuleScopes.isEmpty()) {
			addActionMessage("message", request,
					"error.nonExisting.AssociatedCurricularCourses");

		} else {
			listInformationBean.setDegreeModuleScope(degreeModuleScopes);
			request.setAttribute("degreeModuleScopes", degreeModuleScopes);
		}
		request.setAttribute("executionYearID", executionDegreeBean
				.getExecutionYear().getIdInternal());
		request.setAttribute("listInformationBean", listInformationBean);
		request.setAttribute("executionDegreeBean", executionDegreeBean);
		return mapping.findForward("chooseCurricularCourse");
	}

	public ActionForward searchByCurricularCourse(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) getRenderedObject();

		final Integer curricularCourseOID = getIntegerFromRequest(request,
				"curricularCourseCode");
		final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject
				.readDegreeModuleByOID(curricularCourseOID);
		final Integer semester = getIntegerFromRequest(request, "semester");
		final Integer year = getIntegerFromRequest(request, "year");
		final Integer executionYearOID = getIntegerFromRequest(request,
				"executionYearID");
		final ExecutionYear executionYear = rootDomainObject
				.readExecutionYearByOID(executionYearOID);

		if (executionDegreeBean == null) {
			executionDegreeBean = new ExecutionDegreeListBean();
		}

		executionDegreeBean.setCurricularCourse(curricularCourse);
		executionDegreeBean.setExecutionYear(executionYear);

		Object[] args = { executionDegreeBean, curricularCourseOID, semester };

		try {
			final List<Enrolment> registrations = (List<Enrolment>) ServiceUtils
					.executeService(getUserView(request),
							"SearchStudentByCriteria", args);

			request.setAttribute("executionDegreeBean", executionDegreeBean);
			request.setAttribute("semester", semester);
			request.setAttribute("year", year);
			request.setAttribute("enrolmentList", registrations);

		} catch (DomainException e) {
			addActionMessage(request, e.getMessage());

		}
		return mapping.findForward("studentByCurricularCourse");
	}

	public ActionForward chooseExecutionDegreeInvalid(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("executionDegreeBean", RenderUtils.getViewState()
				.getMetaObject().getObject());
		request.setAttribute("listInformationBean", new ListInformationBean());

		return mapping.getInputForward();
	}

}
