/*
 * Created on 13/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * @author Fernanda Quitério
 *  
 */
public class ReadExams implements IService {

    /**
     * Executes the service. Returns the current collection of exams
     * 
     *  
     */

    public List run(Integer executionCourseCode) throws FenixServiceException {
        try {
            ISuportePersistente sp;
            IExecutionCourse executionCourse;

            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseCode);
            if (executionCourse == null) {
                throw new NonExistingServiceException();
            }
            List infoExams = new ArrayList();
            List exams = null;

            if (executionCourse != null) {
                exams = executionCourse.getAssociatedExams();

                Iterator iter = exams.iterator();
                while (iter.hasNext()) {
                    infoExams.add(Cloner.copyIExam2InfoExam((IExam) iter.next()));
                }

            }
            return infoExams;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}