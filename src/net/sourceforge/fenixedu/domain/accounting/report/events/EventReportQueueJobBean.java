package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.joda.time.LocalDate;

public class EventReportQueueJobBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean exportGratuityEvents;
    private Boolean exportAcademicServiceRequestEvents;
    private Boolean exportIndividualCandidacyEvents;
    private Boolean exportPhdEvents;
    private Boolean exportResidenceEvents;
    private Boolean exportOthers;
    private Boolean forDegreeAdministrativeOffice;
    private Boolean forMasterDegreeAdministrativeOffice;
    private LocalDate beginDate;
    private LocalDate endDate;

    private ExecutionYear executionYear;

    public EventReportQueueJobBean() {

    }

    public Boolean getExportGratuityEvents() {
	return exportGratuityEvents;
    }

    public void setExportGratuityEvents(Boolean exportGratuityEvents) {
	this.exportGratuityEvents = exportGratuityEvents;
    }

    public Boolean getExportAcademicServiceRequestEvents() {
	return exportAcademicServiceRequestEvents;
    }

    public void setExportAcademicServiceRequestEvents(Boolean exportAcademicServiceRequestEvents) {
	this.exportAcademicServiceRequestEvents = exportAcademicServiceRequestEvents;
    }

    public Boolean getExportIndividualCandidacyEvents() {
	return exportIndividualCandidacyEvents;
    }

    public void setExportIndividualCandidacyEvents(Boolean exportIndividualCandidacyEvents) {
	this.exportIndividualCandidacyEvents = exportIndividualCandidacyEvents;
    }

    public Boolean getExportPhdEvents() {
	return exportPhdEvents;
    }

    public void setExportPhdEvents(Boolean phdEvents) {
	this.exportPhdEvents = phdEvents;
    }

    public Boolean getExportResidenceEvents() {
	return exportResidenceEvents;
    }

    public void setExportResidenceEvents(Boolean exportResidenceEvents) {
	this.exportResidenceEvents = exportResidenceEvents;
    }

    public Boolean getExportOthers() {
	return exportOthers;
    }

    public void setExportOthers(Boolean others) {
	this.exportOthers = others;
    }

    public Boolean getForDegreeAdministrativeOffice() {
	return forDegreeAdministrativeOffice;
    }

    public void setForDegreeAdministrativeOffice(Boolean forDegreeAdministrativeOffice) {
	this.forDegreeAdministrativeOffice = forDegreeAdministrativeOffice;
    }

    public Boolean getForMasterDegreeAdministrativeOffice() {
	return forMasterDegreeAdministrativeOffice;
    }

    public void setForMasterDegreeAdministrativeOffice(Boolean forMasterDegreeAdministrativeOffice) {
	this.forMasterDegreeAdministrativeOffice = forMasterDegreeAdministrativeOffice;
    }

    public LocalDate getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
	this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }

    public ExecutionYear getExecutionYear() {
	return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = executionYear;
    }

    public static EventReportQueueJobBean createBeanForManager() {
	EventReportQueueJobBean bean = new EventReportQueueJobBean();
	bean.setForDegreeAdministrativeOffice(true);
	bean.setForMasterDegreeAdministrativeOffice(true);

	bean.setExportGratuityEvents(true);
	bean.setExportAcademicServiceRequestEvents(true);
	bean.setExportIndividualCandidacyEvents(true);
	bean.setExportPhdEvents(true);
	bean.setExportResidenceEvents(true);
	bean.setExportOthers(true);

	return bean;
    }

    public static EventReportQueueJobBean createBeanForDegreeAdministrativeOffice() {
	EventReportQueueJobBean bean = new EventReportQueueJobBean();
	bean.setForDegreeAdministrativeOffice(true);
	bean.setForMasterDegreeAdministrativeOffice(false);

	bean.setExportGratuityEvents(true);
	bean.setExportAcademicServiceRequestEvents(true);
	bean.setExportIndividualCandidacyEvents(true);
	bean.setExportPhdEvents(false);
	bean.setExportResidenceEvents(false);
	bean.setExportOthers(true);

	return bean;
    }

    public static EventReportQueueJobBean createBeanForMasterDegreeAdministrativeOffcie() {
	EventReportQueueJobBean bean = new EventReportQueueJobBean();
	bean.setForDegreeAdministrativeOffice(false);
	bean.setForMasterDegreeAdministrativeOffice(true);

	bean.setExportGratuityEvents(true);
	bean.setExportAcademicServiceRequestEvents(true);
	bean.setExportIndividualCandidacyEvents(true);
	bean.setExportPhdEvents(true);
	bean.setExportResidenceEvents(false);
	bean.setExportOthers(true);

	return bean;
    }

}
