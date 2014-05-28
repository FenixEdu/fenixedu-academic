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
package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.io.Serializable;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;

public class EventReportQueueJobBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean exportGratuityEvents;
    private Boolean exportAcademicServiceRequestEvents;
    private Boolean exportIndividualCandidacyEvents;
    private Boolean exportPhdEvents;
    private Boolean exportResidenceEvents;
    private Boolean exportOthers;
    private LocalDate beginDate;
    private LocalDate endDate;

    private AdministrativeOffice administrativeOffice;

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

    public AdministrativeOffice getAdministrativeOffice() {
        return administrativeOffice;
    }

    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
        this.administrativeOffice = administrativeOffice;
        if (administrativeOffice != null) {
            this.exportPhdEvents = administrativeOffice.getHasAnyPhdProgram();
        }
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public Set<AdministrativeOffice> getAvailableOffices() {
        return AcademicAuthorizationGroup.getOfficesForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_EVENT_REPORTS);
    }

    public Set<AdministrativeOffice> getAvailableOfficesForManager() {
        return Bennu.getInstance().getAdministrativeOfficesSet();
    }

    public static EventReportQueueJobBean createBeanForManager() {
        EventReportQueueJobBean bean = new EventReportQueueJobBean();

        bean.setExportGratuityEvents(true);
        bean.setExportAcademicServiceRequestEvents(true);
        bean.setExportIndividualCandidacyEvents(true);
        bean.setExportPhdEvents(true);
        bean.setExportResidenceEvents(true);
        bean.setExportOthers(true);

        return bean;
    }

    public static EventReportQueueJobBean createBeanForAdministrativeOffice() {
        EventReportQueueJobBean bean = new EventReportQueueJobBean();

        bean.setExportGratuityEvents(true);
        bean.setExportAcademicServiceRequestEvents(true);
        bean.setExportIndividualCandidacyEvents(true);
        bean.setExportPhdEvents(true);
        bean.setExportResidenceEvents(false);
        bean.setExportOthers(true);

        return bean;
    }

}
