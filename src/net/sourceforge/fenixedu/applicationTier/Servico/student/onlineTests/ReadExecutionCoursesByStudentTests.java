/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadExecutionCoursesByStudentTests implements IService {

    public Object run(String userName) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport.getIPersistentStudentTestQuestion();

        final IStudent student = persistentSuport.getIPersistentStudent().readByUsername(userName);
        final List<IAttends> attends = persistentSuport.getIFrequentaPersistente().readByStudentNumber(student.getNumber(), student.getDegreeType());

        final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        for (IAttends attend : attends) {
            final IExecutionCourse executionCourse = attend.getDisciplinaExecucao();
            if (persistentStudentTestQuestion.countStudentTestByStudentAndExecutionCourse(executionCourse.getIdInternal(), student.getIdInternal()) != 0) {
                infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            }
        }
        return infoExecutionCourses;
    }
}