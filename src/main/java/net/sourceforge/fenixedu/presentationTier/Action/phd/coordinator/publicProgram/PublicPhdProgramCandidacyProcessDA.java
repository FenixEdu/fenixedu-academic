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
package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.publicProgram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea;
import net.sourceforge.fenixedu.domain.phd.ThesisSubject;
import net.sourceforge.fenixedu.domain.phd.candidacy.EPFLPhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.CoordinatorApplication.CoordinatorPhdApp;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice.PhdProgramCandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@StrutsFunctionality(app = CoordinatorPhdApp.class, path = "ist-epfl", titleKey = "label.phd.ist.epfl.collaboration.type",
        accessGroup = "nobody")
@Mapping(path = "/candidacies/phdProgramCandidacyProcess", module = "coordinator")
@Forwards({ @Forward(name = "listProcesses", path = "/phd/coordinator/publicProgram/listProcesses.jsp"),
        @Forward(name = "viewProcess", path = "/phd/coordinator/publicProgram/viewProcess.jsp"),
        @Forward(name = "viewCandidacyRefereeLetter", path = "/phd/coordinator/publicProgram/viewCandidacyRefereeLetter.jsp"),
        @Forward(name = "manageFocusAreas", path = "/phd/coordinator/publicProgram/manageFocusAreas.jsp"),
        @Forward(name = "manageThesisSubjects", path = "/phd/coordinator/publicProgram/manageThesisSubjects.jsp"),
        @Forward(name = "editThesisSubject", path = "/phd/coordinator/publicProgram/editThesisSubject.jsp") })
public class PublicPhdProgramCandidacyProcessDA extends PhdProgramCandidacyProcessDA {

    @EntryPoint
    public ActionForward listProcesses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        SelectPhdCandidacyPeriodBean selectPeriodBean = getSelectPhdCandidacyPeriodBean();

        if (selectPeriodBean == null) {
            selectPeriodBean = new SelectPhdCandidacyPeriodBean(EPFLPhdCandidacyPeriod.getMostRecentCandidacyPeriod());
        }

        final Statistics statistics = new Statistics();

        final List<PublicPhdCandidacyBean> candidacyHashCodes = new ArrayList<PublicPhdCandidacyBean>();
        for (final PublicCandidacyHashCode hashCode : Bennu.getInstance().getCandidacyHashCodesSet()) {
            if (hashCode.isFromPhdProgram()) {
                final PhdProgramPublicCandidacyHashCode phdHashCode = (PhdProgramPublicCandidacyHashCode) hashCode;

                PhdCandidacyPeriod phdCandidacyPeriod = selectPeriodBean.getPhdCandidacyPeriod();

                if (!phdHashCode.hasPhdProgramCandidacyProcess()) {

                    if (phdCandidacyPeriod.contains(phdHashCode.getWhenCreated())) {
                        statistics.plusTotalRequests();
                        candidacyHashCodes.add(new PublicPhdCandidacyBean(phdHashCode));
                    }

                    continue;
                }

                PhdIndividualProgramProcess individualProgramProcess = phdHashCode.getIndividualProgramProcess();

                if (individualProgramProcess.getExecutionYear() != ExecutionYear.readCurrentExecutionYear()) {
                    continue;
                }

                if (!PhdIndividualProgramCollaborationType.EPFL.equals(individualProgramProcess.getCollaborationType())) {
                    continue;
                }

                if (!PhdIndividualProgramProcessState.CANDIDACY.equals(individualProgramProcess.getActiveState())) {
                    continue;
                }

                if (phdHashCode.hasCandidacyProcess()) {
                    statistics.plusTotalCandidates();
                }

                if (phdHashCode.hasCandidacyProcess() && phdHashCode.getPhdProgramCandidacyProcess().isValidatedByCandidate()) {
                    statistics.plusTotalValidated();
                }

                candidacyHashCodes.add(new PublicPhdCandidacyBean(phdHashCode));
            }
        }

        request.setAttribute("candidacyHashCodes", candidacyHashCodes);
        request.setAttribute("statistics", statistics);
        request.setAttribute("selectPeriodBean", selectPeriodBean);

        RenderUtils.invalidateViewState();

