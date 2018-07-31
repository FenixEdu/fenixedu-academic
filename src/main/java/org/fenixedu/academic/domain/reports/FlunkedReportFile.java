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
package org.fenixedu.academic.domain.reports;

import java.util.LinkedList;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;

public class FlunkedReportFile extends FlunkedReportFile_Base {

    public FlunkedReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem de prescrições";
    }

    @Override
    protected String getPrefix() {
        return "prescricoes";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) {
        spreadsheet.setHeader("número aluno");
        spreadsheet.setHeader("ciclo estudos");
        setDegreeHeaders(spreadsheet);

        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (checkDegreeType(getDegreeType(), degree)) {
                for (final Registration registration : degree.getRegistrationsSet()) {
                    LinkedList<RegistrationState> states = registration.getRegistrationStatesSet().stream()
                            .filter(item -> item.getExecutionYear() != null && item.getExecutionYear().equals(getExecutionYear()))
                            .sorted(RegistrationState.DATE_COMPARATOR)
                            .collect(Collectors.toCollection(LinkedList::new));

                    if (!states.isEmpty() && states.getLast().getStateType() == RegistrationStateType.FLUNKED) {
                        final Row row = spreadsheet.addRow();
                        row.setCell(registration.getNumber());
                        CycleType cycleType = registration.getCycleType(states.getLast().getExecutionYear());
                        row.setCell(cycleType != null ? cycleType.toString() : "");
                        setDegreeCells(row, degree);
                    }
                }
            }
        }
    }

}
