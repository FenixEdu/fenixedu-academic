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
/*
 * EditarTurno.java Created on 27 de Outubro de 2002, 21:00
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditarTurno {

    @Atomic
    public static Object run(InfoShift infoShiftOld, InfoShiftEditor infoShiftNew) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final Shift shiftToEdit = FenixFramework.getDomainObject(infoShiftOld.getExternalId());
        final ExecutionCourse newExecutionCourse =
                FenixFramework.getDomainObject(infoShiftNew.getInfoDisciplinaExecucao().getExternalId());
        shiftToEdit.edit(infoShiftNew.getTipos(), infoShiftNew.getLotacao(), newExecutionCourse, infoShiftNew.getNome(),
                infoShiftNew.getComment());
        return InfoShift.newInfoFromDomain(shiftToEdit);
    }
}