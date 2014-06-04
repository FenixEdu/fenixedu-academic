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
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.over23;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.Over23CandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminCandidaciesApp;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
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

@StrutsFunctionality(app = AcademicAdminCandidaciesApp.class, path = "over-23", titleKey = "label.candidacy.over23",
        accessGroup = "(academic(MANAGE_CANDIDACY_PROCESSES) | academic(MANAGE_INDIVIDUAL_CANDIDACIES))",
        bundle = "ApplicationResources")
@Mapping(path = "/caseHandlingOver23CandidacyProcess", module = "academicAdministration",
        formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
        @Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
        @Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
        @Forward(name = "send-to-jury", path = "/candidacy/over23/sendToJury.jsp"),
        @Forward(name = "view-candidacy-results", path = "/candidacy/over23/viewCandidacyResults.jsp"),
        @Forward(name = "insert-candidacy-results", path = "/candidacy/over23/introduceCandidacyResults.jsp"),
        @Forward(name = "create-registrations", path = "/candidacy/createRegistrations.jsp"),
        @Forward(name = "prepare-select-available-degrees", path = "/candidacy/selectAvailableDegrees.jsp") })
public class Over23CandidacyProcessDA extends CandidacyProcessDA {

    @Override
    protected Class getProcessType() {
        return Over23CandidacyProcess.class;
    }

    @Override
    protected Class getChildProcessType() {
        return Over23IndividualCandidacyProcess.class;
    }

    @Override
    protected Class getCandidacyPeriodType() {
        return Over23CandidacyPeriod.class;
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        if (!hasExecutionInterval(request)) {
            final List<ExecutionInterval> executionIntervals =
                    ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType());
            if (executionIntervals.size() == 1) {
                setCandidacyProcessInformation(request, getCandidacyProcess(request, executionIntervals.iterator().next()));
            } else {
                request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
                request.setAttribute("executionIntervals", executionIntervals);
            }
        } else {
            setCandidacyProcessInformation(request, getCandidacyProcess(request, getExecutionInterval(request)));
        }
    }

    @Override
    protected ActionForward introForward(ActionMapping mapping) {
        return mapping.findForward("intro");
    }

    @Override
    protected Over23CandidacyProcess getCandidacyProcess(HttpServletRequest request, final ExecutionInterval executionInterval) {
        return executionInterval.hasOver23CandidacyPeriod() ? executionInterval.getOver23CandidacyPeriod()
                .getOver23CandidacyProcess() : null;
    }

    public ActionForward prepareExecuteSendInformationToJury(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("send-to-jury");
    }

    public ActionForward executeSendInformationToJury(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "SendInformationToJury", null);
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return prepareExecuteSendInformationToJury(mapping, actionForm, request, response);
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecutePrintCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + getReportFilename());

        final ServletOutputStream writer = response.getOutputStream();
        final Over23CandidacyProcess process = getProcess(request);
        final Spreadsheet spreadsheet = buildReport(process.getOver23IndividualCandidaciesThatCanBeSendToJury());

        spreadsheet.exportToXLSSheet(writer);
        writer.flush();
        response.flushBuffer();

        return null;
    }

    private Spreadsheet buildReport(final List<Over23IndividualCandidacyProcess> over23IndividualCandidacies) {
        final Spreadsheet result = new CandidacyReport("candidacies");

        result.setHeaders(new String[] { BundleUtil.getString(Bundle.APPLICATION, "label.name"),
                BundleUtil.getString(Bundle.APPLICATION, "label.identificationNumber"),
                BundleUtil.getString(Bundle.APPLICATION, "label.degrees") });

        for (final Over23IndividualCandidacyProcess candidacy : over23IndividualCandidacies) {
            if (!candidacy.canExecuteActivity(Authenticate.getUser())) {
                continue;
            }
            final Row row = result.addRow();
            row.setCell(candidacy.getPersonalDetails().getName());
            row.setCell(candidacy.getPersonalDetails().getDocumentIdNumber());

            int count = 1;
            String degrees = "";
            for (final Degree degree : candidacy.getSelectedDegreesSortedByOrder()) {
                degrees += count++ + " - " + degree.getNameFor(candidacy.getCandidacyExecutionInterval()) + "\n";
            }
            row.setCell(degrees);
        }

        return result;
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        setCandidacyProcessInformation(request, getProcess(request));
        super.listProcessAllowedActivities(mapping, form, request, response);

        return mapping.findForward("intro");
    }

    @Override
    protected Over23CandidacyProcess getProcess(HttpServletRequest request) {
        return (Over23CandidacyProcess) super.getProcess(request);
    }

    private class CandidacyXLSExporter extends SpreadsheetXLSExporter {

        @Override
        protected void exportXLSLine(final HSSFSheet sheet, final HSSFCellStyle cellStyle, final List<Object> cells,
                final int offset) {

            final HSSFRow row = sheet.createRow(sheet.getLastRowNum() + offset);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            int count = 0;
            for (final Object cellValue : cells) {
                if (++count == 3) {
                    cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                } else {
                    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                }
                addColumn(cellStyle, row, cellValue);
            }
        }
    }

    private class CandidacyReport extends Spreadsheet {

        public CandidacyReport(final String name) {
            super(name);
        }

        @Override
        public void exportToXLSSheet(final OutputStream outputStream) throws IOException {
            new CandidacyXLSExporter().exportToXLSSheet(this, outputStream);
        }
    }

    static public class Over23CandidacyDegreeBean extends CandidacyDegreeBean {
        public Over23CandidacyDegreeBean(final Over23IndividualCandidacyProcess process) {
            setPersonalDetails(process.getPersonalDetails());
            setDegree(process.getAcceptedDegree());
            setState(process.getCandidacyState());
            setRegistrationCreated(process.hasRegistrationForCandidacy());
        }
    }

    @Override
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(final HttpServletRequest request) {
        final Over23CandidacyProcess process = getProcess(request);
        final List<CandidacyDegreeBean> candidacyDegreeBeans = new ArrayList<CandidacyDegreeBean>();
        for (final Over23IndividualCandidacyProcess child : process.getAcceptedOver23IndividualCandidacies()) {
            if (child.canExecuteActivity(Authenticate.getUser())) {
                candidacyDegreeBeans.add(new Over23CandidacyDegreeBean(child));
            }
        }
        Collections.sort(candidacyDegreeBeans);
        return candidacyDegreeBeans;
    }

    @Override
    protected Spreadsheet buildIndividualCandidacyReport(Spreadsheet spreadsheet,
            IndividualCandidacyProcess individualCandidacyProcess) {
        return null;
    }

}
