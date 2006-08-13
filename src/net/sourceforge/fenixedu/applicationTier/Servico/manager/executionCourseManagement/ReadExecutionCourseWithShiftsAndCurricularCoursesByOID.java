/*
 * Created on 2004/11/17
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author Luis Cruz
 *  
 */
public class ReadExecutionCourseWithShiftsAndCurricularCoursesByOID extends Service {

    public InfoExecutionCourse run(final Integer oid) throws ExcepcaoPersistencia {
        InfoExecutionCourse infoExecutionCourse = null;

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(oid);
        if (executionCourse != null) {
            infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);

            infoExecutionCourse.setInfoExecutionPeriod(InfoExecutionPeriod
                    .newInfoFromDomain(executionCourse.getExecutionPeriod()));

            infoExecutionCourse.setAssociatedInfoCurricularCourses(new ArrayList());
            for (Iterator iterator = executionCourse.getAssociatedCurricularCourses().iterator(); iterator
                    .hasNext();) {
                CurricularCourse curricularCourse = (CurricularCourse) iterator.next();
                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                        .newInfoFromDomain(curricularCourse);
                infoCurricularCourse.setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                        .newInfoFromDomain(curricularCourse.getDegreeCurricularPlan()));

                infoExecutionCourse.getAssociatedInfoCurricularCourses().add(infoCurricularCourse);
            }

            infoExecutionCourse.setAssociatedInfoShifts(new ArrayList(executionCourse.getAssociatedShiftsCount()));
            for (final Shift shift : executionCourse.getAssociatedShiftsSet()) {
            	final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                infoShift.setInfoLessons(new ArrayList(shift.getAssociatedLessonsCount()));
                for (final Lesson lesson : shift.getAssociatedLessons()) {
                	infoShift.getInfoLessons().add(InfoLesson.newInfoFromDomain(lesson));
                }
                infoExecutionCourse.getAssociatedInfoShifts().add(infoShift);
            }
        }

        return infoExecutionCourse;
    }

}