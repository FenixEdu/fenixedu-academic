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
/*
 * Created 2004/04/25
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.finalDegreeWork;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.student.ConfirmAttributionOfFinalDegreeWork;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupStudent;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentSeniorsApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
@StrutsFunctionality(app = StudentSeniorsApp.class, path = "confirm-attribution",
        titleKey = "link.finalDegreeWork.confirmAttribution")
@Mapping(module = "student", path = "/finalDegreeWorkAttribution",
        input = "/finalDegreeWorkAttribution.do?method=prepare&page=0", formBean = "finalDegreeWorkAttributionForm")
@Forwards({ @Forward(name = "NoConfirmationInProcessException", path = "/student/finalDegreeWork/noConfirmationInProcess.jsp"),
        @Forward(name = "prepareShowFinalDegreeWorkList", path = "/student/finalDegreeWorkAttribution.do?method=prepare&page=0"),
        @Forward(name = "showFinalDegreeWorkList", path = "/student/finalDegreeWork/attribution.jsp") })
@Exceptions(
        value = {
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.ConfirmAttributionOfFinalDegreeWork.NoAttributionToConfirmException.class,
                        key = "error.message.NoAttributionToConfirmException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.NoDegreeStudentCurricularPlanFoundException.class,
                        key = "error.message.NoDegreeStudentCurricularPlanFoundException",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request") })
public class FinalDegreeWorkAttributionDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final ExecutionYear executionYear) throws Exception {
        final DynaActionForm finalDegreeWorkAttributionForm = (DynaActionForm) form;
        finalDegreeWorkAttributionForm.set("executionYearOID", executionYear.getExternalId().toString());

        final Set<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);
        executionYears.addAll(rootDomainObject.getExecutionYearsSet());
        request.setAttribute("executionYears", executionYears);

        final FinalDegreeWorkGroup group = findGroup(executionYear);
        if (group != null) {
            final InfoGroup infoGroup = InfoGroup.newInfoFromDomain(group);

            final ExecutionDegree executionDegree = group.getExecutionDegree();
            if (!executionDegree.hasScheduling() || executionDegree.getScheduling().getAttributionByTeachers() != Boolean.TRUE) {
                return mapping.findForward("NoConfirmationInProcessException");
            }

            Collections.sort(infoGroup.getGroupProposals(), new BeanComparator("orderOfPreference"));

            request.setAttribute("infoGroup", infoGroup);

            InfoGroupProposal infoGroupProposal =
                    (InfoGroupProposal) CollectionUtils.find(infoGroup.getGroupProposals(),
                            new PREDICATE_FIND_PROPOSAL_ATTRIBUTED_TO_GROUP_BY_TEACHER(infoGroup.getExternalId()));
            if (infoGroupProposal != null) {
                finalDegreeWorkAttributionForm.set("attributedByTeacher", infoGroupProposal.getFinalDegreeWorkProposal()
                        .getExternalId().toString());
            }

            String confirmAttributions[] = new String[infoGroup.getGroupStudents().size()];
            for (int i = 0; i < infoGroup.getGroupStudents().size(); i++) {
                InfoGroupStudent infoGroupStudent = infoGroup.getGroupStudents().get(i);
                if (infoGroupStudent != null && infoGroupStudent.getFinalDegreeWorkProposalConfirmation() != null) {
                    confirmAttributions[i] = infoGroupStudent.getFinalDegreeWorkProposalConfirmation().getExternalId().toString();
                    confirmAttributions[i] += infoGroupStudent.getStudent().getExternalId();
                }
            }
            finalDegreeWorkAttributionForm.set("confirmAttributions", confirmAttributions);

            request.setAttribute("finalDegreeWorkAttributionForm", finalDegreeWorkAttributionForm);
        }

        return mapping.findForward("showFinalDegreeWorkList");

    }

    public ActionForward prepareWithArgs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear executionYear = getDomainObject(request, "executionYearOID");
        return prepare(mapping, form, request, executionYear);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final DynaActionForm finalDegreeWorkAttributionForm = (DynaActionForm) form;
        final ExecutionYear executionYear;
        final String executionYearOID = (String) finalDegreeWorkAttributionForm.get("executionYearOID");
        executionYear =
                executionYearOID == null || executionYearOID.equals("") ? ExecutionYear.readCurrentExecutionYear() : FenixFramework
                        .<ExecutionYear> getDomainObject(executionYearOID);
        return prepare(mapping, finalDegreeWorkAttributionForm, request, executionYear);
    }

    private FinalDegreeWorkGroup findGroup(final ExecutionYear executionYear) {
        final User userView = Authenticate.getUser();
        for (final Registration registration : userView.getPerson().getStudent().getRegistrationsSet()) {
            for (final GroupStudent groupStudent : registration.getAssociatedGroupStudentsSet()) {
                final FinalDegreeWorkGroup group = groupStudent.getFinalDegreeDegreeWorkGroup();
                if (group != null && !group.getGroupProposalsSet().isEmpty()) {
                    final ExecutionDegree executionDegree = group.getExecutionDegree();
                    if (executionDegree != null && executionDegree.getExecutionYear() == executionYear) {
                        return group;
                    }
                }
            }
        }
        return null;
    }

    public ActionForward confirmAttribution(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm finalDegreeWorkAttributionForm = (DynaActionForm) form;

        String selectedGroupProposalOID = (String) finalDegreeWorkAttributionForm.get("selectedGroupProposal");

        User userView = Authenticate.getUser();

        if (selectedGroupProposalOID != null && !selectedGroupProposalOID.equals("")) {

            ConfirmAttributionOfFinalDegreeWork.run(userView.getUsername(), selectedGroupProposalOID);
        }

        return mapping.findForward("prepareShowFinalDegreeWorkList");
    }

    private class PREDICATE_FIND_PROPOSAL_ATTRIBUTED_TO_GROUP_BY_TEACHER implements Predicate {

        String groupID = null;

        @Override
        public boolean evaluate(Object arg0) {
            InfoGroupProposal infoGroupProposal = (InfoGroupProposal) arg0;
            return (infoGroupProposal.getFinalDegreeWorkProposal().getGroupAttributedByTeacher() != null)
                    && (groupID.equals(infoGroupProposal.getFinalDegreeWorkProposal().getGroupAttributedByTeacher()
                            .getExternalId()));
        }

        public PREDICATE_FIND_PROPOSAL_ATTRIBUTED_TO_GROUP_BY_TEACHER(String groupID) {
            super();
            this.groupID = groupID;
        }
    }

}