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
 * LerAulasDeTurno.java
 *
 * Created on 28 de Outubro de 2002, 22:23
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LerAulasDeTurno {

    @Atomic
    public static List run(ShiftKey shiftKey) {

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(shiftKey.getInfoExecutionCourse().getExternalId());
        final Shift shift = executionCourse.findShiftByName(shiftKey.getShiftName());

        final List<InfoLesson> infoAulas = new ArrayList<InfoLesson>();
        for (final Lesson lesson : shift.getAssociatedLessons()) {
            infoAulas.add(InfoLesson.newInfoFromDomain(lesson));
        }
        return infoAulas;
    }

}