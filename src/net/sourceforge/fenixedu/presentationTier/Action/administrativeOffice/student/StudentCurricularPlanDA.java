/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.factoryExecutors.StudentCurricularPlanFactoryExecutor;
import net.sourceforge.fenixedu.applicationTier.factoryExecutors.StudentCurricularPlanFactoryExecutor.StudentCurricularPlanCreator;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentCurricularPlanDA extends FenixDispatchAction {

    public ActionForward prepareCreateSCP(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final Registration registration = rootDomainObject.readRegistrationByOID(getIntegerFromRequest(
		request, "registrationId"));

	request.setAttribute("studentCurricularPlanCreator",
		new StudentCurricularPlanFactoryExecutor.StudentCurricularPlanCreator(registration));
	request.setAttribute("registration", registration);

	return mapping.findForward("showCreateNewStudentCurricularPlan");
    }

    public ActionForward createSCP(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	executeFactoryMethod();
	addActionMessage(request, "message.registration.addNewSCP.success");

	request.setAttribute("registration", ((StudentCurricularPlanCreator) getRenderedObject())
		.getRegistration());
	return mapping.findForward("viewRegistrationDetails");
    }

}
