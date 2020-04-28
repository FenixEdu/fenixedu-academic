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
/*
 * EditarTurno.java Created on 27 de Outubro de 2002, 21:00
 */

package org.fenixedu.academic.service.services.resourceAllocationManager;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftEnrolment;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.InfoShift;
import org.fenixedu.academic.dto.InfoShiftEditor;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditarTurno {

    @Atomic
    public static Object run(InfoShift infoShiftOld, InfoShiftEditor infoShiftNew) {
        final Shift shiftToEdit = FenixFramework.getDomainObject(infoShiftOld.getExternalId());
        final ExecutionCourse newExecutionCourse =
                FenixFramework.getDomainObject(infoShiftNew.getInfoDisciplinaExecucao().getExternalId());

        final Integer newCapacity = infoShiftNew.getLotacao();
        if (newCapacity != null && ShiftEnrolment.getTotalEnrolments(shiftToEdit) > newCapacity.intValue()) {
            throw new DomainException("errors.exception.invalid.finalAvailability");
        }

        shiftToEdit.edit(infoShiftNew.getTipos(), newExecutionCourse, infoShiftNew.getNome(), infoShiftNew.getComment());
        shiftToEdit.setLotacao(newCapacity);
        return InfoShift.newInfoFromDomain(shiftToEdit);
    }
}