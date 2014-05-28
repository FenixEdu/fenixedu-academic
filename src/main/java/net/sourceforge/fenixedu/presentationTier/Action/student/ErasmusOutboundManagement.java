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
package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacy;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriodConfirmationOption;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentParticipateApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@StrutsFunctionality(app = StudentParticipateApp.class, path = "outbound-mobility",
        titleKey = "link.title.student.mobility.outbound")
@Mapping(module = "student", path = "/erasmusOutboundManagement")
@Forwards(value = { @Forward(name = "erasmusOutboundManagement", path = "/student/erasmusOutboundManagement.jsp",
        tileProperties = @Tile(title = "private.student.erasmusOutboundManagement")) })
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