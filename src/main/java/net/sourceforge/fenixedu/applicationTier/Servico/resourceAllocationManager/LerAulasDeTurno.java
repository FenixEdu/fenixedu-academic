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