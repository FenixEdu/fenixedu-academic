/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadExecutionCoursesByStudentTests extends Service {

    public Object run(final Registration registration) throws ExcepcaoPersistencia {
        final List<Attends> attends = registration.getAssociatedAttends();

        final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        for (Attends attend : attends) {
            final ExecutionCourse executionCourse = attend.getDisciplinaExecucao();
            if (registration.countDistributedTestsByExecutionCourse(executionCourse) != 0) {
                infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            }
        }
        return infoExecutionCourses;
    }
}