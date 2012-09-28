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
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.DegreeProjectTutorialService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ManageDegreeProjectTutorialServicesDispatchAction extends FenixDispatchAction {

    public ActionForward showProjectTutorialServiceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	String professorshipID = (String) getFromRequest(request, "professorshipID");
	Professorship professorship = AbstractDomainObject.fromExternalId(professorshipID);
	if (professorship == null) {
	    return mapping.findForward("teacher-not-found");
	}
	List<ProjectTutorialServiceBean> projectTutorialServiceBeans = new ArrayList<ProjectTutorialServiceBean>();
	for (Attends attend : professorship.getExecutionCourse().getAttends()) {
	    if (attend.hasEnrolment()) {
		ProjectTutorialServiceBean projectTutorialServiceBean = new ProjectTutorialServiceBean(professorship, attend);
		projectTutorialServiceBeans.add(projectTutorialServiceBean);
	    }
	}

	request.setAttribute("professorship", professorship);
	request.setAttribute("projectTutorialServiceBeans", projectTutorialServiceBeans);
	return mapping.findForward("show-project-tutorial-service");
    }

    public ActionForward updateProjectTutorialService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String professorshipID = (String) getFromRequest(request, "professorshipID");
	Professorship professorship = AbstractDomainObject.fromExternalId(professorshipID);
	List<ProjectTutorialServiceBean> projectTutorialServiceBeans = getRenderedObject("projectTutorialService");
	try {
	    DegreeProjectTutorialService.updateProjectTutorialService(projectTutorialServiceBeans);
	} catch (DomainException domainException) {
	    addActionMessage("error", request, domainException.getMessage());
	    request.setAttribute("professorship", professorship);
	    request.setAttribute("projectTutorialServiceBeans", projectTutorialServiceBeans);
	    return mapping.findForward("show-project-tutorial-service");
	}
	request.setAttribute("teacherOid", professorship.getTeacher().getExternalId());
	request.setAttribute("executionYearOid", professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear()
		.getNextExecutionYear().getExternalId());
	return mapping.findForward("viewAnnualTeachingCredits");
    }

}
