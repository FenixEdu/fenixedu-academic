/*
 * CriarAula.java
 *
 * Created on 2003/03/30
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o CriarAula.
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class AssociateExecutionCourseToExam implements IService {

    public Boolean run(InfoViewExamByDayAndShift infoViewExam, InfoExecutionCourse infoExecutionCourse)
            throws FenixServiceException, ExcepcaoPersistencia {

        Boolean result = new Boolean(false);

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

        IExecutionCourse executionCourseToBeAssociatedWithExam = executionCourseDAO
                .readByExecutionCourseInitialsAndExecutionPeriodId(infoExecutionCourse.getSigla(),
                        infoExecutionCourse.getInfoExecutionPeriod().getIdInternal());

        // We assume it's the same execution period.
        IExecutionCourse someExecutionCourseAlreadyAssociatedWithExam = executionCourseDAO
                .readByExecutionCourseInitialsAndExecutionPeriodId(((InfoExecutionCourse) infoViewExam
                        .getInfoExecutionCourses().get(0)).getSigla(), infoExecutionCourse
                        .getInfoExecutionPeriod().getIdInternal());

        // Obtain a mapped exam
        IExam examFromDBToBeAssociated = null;
        List<IExam> associatedExams = new ArrayList();
        List<IEvaluation> associatedEvaluations = someExecutionCourseAlreadyAssociatedWithExam
                .getAssociatedEvaluations();
        for (IEvaluation evaluation : associatedEvaluations) {
            if (evaluation instanceof Exam) {
                associatedExams.add((IExam) evaluation);
            }
        }
        for (int i = 0; i < associatedExams.size(); i++) {
            IExam exam = associatedExams.get(i);
            if (exam.getSeason().equals(infoViewExam.getInfoExam().getSeason())) {
                examFromDBToBeAssociated = exam;
            }
        }

        // Check that the execution course which will be associated with the
        // exam
        // doesn't already have an exam scheduled for the corresponding
        // season
        List<IExam> associatedExams2 = new ArrayList();
        List<IEvaluation> associatedEvaluations2 = executionCourseToBeAssociatedWithExam
                .getAssociatedEvaluations();
        for (IEvaluation evaluation : associatedEvaluations) {
            if (evaluation instanceof Exam) {
                associatedExams.add((IExam) evaluation);
            }
        }
        for (int i = 0; i < associatedExams2.size(); i++) {
            IExam exam = associatedExams2.get(i);
            if (exam.getSeason().equals(infoViewExam.getInfoExam().getSeason())) {
                throw new ExistingServiceException();
            }
        }

        
       /* IExamExecutionCourse examExecutionCourse = DomainFactory.makeExamExecutionCourse(
                examFromDBToBeAssociated, executionCourseToBeAssociatedWithExam);*/

        result = new Boolean(true);
        return result;
    }

}
