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
package org.fenixedu.academic.ui.struts.action.candidacy.degreeTransfer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.Region;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.degreeTransfer.DegreeTransferCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyProcess;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.period.DegreeTransferCandidacyPeriod;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminCandidaciesApp;
import org.fenixedu.academic.ui.struts.action.candidacy.CandidacyProcessDA;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.fenixedu.commons.spreadsheet.StyledExcelSpreadsheet;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

@StrutsFunctionality(app = AcademicAdminCandidaciesApp.class, path = "degree-transfer",
        titleKey = "label.candidacy.degreeTransfer",
        accessGroup = "(academic(MANAGE_CANDIDACY_PROCESSES) | academic(MANAGE_INDIVIDUAL_CANDIDACIES))",
        bundle = "ApplicationResources")
@Mapping(path = "/caseHandlingDegreeTransferCandidacyProcess", module = "academicAdministration",
        formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
        @Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
        @Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
        @Forward(name = "send-to-coordinator", path = "/candidacy/sendToCoordinator.jsp"),
        @Forward(name = "send-to-scientificCouncil", path = "/candidacy/sendToScientificCouncil.jsp"),
        @Forward(name = "view-candidacy-results", path = "/candidacy/degreeTransfer/viewCandidacyResults.jsp"),
        @Forward(name = "introduce-candidacy-results", path = "/candidacy/degreeTransfer/introduceCandidacyResults.jsp"),
        @Forward(name = "create-registrations", path = "/candidacy/createRegistrations.jsp"),
        @Forward(name = "prepare-select-available-degrees", path = "/candidacy/selectAvailableDegrees.jsp") })
public class DegreeTransferCandidacyProcessDA extends CandidacyProcessDA {

    private static final int MAX_GRADE_VALUE = 20;

    @Override
    protected Class getProcessType() {
        return DegreeTransferCandidacyProcess.class;
    }

    @Override
    protected Class getChildProcessType() {
        return DegreeTransferIndividualCandidacyProcess.class;
    }

    @Override
    protected Class getCandidacyPeriodType() {
        return DegreeTransferCandidacyPeriod.class;
    }

    @Override
    protected CandidacyProcess getCandidacyProcess(HttpServletRequest request, final ExecutionInterval executionInterval) {
        return executionInterval.hasDegreeTransferCandidacyPeriod() ? executionInterval.getDegreeTransferCandidacyPeriod()
                .getDegreeTransferCandidacyProcess() : null;
    }

    @Override
    protected DegreeTransferCandidacyProcess getProcess(HttpServletRequest request) {
        return (DegreeTransferCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected ActionForward introForward(ActionMapping mapping) {
        return mapping.findForward("intro");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setChooseDegreeBean(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        setCandidacyProcessInformation(request, getProcess(request));
        setCandidacyProcessInformation(form, getProcess(request));
        return introForward(mapping);
    }

    protected void setCandidacyProcessInformation(final ActionForm actionForm, final CandidacyProcess process) {
        final CandidacyProcessForm form = (CandidacyProcessForm) actionForm;
        form.setExecutionIntervalId(process.getCandidacyExecutionInterval().getExternalId());
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

    public ActionForward prepareExecutePrintCandidaciesFromInstitutionDegrees(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename="
                + getLabel("label.candidacy.degreeTransfer.institution.report.filename") + ".xls");
        writeReportForInstitutionDegrees(getProcess(request), response.getOutputStream());
        response.getOutputStream().flush();
        response.flushBuffer();
        return null;
    }

    private void writeReportForInstitutionDegrees(DegreeTransferCandidacyProcess process, ServletOutputStream outputStream)
            throws IOException {
        final StyledExcelSpreadsheet excelSpreadsheet = new StyledExcelSpreadsheet();
        for (final Entry<Degree, SortedSet<DegreeTransferIndividualCandidacyProcess>> entry : process
                .getValidInstitutionIndividualCandidacyProcessesByDegree().entrySet()) {
            createSpreadsheet(excelSpreadsheet, entry.getKey(), entry.getValue());
        }
        excelSpreadsheet.getWorkbook().write(outputStream);

    }

    public ActionForward prepareExecutePrintCandidaciesFromExternalDegrees(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename="
                + getLabel("label.candidacy.degreeTransfer.external.report.filename") + ".xls");
        writeReportForExternalDegrees(getProcess(request), response.getOutputStream());
        response.getOutputStream().flush();
        response.flushBuffer();
        return null;
    }

    private void writeReportForExternalDegrees(DegreeTransferCandidacyProcess process, ServletOutputStream outputStream)
            throws IOException {
        final StyledExcelSpreadsheet excelSpreadsheet = new StyledExcelSpreadsheet();
        for (final Entry<Degree, SortedSet<DegreeTransferIndividualCandidacyProcess>> entry : process
                .getValidExternalIndividualCandidacyProcessesByDegree().entrySet()) {
            createSpreadsheet(excelSpreadsheet, entry.getKey(), entry.getValue());
        }
        excelSpreadsheet.getWorkbook().write(outputStream);
    }

    private String getLabel(final String key) {
        return BundleUtil.getString(Bundle.APPLICATION, key);
    }

    private void createSpreadsheet(final StyledExcelSpreadsheet excelSpreadsheet, final Degree degree,
            final SortedSet<DegreeTransferIndividualCandidacyProcess> candidacies) {
        excelSpreadsheet.getSheet(degree.getSigla());
        createHeader(excelSpreadsheet, degree);
        createBody(excelSpreadsheet, candidacies);
    }

    private void createHeader(final StyledExcelSpreadsheet spreadsheet, final Degree degree) {
        // title
        spreadsheet.newHeaderRow();
        spreadsheet.addCell(degree.getName(), spreadsheet.getExcelStyle().getTitleStyle());

        // empty row
        spreadsheet.newHeaderRow();

        // table header
        spreadsheet.newHeaderRow();
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.identification"));
        spreadsheet.addHeader(2, BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.degree.and.school"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.affinity"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.degreeNature"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.concludedUCs"));
        spreadsheet.addHeader(8, "");
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.approvedEctsRate"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.gradeRate"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.degreeTransfer.seriesCandidacyGrade"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.result"));

