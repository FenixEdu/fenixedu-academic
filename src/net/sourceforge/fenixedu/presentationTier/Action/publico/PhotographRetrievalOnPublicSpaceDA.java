package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.presentationTier.Action.person.RetrievePersonalPhotoAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/retrievePersonalPhoto", module = "publico")
public class PhotographRetrievalOnPublicSpaceDA extends RetrievePersonalPhotoAction {
    public ActionForward retrievePhotographOnPublicSpace(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        Person person = AbstractDomainObject.fromExternalId(request.getParameter("personId"));
        return retrievePhotograph(request, response, person);
    }

    public ActionForward retrieveByIstId(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        User user = User.readUserByUserUId(request.getParameter("istId"));
        return retrievePhotograph(request, response, user.getPerson());
    }
}
