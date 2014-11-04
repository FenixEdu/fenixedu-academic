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
package org.fenixedu.academic.ui.struts.action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacy;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacyPeriodConfirmationOption;
import org.fenixedu.academic.domain.mobility.outbound.OutboundMobilityCandidacySubmission;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentParticipateApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = StudentParticipateApp.class, path = "outbound-mobility",
        titleKey = "link.title.student.mobility.outbound")
@Mapping(module = "student", path = "/erasmusOutboundManagement")
@Forwards(@Forward(name = "erasmusOutboundManagement", path = "/student/erasmusOutboundManagement.jsp"))
public class ErasmusOutboundManagement extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final Student student = getUserView(request).getPerson().getStudent();
        return prepare(mapping, request, student);
    }

    public ActionForward prepare(final ActionMapping mapping, final HttpServletRequest request, final Student student) {
        request.setAttribute("student", student);
        return mapping.findForward("erasmusOutboundManagement");
    }

    public ActionForward apply(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final Student student = getUserView(request).getPerson().getStudent();
        final OutboundMobilityCandidacyContest contest = getDomainObject(request, "contestOid");
        contest.apply(student);
        return prepare(mapping, request, student);
    }

    public ActionForward removeCandidacy(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final OutboundMobilityCandidacy candidacy = getDomainObject(request, "candidacyOid");
        candidacy.delete();
        return prepare(mapping, form, request, response);
    }

    public ActionForward reorder(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final Student student = getUserView(request).getPerson().getStudent();
        final OutboundMobilityCandidacy candidacy = getDomainObject(request, "candidacyOid");
        final int index = Integer.parseInt((String) getFromRequest(request, "index"));
        candidacy.reorder(index + 1);
        return prepare(mapping, request, student);
    }

    public ActionForward selectOption(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final OutboundMobilityCandidacySubmission submission = getDomainObject(request, "submissionOid");
        final OutboundMobilityCandidacyPeriodConfirmationOption option = getDomainObject(request, "optionOid");
        submission.selectOption(option);
        return prepare(mapping, form, request, response);
    }

    public ActionForward removeOption(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException, FenixServiceException {
        final OutboundMobilityCandidacySubmission submission = getDomainObject(request, "submissionOid");
        final OutboundMobilityCandidacyPeriodConfirmationOption option = getDomainObject(request, "optionOid");
        submission.removeOption(option);
        return prepare(mapping, form, request, response);
    }

}