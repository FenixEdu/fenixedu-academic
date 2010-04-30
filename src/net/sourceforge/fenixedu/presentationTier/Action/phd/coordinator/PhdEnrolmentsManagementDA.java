package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.presentationTier.Action.phd.ManageEnrolmentsBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;
import net.sourceforge.fenixedu.presentationTier.renderers.degreeStructure.DegreeCurricularPlanRendererConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/phdEnrolmentsManagement", module = "coordinator")
@Forwards(tileProperties = @Tile(navLocal = "/coordinator/localNavigationBar.jsp"), value = {

@Forward(name = "showPhdProgram", path = "/phd/coordinator/enrolments/showPhdProgram.jsp"),

@Forward(name = "showEnrolments", path = "/phd/coordinator/enrolments/showEnrolments.jsp")

})
public class PhdEnrolmentsManagementDA extends PhdProcessDA {

    private Set<PhdProgram> getManagedPhdPrograms(HttpServletRequest request) {
	final Set<PhdProgram> result = new HashSet<PhdProgram>();
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	for (final Coordinator coordinator : getLoggedPerson(request).getCoordinators()) {
	    if (coordinator.getExecutionDegree().getDegree().hasPhdProgram()
		    && coordinator.getExecutionDegree().getExecutionYear() == currentExecutionYear) {
		result.add(coordinator.getExecutionDegree().getDegree().getPhdProgram());
	    }
	}

	return result;
    }

    public ActionForward showPhdProgram(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgram program = getDomainObject(request, "phdProgramOid");
	if (program != null) {
	    initPhdProgramConfig(request, program);
	    return mapping.findForward("showPhdProgram");
	}

	final Set<PhdProgram> phdPrograms = getManagedPhdPrograms(request);

	if (phdPrograms.size() == 1) {
	    initPhdProgramConfig(request, phdPrograms.iterator().next());
	    return mapping.findForward("showPhdProgram");
	}

	request.setAttribute("phdPrograms", phdPrograms);
	return mapping.findForward("showPhdProgram");
    }

    private void initPhdProgramConfig(HttpServletRequest request, PhdProgram program) {

	final DegreeCurricularPlanRendererConfig config = new DegreeCurricularPlanRendererConfig();

	config.setDegreeCurricularPlan(program.getDegree().getLastActiveDegreeCurricularPlan());
	config.setExecutionInterval(ExecutionYear.readCurrentExecutionYear());
	config.setOrganizeBy(DegreeCurricularPlanRendererConfig.OrganizeType.GROUPS);
	config.setViewCurricularCourseUrl(getClass().getAnnotation(Mapping.class).path() + ".do");
	config.addViewCurricularCourseUrlParameter("method", "manageEnrolments");

	request.setAttribute("rendererConfig", config);
	request.setAttribute("phdProgram", program);
    }

    public ActionForward changeDegreeCurricularPlanConfig(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final DegreeCurricularPlanRendererConfig config = (DegreeCurricularPlanRendererConfig) getRenderedObject("rendererConfig");

	request.setAttribute("rendererConfig", config);
	request.setAttribute("phdProgram", config.getDegreeCurricularPlan().getDegree().getPhdProgram());

	return mapping.findForward("showPhdProgram");
    }

    public ActionForward manageEnrolments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ManageEnrolmentsBean bean = (ManageEnrolmentsBean) getRenderedObject("manageEnrolmentsBean");
	if (bean == null) {
	    bean = new ManageEnrolmentsBean();
	    bean.setSemester(ExecutionSemester.readActualExecutionSemester());
	    bean.setCurricularCourse((CurricularCourse) getDomainObject(request, "degreeModuleOid"));
	}

	searchEnrolments(bean);
	request.setAttribute("manageEnrolmentsBean", bean);
	
	return mapping.findForward("showEnrolments");
    }

    private void searchEnrolments(final ManageEnrolmentsBean bean) {
	bean.setEnrolments(bean.getCurricularCourse().getEnrolmentsByAcademicInterval(bean.getSemester().getAcademicInterval()));
    }

}