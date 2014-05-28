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
package net.sourceforge.fenixedu.presentationTier.Action.commons.student;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacy;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.SearchForStudentsDA;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
            if (person.hasRole(RoleType.MANAGER) || isMobilityCoordinator(document, person)) {
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
