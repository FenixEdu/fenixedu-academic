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
 * LerTurnosDeDisciplinaExecucao.java
 *
 * Created on 01 de Dezembro de 2002, 17:51
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LerTurnosDeDisciplinaExecucao {

    @Atomic
    public static List<InfoShift> run(InfoExecutionCourse infoExecutionCourse) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        List<InfoShift> infoShifts = new ArrayList<InfoShift>();

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(infoExecutionCourse.getExternalId());
        Iterator<Shift> itShiftList = executionCourse.getAssociatedShifts().iterator();

        while (itShiftList.hasNext()) {
            Shift shift = itShiftList.next();
            infoShifts.add(InfoShift.newInfoFromDomain(shift));
        }

        return infoShifts;
    }
}