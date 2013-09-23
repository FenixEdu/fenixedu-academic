package net.sourceforge.fenixedu.applicationTier.Servico.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "person", path = "/identificationCard", parameter = "method")
@Forwards(value = {
        @Forward(name = "show.card.information", path = "/person/identificationCard/showCardInformation.jsp")
})
public class IdentificationCardDA extends FenixDispatchAction {

    public ActionForward prepare(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final Person person = AccessControl.getPerson();
        request.setAttribute("person", person);
        return mapping.findForward("show.card.information");
    }

}
