package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.manageExternalSupervision;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.DirectiveCouncilApplication.DirectiveCouncilExternalSupervision;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = DirectiveCouncilExternalSupervision.class, path = "manage",
        titleKey = "link.directiveCouncil.manageExternalSupervision")
@Mapping(path = "/manageExternalSupervision", module = "directiveCouncil")
@Forwards({
        @Forward(name = "selectRegistrationAgreement",
                path = "/directiveCouncil/manageExternalSupervision/selectRegistrationAgreement.jsp"),
        @Forward(name = "showSupervisors", path = "/directiveCouncil/manageExternalSupervision/showSupervisors.jsp") })
public class ManageExternalSupervisionDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareSelectAgreement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("sessionBean", new ManageExternalSupervisionBean());
        RenderUtils.invalidateViewState();
        return mapping.findForward("selectRegistrationAgreement");
    }

    public ActionForward showSupervisors(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ManageExternalSupervisionBean bean = getRenderedObject("sessionBean");
        RegistrationProtocol registrationProtocol =
                RegistrationProtocol.serveRegistrationProtocol(bean.getRegistrationAgreement());
        bean.setRegistrationProtocol(registrationProtocol);
        request.setAttribute("sessionBean", bean);
        RenderUtils.invalidateViewState("sessionBean");
        return mapping.findForward("showSupervisors");
    }

    public ActionForward addSupervisor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ManageExternalSupervisionBean bean = getRenderedObject("sessionBean");
        bean.addSupervisor();
        bean.setNewSupervisor(null);

        request.setAttribute("sessionBean", bean);
        RenderUtils.invalidateViewState("sessionBean");
        request.setAttribute("startVisible", true);

        return mapping.findForward("showSupervisors");
    }

    public ActionForward invalidAddSupervisor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ManageExternalSupervisionBean bean = getRenderedObject("sessionBean");
        bean.setNewSupervisor(null);

        request.setAttribute("sessionBean", bean);
        RenderUtils.invalidateViewState("sessionBean");
        request.setAttribute("startVisible", true);

        return mapping.findForward("showSupervisors");
    }

    public ActionForward deleteSupervisor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final String personId = request.getParameter("supervisorId");
        Person person = (Person) FenixFramework.getDomainObject(personId);
        final String registrationProtocolId = request.getParameter("registrationProtocolId");
        RegistrationProtocol registrationProtocol = FenixFramework.getDomainObject(registrationProtocolId);

        ManageExternalSupervisionBean bean = new ManageExternalSupervisionBean(registrationProtocol);
        bean.removeSupervisor(person);

        request.setAttribute("sessionBean", bean);
        RenderUtils.invalidateViewState("sessionBean");

        return mapping.findForward("showSupervisors");
    }

}
