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

@Mapping(path = "/managePersonFunctions", module = "scientificCouncil")
public class ManagePersonFunctions extends FenixDispatchAction {

    public ActionForward deletePersonFunction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Integer personFunctionId = new Integer(request.getParameter("personFunctionID"));

        final PersonFunction personFunction = (PersonFunction) rootDomainObject.readAccountabilityByOID(personFunctionId);
        final Person person = personFunction.getPerson();

        personFunction.delete();

        final ActionForward actionForward =
                new ActionForward(
                        "/functionsManagement/listPersonFunctions.faces?personID="
                                + person.getIdInternal()
                                + "&"
                                + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME
                                + "=/conselho-cientifico/conselho-cientifico");
        actionForward.setRedirect(false);
        return actionForward;
    }

}
