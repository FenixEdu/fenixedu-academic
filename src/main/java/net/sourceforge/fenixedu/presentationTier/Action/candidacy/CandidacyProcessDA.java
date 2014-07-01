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
package net.sourceforge.fenixedu.presentationTier.Action.candidacy;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessSelectDegreesBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyPersonalDetails;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.casehandling.CaseHandlingDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.SpreadsheetXLSExporter;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

/**
 * INFO: when extending this class pay attention to the following aspects
 * 
 * <p>
 * Must configure the following forwards: intro (common value: /candidacy/mainCandidacyProcess.jsp), prepare-create-new-process
 * (common value: /candidacy/createCandidacyPeriod.jsp; used schemas: <process_name>Bean.manage), prepare-edit-candidacy-period
 * (common value: /candidacy/editCandidacyPeriod.jsp; used schemas: <process_name>Bean.manage)
 * 
 */

abstract public class CandidacyProcessDA extends CaseHandlingDispatchAction {

    abstract protected Class getChildProcessType();

    abstract protected Class getCandidacyPeriodType();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setExecutionInterval(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @EntryPoint
    public ActionForward intro(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        setStartInformation(actionForm, request, response);
        return introForward(mapping);
    }

    protected void setExecutionInterval(final HttpServletRequest request) {
        final String executionIntervalId = (String) getFromRequest(request, "executionIntervalId");
        if (executionIntervalId != null) {
            request.setAttribute("executionInterval", FenixFramework.getDomainObject(executionIntervalId));
        }
    }

    protected ExecutionInterval getExecutionInterval(final HttpServletRequest request) {
        return (ExecutionInterval) request.getAttribute("executionInterval");
    }

    protected boolean hasExecutionInterval(final HttpServletRequest request) {
        return getExecutionInterval(request) != null;
    }

    /**
     * Set information used to present main candidacy process page
     */
    abstract protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response);

    abstract protected ActionForward introForward(final ActionMapping mapping);

