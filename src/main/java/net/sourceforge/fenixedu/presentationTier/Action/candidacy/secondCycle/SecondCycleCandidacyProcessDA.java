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
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.secondCycle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.SecondCycleCandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminCandidaciesApp;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.excel.SpreadsheetXLSExporter;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

@StrutsFunctionality(app = AcademicAdminCandidaciesApp.class, path = "second-cycle", titleKey = "label.candidacy.secondCycle",
        accessGroup = "(academic(MANAGE_CANDIDACY_PROCESSES) | academic(MANAGE_INDIVIDUAL_CANDIDACIES))",
        bundle = "ApplicationResources")
@Mapping(path = "/caseHandlingSecondCycleCandidacyProcess", module = "academicAdministration",
        formBeanClass = SecondCycleCandidacyProcessDA.SecondCycleCandidacyProcessForm.class)
@Forwards({
        @Forward(name = "intro", path = "/candidacy/secondCycle/mainCandidacyProcess.jsp"),
        @Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
        @Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
        @Forward(name = "send-to-coordinator", path = "/candidacy/sendToCoordinator.jsp"),
        @Forward(name = "introduce-candidacy-results", path = "/candidacy/secondCycle/introduceCandidacyResults.jsp"),
        @Forward(name = "introduce-candidacy-results-for-degree",
                path = "/candidacy/secondCycle/introduceCandidacyResultsForDegree.jsp"),
        @Forward(name = "send-to-scientificCouncil", path = "/candidacy/sendToScientificCouncil.jsp"),
        @Forward(name = "create-registrations", path = "/candidacy/createRegistrations.jsp"),
        @Forward(name = "view-child-process-with-missing-required-documents",
                path = "/candidacy/secondCycle/viewChildProcessWithMissingRequiredDocuments.jsp"),
        @Forward(name = "prepare-select-available-degrees", path = "/candidacy/selectAvailableDegrees.jsp") })
public class SecondCycleCandidacyProcessDA extends CandidacyProcessDA {

    static public class SecondCycleCandidacyProcessForm extends CandidacyProcessForm {
        private String selectedProcessId;

        public String getSelectedProcessId() {
            return selectedProcessId;
        }

        public void setSelectedProcessId(String selectedProcessId) {
            this.selectedProcessId = selectedProcessId;
        }
    }

    @Override
    protected Class getProcessType() {
        return SecondCycleCandidacyProcess.class;
    }

    @Override
    protected Class getChildProcessType() {
        return SecondCycleIndividualCandidacyProcess.class;
    }

    @Override
    protected Class getCandidacyPeriodType() {
        return SecondCycleCandidacyPeriod.class;
    }

    @Override
    protected SecondCycleCandidacyProcess getProcess(HttpServletRequest request) {
        return (SecondCycleCandidacyProcess) super.getProcess(request);
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setChooseDegreeBean(request);
        return super.execute(mapping, actionForm, request, response);
    }

    protected void setChooseDegreeBean(HttpServletRequest request) {
        ChooseDegreeBean chooseDegreeBean = (ChooseDegreeBean) getObjectFromViewState("choose.degree.bean");

        if (chooseDegreeBean == null) {
            chooseDegreeBean = new ChooseDegreeBean();
        }
        final SecondCycleCandidacyProcess process = (SecondCycleCandidacyProcess) readProcess(request);
        chooseDegreeBean.setCandidacyProcess(process);

        request.setAttribute("chooseDegreeBean", chooseDegreeBean);
    }

