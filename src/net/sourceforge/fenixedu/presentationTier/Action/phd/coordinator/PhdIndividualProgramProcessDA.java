package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.CommonPhdIndividualProgramProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.PhdRegistrationConclusionBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/phdIndividualProgramProcess", module = "coordinator")
@Forwards(tileProperties = @Tile(navLocal = "/coordinator/localNavigationBar.jsp"), value = {

@Forward(name = "manageProcesses", path = "/phd/coordinator/manageProcesses.jsp"),

@Forward(name = "viewProcess", path = "/phd/coordinator/viewProcess.jsp"),

@Forward(name = "viewAlertMessages", path = "/phd/coordinator/viewAlertMessages.jsp"),

@Forward(name = "viewProcessAlertMessages", path = "/phd/coordinator/viewProcessAlertMessages.jsp"),

@Forward(name = "viewCurriculum", path = "/phd/coordinator/viewCurriculum.jsp"),

@Forward(name = "manageEnrolments", path = "/phd/coordinator/manageEnrolments.jsp")

})
public class PhdIndividualProgramProcessDA extends CommonPhdIndividualProgramProcessDA {

    @Override
    protected SearchPhdIndividualProgramProcessBean initializeSearchBean(HttpServletRequest request) {
	SearchPhdIndividualProgramProcessBean searchBean = new SearchPhdIndividualProgramProcessBean();
	searchBean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	searchBean.setPhdPrograms(getManagedPhdPrograms(request));
	searchBean.setFilterPhdProcesses(false);

	return searchBean;
    }

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

    @Override
    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdIndividualProgramProcess process = getProcess(request);
	if (process != null && process.hasRegistration()) {
	    request.setAttribute("registrationConclusionBean", new PhdRegistrationConclusionBean(process.getRegistration()));
	}

	return super.viewProcess(mapping, form, request, response);
    }

    public ActionForward manageEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdIndividualProgramProcess process = getProcess(request);
	request.setAttribute("enrolments", process.getRegistration().getLastStudentCurricularPlan().getEnrolments());

	return mapping.findForward("manageEnrolments");
    }
}
