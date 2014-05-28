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
package net.sourceforge.fenixedu.presentationTier.Action.externalServices.epfl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExternalUser;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessNumber;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.person.RetrievePersonalPhotoAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "external", path = "/epflCandidateInformation", scope = "request", validate = false)
public class ExportPhdIndividualProgramProcessInformation extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setLocale(request, Locale.ENGLISH);
        I18N.setLocale(request.getSession(), Locale.ENGLISH);

        final ActionForward actionForward = checkPermissions(request, response);
        if (actionForward == null) {
            final String method = request.getParameter("method");
            if (method == null || method.isEmpty() || method.equals("login")) {
                displayPresentationPage(request, response);
            } else if (method.equals("displayCandidatePage")) {
                displayCandidatePage(request, response);
            } else if (method.equals("displayRefereePage")) {
                displayRefereePage(request, response);
            } else if (method.equals("downloadCandidateDocuments")) {
                downloadCandidateDocuments(request, response);
            } else if (method.equals("displayPhoto")) {
                displayPhoto(request, response);
            } else if (method.equals("exportInformationXml")) {
                exportInformationXml(request, response);
            }
        }
        return actionForward;
    }

    private void displayPresentationPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final byte[] presentationPage = ExportPhdIndividualProgramProcessesInHtml.exportPresentationPage();
        writeResponse(response, presentationPage, "text/html");
    }

    private void displayCandidatePage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String hashCode = request.getParameter("process");
        final PhdProgramPublicCandidacyHashCode code =
                (PhdProgramPublicCandidacyHashCode) PhdProgramPublicCandidacyHashCode.getPublicCandidacyCodeByHash(hashCode);
        final byte[] candidatePage = ExportPhdIndividualProgramProcessesInHtml.drawCandidatePage(code);
        writeResponse(response, candidatePage, "text/html");
    }

    private void displayRefereePage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String refereeOid = request.getParameter("refereeOid");
        final int count = Integer.parseInt(request.getParameter("count"));
        final PhdCandidacyReferee referee = FenixFramework.getDomainObject(refereeOid);
        final byte[] refereePage = ExportPhdIndividualProgramProcessesInHtml.drawLetter(referee, count);
        writeResponse(response, refereePage, "text/html");
    }

    private void downloadCandidateDocuments(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String candidateOid = request.getParameter("candidateOid");
        final PhdProgramPublicCandidacyHashCode code = FenixFramework.getDomainObject(candidateOid);
        final byte[] documents = ExportPhdIndividualProgramProcessesInHtml.createZip(code);
        final String email = code.getEmail().substring(0, code.getEmail().indexOf("@"));
        final String documentName = email + "-documents.zip";
        response.addHeader("Content-Disposition", "attachment; filename=" + documentName);
        writeResponse(response, documents, "application/zip");
    }

    private void displayPhoto(final HttpServletRequest request, final HttpServletResponse response) {
        final String photoOid = request.getParameter("photoOid");
        if (photoOid == null || photoOid.isEmpty()) {
            RetrievePersonalPhotoAction.writeUnavailablePhoto(response, getServlet());
        } else {
            final Photograph photo = FenixFramework.getDomainObject(photoOid);
            RetrievePersonalPhotoAction.writePhoto(response, photo);
        }
    }

    private void exportInformationXml(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        response.addHeader("Content-Disposition", "attachment; filename=epfl.xml");
        response.setContentType(Charset.defaultCharset().name());
        final byte[] content = ExportEPFLPhdProgramCandidacies.run();
        writeResponse(response, content, "application/xml");
    }

    private void writeResponse(HttpServletResponse response, final byte[] presentationPage, final String contentType)
            throws IOException {
        final ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType(contentType);
        outputStream.write(presentationPage);
        outputStream.close();
    }

    private ActionForward checkPermissions(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        final User userView = Authenticate.getUser();
        if (userView == null) {
            final String externalUser = (String) request.getSession().getAttribute(getClass().getName());
            if (externalUser != null && !externalUser.isEmpty()) {
                return null;
            }
            final String username = get(request, "username");
            if (username == null) {
                return displayLoginPage(request, response);
            }
            final String password = get(request, "password");
            if (isValidExternalUser(username, password)) {
                request.getSession().setAttribute(getClass().getName(), username);
                return null;
            }
        } else if (userView.getPerson().hasRole(RoleType.MANAGER)) {
            return null;
        }
        return displayUnAuhtorizedPage(request, response);
    }

    private String get(final HttpServletRequest request, final String attribute) {
        final String parameter = request.getParameter(attribute);
        return parameter == null ? (String) request.getAttribute(attribute) : parameter;
    }

    private boolean isValidExternalUser(final String username, final String password) {
        return !StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)
                && isValidExternalUserPassword(username, password);
    }

    private boolean isValidExternalUserPassword(final String username, final String password) {
        for (final ExternalUser externalUser : Bennu.getInstance().getExternalUserSet()) {
            if (externalUser.verify(username, password)) {
                return true;
            }
        }
        return false;
    }

    private ActionForward displayLoginPage(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        final String url = "http://fenix.ist.utl.pt/phd/epfl/applications/login";
        return new ActionForward(url, true);
    }

    private ActionForward displayUnAuhtorizedPage(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        final String url = "http://fenix.ist.utl.pt/phd/epfl/applications/notAuthorized";
        return new ActionForward(url, true);
    }

    private PhdIndividualProgramProcess readProcessByNumber(int year, String number) {
        Set<PhdIndividualProgramProcessNumber> processList = PhdIndividualProgramProcessNumber.readByYear(year);

        for (PhdIndividualProgramProcessNumber process : processList) {
            if (process.getNumber().toString().equals(number)) {
                return process.getProcess();
            }
        }

        return null;
    }

}