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
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ManageDegreeProjectTutorialServicesDispatchAction extends FenixDispatchAction {

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
		.getExecutionYear().getNextExecutionYear().getExternalId());
	return mapping.findForward("viewAnnualTeachingCredits");
    }

    @Service
    private void updateProjectTutorialService(ProjectTutorialServiceBean projectTutorialService) {
	DegreeProjectTutorialService degreeProjectTutorialService = projectTutorialService.getProfessorship()
		.getDegreeProjectTutorialService();
	if (degreeProjectTutorialService == null) {
	    degreeProjectTutorialService = new DegreeProjectTutorialService(projectTutorialService.getProfessorship());
	}

	for (Attends attend : projectTutorialService.getOrientations()) {
	    if (!degreeProjectTutorialService.getAttends().contains(attend) && attend.getDegreeProjectTutorialService() == null) {
		degreeProjectTutorialService.getAttends().add(attend);
	    }
	}
	List<Attends> attends = new ArrayList<Attends>(degreeProjectTutorialService.getAttends());
	for (Attends attend : attends) {
	    if (!projectTutorialService.getOrientations().contains(attend)) {
		degreeProjectTutorialService.removeAttends(attend);
	    }
	}

    }

}
