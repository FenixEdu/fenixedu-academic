/*
 * Created on 2005/05/11
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.resource.spi.work.ExecutionContext;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonWithInfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonWithInfoRoomAndInfoRoomOccupationAndInfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Luis Cruz
 */
public class LerAulasDeDisciplinaExecucao implements IService {

    public Object run(final InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();

        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, infoExecutionCourse.getIdInternal());
        final List<IShift> shifts = executionCourse.getAssociatedShifts();

        // An estimated upper bound for the number of elements is three lessons per shift.
        final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>(shifts.size() * 3);

        for (final IShift shift : shifts) {
            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

            final List<ILesson> lessons = shift.getAssociatedLessons();
            for (final ILesson lesson : lessons) {
                final InfoLesson infoLesson = InfoLessonWithInfoRoomAndInfoRoomOccupationAndInfoPeriod.newInfoFromDomain(lesson);
                infoLesson.setInfoShift(infoShift);

                infoLessons.add(infoLesson);
            }
        }

        return infoLessons;
    }

}