package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/academicAdministration", module = "academicAdministration")
@Forwards({ @Forward(name = "indexAdmin", path = "/academicAdministration/indexAdmin.jsp"),
        @Forward(name = "indexOffice", path = "/academicAdministration/indexOffice.jsp") })
public class AcademicAdministrationDispatchAction extends FenixDispatchAction {

    public ActionForward indexAdmin(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("indexAdmin");
    }

    public ActionForward indexOffice(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("indexOffice");
    }

}
