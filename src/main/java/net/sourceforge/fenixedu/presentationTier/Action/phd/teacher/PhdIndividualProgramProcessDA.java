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
package net.sourceforge.fenixedu.presentationTier.Action.phd.teacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.CommonPhdIndividualProgramProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdCandidacyPredicateContainer;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdInactivePredicateContainer;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdSeminarPredicateContainer;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdThesisPredicateContainer;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherApplication.TeacherPhdApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.predicates.PredicateContainer;

@StrutsFunctionality(app = TeacherPhdApp.class, path = "processes", titleKey = "label.phd.manageProcesses")
@Mapping(path = "/phdIndividualProgramProcess", module = "teacher")
@Forwards({
        @Forward(name = "manageProcesses", path = "/phd/teacher/manageProcesses.jsp"),
        @Forward(name = "viewProcess", path = "/phd/teacher/viewProcess.jsp"),
        @Forward(name = "viewInactiveProcesses", path = "/phd/teacher/viewInactiveProcesses.jsp"),
        @Forward(name = "searchResults", path = "/phd/teacher/searchResults.jsp"),
        @Forward(name = "viewAlertMessages", path = "/phd/teacher/viewAlertMessages.jsp"),
        @Forward(name = "viewAlertMessageArchive", path = "/phd/teacher/viewAlertMessageArchive.jsp"),
        @Forward(name = "viewAlertMessage", path = "/phd/teacher/viewAlertMessage.jsp"),
        @Forward(name = "viewProcessAlertMessages", path = "/phd/teacher/viewProcessAlertMessages.jsp"),
        @Forward(name = "viewProcessAlertMessageArchive", path = "/phd/teacher/viewProcessAlertMessageArchive.jsp"),
        @Forward(name = "requestPublicPresentationSeminarComission",
                path = "/phd/teacher/requestPublicPresentationSeminarComission.jsp"),
        @Forward(name = "exemptPublicPresentationSeminarComission",
                path = "/phd/teacher/exemptPublicPresentationSeminarComission.jsp"),
        @Forward(name = "manageGuidanceDocuments", path = "/phd/teacher/manageGuidanceDocuments.jsp"),
        @Forward(name = "uploadGuidanceDocument", path = "/phd/teacher/uploadGuidanceDocument.jsp") })
public class PhdIndividualProgramProcessDA extends CommonPhdIndividualProgramProcessDA {

    private static final PredicateContainer<?>[] CANDIDACY_CATEGORY = { PhdCandidacyPredicateContainer.DELIVERED,
            PhdCandidacyPredicateContainer.PENDING, PhdCandidacyPredicateContainer.APPROVED,
            PhdCandidacyPredicateContainer.CONCLUDED };

    private static final PredicateContainer<?>[] SEMINAR_CATEGORY = { PhdSeminarPredicateContainer.SEMINAR_PROCESS_STARTED,
            PhdSeminarPredicateContainer.AFTER_FIRST_SEMINAR_REUNION };

    private static final PredicateContainer<?>[] THESIS_CATEGORY = { PhdThesisPredicateContainer.PROVISIONAL_THESIS_DELIVERED,
            PhdThesisPredicateContainer.DISCUSSION_SCHEDULED };

    @Override
    protected SearchPhdIndividualProgramProcessBean initializeSearchBean(HttpServletRequest request) {

        final SearchPhdIndividualProgramProcessBean searchBean = new SearchPhdIndividualProgramProcessBean();
        searchBean.setFilterPhdPrograms(false);

        final List<PhdIndividualProgramProcess> processes = new ArrayList<PhdIndividualProgramProcess>();
        for (final InternalPhdParticipant participant : getLoggedPerson(request).getInternalParticipantsSet()) {
            processes.add(participant.getIndividualProcess());
        }

        searchBean.setProcesses(processes);

        return searchBean;

    }

    @Override
    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();
        final PhdIndividualProgramProcess process = getProcess(request);
        Collection<PhdParticipant> guidingsList = process.getGuidingsSet();
        Collection<PhdParticipant> assistantGuidingsList = process.getAssistantGuidingsSet();
        request.setAttribute("guidingsList", guidingsList);
        request.setAttribute("assistantGuidingsList", assistantGuidingsList);
        return forwardToViewProcess(mapping, request);
    }

    @Override
    protected PhdInactivePredicateContainer getConcludedContainer() {
        return PhdInactivePredicateContainer.CONCLUDED_THIS_YEAR;
    }

    @Override
    protected List<PredicateContainer<?>> getThesisCategory() {
        return Arrays.asList(THESIS_CATEGORY);
    }

    @Override
    protected List<PredicateContainer<?>> getSeminarCategory() {
        return Arrays.asList(SEMINAR_CATEGORY);
    }

    @Override
    protected List<PredicateContainer<?>> getCandidacyCategory() {
        return Arrays.asList(CANDIDACY_CATEGORY);
    }

    @Override
    public ActionForward prepareRequestPublicPresentationSeminarComission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        ActionForward forward = super.prepareRequestPublicPresentationSeminarComission(mapping, form, request, response);

        PublicPresentationSeminarProcessBean bean =
                (PublicPresentationSeminarProcessBean) request.getAttribute("requestPublicPresentationSeminarComissionBean");
        bean.setGenerateAlert(true);

        return forward;
    }

    @Override
    @EntryPoint
    public ActionForward manageProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return super.manageProcesses(mapping, form, request, response);
    }
}
