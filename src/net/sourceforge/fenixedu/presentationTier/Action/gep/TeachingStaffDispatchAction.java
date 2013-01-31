/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadActiveCurricularCourseScopesByDegreeCurricularPlanIDAndExecutionYearID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYearByID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.InsertInstitution;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.institution.ReadAllInstitutions;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(
		module = "gep",
		path = "/teachingStaff",
		input = "/teachingStaff.do?method=prepare",
		attribute = "teachingStaffForm",
		formBean = "teachingStaffForm",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(name = "chooseExecutionCourse", path = "/gep/teachingStaff/chooseExecutionCourse.jsp", tileProperties = @Tile(
				title = "private.gep.gepportal.consultationguidelines")),
		@Forward(
				name = "chooseExecutionYearAndDegreeCurricularPlan",
				path = "/gep/teachingStaff/chooseExecutionYearAndDegreeCurricularPlan.jsp",
				tileProperties = @Tile(title = "private.gep.surveys.faculty")),
		@Forward(name = "viewTeachingStaff", path = "/gep/teachingStaff/viewTeachingStaff.jsp", tileProperties = @Tile(
				title = "private.gep.gepportal.consultationguidelines")) })
public class TeachingStaffDispatchAction extends FenixDispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		IUserView userView = UserView.getUser();

		request.setAttribute("executionYears", ReadNotClosedExecutionYears.run());

		return mapping.findForward("chooseExecutionYearAndDegreeCurricularPlan");
	}

	public ActionForward selectExecutionYear(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		IUserView userView = UserView.getUser();

		final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
		Integer executionYearID = executionYear.getIdInternal();
		Object[] args = { executionYearID };

		List degreeCurricularPlans = (List) ServiceUtils.executeService("ReadActiveDegreeCurricularPlansByExecutionYear", args);
		final ComparatorChain comparatorChain = new ComparatorChain();
		comparatorChain.addComparator(new BeanComparator("infoDegree.tipoCurso"));
		comparatorChain.addComparator(new BeanComparator("infoDegree.nome"));
		Collections.sort(degreeCurricularPlans, comparatorChain);

		request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);
		request.setAttribute("executionYears", ReadNotClosedExecutionYears.run());
		request.setAttribute("executionYear", executionYear);

		return mapping.findForward("chooseExecutionYearAndDegreeCurricularPlan");
	}

	public ActionForward selectExecutionDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
		Integer degreeCurricularPlanID = (Integer) dynaActionForm.get("degreeCurricularPlanID");
		Integer executionYearID = (Integer) dynaActionForm.get("executionYearID");

		Set<DegreeModuleScope> degreeModuleScopes =
				ReadActiveCurricularCourseScopesByDegreeCurricularPlanIDAndExecutionYearID.run(degreeCurricularPlanID,
						executionYearID);

		SortedSet<DegreeModuleScope> sortedScopes = new TreeSet<DegreeModuleScope>(DegreeModuleScope.COMPARATOR_BY_NAME);
		sortedScopes.addAll(degreeModuleScopes);

		InfoExecutionYear infoExecutionYear = ReadExecutionYearByID.run(executionYearID);

		request.setAttribute("sortedScopes", sortedScopes);
		request.setAttribute("executionYear", infoExecutionYear.getYear());

		return mapping.findForward("chooseExecutionCourse");
	}

	public ActionForward viewTeachingStaff(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		Integer executionCourseID = Integer.valueOf(request.getParameter("executionCourseID"));

		ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);

		List institutions = (List) ReadAllInstitutions.run();
		Collections.sort(institutions, new BeanComparator("name"));

		request.setAttribute("professorships", executionCourse.getProfessorships());
		request.setAttribute("institutions", institutions);
		request.setAttribute("nonAffiliatedTeachers", executionCourse.getNonAffiliatedTeachers());

		DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
		dynaActionForm.set("executionCourseID", executionCourseID);
		dynaActionForm.set("nonAffiliatedTeacherID", null);

		return mapping.findForward("viewTeachingStaff");
	}

	public ActionForward createNewNonAffiliatedTeacher(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
		String nonAffiliatedTeacherName = (String) dynaActionForm.get("nonAffiliatedTeacherName");
		Integer nonAffiliatedTeacherInstitutionID = (Integer) dynaActionForm.get("nonAffiliatedTeacherInstitutionID");
		String nonAffiliatedTeacherInstitutionName = (String) dynaActionForm.get("nonAffiliatedTeacherInstitutionName");

		if (nonAffiliatedTeacherName.length() == 0) {
			// define a teacher name!
			return viewTeachingStaff(mapping, actionForm, request, response);
		}

		if (nonAffiliatedTeacherInstitutionID == 0 && nonAffiliatedTeacherInstitutionName.length() == 0) {
			// define an institution!
			return viewTeachingStaff(mapping, actionForm, request, response);
		}

		final Unit institution =
				nonAffiliatedTeacherInstitutionID == 0 ? (Unit) InsertInstitution.run(nonAffiliatedTeacherInstitutionName) : (Unit) rootDomainObject
						.readPartyByOID(nonAffiliatedTeacherInstitutionID);

		NonAffiliatedTeacher.associateToInstitutionAndExecutionCourse(nonAffiliatedTeacherName, institution,
				rootDomainObject.readExecutionCourseByOID((Integer) dynaActionForm.get("executionCourseID")));

		return viewTeachingStaff(mapping, actionForm, request, response);

	}

	public ActionForward removeNonAffiliatedTeacher(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		final ExecutionCourse executionCourse =
				rootDomainObject.readExecutionCourseByOID(getIntegerFromRequest(request, "executionCourseID"));
		final NonAffiliatedTeacher nonAffiliatedTeacher =
				rootDomainObject.readNonAffiliatedTeacherByOID(getIntegerFromRequest(request, "nonAffiliatedTeacherID"));

		nonAffiliatedTeacher.removeExecutionCourse(executionCourse);

		return viewTeachingStaff(mapping, actionForm, request, response);
	}

}