        return mapping.findForward("listProcesses");
    }

    private SelectPhdCandidacyPeriodBean getSelectPhdCandidacyPeriodBean() {
        return (SelectPhdCandidacyPeriodBean) getObjectFromViewState("select-period-bean");
    }

    public ActionForward viewProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("hashCode", getDomainObject(request, "hashCodeId"));
        return mapping.findForward("viewProcess");
    }

    public ActionForward viewCandidacyRefereeLetter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyRefereeLetter",
                ((PhdCandidacyReferee) getDomainObject(request, "candidacyRefereeId")).getLetter());
        return mapping.findForward("viewCandidacyRefereeLetter");
    }

    public ActionForward sendCandidacyRefereeEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdCandidacyReferee referee = getDomainObject(request, "candidacyRefereeId");
        referee.sendEmail();
        addActionMessage("error", request, "message.resent.email.to", referee.getEmail());
        return viewProcess(mapping, actionForm, request, response);
    }

    public ActionForward manageFocusAreas(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("focusAreas", Bennu.getInstance().getPhdProgramFocusAreasSet());

        return mapping.findForward("manageFocusAreas");
    }

    public ActionForward manageThesisSubjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        PhdProgramFocusArea focusArea = getDomainObject(request, "focusAreaId");

        request.setAttribute("focusArea", focusArea);
        request.setAttribute("thesisSubjectBean", new ThesisSubjectBean());
        request.setAttribute("thesisSubjects", focusArea.getThesisSubjects());

        return mapping.findForward("manageThesisSubjects");
    }

    public ActionForward addThesisSubject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        ThesisSubjectBean bean = getRenderedObject("thesisSubjectBean");
        PhdProgramFocusArea focusArea = getDomainObject(request, "focusAreaId");

        try {
            ThesisSubject.createThesisSubject(focusArea, bean.getName(), bean.getDescription(), bean.getTeacher(),
                    bean.getExternalAdvisorName());

        } catch (PhdDomainOperationException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
            return addThesisSubjectInvalid(mapping, form, request, response);
        }

        RenderUtils.invalidateViewState();
        return manageThesisSubjects(mapping, form, request, response);
    }

    public ActionForward addThesisSubjectInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramFocusArea focusArea = getDomainObject(request, "focusAreaId");
        ThesisSubjectBean bean = getRenderedObject("thesisSubjectBean");

        request.setAttribute("focusArea", focusArea);
        request.setAttribute("thesisSubjectBean", bean);
        request.setAttribute("thesisSubjects", focusArea.getThesisSubjects());

        return mapping.findForward("manageThesisSubjects");
    }

    public ActionForward removeThesisSubject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ThesisSubject thesisSubject =
                (ThesisSubject) FenixFramework.getDomainObject((String) getFromRequest(request, "thesisSubjectId"));
        try {
            thesisSubject.delete();
        } catch (PhdDomainOperationException e) {
            addActionMessage("errors", request, e.getKey(), e.getArgs());
        }

        RenderUtils.invalidateViewState();
        return manageThesisSubjects(mapping, form, request, response);
    }

    public ActionForward prepareEditThesisSubject(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        ThesisSubject subject = getDomainObject(request, "thesisSubjectId");
        PhdProgramFocusArea focusArea = getDomainObject(request, "focusAreaId");

        ThesisSubjectBean bean = new ThesisSubjectBean(subject);

        request.setAttribute("bean", bean);
        request.setAttribute("focusArea", focusArea);
        request.setAttribute("thesisSubject", subject);

        return mapping.findForward("editThesisSubject");
    }

    public ActionForward editThesisSubject(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        ThesisSubject subject = getDomainObject(request, "thesisSubjectId");
        ThesisSubjectBean bean = getRenderedObject("bean");

        try {
            subject.edit(bean.getName(), bean.getDescription(), bean.getTeacher(), bean.getExternalAdvisorName());
        } catch (PhdDomainOperationException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
            return editThesisSubjectInvalid(mapping, form, request, response);
        }

        return manageThesisSubjects(mapping, form, request, response);

    }

    public ActionForward editThesisSubjectInvalid(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        ThesisSubjectBean bean = getRenderedObject("bean");
        request.setAttribute("bean", bean);

        return mapping.findForward("editThesisSubject");
    }

    static public class SelectPhdCandidacyPeriodBean implements Serializable {

        private static final long serialVersionUID = 1L;

        private PhdCandidacyPeriod candidacyPeriod;

        public PhdCandidacyPeriod getPhdCandidacyPeriod() {
            return this.candidacyPeriod;
        }

        public void setPhdCandidacyPeriod(final PhdCandidacyPeriod candidacyPeriod) {
            this.candidacyPeriod = candidacyPeriod;
        }

        public SelectPhdCandidacyPeriodBean(final PhdCandidacyPeriod candidacyPeriod) {
            this.candidacyPeriod = candidacyPeriod;
        }

    }

    static public final class PhdCandidacyPeriodDataProvider extends AbstractDomainObjectProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            return getCandidacyPeriods();
        }

        private List<PhdCandidacyPeriod> getCandidacyPeriods() {
            List<PhdCandidacyPeriod> candidacyPeriodList = new ArrayList<PhdCandidacyPeriod>();

            CollectionUtils.select(Bennu.getInstance().getCandidacyPeriodsSet(), new Predicate() {

                @Override
                public boolean evaluate(Object arg0) {
                    return arg0 instanceof PhdCandidacyPeriod;
                }

            }, candidacyPeriodList);

            return candidacyPeriodList;
        }

    }

    static public class Statistics implements Serializable {
        static private final long serialVersionUID = 1L;

        private int totalRequests = 0;
        private int totalCandidates = 0;
        private int totalValidated = 0;

        Statistics() {
        }

        public int getTotalRequests() {
            return totalRequests;
        }

        private void plusTotalRequests() {
            totalRequests++;
        }

        public int getTotalCandidates() {
            return totalCandidates;
        }

        private void plusTotalCandidates() {
            totalCandidates++;
        }

        public int getTotalValidated() {
            return totalValidated;
        }

        private void plusTotalValidated() {
            totalValidated++;
        }
    }

    static public class PublicPhdCandidacyBean implements Serializable {

        private static final long serialVersionUID = 1L;

        private PhdProgramPublicCandidacyHashCode hashCode;

        private String email;
        private String name;
        private String phdFocusArea;
        private boolean candidate;
        private boolean validated;

        public PublicPhdCandidacyBean() {
        }

        public PublicPhdCandidacyBean(final PhdProgramPublicCandidacyHashCode hashCode) {
            setHashCode(hashCode);

            setEmail(hashCode.getEmail());
            setName(hashCode.hasCandidacyProcess() ? hashCode.getPerson().getName() : null);
            setPhdFocusArea(hashCode.hasCandidacyProcess()
                    && hashCode.getIndividualProgramProcess().getPhdProgramFocusArea() != null ? hashCode
                    .getIndividualProgramProcess().getPhdProgramFocusArea().getName().getContent() : null);
            setCandidate(hashCode.hasCandidacyProcess());
            setValidated(hashCode.hasCandidacyProcess() ? hashCode.getPhdProgramCandidacyProcess().isValidatedByCandidate() : false);
        }

        public PhdProgramPublicCandidacyHashCode getHashCode() {
            return this.hashCode;
        }

        public void setHashCode(PhdProgramPublicCandidacyHashCode hashCode) {
            this.hashCode = hashCode;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhdFocusArea() {
            return phdFocusArea;
        }

        public void setPhdFocusArea(String phdFocusArea) {
            this.phdFocusArea = phdFocusArea;
        }

        public boolean isCandidate() {
            return candidate;
        }

        public void setCandidate(boolean candidate) {
            this.candidate = candidate;
        }

        public boolean isValidated() {
            return validated;
        }

        public void setValidated(boolean validated) {
            this.validated = validated;
        }
    }

    public static class ThesisSubjectBean implements Serializable {

        private static final long serialVersionUID = 1L;

        private MultiLanguageString name;
        private MultiLanguageString description;
        private Teacher teacher;
        private String externalAdvisorName;

        public ThesisSubjectBean() {

        }

        public ThesisSubjectBean(final ThesisSubject thesisSubject) {
            setName(thesisSubject.getName());
            setDescription(thesisSubject.getDescription());
            setTeacher(thesisSubject.getTeacher());
            setExternalAdvisorName(thesisSubject.getExternalAdvisorName());
        }

        public MultiLanguageString getName() {
            return name;
        }

        public void setName(MultiLanguageString name) {
            this.name = name;
        }

        public MultiLanguageString getDescription() {
            return description;
        }

        public void setDescription(MultiLanguageString description) {
            this.description = description;
        }

        public Teacher getTeacher() {
            return teacher;
        }

        public void setTeacher(final Teacher teacher) {
            this.teacher = teacher;
        }

        public String getExternalAdvisorName() {
            return externalAdvisorName;
        }

        public void setExternalAdvisorName(String externalAdvisorName) {
            this.externalAdvisorName = externalAdvisorName;
        }
    }

}
