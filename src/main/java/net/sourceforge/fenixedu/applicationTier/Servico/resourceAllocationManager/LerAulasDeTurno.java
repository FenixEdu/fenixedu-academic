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
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.services.Service;

public class LerAulasDeTurno {

    @Service
    public static List run(ShiftKey shiftKey) {

        final ExecutionCourse executionCourse =
                RootDomainObject.getInstance().readExecutionCourseByOID(shiftKey.getInfoExecutionCourse().getIdInternal());
        final Shift shift = executionCourse.findShiftByName(shiftKey.getShiftName());

        final List<InfoLesson> infoAulas = new ArrayList<InfoLesson>();
        for (final Lesson lesson : shift.getAssociatedLessons()) {
            infoAulas.add(InfoLesson.newInfoFromDomain(lesson));
        }
        return infoAulas;
    }

}