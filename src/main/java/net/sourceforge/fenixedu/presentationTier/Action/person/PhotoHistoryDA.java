package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import edu.emory.mathcs.backport.java.util.Collections;

/**
 * 
 * @author Gil Lacerda (gil.lacerda@ist.utl.pt)
 * 
 */

@Mapping(path = "/photoHistory", module = "person")
@Forwards(@Forward(name = "userHistory", path = "/person/visualizePhotoHistory.jsp"))
public class PhotoHistoryDA extends FenixDispatchAction {

    public ActionForward userHistory(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getUserView(request).getPerson();
        List<Photograph> photoHistory = person.getPhotographHistory();
        // most recent photos first
        Collections.reverse(photoHistory);
        request.setAttribute("history", photoHistory);
        return mapping.findForward("userHistory");
    }

}
