/*
 * Created on 10/Set/2003, 20:47:24
 * changed on 4/Jan/2004, 19:33:11 (generalize for any execution course)
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.StudentGroupAttendacyInformation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 10/Set/2003, 20:47:24
 * 
 */
public class GetProjectGroupAttendantsByExecutionCourseIDANDStudentUsername implements IService {

    public StudentGroupAttendacyInformation run(Integer executionCourseID, String username)
            throws BDException, ExcepcaoPersistencia {

        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IFrequentaPersistente persistentAttendacy = persistenceSupport.getIFrequentaPersistente();
        IStudent student = persistenceSupport.getIPersistentStudent().readByUsername(username);

        IPersistentExecutionCourse persistentExecutionCourse = persistenceSupport
                .getIPersistentExecutionCourse();

        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);

        IAttends attendacy = persistentAttendacy.readByAlunoAndDisciplinaExecucao(student,
                executionCourse);
        if (attendacy == null)
            return null; // the student is not enrolled on this course

        List<IStudentGroupAttend> studentGroupAttends = attendacy.getStudentGroupAttends();
        IStudentGroupAttend groupAttend = (studentGroupAttends.isEmpty()) ? null : studentGroupAttends
                .get(0);
        StudentGroupAttendacyInformation info = new StudentGroupAttendacyInformation();
        if (groupAttend == null)
            return null; // the student has not a group, at least at this
                            // course
        info.setShiftName(groupAttend.getStudentGroup().getShift().getNome());
        List lessons = groupAttend.getStudentGroup().getShift().getAssociatedLessons();
        info.setDegreesNames(executionCourse.getAssociatedCurricularCourses());
        info.setLessons(lessons);
        info.setGroupNumber(groupAttend.getStudentGroup().getGroupNumber());

        List groupAttends = groupAttend.getStudentGroup().getStudentGroupAttends();

        info.setGroupAttends(groupAttends);

        return info;
    }
}