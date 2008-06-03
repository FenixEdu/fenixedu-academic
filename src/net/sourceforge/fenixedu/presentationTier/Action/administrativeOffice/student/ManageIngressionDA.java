/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.factoryExecutors.RegistrationIngressionFactorExecutor.RegistrationIngressionEditor;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ManageIngressionDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("ingressionBean", new RegistrationIngressionEditor(rootDomainObject
		.readRegistrationByOID(getIntegerFromRequest(request, "registrationId"))));

	return mapping.findForward("showEditIngression");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	
	RegistrationIngressionEditor ingressionInformationBean = (RegistrationIngressionEditor) getRenderedObject();
	if (ingressionInformationBean.getRegistrationAgreement() != null
		&& !ingressionInformationBean.getRegistrationAgreement().isNormal()) {
	    ingressionInformationBean.setIngression(null);
	}

	RenderUtils.invalidateViewState();
	request.setAttribute("ingressionBean", ingressionInformationBean);
	return mapping.findForward("showEditIngression");
    }

    public ActionForward editIngression(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	try {
	    executeFactoryMethod();
	} catch (DomainException e) {
	    request.setAttribute("ingressionBean", getRenderedObject());
	    RenderUtils.invalidateViewState();
	    addActionMessage(request, e.getKey());
	    return mapping.findForward("showEditIngression");
	}
	
	addActionMessage(request, "message.registration.ingression.edit.success");
	request.setAttribute("registrationId", ((RegistrationIngressionEditor) getRenderedObject())
		.getRegistration().getIdInternal());

	return prepare(mapping, actionForm, request, response);
    }

}
