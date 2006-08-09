/*
 * 
 * Created on 2003/08/13
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadAvailableShiftsForClass extends Service {

    public Object run(InfoClass infoClass) throws ExcepcaoPersistencia {

        List infoShifts = null;

        SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID(infoClass.getIdInternal());
        Set<Shift> shifts = schoolClass.findAvailableShifts();

        infoShifts = (List) CollectionUtils.collect(shifts, new Transformer() {
            public Object transform(Object arg0) {
                Shift shift = (Shift) arg0;
                final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

                final ExecutionCourse executionCourse = shift.getDisciplinaExecucao();
                final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                        .newInfoFromDomain(executionCourse);
                infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

                final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>(shift.getAssociatedLessonsCount());
                infoShift.setInfoLessons(infoLessons);
                
                for (final Lesson lesson : shift.getAssociatedLessons()) {
                	infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
                }
                return infoShift;
            }
        });

        return infoShifts;
    }

}