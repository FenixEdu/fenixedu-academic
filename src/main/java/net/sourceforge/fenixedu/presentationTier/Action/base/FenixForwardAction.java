package net.sourceforge.fenixedu.presentationTier.Action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/degreeSite/showCourseSite", attribute = "chooseContextDegreeForm",
        formBean = "chooseContextDegreeForm", scope = "request",
        parameter = "/publico/showCourseSite.do?method=showCurricularCourseSite")
public class FenixForwardAction extends ForwardAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return super.execute(mapping, actionForm, request, response);
    }

    protected static User getUserView(HttpServletRequest request) {
        return Authenticate.getUser();
    }

    protected Person getLoggedPerson(HttpServletRequest request) {
        return getUserView(request).getPerson();
    }

}
