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
package net.sourceforge.fenixedu.domain.reports;

import java.util.Collections;
import java.util.LinkedList;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

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
                    LinkedList<RegistrationState> states = new LinkedList<RegistrationState>();
                    states.addAll(registration.getRegistrationStates());
                    CollectionUtils.filter(states, new Predicate() {
                        @Override
                        public boolean evaluate(Object item) {
                            return ((RegistrationState) item).getExecutionYear() != null
                                    && ((RegistrationState) item).getExecutionYear().equals(getExecutionYear());
                        }
                    });
                    Collections.sort(states, RegistrationState.DATE_COMPARATOR);
                    if (!states.isEmpty() && states.getLast().getStateType().equals(RegistrationStateType.FLUNKED)) {
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
