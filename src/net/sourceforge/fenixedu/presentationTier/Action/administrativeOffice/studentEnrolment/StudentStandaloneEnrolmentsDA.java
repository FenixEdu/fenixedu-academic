package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.NoCourseGroupEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentStandaloneEnrolmentBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentStandaloneEnrolments", module = "academicAdminOffice")
@Forwards( {
	@Forward(name = "showExtraEnrolments", path = "/academicAdminOffice/showNoCourseGroupCurriculumGroupEnrolments.jsp"),
	@Forward(name = "chooseExtraEnrolment", path = "/academicAdminOffice/chooseNoCourseGroupCurriculumGroupEnrolment.jsp"),
	@Forward(name = "showDegreeModulesToEnrol", path = "/studentEnrolments.do?method=prepareFromExtraEnrolment")

})
public class StudentStandaloneEnrolmentsDA extends NoCourseGroupCurriculumGroupEnrolmentsDA {

    @Override
    protected NoCourseGroupEnrolmentBean createNoCourseGroupEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionSemester) {
	return new StudentStandaloneEnrolmentBean(studentCurricularPlan, executionSemester);
    }

    @Override
    protected String getActionName() {
	return "studentStandaloneEnrolments";
    }

    @Override
    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final Enrolment enrolment = getEnrolment(request);
	final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
	final ExecutionSemester executionSemester = getExecutionSemester(request);

	try {
	    final List<CurriculumModule> curriculumModules = Arrays.<CurriculumModule> asList(enrolment);

	    studentCurricularPlan.removeCurriculumModulesFromNoCourseGroupCurriculumGroup(curriculumModules, executionSemester,
		    NoCourseGroupCurriculumGroupType.STANDALONE, AccessControl.getPerson());

	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}

	return showExtraEnrolments(createNoCourseGroupEnrolmentBean(studentCurricularPlan, executionSemester), mapping,
		actionForm, request, response);
    }
}
