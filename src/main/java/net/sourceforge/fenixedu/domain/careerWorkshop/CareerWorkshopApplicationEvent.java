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
package net.sourceforge.fenixedu.domain.careerWorkshop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

public class CareerWorkshopApplicationEvent extends CareerWorkshopApplicationEvent_Base {

    public CareerWorkshopApplicationEvent(DateTime beginDate, DateTime endDate, String relatedInformation) {
        super();
        evaluateDatesConsistency(beginDate, endDate);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setRelatedInformation(relatedInformation);
        setRootDomainObject(Bennu.getInstance());
    }

    public void evaluateDatesConsistency(DateTime beginDate, DateTime endDate) {
        if (beginDate == null || endDate == null) {
            throw new DomainException("error.careerWorkshop.creatingNewEvent: Invalid values for begin/end dates");
        }
        if (!beginDate.isBefore(endDate)) {
            throw new DomainException("error.careerWorkshop.creatingNewEvent: Inconsistent values for begin/end dates");
        }
        if (isOverlapping(beginDate, endDate)) {
            throw new DomainException("error.careerWorkshop.creatingNewEvent: New period overlaps existing period");
        }
    }

    public void delete() {
        if (!getCareerWorkshopApplications().isEmpty()) {
            throw new DomainException(
                    "error.careerWorkshop.deletingEvent: This event already have applications associated, therefore it cannot be destroyed.");
        }
        if (getCareerWorkshopConfirmationEvent() != null) {
            throw new DomainException("error.careerWorkshop.deletingEvent: A confirmation period is already defined.");
        }
        setRootDomainObject(null);
        setSpreadsheet(null);
        deleteDomainObject();
    }

    public CareerWorkshopSpreadsheet getApplications() {
        if (hasBennu()) {
            if (getLastUpdate() == null || getSpreadsheet() == null) {
                generateSpreadsheet();
            }
            if (getLastUpdate().plusDays(1).isAfter(getSpreadsheet().getUploadTime())) {
                generateSpreadsheet();
            }
        }
        return getSpreadsheet();
    }

    @Atomic
    public void generateSpreadsheet() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ISTCareerWorkshopsApplications-");
        DateTime stamp = (getLastUpdate() != null ? getLastUpdate() : new DateTime());
        stringBuilder.append(stamp.toString("ddMMyyyyhhmm"));
        stringBuilder.append(".csv");

        final SheetData<CareerWorkshopApplication> dataSheet = new SheetData<CareerWorkshopApplication>(getProcessedList()) {

            @Override
            protected void makeLine(CareerWorkshopApplication item) {
                DateTime timestamp = item.getSealStamp();

                Registration reg = null;
                for (Registration regIter : item.getStudent().getActiveRegistrationsIn(
                        ExecutionSemester.readActualExecutionSemester())) {
                    if (regIter.getStudentCurricularPlan(ExecutionSemester.readActualExecutionSemester()).isSecondCycle()) {
                        reg = regIter;
                        break;
                    }
                }
                if (reg == null) {
                    reg = item.getStudent().getLastRegistration();
                }

                Integer registrationLength = 0;
                ExecutionYear bottom = reg.getIngressionYear().getPreviousExecutionYear();
                ExecutionYear yearIter = ExecutionYear.readCurrentExecutionYear();
                while (yearIter != bottom) {
                    if (reg.hasAnyActiveState(yearIter)) {
                        registrationLength++;
                    }
                    yearIter = yearIter.getPreviousExecutionYear();
                }
                int[] sessionPreferences = item.getSessionPreferences();
                CareerWorkshopSessions[] sessionsList = CareerWorkshopSessions.values();
                int[] themePreferences = item.getThemePreferences();
                CareerWorkshopThemes[] themesList = CareerWorkshopThemes.values();

                addCell("Data de inscrição", timestamp.toString("dd-MM-yyyy"));
                addCell("Hora de inscrição", timestamp.toString("HH:mm"));
                addCell("Número aluno", item.getStudent().getNumber());
                addCell("Nome", item.getStudent().getName());
                addCell("Email", item.getStudent().getPerson().getDefaultEmailAddressValue());
                addCell("Curso", reg.getDegree().getSigla());
                if (reg.getCurriculum(ExecutionYear.readCurrentExecutionYear(), CycleType.SECOND_CYCLE)
                        .getStudentCurricularPlan() != null) {
                    addCell("Ano Curricular", reg.getCurriculum(ExecutionYear.readCurrentExecutionYear(), CycleType.SECOND_CYCLE)
                            .getCurricularYear());
                } else {
                    addCell("Ano Curricular", "--");
                }
                addCell("Número de inscrições", registrationLength);
                for (int i = 0; i < sessionPreferences.length; i++) {
                    addCell(sessionsList[i].getDescription(), sessionPreferences[i] + 1);
                }
                for (int i = 0; i < themePreferences.length; i++) {
                    addCell(themesList[i].getDescription(), themePreferences[i] + 1);
                }

            }

        };

        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            new SpreadsheetBuilder().addSheet(stringBuilder.toString(), dataSheet).build(WorkbookExportFormat.CSV, io);

