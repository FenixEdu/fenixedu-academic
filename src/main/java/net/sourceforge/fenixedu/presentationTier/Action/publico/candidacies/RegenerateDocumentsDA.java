package net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.candidate.degree.DegreeCandidacyManagementDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;
import pt.ist.bennu.core.security.UserSession;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/regenerateDocuments", module = "publico")
@Forwards({ @Forward(name = "printAllDocuments", path = "regenerateDocuments.printAllDocuments") })
public class RegenerateDocumentsDA extends FenixDispatchAction {

    public ActionForward doOperation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            User user = User.findByUsername("ist24439");
            Authenticate.setUser(user);
            return new DegreeCandidacyManagementDispatchAction().doOperation(mapping, form, request, response);
        } finally {
            Authenticate.setUser((UserSession) null);
        }
    }

}
