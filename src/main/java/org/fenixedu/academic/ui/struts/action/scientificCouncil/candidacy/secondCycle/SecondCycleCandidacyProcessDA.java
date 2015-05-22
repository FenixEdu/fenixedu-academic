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
package org.fenixedu.academic.ui.struts.action.scientificCouncil.candidacy.secondCycle;

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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess;
import org.fenixedu.academic.domain.period.SecondCycleCandidacyPeriod;
import org.fenixedu.academic.ui.struts.action.candidacy.CandidacyProcessDA;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificApplicationsApp;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.fenixedu.commons.spreadsheet.SpreadsheetXLSExporter;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ScientificApplicationsApp.class, path = "second-cycle",
        titleKey = "title.application.name.secondCycle")
@Mapping(path = "/caseHandlingSecondCycleCandidacyProcess", module = "scientificCouncil",
        formBeanClass = SecondCycleCandidacyProcessDA.SecondCycleCandidacyProcessForm.class)
@Forwards(@Forward(name = "intro", path = "/scientificCouncil/candidacy/secondCycle/mainCandidacyProcess.jsp"))
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

    private void setChooseDegreeBean(HttpServletRequest request) {
        ChooseDegreeBean chooseDegreeBean = (ChooseDegreeBean) getObjectFromViewState("choose.degree.bean");

        if (chooseDegreeBean == null) {
            chooseDegreeBean = new ChooseDegreeBean();
        }

        request.setAttribute("chooseDegreeBean", chooseDegreeBean);
    }

    private ChooseDegreeBean getChooseDegreeBean(HttpServletRequest request) {
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
                    setCandidacyProcessInformation(request, candidacyProcesses.iterator().next());
                    setCandidacyProcessInformation(actionForm, getProcess(request));
                    request.setAttribute("candidacyProcesses", candidacyProcesses);
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
                    setCandidacyProcessInformation(request, candidacyProcesses.iterator().next());
                    setCandidacyProcessInformation(actionForm, getProcess(request));
                    request.setAttribute("candidacyProcesses", candidacyProcesses);
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

    private List<SecondCycleCandidacyProcess> getCandidacyProcesses(final ExecutionInterval executionInterval) {
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

    private void setCandidacyProcessInformation(final ActionForm actionForm, final SecondCycleCandidacyProcess process) {
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
    protected List<IndividualCandidacyProcess> getChildProcesses(final CandidacyProcess process, HttpServletRequest request) {
        Collection<IndividualCandidacyProcess> processes = process.getChildProcessesSet();
        List<IndividualCandidacyProcess> selectedDegreesIndividualCandidacyProcesses =
                new ArrayList<IndividualCandidacyProcess>();
        Degree selectedDegree = getChooseDegreeBean(request).getDegree();

        for (IndividualCandidacyProcess child : processes) {
            if ((selectedDegree == null)
                    || ((SecondCycleIndividualCandidacyProcess) child).getSelectedDegrees().contains(selectedDegree)) {
                selectedDegreesIndividualCandidacyProcesses.add(child);
            }
        }

        return selectedDegreesIndividualCandidacyProcesses;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        final String degreeCurricularPlanOID = DegreeCoordinatorIndex.findDegreeCurricularPlanID(request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        if (degreeCurricularPlanOID != null) {
            return FenixFramework.getDomainObject(degreeCurricularPlanOID);
        }

        return null;
    }

    @Override
    protected Spreadsheet buildIndividualCandidacyReport(Spreadsheet spreadsheet,
            IndividualCandidacyProcess individualCandidacyProcess) {
        // TODO Auto-generated method stub
        return null;
    }
}
