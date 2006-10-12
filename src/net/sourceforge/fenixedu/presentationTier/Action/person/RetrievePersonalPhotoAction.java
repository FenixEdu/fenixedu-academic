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
import net.sourceforge.fenixedu.domain.FileEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RetrievePersonalPhotoAction extends FenixDispatchAction {

    public ActionForward retrieveOwnPhoto(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	IUserView userView = SessionUtils.getUserView(request);
	FileEntry personalPhoto = userView.getPerson().getPersonalPhoto();

	if (personalPhoto != null) {

	    try {
		response.setContentType(personalPhoto.getContentType().getMimeType());
		DataOutputStream dos = new DataOutputStream(response.getOutputStream());
		dos.write(personalPhoto.getContents());
		dos.close();
	    } catch (IOException e) {
	    }
	}

	return null;

    }

    public ActionForward retrieveByID(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	IUserView userView = SessionUtils.getUserView(request);
	Integer personID = new Integer(request.getParameter("personCode"));

	Person person = (Person) rootDomainObject.readPartyByOID(personID);
	FileEntry personalPhoto = person.getPersonalPhoto();

	if (personalPhoto != null) {

	    if (!person.getAvailablePhoto()) {
		Person requester = userView.getPerson();
		if (!(person.hasRole(RoleType.STUDENT)
                        && (requester.hasRole(RoleType.TEACHER)
                                || requester.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)))) {
		    return null;
		}
	    }

	    try {
		response.setContentType(personalPhoto.getContentType().getMimeType());
		DataOutputStream dos = new DataOutputStream(response.getOutputStream());
		dos.write(personalPhoto.getContents());
		dos.close();
	    } catch (IOException e) {
	    }
	}

	return null;

    }

}
