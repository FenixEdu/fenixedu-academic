/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RetrievePersonalPhotoAction extends FenixDispatchAction {

    public static ActionForward retrieve(final HttpServletRequest request, final HttpServletResponse response, final Person person) {
	if (person != null) {
	    final Photograph personalPhoto = person.getPersonalPhoto();
	    if (personalPhoto != null) {
		if (isPhotoAvailable(personalPhoto, person)) {
		    writePhoto(response, personalPhoto);
		}
	    }
	}
	return null;
    }

    public static ActionForward retrieveByUUID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final String uuid = request.getParameter("uuid");
	final User user = User.readUserByUserUId(uuid);
	return user == null ? null : retrieve(request, response, user.getPerson());
    }

    private static boolean isPhotoAvailable(final Photograph personalPhoto, final Person person) {
	if (person.isPhotoPubliclyAvailable()) {
	    return true;
	}
	final IUserView userView = UserView.getUser();
	final Person requester = userView == null ? null : userView.getPerson();
	if (requester != null) {
	    if (requester == person) {
		return true;
	    }
	    if (requester.hasRole(RoleType.MANAGER) || requester.hasRole(RoleType.DIRECTIVE_COUNCIL)) {
		return true;
	    }
	    if (person.hasRole(RoleType.STUDENT)
		    && (requester.hasRole(RoleType.TEACHER) || requester.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE))) {
		return true;
	    }
	    if (requester.hasRole(RoleType.STUDENT) || requester.hasRole(RoleType.ALUMNI)) {
		return person.getAvailablePhoto();
	    }
	}
	return false;
    }

    public ActionForward retrieveOwnPhoto(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final IUserView userView = UserView.getUser();
	final Photograph personalPhoto = userView.getPerson().getPersonalPhotoEvenIfPending();
	if (personalPhoto != null) {
	    writePhoto(response, personalPhoto);
	}
	return null;
    }

    public ActionForward retrieveByID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final Integer personID = new Integer(request.getParameter("personCode"));
	final Person person = (Person) rootDomainObject.readPartyByOID(personID);
	return retrieve(request, response, person);
    }

    public ActionForward retrievePendingByID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final Integer photoID = new Integer(request.getParameter("photoCode"));
	Photograph photo = rootDomainObject.readPhotographByOID(photoID);
	if (photo != null) {
	    writePhoto(response, photo);
	}
	return null;
    }

    protected static void writePhoto(final HttpServletResponse response, final Photograph personalPhoto) {
	try {
	    response.setContentType(personalPhoto.getContentType().getMimeType());
	    final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
	    dos.write(personalPhoto.getContents());
	    dos.close();
	} catch (IOException e) {
	}
    }

}
