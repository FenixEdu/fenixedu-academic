/*
 * Created on 2005/05/11
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Luis Cruz
 */
public class LerAulasDeDisciplinaExecucao {

    @Service
    public static Object run(final InfoExecutionCourse infoExecutionCourse) {

        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(infoExecutionCourse.getIdInternal());
        final Set<Shift> shifts = executionCourse.getAssociatedShifts();

        // An estimated upper bound for the number of elements is three lessons
        // per shift.
        final int estimatedNumberOfLessons = shifts.size() * 3;

        final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>(estimatedNumberOfLessons);

        for (final Shift shift : shifts) {
            for (final Lesson lesson : shift.getAssociatedLessons()) {
                infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
            }
        }

        return infoLessons;
    }

}