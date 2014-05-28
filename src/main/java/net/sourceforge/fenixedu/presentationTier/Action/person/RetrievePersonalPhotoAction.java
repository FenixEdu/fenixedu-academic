/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.ContentType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.io.ByteStreams;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(module = "person", path = "/retrievePersonalPhoto", scope = "session", parameter = "method",
        functionality = VisualizePersonalInfo.class)
public class RetrievePersonalPhotoAction extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(RetrievePersonalPhotoAction.class);

    public ActionForward retrieveByUUID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        final String uuid = request.getParameter("uuid");
        final User user = User.findByUsername(uuid);
        return user == null ? null : retrievePhotograph(request, response, user.getPerson());
    }

    public ActionForward retrieveOwnPhoto(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final User userView = Authenticate.getUser();
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
        final Person person = (Person) FenixFramework.getDomainObject(personID);
        return retrievePhotograph(request, response, person);
    }

    public ActionForward retrievePendingByID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        final String photoID = request.getParameter("photoCode");
        Photograph photo = FenixFramework.getDomainObject(photoID);
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
            response.setContentType(ContentType.PNG.getMimeType());
            final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            byte[] avatar = personalPhoto.getDefaultAvatar();
            dos.write(avatar);
            dos.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void writeUnavailablePhoto(HttpServletResponse response) {
        writeUnavailablePhoto(response, getServlet());
    }

    public static void writeUnavailablePhoto(HttpServletResponse response, ActionServlet actionServlet) {
        try {
            response.setContentType("image/gif");
            InputStream stream =
                    RetrievePersonalPhotoAction.class.getClassLoader().getResourceAsStream(
                            "images/photo_placer01_" + I18N.getLocale().getLanguage() + ".gif");
            ByteStreams.copy(stream, response.getOutputStream());
            stream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
