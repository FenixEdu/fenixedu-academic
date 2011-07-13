package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.io.FileUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;

import pt.ist.fenixWebFramework.security.UserView;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(module = "person", path = "/retrievePersonalPhoto", scope = "session", parameter = "method")
public class RetrievePersonalPhotoAction extends FenixDispatchAction {
    public ActionForward retrieveByUUID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final String uuid = request.getParameter("uuid");
	final User user = User.readUserByUserUId(uuid);
	return user == null ? null : retrievePhotograph(request, response, user.getPerson());
    }

    public ActionForward retrieveOwnPhoto(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final IUserView userView = UserView.getUser();
	final Photograph personalPhoto = userView.getPerson().getPersonalPhotoEvenIfPending();
	if (personalPhoto != null) {
	    writePhoto(response, personalPhoto);
	    return null;
	}
	writeUnavailablePhoto(response);
	return null;
    }

    public ActionForward retrieveByID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final Integer personID = new Integer(request.getParameter("personCode"));
	final Person person = (Person) rootDomainObject.readPartyByOID(personID);
	return retrievePhotograph(request, response, person);
    }

    public ActionForward retrievePendingByID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final Integer photoID = new Integer(request.getParameter("photoCode"));
	Photograph photo = rootDomainObject.readPhotographByOID(photoID);
	if (photo != null) {
	    writePhoto(response, photo);
	    return null;
	}
	writeUnavailablePhoto(response);
	return null;
    }

    protected ActionForward retrievePhotograph(final HttpServletRequest request, final HttpServletResponse response,
	    final Person person) {
	if (person != null) {
	    final Photograph personalPhoto = person.getPersonalPhoto();
	    if (personalPhoto != null) {
		if (person.isPhotoAvailableToCurrentUser()) {
		    writePhoto(response, personalPhoto);
		    return null;
		}
	    }
	}
	writeUnavailablePhoto(response);
	return null;
    }

    public static void writePhoto(final HttpServletResponse response, final Photograph personalPhoto) {
	try {
	    response.setContentType(personalPhoto.getContentType().getMimeType());
	    final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
	    dos.write(personalPhoto.getContents());
	    dos.close();
	} catch (IOException e) {
	}
    }

    public void writeUnavailablePhoto(HttpServletResponse response) {
	writeUnavailablePhoto(response, getServlet());
    }

    public static void writeUnavailablePhoto(HttpServletResponse response, ActionServlet actionServlet) {
	try {
	    final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
	    final String path = actionServlet.getServletContext().getRealPath(
		    "/images/photo_placer01_" + Language.getDefaultLanguage().name() + ".gif");
	    dos.write(FileUtils.readFileToByteArray(new File(path)));
	    dos.close();
	} catch (IOException e) {
	}
    }
}
