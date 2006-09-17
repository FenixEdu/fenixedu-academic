/*
 * Created on 29/Ago/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author naat
 * @author zenida
 * 
 */
public class ViewStudentTimeTable extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException,
	    FenixServiceException {

	final Person person = getUserView(request).getPerson();

	// FIXME: after old student migration this test should be remove
	// registrations should be read using
        // person.getStudent().getRegistrations()
	request.setAttribute("registrations", (person.hasStudent()) ? person.getStudent()
		.getRegistrations() : person.getStudents());

	return mapping.findForward("chooseRegistration");
    }

    public ActionForward showTimeTable(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, FenixServiceException {

	final List<InfoLesson> infoLessons = (List) ServiceUtils.executeService(getUserView(request),
		"ReadStudentTimeTable", new Object[] { getRegistration(actionForm) });

	request.setAttribute("infoLessons", infoLessons);

	return mapping.findForward("showTimeTable");

    }

    private Registration getRegistration(ActionForm form) {
	return rootDomainObject.readRegistrationByOID((Integer) ((DynaActionForm) form)
		.get("registrationId"));
    }

}