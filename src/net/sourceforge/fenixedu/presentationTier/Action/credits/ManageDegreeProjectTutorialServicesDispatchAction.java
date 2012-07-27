package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.credits.util.ProjectTutorialServiceBean;
import net.sourceforge.fenixedu.domain.teacher.DegreeProjectTutorialService;
import net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil.ViewTeacherCreditsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "scientificCouncil", path = "/degreeProjectTutorialService", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "show-project-tutorial-service", path = "/credits/degreeTeachingService/showProjectTutorialService.jsp") })
public class ManageDegreeProjectTutorialServicesDispatchAction extends ViewTeacherCreditsDA {

    public ActionForward showProjectTutorialServiceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	String professorshipID = (String) getFromRequest(request, "professorshipID");
	Professorship professorship = AbstractDomainObject.fromExternalId(professorshipID);
	if (professorship == null) {
	    return mapping.findForward("teacher-not-found");
	}
	request.setAttribute("projectTutorialService", new ProjectTutorialServiceBean(professorship));
	return mapping.findForward("show-project-tutorial-service");
    }

    public ActionForward updateProjectTutorialService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ProjectTutorialServiceBean projectTutorialService = getRenderedObject("projectTutorialService");

	updateProjectTutorialService(projectTutorialService);

	request.setAttribute("teacherOid", projectTutorialService.getProfessorship().getTeacher().getExternalId());
	request.setAttribute("executionYearOid", projectTutorialService.getProfessorship().getExecutionCourse()
		.getExecutionYear().getExternalId());
	return viewAnnualTeachingCredits(mapping, form, request, response);
    }

    @Service
    private void updateProjectTutorialService(ProjectTutorialServiceBean projectTutorialService) {
	if (projectTutorialService.getProfessorship().getDegreeProjectTutorialService() == null) {
	    new DegreeProjectTutorialService(projectTutorialService.getProfessorship());
	}

	for (Attends attend : projectTutorialService.getOrientations()) {
	    if (!projectTutorialService.getProfessorship().getDegreeProjectTutorialService().getAttends().contains(attend)
		    && attend.getDegreeProjectTutorialService() == null) {
		projectTutorialService.getProfessorship().getDegreeProjectTutorialService().getAttends().add(attend);
	    }
	}
	List<Attends> attends = new ArrayList<Attends>(projectTutorialService.getProfessorship()
		.getDegreeProjectTutorialService().getAttends());
	for (Attends attend : attends) {
	    if (!projectTutorialService.getOrientations().contains(attend)) {
		projectTutorialService.getProfessorship().getDegreeProjectTutorialService().removeAttends(attend);
	    }
	}

    }

}