    protected ChooseDegreeBean getChooseDegreeBean(HttpServletRequest request) {
        return (ChooseDegreeBean) request.getAttribute("chooseDegreeBean");
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        if (!hasExecutionInterval(request)) {
            final List<ExecutionInterval> executionIntervals = getExecutionIntervalsWithCandidacyPeriod();

            if (executionIntervals.size() == 1) {
                final ExecutionInterval executionInterval = executionIntervals.iterator().next();
                final List<SecondCycleCandidacyProcess> candidacyProcesses = getCandidacyProcesses(executionInterval);

                if (candidacyProcesses.size() == 1) {
                    final SecondCycleCandidacyProcess process = candidacyProcesses.iterator().next();
                    setCandidacyProcessInformation(request, process);
                    setCandidacyProcessInformation(actionForm, getProcess(request));
                    request.setAttribute("candidacyProcesses", candidacyProcesses);
                    ChooseDegreeBean chooseDegreeBean = getChooseDegreeBean(request);
                    chooseDegreeBean.setCandidacyProcess(process);
                    return;
                }
            }

            request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
            request.setAttribute("executionIntervals", executionIntervals);

        } else {
            final ExecutionInterval executionInterval = getExecutionInterval(request);
            final SecondCycleCandidacyProcess candidacyProcess = getCandidacyProcess(request, executionInterval);

            if (candidacyProcess != null) {
                setCandidacyProcessInformation(request, candidacyProcess);
                setCandidacyProcessInformation(actionForm, getProcess(request));
            } else {
                final List<SecondCycleCandidacyProcess> candidacyProcesses = getCandidacyProcesses(executionInterval);

                if (candidacyProcesses.size() == 1) {
                    final SecondCycleCandidacyProcess process = candidacyProcesses.iterator().next();
                    setCandidacyProcessInformation(request, process);
                    setCandidacyProcessInformation(actionForm, getProcess(request));
                    request.setAttribute("candidacyProcesses", candidacyProcesses);
                    ChooseDegreeBean chooseDegreeBean = getChooseDegreeBean(request);
                    chooseDegreeBean.setCandidacyProcess(process);
                    return;
                }

                request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
                request.setAttribute("executionIntervals", getExecutionIntervalsWithCandidacyPeriod());
            }
            request.setAttribute("candidacyProcesses", getCandidacyProcesses(executionInterval));
        }
    }

