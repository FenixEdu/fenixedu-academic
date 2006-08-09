/*
 * Created on 2003/07/30
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadShiftByOID extends Service {

    public InfoShift run(final Integer oid) throws ExcepcaoPersistencia {
        final Shift shift = rootDomainObject.readShiftByOID(oid);

        if (shift != null) {
            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

            final ExecutionCourse executionCourse = shift.getDisciplinaExecucao();
            final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

            final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
            final InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);
            infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

            final Collection lessons = shift.getAssociatedLessons();
            final List infoLessons = new ArrayList(lessons.size());
            infoShift.setInfoLessons(infoLessons);
            for (final Iterator<Lesson> iterator = lessons.iterator(); iterator.hasNext(); ) {
                infoLessons.add(InfoLesson.newInfoFromDomain(iterator.next()));
            }

            final Collection schoolClasses = shift.getAssociatedClasses();
            final List infoSchoolClasses = new ArrayList(schoolClasses.size());
            infoShift.setInfoClasses(infoSchoolClasses);
            for (final Iterator iterator = schoolClasses.iterator(); iterator.hasNext(); ) {
                final SchoolClass schoolClass = (SchoolClass) iterator.next();
                final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
                infoSchoolClasses.add(infoClass);
            }

            return infoShift;
        }
        return null;
    }

}