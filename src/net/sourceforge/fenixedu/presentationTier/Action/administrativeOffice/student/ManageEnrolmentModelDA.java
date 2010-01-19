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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(path = "/manageEnrolmentModel", module = "academicAdminOffice")
@Forwards( { @Forward(name = "showManageEnrolmentModel", path = "/academicAdminOffice/manageEnrolmentModel.jsp") })
public class ManageEnrolmentModelDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	EnrolmentModelFactoryEditor enrolmentModelFactoryEditor = null;

	if (RenderUtils.getViewState() != null) {
	    executeFactoryMethod();
	    enrolmentModelFactoryEditor = (EnrolmentModelFactoryEditor) RenderUtils.getViewState().getMetaObject().getObject();
	} else {
	    Registration registration = rootDomainObject.readRegistrationByOID(getRequestParameterAsInteger(request,
		    "registrationID"));
	    enrolmentModelFactoryEditor = new EnrolmentModelFactoryEditor(registration);
	}

	request.setAttribute("enrolmentModelBean", enrolmentModelFactoryEditor);
	return mapping.findForward("showManageEnrolmentModel");
    }

}
