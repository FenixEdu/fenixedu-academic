package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/managePersonFunctions", module = "scientificCouncil")
public class ManagePersonFunctions extends FenixDispatchAction {

    public ActionForward deletePersonFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String personFunctionId = request.getParameter("personFunctionID");

        final PersonFunction personFunction = AbstractDomainObject.fromExternalId(personFunctionId);
        final Person person = personFunction.getPerson();

        personFunction.delete();

        final ActionForward actionForward =
                new ActionForward(
                        "/functionsManagement/listPersonFunctions.faces?personID="
                                + person.getExternalId()
                                + "&"
                                + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME
                                + "=/conselho-cientifico/conselho-cientifico");
        actionForward.setRedirect(false);
        return actionForward;
    }

}
