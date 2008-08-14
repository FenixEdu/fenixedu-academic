package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDegreeTeachingServicesDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ScientificCouncilManageDegreeTeachingServicesDispatchAction extends ManageDegreeTeachingServicesDispatchAction {

    public ActionForward showTeachingServiceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm dynaForm = (DynaActionForm) form;
	Integer professorshipID = (Integer) dynaForm.get("professorshipID");
	Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);

	if (professorship == null) {
	    return mapping.findForward("teacher-not-found");
	}

	teachingServiceDetailsProcess(professorship, request, dynaForm);
	return mapping.findForward("show-teaching-service-percentages");
    }

    public ActionForward updateTeachingServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
	return updateTeachingServices(mapping, form, request, RoleType.SCIENTIFIC_COUNCIL);
    }
}
