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
package org.fenixedu.academic.ui.struts.action.commons.student;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacy;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacy;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacySubmission;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/viewStudentApplication", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "view", path = "/student/application/view.jsp") })
public class ViewStudentApplicationDA extends FenixDispatchAction {

    public ActionForward view(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        final Candidacy application = getDomainObject(request, "applicationOID");
        request.setAttribute("candidacy", application);
        return mapping.findForward("view");
    }

    public ActionForward downloadDocument(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        final IndividualCandidacyDocumentFile document = getDomainObject(request, "documentOID");
        if (document != null && isAuthorized(document)) {
            response.setContentType(document.getContentType());
            response.addHeader("Content-Disposition", "attachment; filename=\"" + document.getFilename() + "\"");
            response.setContentLength(document.getSize().intValue());
            final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            dos.write(document.getContent());
            dos.close();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(HttpStatus.getStatusText(HttpStatus.SC_BAD_REQUEST));
            response.getWriter().close();
        }
        return null;
    }

    private boolean isAuthorized(final IndividualCandidacyDocumentFile document) {
        final User user = Authenticate.getUser();
        if (user != null) {
            final Person person = user.getPerson();
            if (isMobilityCoordinator(document, person)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMobilityCoordinator(final IndividualCandidacyDocumentFile document, final Person person) {
        for (final IndividualCandidacy individualCandidacy : document.getIndividualCandidacySet()) {
            final Registration candidacyRegistration = individualCandidacy.getRegistration();
            if (candidacyRegistration != null) {
                final Student student = candidacyRegistration.getStudent();
                if (student != null) {
                    if (student.getPerson() == person) {
                        return true;
                    }
                    for (final Registration registration : student.getRegistrationsSet()) {
                        for (final OutboundMobilityCandidacySubmission submission : registration
                                .getOutboundMobilityCandidacySubmissionSet()) {
                            for (final OutboundMobilityCandidacy candidacy : submission.getOutboundMobilityCandidacySet()) {
                                final OutboundMobilityCandidacyContest contest = candidacy.getOutboundMobilityCandidacyContest();
                                final OutboundMobilityCandidacyContestGroup group =
                                        contest.getOutboundMobilityCandidacyContestGroup();
                                for (final Person coordinator : group.getMobilityCoordinatorSet()) {
                                    if (coordinator == person) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