    private List<ExecutionInterval> getExecutionIntervalsWithCandidacyPeriod() {
        return ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType());
    }

    protected List<SecondCycleCandidacyProcess> getCandidacyProcesses(final ExecutionInterval executionInterval) {
        final List<SecondCycleCandidacyProcess> result = new ArrayList<SecondCycleCandidacyProcess>();
        for (final SecondCycleCandidacyPeriod period : executionInterval.getSecondCycleCandidacyPeriods()) {
            result.add(period.getSecondCycleCandidacyProcess());
        }
        return result;
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        setCandidacyProcessInformation(request, getProcess(request));
        setCandidacyProcessInformation(form, getProcess(request));
        request.setAttribute("candidacyProcesses", getCandidacyProcesses(getProcess(request).getCandidacyExecutionInterval()));
        return introForward(mapping);
    }

    protected void setCandidacyProcessInformation(final ActionForm actionForm, final SecondCycleCandidacyProcess process) {
        final SecondCycleCandidacyProcessForm form = (SecondCycleCandidacyProcessForm) actionForm;
        form.setSelectedProcessId(process.getExternalId());
        form.setExecutionIntervalId(process.getCandidacyExecutionInterval().getExternalId());
    }

    @Override
    protected ActionForward introForward(ActionMapping mapping) {
        return mapping.findForward("intro");
    }

    @Override
    protected SecondCycleCandidacyProcess getCandidacyProcess(final HttpServletRequest request,
            final ExecutionInterval executionInterval) {

        final String selectedProcessId = getStringFromRequest(request, "selectedProcessId");
        if (selectedProcessId != null) {
            for (final SecondCycleCandidacyPeriod candidacyPeriod : executionInterval.getSecondCycleCandidacyPeriods()) {
                if (candidacyPeriod.getSecondCycleCandidacyProcess().getExternalId().equals(selectedProcessId)) {
                    return candidacyPeriod.getSecondCycleCandidacyProcess();
                }
            }
        }
        return null;
    }

    public ActionForward prepareExecuteSendToCoordinator(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("send-to-coordinator");
    }

    public ActionForward executeSendToCoordinator(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "SendToCoordinator");
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return prepareExecuteSendToCoordinator(mapping, actionForm, request, response);
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecutePrintCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + getReportFilename());

        final ServletOutputStream writer = response.getOutputStream();
        writeReport(getProcess(request), writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    public ActionForward prepareExecuteSendToScientificCouncil(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("send-to-scientificCouncil");
    }

    public ActionForward executeSendToScientificCouncil(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "SendToScientificCouncil");
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return prepareExecuteSendToScientificCouncil(mapping, actionForm, request, response);
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    private void writeReport(final SecondCycleCandidacyProcess process, final ServletOutputStream writer) throws IOException {
        final List<Spreadsheet> spreadsheets = new ArrayList<Spreadsheet>();
        for (final Entry<Degree, SortedSet<SecondCycleIndividualCandidacyProcess>> entry : process
                .getValidSecondCycleIndividualCandidaciesByDegree().entrySet()) {
            spreadsheets.add(buildReport(entry.getKey(), entry.getValue()));
        }
        new SpreadsheetXLSExporter().exportToXLSSheets(writer, spreadsheets);
    }

    private Spreadsheet buildReport(final Degree degree, final SortedSet<SecondCycleIndividualCandidacyProcess> name) {
        final Spreadsheet spreadsheet = new Spreadsheet(degree.getSigla(), getHeader());

        for (final SecondCycleIndividualCandidacyProcess process : name) {
            if (!process.canExecuteActivity(Authenticate.getUser())) {
                continue;
            }
            final Row row = spreadsheet.addRow();
            row.setCell(process.getPersonalDetails().getName());
            row.setCell(process.getPrecedentDegreeInformation().getConclusionGrade());
            row.setCell(process.getCandidacyProfessionalExperience());
            row.setCell(process.getPrecedentDegreeInformation().getDegreeAndInstitutionName());
            row.setCell(process.getCandidacyAffinity());
            row.setCell(process.getCandidacyDegreeNature());
            row.setCell(process.getCandidacyGrade());
            row.setCell(process.getCandidacyInterviewGrade() != null ? process.getCandidacyInterviewGrade() : " ");
            row.setCell(process.getCandidacySeriesGrade());
            if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
                row.setCell(BundleUtil.getString(Bundle.ENUMERATION, process.getCandidacyState().getQualifiedName()));
            } else {
                row.setCell(" ");
            }
        }

        return spreadsheet;
    }

    private List<Object> getHeader() {
        final List<Object> result = new ArrayList<Object>();
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.name"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.mfc"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.professionalExperience"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.degree.and.school"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.affinity"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.degreeNature"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.grade"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.interviewGrade"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.seriesGrade"));
        result.add(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.result"));
        return result;
    }

    static public class SecondCycleCandidacyDegreeBean extends CandidacyDegreeBean {
        private String notes;

        public SecondCycleCandidacyDegreeBean(final SecondCycleIndividualCandidacyProcess process) {
            setPersonalDetails(process.getPersonalDetails());
            setDegree(process.getCandidacySelectedDegree());
            setState(process.getCandidacyState());
            setRegistrationCreated(process.hasRegistrationForCandidacy());
            setNotes(process.getCandidacyNotes());
        }

        public String getNotes() {
            return notes;
        }

        private void setNotes(String notes) {
            this.notes = notes;
        }
    }

    @Override
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(HttpServletRequest request) {
        final SecondCycleCandidacyProcess process = getProcess(request);
        final List<CandidacyDegreeBean> candidacyDegreeBeans = new ArrayList<CandidacyDegreeBean>();
        for (final SecondCycleIndividualCandidacyProcess child : process.getAcceptedSecondCycleIndividualCandidacies()) {
            candidacyDegreeBeans.add(new SecondCycleCandidacyDegreeBean(child));
        }
        Collections.sort(candidacyDegreeBeans);
        return candidacyDegreeBeans;
    }

    @Override
    protected List<Object> getCandidacyHeader() {
        final List<Object> result = new ArrayList<Object>(super.getCandidacyHeader());
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.notes"));
        return result;
    }

    @Override
    protected Spreadsheet buildIndividualCandidacyReport(final Spreadsheet spreadsheet,
            final IndividualCandidacyProcess individualCandidacyProcess) {
        SecondCycleIndividualCandidacyProcess secondCycleIndividualCandidacyProcess =
                (SecondCycleIndividualCandidacyProcess) individualCandidacyProcess;

        final Row row = spreadsheet.addRow();
        row.setCell(secondCycleIndividualCandidacyProcess.getProcessCode());
        row.setCell(secondCycleIndividualCandidacyProcess.getPersonalDetails().getName());
        row.setCell(secondCycleIndividualCandidacyProcess.getPersonalDetails().getIdDocumentType().getLocalizedName());
        row.setCell(secondCycleIndividualCandidacyProcess.getPersonalDetails().getDocumentIdNumber());

        row.setCell(secondCycleIndividualCandidacyProcess.getPersonalDetails().getCountry() != null ? secondCycleIndividualCandidacyProcess
                .getPersonalDetails().getCountry().getCountryNationality().getContent() : "");

        row.setCell(secondCycleIndividualCandidacyProcess.getPrecedentDegreeInformation().getDegreeAndInstitutionName());
        row.setCell(secondCycleIndividualCandidacyProcess.getPrecedentDegreeInformation().getDegreeDesignation());
        row.setCell(secondCycleIndividualCandidacyProcess.getPrecedentDegreeInformation().getConclusionDate() != null ? secondCycleIndividualCandidacyProcess
                .getPrecedentDegreeInformation().getConclusionDate().toString(dateFormat) : "");
        row.setCell(secondCycleIndividualCandidacyProcess.getPrecedentDegreeInformation().getConclusionGrade());

        StringBuilder degreesSb = new StringBuilder();
        for (Degree degree : secondCycleIndividualCandidacyProcess.getCandidacy().getSelectedDegrees()) {
            degreesSb.append(degree.getName()).append("\n");
        }

        row.setCell(degreesSb.toString());
        row.setCell(BundleUtil.getString(Bundle.ENUMERATION, individualCandidacyProcess.getCandidacyState().getQualifiedName()));
        row.setCell(BundleUtil.getString(Bundle.CANDIDATE, secondCycleIndividualCandidacyProcess.getProcessChecked() != null
                && secondCycleIndividualCandidacyProcess.getProcessChecked() ? MESSAGE_YES : MESSAGE_NO));
        row.setCell(secondCycleIndividualCandidacyProcess.getCandidacyNotes());
        return spreadsheet;
    }

    @Override
    protected Predicate<IndividualCandidacyProcess> getChildProcessSelectionPredicate(final CandidacyProcess process,
            HttpServletRequest request) {
        final Degree selectedDegree = getChooseDegreeBean(request).getDegree();
        if (selectedDegree == null) {
            return Predicates.alwaysTrue();
        } else {
            return new Predicate<IndividualCandidacyProcess>() {
                @Override
                public boolean apply(IndividualCandidacyProcess process) {
                    return ((SecondCycleIndividualCandidacyProcess) process).getCandidacy().getSelectedDegrees()
                            .contains(selectedDegree);
                }
            };
        }
    }

    public ActionForward prepareExecuteViewChildProcessWithMissingRequiredDocumentFiles(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        setCandidacyProcessInformation(request, getProcess(request));
        setCandidacyProcessInformation(form, getProcess(request));
        request.setAttribute("candidacyProcesses", getCandidacyProcesses(getProcess(request).getCandidacyExecutionInterval()));

        request.setAttribute("childsWithMissingRequiredDocuments", getChildsWithMissingRequiredDocuments(getProcess(request)));

        return mapping.findForward("view-child-process-with-missing-required-documents");
    }

    private Collection<IndividualCandidacyProcess> getChildsWithMissingRequiredDocuments(SecondCycleCandidacyProcess process) {

        return Collections2.filter(process.getChildsWithMissingRequiredDocuments(), CAN_EXECUTE_ACTIVITY_PREDICATE);
    }

}
