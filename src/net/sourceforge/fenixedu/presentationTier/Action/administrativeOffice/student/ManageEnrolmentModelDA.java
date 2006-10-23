/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationDataByExecutionYear.EnrolmentModelFactoryEditor;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ManageEnrolmentModelDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	EnrolmentModelFactoryEditor enrolmentModelFactoryEditor = null;

	if (RenderUtils.getViewState() != null) {
	    executeFactoryMethod(request);
	    enrolmentModelFactoryEditor = (EnrolmentModelFactoryEditor) RenderUtils.getViewState()
		    .getMetaObject().getObject();
	} else {
	    Registration registration = rootDomainObject
		    .readRegistrationByOID(getRequestParameterAsInteger(request, "registrationID"));
	    enrolmentModelFactoryEditor = new EnrolmentModelFactoryEditor(registration);
	}

	request.setAttribute("enrolmentModelBean", enrolmentModelFactoryEditor);
	return mapping.findForward("showManageEnrolmentModel");
    }

}