            setSpreadsheet(new CareerWorkshopSpreadsheet(stringBuilder.toString(), io.toByteArray()));
            setLastUpdate(new DateTime());
        } catch (IOException ioe) {
            throw new DomainException("error.careerWorkshop.criticalFailureGeneratingTheSpreadsheetFile", ioe);
        }
    }

    public String getFormattedBeginDate() {
        return getBeginDate().toString("dd-MM-yyyy");
    }

    public String getFormattedEndDate() {
        return getEndDate().toString("dd-MM-yyyy");
    }

    public Boolean getIsConfirmationPeriodAttached() {
        return (getCareerWorkshopConfirmationEvent() != null ? true : false);
    }

    public String getConfirmationBeginDate() {
        if (getCareerWorkshopConfirmationEvent() == null) {
            return "--";
        }
        return getCareerWorkshopConfirmationEvent().getFormattedBeginDate();
    }

    public String getConfirmationEndDate() {
        if (getCareerWorkshopConfirmationEvent() == null) {
            return "--";
        }
        return getCareerWorkshopConfirmationEvent().getFormattedEndDate();
    }

    public int getNumberOfApplications() {
        int result = 0;
        for (CareerWorkshopApplication application : getCareerWorkshopApplications()) {
            if (application.getSealStamp() != null) {
                result++;
            }
        }
        return result;
    }

    public int getNumberOfConfirmations() {
        if (getCareerWorkshopConfirmationEvent() == null) {
            return 0;
        }
        return getCareerWorkshopConfirmationEvent().getNumberOfConfirmations();
    }

    private boolean isOverlapping(DateTime beginDate, DateTime endDate) {
        for (CareerWorkshopApplicationEvent each : Bennu.getInstance().getCareerWorkshopApplicationEventsSet()) {
            if ((!beginDate.isBefore(each.getBeginDate()) && !beginDate.isAfter(each.getEndDate()))
                    || (!endDate.isBefore(each.getBeginDate()) && !endDate.isAfter(each.getEndDate()))) {
                return true;
            }
        }
        return false;
    }

    private List<CareerWorkshopApplication> getProcessedList() {
        List<CareerWorkshopApplication> processedApplications = new ArrayList<CareerWorkshopApplication>();
        for (CareerWorkshopApplication application : getCareerWorkshopApplications()) {
            if (application.getSealStamp() != null) {
                processedApplications.add(application);
            }
        }
        Collections.sort(processedApplications, new Comparator<CareerWorkshopApplication>() {

            @Override
            public int compare(CareerWorkshopApplication o1, CareerWorkshopApplication o2) {
                if (o1.getSealStamp().isBefore(o2.getSealStamp())) {
                    return -1;
                }
                if (o1.getSealStamp().isAfter(o2.getSealStamp())) {
                    return 1;
                }
                return 0;
            }
        });
        return processedApplications;
    }

    public boolean isApplicationEventOpened() {
        DateTime today = new DateTime();
        return (today.isBefore(getBeginDate()) || today.isAfter(getEndDate())) ? false : true;
    }

    public boolean isConfirmationPeriodOpened() {
        DateTime today = new DateTime();
        if (getCareerWorkshopConfirmationEvent() == null) {
            return false;
        }
        CareerWorkshopConfirmationEvent confirmation = getCareerWorkshopConfirmationEvent();
        if (confirmation.getBeginDate() == null || confirmation.getEndDate() == null) {
            return false;
        }
        return (today.isBefore(confirmation.getBeginDate()) || today.isAfter(confirmation.getEndDate())) ? false : true;
    }

    static public CareerWorkshopApplicationEvent getActualEvent() {
        for (CareerWorkshopApplicationEvent each : Bennu.getInstance().getCareerWorkshopApplicationEventsSet()) {
            if (each.isApplicationEventOpened()) {
                return each;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplication> getCareerWorkshopApplications() {
        return getCareerWorkshopApplicationsSet();
    }

    @Deprecated
    public boolean hasAnyCareerWorkshopApplications() {
        return !getCareerWorkshopApplicationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndDate() {
        return getEndDate() != null;
    }

    @Deprecated
    public boolean hasBeginDate() {
        return getBeginDate() != null;
    }

    @Deprecated
    public boolean hasRelatedInformation() {
        return getRelatedInformation() != null;
    }

    @Deprecated
    public boolean hasLastUpdate() {
        return getLastUpdate() != null;
    }

    @Deprecated
    public boolean hasCareerWorkshopConfirmationEvent() {
        return getCareerWorkshopConfirmationEvent() != null;
    }

    @Deprecated
    public boolean hasSpreadsheet() {
        return getSpreadsheet() != null;
    }

}
