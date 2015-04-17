/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.person;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.fenixedu.academic.domain.Photograph;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.ContentType;
import org.fenixedu.bennu.core.domain.Avatar;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@Mapping(module = "person", path = "/retrievePersonalPhoto", scope = "session", parameter = "method",
        functionality = VisualizePersonalInfo.class)
public class RetrievePersonalPhotoAction extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(RetrievePersonalPhotoAction.class);

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
        response.setContentType("image/png");
        try (InputStream mm =
                RetrievePersonalPhotoAction.class.getClassLoader().getResourceAsStream("META-INF/resources/img/mysteryman.png")) {
            response.getOutputStream().write(Avatar.process(mm, "image/png", 100));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
