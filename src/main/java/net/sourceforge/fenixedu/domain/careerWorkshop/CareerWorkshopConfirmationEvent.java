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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

public class CareerWorkshopConfirmationEvent extends CareerWorkshopConfirmationEvent_Base {

    public CareerWorkshopConfirmationEvent(CareerWorkshopApplicationEvent applicationEvent, DateTime beginDate, DateTime endDate) {
        super();
        evaluateConsistency(applicationEvent, beginDate, endDate);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setCareerWorkshopApplicationEvent(applicationEvent);
        setRootDomainObject(Bennu.getInstance());
    }

    private void evaluateConsistency(CareerWorkshopApplicationEvent applicationEvent, DateTime beginDate, DateTime endDate) {
        if (applicationEvent == null) {
            throw new DomainException("error.careerWorkshop.creatingConfirmationEvent: Invalid ApplicationEvent");
        }
        if (beginDate == null || endDate == null) {
            throw new DomainException("error.careerWorkshop.creatingConfirmationEvent: Invalid values for begin/end dates");
        }
        if (!beginDate.isBefore(endDate)) {
            throw new DomainException("error.careerWorkshop.creatingConfirmationEvent: Inconsistent values for begin/end dates");
        }
        if (applicationEvent.getCareerWorkshopConfirmationEvent() != null) {
            throw new DomainException(
                    "error.careerWorkshop.creatingConfirmationEvent: ApplicationEvent already has confirmation period created");
        }
    }

    public void delete() {
        if (!getCareerWorkshopConfirmations().isEmpty()) {
            throw new DomainException(
                    "error.careerWorkshop.deletingConfirmationPeriod: There are confirmations already associated");
        }
        setRootDomainObject(null);
        setConfirmations(null);
        setCareerWorkshopApplicationEvent(null);
        deleteDomainObject();
    }

    public String getFormattedBeginDate() {
        return getBeginDate().toString("dd-MM-yyyy");
    }

    public String getFormattedEndDate() {
        return getEndDate().toString("dd-MM-yyyy");
    }

    public int getNumberOfConfirmations() {
        int result = 0;
        for (CareerWorkshopConfirmation confirmation : getCareerWorkshopConfirmations()) {
            if (confirmation.getSealStamp() != null) {
                if (confirmation.getConfirmation() != null) {
                    result += (confirmation.getConfirmation() ? 1 : 0);
                }
            }
        }
        return result;
    }

    @Override
    public CareerWorkshopConfirmationSpreadsheet getConfirmations() {
        if (hasBennu()) {
            if (getLastUpdate() == null || super.getConfirmations() == null) {
                generateSpreadsheet();
            }
            if (getLastUpdate().plusDays(1).isAfter(super.getConfirmations().getUploadTime())) {
                generateSpreadsheet();
            }
        }
        return super.getConfirmations();
    }

    @Atomic
    public void generateSpreadsheet() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ISTCareerWorkshopsConfirmations-");
        DateTime stamp = (getLastUpdate() != null ? getLastUpdate() : new DateTime());
        stringBuilder.append(stamp.toString("ddMMyyyyhhmm"));
        stringBuilder.append(".csv");

        final SheetData<CareerWorkshopConfirmation> dataSheet = new SheetData<CareerWorkshopConfirmation>(getProcessedList()) {

            @Override
            protected void makeLine(CareerWorkshopConfirmation item) {
                DateTime timestamp = item.getSealStamp();

                addCell("Data de confirmação", (timestamp == null ? "" : timestamp.toString("dd-MM-yyyy")));
                addCell("Hora de confirmação", (timestamp == null ? "" : timestamp.toString("HH:mm")));
                addCell("Confirmou?", getConfirmationStatus(item));
                addCell("Número aluno", item.getStudent().getNumber());
                addCell("Nome", item.getStudent().getName());
                addCell("Email", item.getStudent().getPerson().getDefaultEmailAddressValue());
                addCell("Código", item.getConfirmationCode());

            }

            protected String getConfirmationStatus(CareerWorkshopConfirmation item) {
                if (item.getConfirmation() == null) {
                    return "Não";
                }
                if (!item.getConfirmation()) {
                    return "Não";
                }
                return "SIM";
            }

        };

        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            new SpreadsheetBuilder().addSheet(stringBuilder.toString(), dataSheet).build(WorkbookExportFormat.CSV, io);

            setConfirmations(new CareerWorkshopConfirmationSpreadsheet(stringBuilder.toString(), io.toByteArray()));
            setLastUpdate(new DateTime());
        } catch (IOException ioe) {
            throw new DomainException("error.careerWorkshop.criticalFailureGeneratingTheSpreadsheetFile", ioe);
        }
    }

    @Atomic
    private List<CareerWorkshopConfirmation> getProcessedList() {
        for (CareerWorkshopApplication application : getCareerWorkshopApplicationEvent().getCareerWorkshopApplications()) {
            if (!(application.getCareerWorkshopConfirmation() == null)) {
                continue;
            }
            if (application.getSealStamp() == null) {
                continue;
            }

            new CareerWorkshopConfirmation(application.getStudent(), this, application);
        }
        List<CareerWorkshopConfirmation> processedConfirmations =
                new ArrayList<CareerWorkshopConfirmation>(getCareerWorkshopConfirmations());
        Collections.sort(processedConfirmations, new Comparator<CareerWorkshopConfirmation>() {

            @Override
            public int compare(CareerWorkshopConfirmation o1, CareerWorkshopConfirmation o2) {
                if (o1.getSealStamp() == null) {
                    return 1;
                }
                if (o2.getSealStamp() == null) {
                    return -1;
                }
                if (o1.getSealStamp().isBefore(o2.getSealStamp())) {
                    return -1;
                }
                if (o1.getSealStamp().isAfter(o2.getSealStamp())) {
                    return 1;
                }
                return 0;
            }
        });
        return processedConfirmations;
    }

    public CareerWorkshopConfirmationSpreadsheet getConfirmationsWithoutGenerate() {
        return super.getConfirmations();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmation> getCareerWorkshopConfirmations() {
        return getCareerWorkshopConfirmationsSet();
    }

    @Deprecated
    public boolean hasAnyCareerWorkshopConfirmations() {
        return !getCareerWorkshopConfirmationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasConfirmations() {
        return getConfirmations() != null;
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
    public boolean hasLastUpdate() {
        return getLastUpdate() != null;
    }

    @Deprecated
    public boolean hasCareerWorkshopApplicationEvent() {
        return getCareerWorkshopApplicationEvent() != null;
    }

}
