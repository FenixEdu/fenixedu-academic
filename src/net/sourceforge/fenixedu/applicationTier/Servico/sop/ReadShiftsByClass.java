/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 *
 * Created on 2003/08/12
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonWithInfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadShiftsByClass implements IService {

    public Object run(InfoClass infoClass) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        SchoolClass schoolClass = (SchoolClass) sp.getITurmaPersistente().readByOID(SchoolClass.class,
                infoClass.getIdInternal());

        List<Shift> shifts = schoolClass.getAssociatedShifts();

        return CollectionUtils.collect(shifts, new Transformer() {
            public Object transform(Object arg0) {
                Shift shift = (Shift) arg0;
                InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                infoShift.setInfoLessons((List) CollectionUtils.collect(shift.getAssociatedLessons(),
                        new Transformer() {
                            public Object transform(Object arg0) {
                                InfoLesson infoLesson = InfoLessonWithInfoRoom.newInfoFromDomain((Lesson) arg0);
                                Shift shift = ((Lesson) arg0).getShift();
                                InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                                infoLesson.setInfoShift(infoShift);

                                return infoLesson;
                            }
                        }));
                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(shift
                        .getDisciplinaExecucao());
                infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
                return infoShift;
            }
        });
    }

}