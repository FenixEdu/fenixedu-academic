package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/loginForIdentificationCard", module = "external")
public class RedirectToIdentificationCardInterfaceDA extends FenixDispatchAction {

    public ActionForward redirect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final User user = Authenticate.getUser();
        if (user == null) {
            return new ActionForward(
                    "https://id.ist.utl.pt/cas/login?service=https%3A%2F%2Fbarra.ist.utl.pt%2Flogin%2F%3Fnext%3Dhttps%253A%252F%252Fid.ist.utl.pt%252Fcas%252Flogin%253Fservice%253Dhttps%253A%252F%252Ffenix.ist.utl.pt%252Fexternal%252FloginForIdentificationCard.do%253Fmethod%253Dredirect",
                    true);
        } else if (user.getPerson().hasRole(RoleType.PERSON)) {
            final ActionForward actionForward = new ActionForward();
            actionForward.setRedirect(true);
            actionForward.setModule("");

            final String path = "/person/identificationCard.do?method=prepare&contentContextPath_PATH=/pessoal/pessoal";

            actionForward.setPath(path + "&"
                    + pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME
                    + "="
                    + pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.calculateChecksum(path));
            return actionForward;
        } else {
            return null;
        }
    }

}