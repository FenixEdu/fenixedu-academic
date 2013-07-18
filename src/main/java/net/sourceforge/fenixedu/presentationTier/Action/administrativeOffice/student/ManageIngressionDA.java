package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.factoryExecutors.RegistrationIngressionFactorExecutor.RegistrationIngressionEditor;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Input;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */

@Mapping(path = "/manageIngression", module = "academicAdministration", formBeanClass = FenixActionForm.class)
@Forwards({ @Forward(name = "showEditIngression", path = "/academicAdminOffice/manageIngression.jsp") })
public class ManageIngressionDA extends FenixDispatchAction {

    private Registration getRegistration(final HttpServletRequest request) {
        return rootDomainObject.readRegistrationByOID(getIntegerFromRequest(request, "registrationId"));
    }

    private RegistrationIngressionEditor getRegistrationIngressionEditor() {
        return getRenderedObject();
    }

    @Input
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("ingressionBean", new RegistrationIngressionEditor(getRegistration(request)));
        return mapping.findForward("showEditIngression");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        RegistrationIngressionEditor ingressionInformationBean = getRegistrationIngressionEditor();
        if (!ingressionInformationBean.hasRegistrationAgreement()
                || ingressionInformationBean.getRegistrationAgreement().isNormal()) {
            ingressionInformationBean.setAgreementInformation(null);
        }

        RenderUtils.invalidateViewState();
        request.setAttribute("ingressionBean", ingressionInformationBean);
        return mapping.findForward("showEditIngression");
    }

    public ActionForward editIngression(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        try {
            executeFactoryMethod();
        } catch (final DomainException e) {
            request.setAttribute("ingressionBean", getRegistrationIngressionEditor());
            RenderUtils.invalidateViewState();
            addActionMessage(request, e.getKey());
            return mapping.findForward("showEditIngression");
        }

        addActionMessage(request, "message.registration.ingression.and.agreement.edit.success");
        request.setAttribute("registrationId", getRegistrationIngressionEditor().getRegistration().getIdInternal());

        return prepare(mapping, actionForm, request, response);
    }

}
