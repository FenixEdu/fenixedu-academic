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

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExamExecutionCourse;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class AssociateExecutionCourseToExam implements IService {

    /**
     * The actor of this class.
     */
    public AssociateExecutionCourseToExam() {
    }

    public Boolean run(InfoViewExamByDayAndShift infoViewExam, InfoExecutionCourse infoExecutionCourse)
            throws FenixServiceException {

        Boolean result = new Boolean(false);

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionCourse
                            .getInfoExecutionPeriod());

            IExecutionCourse executionCourseToBeAssociatedWithExam = executionCourseDAO
                    .readByExecutionCourseInitialsAndExecutionPeriod(infoExecutionCourse.getSigla(),
                            executionPeriod);

            // We assume it's the same execution period.
            IExecutionCourse someExecutionCourseAlreadyAssociatedWithExam = executionCourseDAO
                    .readByExecutionCourseInitialsAndExecutionPeriod(((InfoExecutionCourse) infoViewExam
                            .getInfoExecutionCourses().get(0)).getSigla(), executionPeriod);

            // Obtain a mapped exam
            IExam examFromDBToBeAssociated = null;
            for (int i = 0; i < someExecutionCourseAlreadyAssociatedWithExam.getAssociatedExams().size(); i++) {
                IExam exam = (IExam) someExecutionCourseAlreadyAssociatedWithExam.getAssociatedExams()
                        .get(i);
                if (exam.getSeason().equals(infoViewExam.getInfoExam().getSeason())) {
                    examFromDBToBeAssociated = exam;
                }
            }

            // Check that the execution course which will be associated with the
            // exam
            // doesn't already have an exam scheduled for the corresponding
            // season
            for (int i = 0; i < executionCourseToBeAssociatedWithExam.getAssociatedExams().size(); i++) {
                IExam exam = (IExam) executionCourseToBeAssociatedWithExam.getAssociatedExams().get(i);
                if (exam.getSeason().equals(infoViewExam.getInfoExam().getSeason())) {
                    throw new ExistingServiceException();
                }
            }

            IExamExecutionCourse examExecutionCourse = new ExamExecutionCourse(examFromDBToBeAssociated,
                    executionCourseToBeAssociatedWithExam);

            try {
                sp.getIPersistentExamExecutionCourse().simpleLockWrite(examExecutionCourse);
            } catch (ExistingPersistentException ex) {
                throw new ExistingServiceException(ex);
            }

            result = new Boolean(true);
        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException(ex.getMessage());
        }

        return result;
    }

}