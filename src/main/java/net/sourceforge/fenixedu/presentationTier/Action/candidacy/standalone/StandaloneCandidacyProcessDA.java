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
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.standalone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.StandaloneCandidacyPeriod;
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

@StrutsFunctionality(app = AcademicAdminCandidaciesApp.class, path = "standalone", titleKey = "label.candidacy.standalone",
        accessGroup = "(academic(MANAGE_CANDIDACY_PROCESSES) | academic(MANAGE_INDIVIDUAL_CANDIDACIES))",
        bundle = "ApplicationResources")
@Mapping(path = "/caseHandlingStandaloneCandidacyProcess", module = "academicAdministration",
        formBeanClass = StandaloneCandidacyProcessDA.StandaloneCandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/candidacy/standalone/mainCandidacyProcess.jsp"),
        @Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
        @Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
        @Forward(name = "send-to-coordinator", path = "/candidacy/sendToCoordinator.jsp"),
        @Forward(name = "view-candidacy-results", path = "/candidacy/standalone/viewCandidacyResults.jsp"),
        @Forward(name = "insert-candidacy-results", path = "/candidacy/standalone/introduceCandidacyResults.jsp"),
        @Forward(name = "create-registrations", path = "/candidacy/createRegistrations.jsp") })
public class StandaloneCandidacyProcessDA extends CandidacyProcessDA {

    static public class StandaloneCandidacyProcessForm extends CandidacyProcessForm {
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
        return StandaloneCandidacyProcess.class;
    }

    @Override
    protected Class getChildProcessType() {
        return StandaloneIndividualCandidacyProcess.class;
    }

    @Override
    protected Class getCandidacyPeriodType() {
        return StandaloneCandidacyPeriod.class;
    }

    @Override
    protected StandaloneCandidacyProcess getProcess(HttpServletRequest request) {
        return (StandaloneCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        if (!hasExecutionInterval(request)) {
            final List<ExecutionInterval> executionIntervals = getExecutionIntervalsWithCandidacyPeriod();

            if (executionIntervals.size() == 1) {
                final ExecutionInterval executionInterval = executionIntervals.iterator().next();
                final List<StandaloneCandidacyProcess> candidacyProcesses = getCandidacyProcesses(executionInterval);

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
            final StandaloneCandidacyProcess candidacyProcess = getCandidacyProcess(request, executionInterval);

            if (candidacyProcess != null) {
                setCandidacyProcessInformation(request, candidacyProcess);
                setCandidacyProcessInformation(actionForm, getProcess(request));
            } else {
                final List<StandaloneCandidacyProcess> candidacyProcesses = getCandidacyProcesses(executionInterval);

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

    private List<StandaloneCandidacyProcess> getCandidacyProcesses(final ExecutionInterval executionInterval) {
        final List<StandaloneCandidacyProcess> result = new ArrayList<StandaloneCandidacyProcess>();
        for (final StandaloneCandidacyPeriod period : executionInterval.getStandaloneCandidacyPeriods()) {
            result.add(period.getStandaloneCandidacyProcess());
        }
        return result;
    }

    private void setCandidacyProcessInformation(final ActionForm actionForm, final StandaloneCandidacyProcess process) {
        final StandaloneCandidacyProcessForm form = (StandaloneCandidacyProcessForm) actionForm;
        form.setSelectedProcessId(process.getExternalId());
        form.setExecutionIntervalId(process.getCandidacyExecutionInterval().getExternalId());
    }

    @Override
    protected StandaloneCandidacyProcess getCandidacyProcess(final HttpServletRequest request,
            final ExecutionInterval executionInterval) {
        final String selectedProcessId = getStringFromRequest(request, "selectedProcessId");
        if (selectedProcessId != null) {
            for (final StandaloneCandidacyPeriod candidacyPeriod : executionInterval.getStandaloneCandidacyPeriods()) {
                if (candidacyPeriod.getStandaloneCandidacyProcess().getExternalId().equals(selectedProcessId)) {
                    return candidacyPeriod.getStandaloneCandidacyProcess();
                }
            }
        }
        return null;
    }

    @Override
    protected ActionForward introForward(ActionMapping mapping) {
        return mapping.findForward("intro");
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        setCandidacyProcessInformation(request, getProcess(request));
        setCandidacyProcessInformation(form, getProcess(request));
        request.setAttribute("candidacyProcesses", getCandidacyProcesses(getProcess(request).getCandidacyExecutionInterval()));
        return introForward(mapping);
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyProcessBean", new CandidacyProcessBean(ExecutionSemester.readActualExecutionSemester()));
        return mapping.findForward("prepare-create-new-process");
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

    private void writeReport(final StandaloneCandidacyProcess process, final ServletOutputStream writer) throws IOException {
        final Spreadsheet spreadsheet = createSpreadSheet();
        for (final StandaloneIndividualCandidacyProcess candidacy : process
                .getSortedStandaloneIndividualCandidaciesThatCanBeSendToJury()) {
            if (candidacy.canExecuteActivity(Authenticate.getUser())) {
                addRow(spreadsheet, candidacy);
            }
        }
        spreadsheet.exportToXLSSheet(writer);
    }

    private void addRow(final Spreadsheet spreadsheet, final StandaloneIndividualCandidacyProcess candidacy) {
        final Row row = spreadsheet.addRow();
        row.setCell(candidacy.getPersonalDetails().getName());
        row.setCell(candidacy.getPersonalDetails().getDocumentIdNumber());

        final StringBuilder names = new StringBuilder();
        final Iterator<CurricularCourse> elements = candidacy.getCurricularCourses().iterator();
        while (elements.hasNext()) {
            names.append(elements.next().getName(candidacy.getCandidacyExecutionInterval()));
            names.append(elements.hasNext() ? ", " : "");
        }
        row.setCell(names.toString());
    }

    private Spreadsheet createSpreadSheet() {
        final Spreadsheet spreadsheet = new Spreadsheet("Candidacies");

        spreadsheet.setHeaders(new String[] {

        BundleUtil.getString(Bundle.APPLICATION, "label.name"),

        BundleUtil.getString(Bundle.APPLICATION, "label.identificationNumber"),

        BundleUtil.getString(Bundle.APPLICATION, "label.curricularCourses"),

        });
        return spreadsheet;
    }

    static public class StandaloneCandidacyDegreeBean extends CandidacyDegreeBean {

        public StandaloneCandidacyDegreeBean(final StandaloneIndividualCandidacyProcess process) {
            setPersonalDetails(process.getPersonalDetails());
            setDegree(process.getCandidacySelectedDegree());
            setState(process.getCandidacyState());
            setRegistrationCreated(process.hasRegistrationForCandidacy());
        }
    }

    @Override
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(HttpServletRequest request) {
        final StandaloneCandidacyProcess process = getProcess(request);
        final List<CandidacyDegreeBean> result = new ArrayList<CandidacyDegreeBean>();
        for (final StandaloneIndividualCandidacyProcess element : process.getAcceptedStandaloneIndividualCandidacies()) {
            result.add(new StandaloneCandidacyDegreeBean(element));
        }
        return result;
    }

    @Override
    protected Spreadsheet buildIndividualCandidacyReport(Spreadsheet spreadsheet,
            IndividualCandidacyProcess individualCandidacyProcess) {
        // TODO Auto-generated method stub
        return null;
    }
}