    @Override
    public ActionForward listProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return intro(mapping, form, request, response);
    }

    protected List<Activity> getAllowedActivities(final CandidacyProcess process) {
        List<Activity> activities = process.getAllowedActivities(Authenticate.getUser());
        ArrayList<Activity> resultActivities = new ArrayList<Activity>();

        for (Activity activity : activities) {
            if (activity.isVisibleForAdminOffice()) {
                resultActivities.add(activity);
            }
        }

        return resultActivities;
    }

    @Override
    protected CandidacyProcess getProcess(HttpServletRequest request) {
        return (CandidacyProcess) super.getProcess(request);
    }

    protected void setCandidacyProcessInformation(final HttpServletRequest request, final CandidacyProcess process) {
        if (process != null) {
            request.setAttribute("process", process);
            request.setAttribute("processActivities", getAllowedActivities(process));
            request.setAttribute("childProcesses", getChildProcesses(process, request));
            request.setAttribute("canCreateChildProcess", canCreateProcess(getChildProcessType().getName()));
            request.setAttribute("childProcessName", getChildProcessType().getSimpleName());
            request.setAttribute("executionIntervalId", process.getCandidacyExecutionInterval().getExternalId());
            request.setAttribute("individualCandidaciesHashCodesNotBounded", getIndividualCandidacyHashCodesNotBounded());
            request.setAttribute("hideCancelledCandidacies", getHideCancelledCandidaciesValue(request));
        }
        request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
        request.setAttribute("executionIntervals",
                ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType()));
    }

    public static class HideCancelledCandidaciesBean implements java.io.Serializable {
        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        private Boolean value = Boolean.FALSE;

        public HideCancelledCandidaciesBean(Boolean value) {
            this.value = value;
        }

        public HideCancelledCandidaciesBean() {
        }

        public Boolean getValue() {
            return this.value;
        }

        public Boolean isValue() {
            return this.value;
        }

        public void setValue(Boolean value) {
            this.value = value;
        }

        public Boolean getOptionValue() {
            return getValue();
        }

        public void setOptionValue(Boolean value) {
            setValue(value);
        }
    }

    protected HideCancelledCandidaciesBean getHideCancelledCandidaciesValue(HttpServletRequest request) {
        Object value = this.getObjectFromViewState("hide.cancelled.candidacies");
        if (value == null) {
            value =
                    getFromRequest(request, "hideCancelledCandidacies") != null ? Boolean.valueOf((String) getFromRequest(
                            request, "hideCancelledCandidacies")) : null;
        }
        return new HideCancelledCandidaciesBean(value != null ? (Boolean) value : Boolean.FALSE);
    }

    private static final Predicate<IndividualCandidacyProcess> IS_CANDIDACY_CANCELED_PREDICATE =
            new Predicate<IndividualCandidacyProcess>() {
                @Override
                public boolean apply(IndividualCandidacyProcess process) {
                    return !process.isCandidacyCancelled();
                }
            };

    protected static final Predicate<IndividualCandidacyProcess> CAN_EXECUTE_ACTIVITY_PREDICATE =
            new Predicate<IndividualCandidacyProcess>() {
                @Override
                public boolean apply(IndividualCandidacyProcess process) {
                    return process.canExecuteActivity(Authenticate.getUser());
                }
            };

    protected Predicate<IndividualCandidacyProcess> getChildProcessSelectionPredicate(final CandidacyProcess process,
            HttpServletRequest request) {
        return Predicates.alwaysTrue();
    }

    protected Collection<IndividualCandidacyProcess> getChildProcesses(final CandidacyProcess process, HttpServletRequest request) {
        HideCancelledCandidaciesBean hideCancelledCandidacies = getHideCancelledCandidaciesValue(request);

        Predicate<IndividualCandidacyProcess> predicate =
                Predicates.and(CAN_EXECUTE_ACTIVITY_PREDICATE, getChildProcessSelectionPredicate(process, request));

        if (hideCancelledCandidacies.getValue()) {
            predicate = Predicates.and(IS_CANDIDACY_CANCELED_PREDICATE, predicate);
        }

        return Collections2.filter(process.getChildProcesses(), predicate);

    }

    private List<PublicCandidacyHashCode> getIndividualCandidacyHashCodesNotBounded() {
        List<PublicCandidacyHashCode> publicCandidacyHashCodeList =
                new ArrayList<PublicCandidacyHashCode>(CollectionUtils.select(Bennu.getInstance().getCandidacyHashCodesSet(),
                        new org.apache.commons.collections.Predicate() {

                            @Override
                            public boolean evaluate(Object arg0) {
                                final PublicCandidacyHashCode hashCode = (PublicCandidacyHashCode) arg0;
                                return hashCode.isFromDegreeOffice() && !hashCode.hasCandidacyProcess();
                            }
                        }));

        return publicCandidacyHashCodeList;
    }

    abstract protected CandidacyProcess getCandidacyProcess(HttpServletRequest request, final ExecutionInterval executionInterval);

    static public class CandidacyProcessForm extends FenixActionForm {
        private String executionIntervalId;

        public String getExecutionIntervalId() {
            return executionIntervalId;
        }

        public void setExecutionIntervalId(String executionIntervalId) {
            this.executionIntervalId = executionIntervalId;
        }
    }

    /**
     * This bean is used to show summary about created registrations for
     * candidates
     */
    static public class CandidacyDegreeBean implements Serializable, Comparable<CandidacyDegreeBean> {

        private IndividualCandidacyPersonalDetails details;
        private Degree degree;
        private IndividualCandidacyState state;
        private boolean isRegistrationCreated;

        public IndividualCandidacyPersonalDetails getPersonalDetails() {
            return this.details;
        }

        public void setPersonalDetails(IndividualCandidacyPersonalDetails details) {
            this.details = details;
        }

        public Degree getDegree() {
            return this.degree;
        }

        public void setDegree(Degree degree) {
            this.degree = degree;
        }

        public IndividualCandidacyState getState() {
            return state;
        }

        public void setState(IndividualCandidacyState state) {
            this.state = state;
        }

        public boolean isRegistrationCreated() {
            return isRegistrationCreated;
        }

        public void setRegistrationCreated(boolean isRegistrationCreated) {
            this.isRegistrationCreated = isRegistrationCreated;
        }

        @Override
        public int compareTo(CandidacyDegreeBean other) {
            return IndividualCandidacyPersonalDetails.COMPARATOR_BY_NAME_AND_ID.compare(getPersonalDetails(),
                    other.getPersonalDetails());
        }
    }

    public static class ChooseMobilityProgramBean implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 8772267678901956140L;

        private MobilityProgram mobilityProgram;

        private CandidacyProcess candidacyProcess;

        public ChooseMobilityProgramBean() {

        }

        public ChooseMobilityProgramBean(final CandidacyProcess process) {
            this.candidacyProcess = process;
        }

        public MobilityProgram getMobilityProgram() {
            return this.mobilityProgram;
        }

        public void setMobilityProgram(MobilityProgram mobilityProgram) {
            this.mobilityProgram = mobilityProgram;
        }

        public CandidacyProcess getCandidacyProcess() {
            return candidacyProcess;
        }

        public void setCandidacyProcess(final CandidacyProcess process) {
            this.candidacyProcess = process;
        }
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyProcessBean", new CandidacyProcessBean(ExecutionYear.readCurrentExecutionYear()));
        return mapping.findForward("prepare-create-new-process");
    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            return super.createNewProcess(mapping, form, request, response);
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
            return mapping.findForward("prepare-create-new-process");
        }
    }

    public ActionForward createNewProcessInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
        return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward prepareExecuteEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final CandidacyProcess process = getProcess(request);
        request.setAttribute("candidacyProcessBean", new CandidacyProcessBean(process));
        return mapping.findForward("prepare-edit-candidacy-period");
    }

    public ActionForward executeEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {
            executeActivity(getProcess(request), "EditCandidacyPeriod", getRenderedObject("candidacyProcessBean"));
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
            return mapping.findForward("prepare-edit-candidacy-period");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward executeEditCandidacyPeriodInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
        return mapping.findForward("prepare-edit-candidacy-period");
    }

    protected String getReportFilename() {
        return BundleUtil.getString(Bundle.APPLICATION, "label.candidacy." + getProcessType().getSimpleName()
                + ".report.filename")
                + "_" + new LocalDate().toString("ddMMyyyy") + ".xls";
    }

    /**
     * Create list of CandidacyDegreeBeans with information related to accepted
     * candidacies
     */
    abstract protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(final HttpServletRequest request);

    public ActionForward prepareExecuteExportCandidacies(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + getReportFilename());

        final ServletOutputStream writer = response.getOutputStream();
        writeCandidaciesReport(request, getProcess(request), writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    private void writeCandidaciesReport(HttpServletRequest request, final CandidacyProcess process,
            final ServletOutputStream writer) throws IOException {
        final Spreadsheet spreadsheet =
                new Spreadsheet(BundleUtil.getString(Bundle.CANDIDATE, "title.candidacies"), getCandidacyHeader());
        HideCancelledCandidaciesBean hideCancelledCandidacies = getHideCancelledCandidaciesValue(request);

        for (final IndividualCandidacyProcess individualProcess : process.getChildProcesses()) {
            if (hideCancelledCandidacies.getValue() && individualProcess.isCandidacyCancelled()) {
                continue;
            }
            if (individualProcess.canExecuteActivity(Authenticate.getUser())) {
                buildIndividualCandidacyReport(spreadsheet, individualProcess);
            }
        }
        new SpreadsheetXLSExporter().exportToXLSSheet(spreadsheet, writer);
    }

    protected final String MESSAGE_YES = "message.yes";
    protected final String MESSAGE_NO = "message.no";
    protected static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy/MM/dd");

    protected abstract Spreadsheet buildIndividualCandidacyReport(final Spreadsheet spreadsheet,
            final IndividualCandidacyProcess individualCandidacyProcess);

    protected List<Object> getCandidacyHeader() {
        final List<Object> result = new ArrayList<Object>();

        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.processCode"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.name"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.identificationType"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.identificationNumber"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.nationality"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.precedent.institution"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.precedent.degree.designation"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.precedent.degree.conclusion.date"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.precedent.degree.conclusion.grade"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.selected.degree"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.state"));
        result.add(BundleUtil.getString(Bundle.CANDIDATE, "label.spreadsheet.verified"));

        return result;
    }

    public static class ChooseDegreeBean implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = -7544460988553206034L;

        private Degree degree;

        private CandidacyProcess candidacyProcess;

        public ChooseDegreeBean() {

        }

        public ChooseDegreeBean(final CandidacyProcess process) {
            this.candidacyProcess = process;
        }

        public Degree getDegree() {
            return this.degree;
        }

        public void setDegree(Degree degree) {
            this.degree = degree;
        }

        public CandidacyProcess getCandidacyProcess() {
            return candidacyProcess;
        }

        public void setCandidacyProcess(final CandidacyProcess process) {
            this.candidacyProcess = process;
        }
    }

    public ActionForward prepareExecuteSelectAvailableDegrees(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final CandidacyProcess process = getProcess(request);
        final CandidacyProcessSelectDegreesBean bean = new CandidacyProcessSelectDegreesBean();
        bean.getDegrees().addAll(process.getDegreeSet());
        request.setAttribute("candidacyProcessBean", bean);
        return mapping.findForward("prepare-select-available-degrees");
    }

    public ActionForward executeSelectAvailableDegrees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeActivity(getProcess(request), "SelectAvailableDegrees", getRenderedObject("candidacyProcessBean"));
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("candidacyProcessBean", getRenderedObject("candidacyProcessBean"));
            return mapping.findForward("prepare-select-available-degrees");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

}
