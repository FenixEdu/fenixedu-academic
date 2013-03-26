package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacy;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriodConfirmationOption;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "student", path = "/erasmusOutboundManagement", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "erasmusOutboundManagement", path = "/student/erasmusOutboundManagement.jsp",
        tileProperties = @Tile(title = "private.student.erasmusOutboundManagement")) })
public class ErasmusOutboundManagement extends FenixDispatchAction {

    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {
        final Student student = getUserView(request).getPerson().getStudent();
        return prepare(mapping, request, student);
    }

    public ActionForward prepare(final ActionMapping mapping, final HttpServletRequest request, final Student student) {
        request.setAttribute("student", student);
        return mapping.findForward("erasmusOutboundManagement");
    }

    public ActionForward apply(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {
        final Student student = getUserView(request).getPerson().getStudent();
        final OutboundMobilityCandidacyContest contest = getDomainObject(request, "contestOid");
        contest.apply(student);
        return prepare(mapping, request, student);
    }

    public ActionForward removeCandidacy(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {
        final OutboundMobilityCandidacy candidacy = getDomainObject(request, "candidacyOid");
        candidacy.delete();
        return prepare(mapping, form, request, response);
    }

    public ActionForward reorder(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {
        final Student student = getUserView(request).getPerson().getStudent();
        final OutboundMobilityCandidacy candidacy = getDomainObject(request, "candidacyOid");
        final int index = Integer.parseInt((String) getFromRequest(request, "index"));
        candidacy.reorder(index + 1);
        return prepare(mapping, request, student);
    }

    public ActionForward selectOption(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {
        final OutboundMobilityCandidacySubmission submission = getDomainObject(request, "submissionOid");
        final OutboundMobilityCandidacyPeriodConfirmationOption option = getDomainObject(request, "optionOid");
        submission.selectOption(option);
        return prepare(mapping, form, request, response);
    }

    public ActionForward removeOption(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {
        final OutboundMobilityCandidacySubmission submission = getDomainObject(request, "submissionOid");
        final OutboundMobilityCandidacyPeriodConfirmationOption option = getDomainObject(request, "optionOid");
        submission.removeOption(option);
        return prepare(mapping, form, request, response);
    }

}