        spreadsheet.newHeaderRow();
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.number"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.name"));
        spreadsheet.addHeader(5, BundleUtil.getString(Bundle.APPLICATION, "label.number"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.gradeSum.abbr"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.approvedEcts"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.APPLICATION, "label.candidacy.enroledEcts"));

        // Id + Nº + Nome merge
        spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 0, 2, (short) 1));
        // Degree name merge
        spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 2, 3, (short) 2));
        // affinity merge
        spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 3, 3, (short) 3));
        // degreeNature merge
        spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 4, 3, (short) 4));
        // UCs merge
        spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 5, 2, (short) 7));
        // A merge
        spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 9, 3, (short) 9));
        // B merge
        spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 10, 3, (short) 10));
        // C merge
        spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 11, 3, (short) 11));
        // result merge
        spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 12, 3, (short) 12));
    }

    private void createBody(final StyledExcelSpreadsheet excelSpreadsheet,
            final SortedSet<DegreeTransferIndividualCandidacyProcess> candidacies) {
        for (final DegreeTransferIndividualCandidacyProcess process : candidacies) {
            if (!process.canExecuteActivity(Authenticate.getUser())) {
                continue;
            }
            excelSpreadsheet.newRow();
            if (process.hasCandidacyStudent()) {
                excelSpreadsheet.addCell(process.getCandidacyStudent().getNumber());
            } else {
                excelSpreadsheet.addCell("-");
            }
            excelSpreadsheet.addCell(process.getPersonalDetails().getName());
            final PrecedentDegreeInformation information = process.getPrecedentDegreeInformation();
            excelSpreadsheet.addCell(information.getDegreeAndInstitutionName());
            excelSpreadsheet.addCell(getValue(process.getCandidacyAffinity()));
            excelSpreadsheet.addCell(getValue(process.getCandidacyDegreeNature()));
            excelSpreadsheet.addCell(getValue(information.getNumberOfApprovedCurricularCourses()));
            excelSpreadsheet.addCell(getValue(information.getGradeSum()));
            excelSpreadsheet.addCell(getValue(information.getApprovedEcts()));
            excelSpreadsheet.addCell(getValue(information.getEnroledEcts()));
            excelSpreadsheet.addCell(getValue(calculateA(process, true)));
            excelSpreadsheet.addCell(getValue(calculateB(process, true)));
            excelSpreadsheet.addCell(getValue(calculateC(process)));
            if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
                excelSpreadsheet.addCell(BundleUtil.getString(Bundle.ENUMERATION, process.getCandidacyState().getQualifiedName())
                        .toUpperCase());
            } else {
                excelSpreadsheet.addCell("");
            }
        }
    }

    private String getValue(final Object value) {
        return value != null ? value.toString() : "";
    }

    private String getValue(final BigDecimal value) {
        return value != null ? value.toPlainString() : "";
    }

    private BigDecimal calculateA(final DegreeTransferIndividualCandidacyProcess process, final boolean setScale) {
        if (process.getCandidacyApprovedEctsRate() != null) {
            return process.getCandidacyApprovedEctsRate();
        }

        final BigDecimal approvedEcts = process.getPrecedentDegreeInformation().getApprovedEcts();
        final BigDecimal enroledEcts = process.getPrecedentDegreeInformation().getEnroledEcts();
        if (approvedEcts != null && enroledEcts != null && enroledEcts.signum() > 0) {
            final BigDecimal result = approvedEcts.divide(enroledEcts, MathContext.DECIMAL32);
            return setScale ? result.setScale(2, RoundingMode.HALF_EVEN) : result;
        }
        return null;
    }

    private BigDecimal calculateB(final DegreeTransferIndividualCandidacyProcess process, final boolean setScale) {
        if (process.getCandidacyGradeRate() != null) {
            return process.getCandidacyGradeRate();
        }

        final Integer total = process.getPrecedentDegreeInformation().getNumberOfApprovedCurricularCourses();
        final BigDecimal gradeSum = process.getPrecedentDegreeInformation().getGradeSum();
        if (gradeSum != null && total != null && total.intValue() != 0) {
            final BigDecimal result = gradeSum.divide(new BigDecimal(total.intValue() * MAX_GRADE_VALUE), MathContext.DECIMAL32);
            return setScale ? result.setScale(2, RoundingMode.HALF_EVEN) : result;
        }
        return null;
    }

    private BigDecimal calculateC(final DegreeTransferIndividualCandidacyProcess process) {
        if (process.getCandidacySeriesCandidacyGrade() != null) {
            return process.getCandidacySeriesCandidacyGrade().setScale(2, RoundingMode.HALF_EVEN);
        }

        final BigDecimal affinity = process.getCandidacyAffinity();
        final Integer nature = process.getCandidacyDegreeNature();
        final BigDecimal valueA = calculateA(process, false);
        final BigDecimal valueB = calculateB(process, false);
        if (valueA != null && valueB != null && affinity != null && nature != null) {
            final BigDecimal value03 = new BigDecimal("0.3");
            final BigDecimal aff = new BigDecimal(affinity.toString()).multiply(new BigDecimal("0.4"), MathContext.DECIMAL32);
            final BigDecimal nat = new BigDecimal(nature).multiply(value03).divide(new BigDecimal(5), MathContext.DECIMAL32);
            final BigDecimal abp = valueA.add(valueB).multiply(value03).divide(new BigDecimal(2), MathContext.DECIMAL32);
            return aff.add(nat).add(abp).multiply(new BigDecimal(200)).setScale(2, RoundingMode.HALF_EVEN);
        }
        return null;
    }

    static public class DegreeTransferCandidacyDegreeBean extends CandidacyDegreeBean {
        DegreeTransferCandidacyDegreeBean(final DegreeTransferIndividualCandidacyProcess process) {
            setPersonalDetails(process.getPersonalDetails());
            setDegree(process.getCandidacySelectedDegree());
            setState(process.getCandidacyState());
            setRegistrationCreated(process.hasRegistrationForCandidacy());
        }
    }

    @Override
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(HttpServletRequest request) {
        final List<CandidacyDegreeBean> result = new ArrayList<CandidacyDegreeBean>();
        for (final DegreeTransferIndividualCandidacyProcess child : getProcess(request)
                .getAcceptedDegreeTransferIndividualCandidacyProcesses()) {
            result.add(new DegreeTransferCandidacyDegreeBean(child));
        }
        return result;
    }

    @Override
    protected List<Object> getCandidacyHeader() {
        final List<Object> result = new ArrayList<Object>();

        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.processCode"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.name"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.identificationType"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.identificationNumber"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.nationality"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.precedent.institution"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.actual.degree.designation"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.selected.degree"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.state"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.verified"));

        return result;
    }

    @Override
    protected Spreadsheet buildIndividualCandidacyReport(final Spreadsheet spreadsheet,
            final IndividualCandidacyProcess individualCandidacyProcess) {
        DegreeTransferIndividualCandidacyProcess degreeTransferIndividualCandidacyProcess =
                (DegreeTransferIndividualCandidacyProcess) individualCandidacyProcess;

        final Row row = spreadsheet.addRow();
        row.setCell(degreeTransferIndividualCandidacyProcess.getProcessCode());
        row.setCell(degreeTransferIndividualCandidacyProcess.getPersonalDetails().getName());
        row.setCell(degreeTransferIndividualCandidacyProcess.getPersonalDetails().getIdDocumentType().getLocalizedName());
        row.setCell(degreeTransferIndividualCandidacyProcess.getPersonalDetails().getDocumentIdNumber());
        row.setCell(degreeTransferIndividualCandidacyProcess.getPersonalDetails().getCountry().getCountryNationality()
                .getContent());
        row.setCell(degreeTransferIndividualCandidacyProcess.getPrecedentDegreeInformation().getPrecedentInstitution().getName());
        row.setCell(degreeTransferIndividualCandidacyProcess.getPrecedentDegreeInformation().getPrecedentDegreeDesignation());
        row.setCell(degreeTransferIndividualCandidacyProcess.getCandidacy().getSelectedDegree().getName());
        row.setCell(BundleUtil.getString(Bundle.ENUMERATION, individualCandidacyProcess.getCandidacyState().getQualifiedName()));
        row.setCell(BundleUtil.getString(Bundle.CANDIDATE, degreeTransferIndividualCandidacyProcess.getProcessChecked() != null
                && degreeTransferIndividualCandidacyProcess.getProcessChecked() ? MESSAGE_YES : MESSAGE_NO));
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
                    return ((DegreeTransferIndividualCandidacyProcess) process).getCandidacy().getSelectedDegree() == selectedDegree;
                }
            };
        }
    }

}
