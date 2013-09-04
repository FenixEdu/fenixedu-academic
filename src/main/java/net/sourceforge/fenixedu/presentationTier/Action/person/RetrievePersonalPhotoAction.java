package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.photograph.PictureAvatar;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.io.FileUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(module = "person", path = "/retrievePersonalPhoto", scope = "session", parameter = "method")
public class RetrievePersonalPhotoAction extends FenixDispatchAction {
    public ActionForward retrieveByUUID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
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
            HttpServletResponse response) {
        // DEBUG HERE
        final String personID = request.getParameter("personCode");
        final Person person = (Person) AbstractDomainObject.fromExternalId(personID);
        return retrievePhotograph(request, response, person);
    }

    public ActionForward retrievePendingByID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        final String photoID = request.getParameter("photoCode");
        Photograph photo = AbstractDomainObject.fromExternalId(photoID);
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
            PictureAvatar avatar = personalPhoto.getAvatar();
            response.setContentType(avatar.getPictureFileFormat().getMimeType());
            final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            dos.write(avatar.getBytes());
            dos.close();
        } catch (IOException e) {
        }
    }

    public void writeUnavailablePhoto(HttpServletResponse response) {
        writeUnavailablePhoto(response, getServlet());
    }

    public static void writeUnavailablePhoto(HttpServletResponse response, ActionServlet actionServlet) {
        try {
            response.setContentType("image/gif");
            final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            final String path =
                    actionServlet.getServletContext().getRealPath(
                            "/images/photo_placer01_" + Language.getDefaultLanguage().name() + ".gif");
            dos.write(FileUtils.readFileToByteArray(new File(path)));
            dos.close();
        } catch (IOException e) {
        }
    }
}
