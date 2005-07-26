/*
 * CreateExam.java Created on 2003/03/26
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * 
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.Season;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateExam implements IService {

    public Boolean run(Calendar examDate, Calendar examTime, Season season,
            InfoExecutionCourse infoExecutionCourse) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

        IExecutionCourse executionCourse = executionCourseDAO
                .readByExecutionCourseInitialsAndExecutionPeriodId(infoExecutionCourse.getSigla(),
                        infoExecutionCourse.getInfoExecutionPeriod().getIdInternal());

        for (int i = 0; i < executionCourse.getAssociatedExams().size(); i++) {
            IExam exam = executionCourse.getAssociatedExams().get(i);
            if (exam.getSeason().equals(season)) {
                throw new ExistingServiceException();
            }
        }

        IExam exam = DomainFactory.makeExam(examDate, examTime, null, season);
        DomainFactory.makeExamExecutionCourse(exam, executionCourse);

        return true;
    }

}