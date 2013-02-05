package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.manageExternalSupervision;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/manageExternalSupervision", module = "directiveCouncil")
@Forwards({
        @Forward(name = "selectRegistrationAgreement",
                path = "/directiveCouncil/manageExternalSupervision/selectRegistrationAgreement.jsp", tileProperties = @Tile(
                        title = "private.steeringcouncil.externaloversight.managementofexternalsupervisors")),
        @Forward(name = "showSupervisors", path = "/directiveCouncil/manageExternalSupervision/showSupervisors.jsp",
                tileProperties = @Tile(title = "private.steeringcouncil.externaloversight.managementofexternalsupervisors")) })
public class ManageExternalSupervisionDA extends FenixDispatchAction {

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
        final Integer personId = Integer.valueOf(request.getParameter("supervisorId"));
        Person person = (Person) rootDomainObject.readPartyByOID(personId);
        final String registrationProtocolId = request.getParameter("registrationProtocolId");
        RegistrationProtocol registrationProtocol = AbstractDomainObject.fromExternalId(registrationProtocolId);

        ManageExternalSupervisionBean bean = new ManageExternalSupervisionBean(registrationProtocol);
        bean.removeSupervisor(person);

        request.setAttribute("sessionBean", bean);
        RenderUtils.invalidateViewState("sessionBean");

        return mapping.findForward("showSupervisors");
    }

}
