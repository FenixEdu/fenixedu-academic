/*
 * Created on 10/Set/2003, 20:47:24
 * changed on 4/Jan/2004, 19:33:11 (generalize for any execution course)
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.StudentGroupAttendacyInformation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentGroup;
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
        Student student = persistenceSupport.getIPersistentStudent().readByUsername(username);

        IPersistentExecutionCourse persistentExecutionCourse = persistenceSupport
                .getIPersistentExecutionCourse();

        ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);

        Attends attendacy = persistentAttendacy.readByAlunoAndDisciplinaExecucao(student
                .getIdInternal(), executionCourse.getIdInternal());
        if (attendacy == null)
            return null; // the student is not enrolled on this course

        List<StudentGroup> attendStudentGroup = attendacy.getStudentGroups();
        StudentGroup studentGroup = (attendStudentGroup.isEmpty()) ? null : attendStudentGroup.get(0);

        if (studentGroup == null)
            return null; // the student has not a group, at least at this
                            // course

        StudentGroupAttendacyInformation info = new StudentGroupAttendacyInformation();
        info.setShiftName(studentGroup.getShift().getNome());
        List lessons = studentGroup.getShift().getAssociatedLessons();
        info.setDegreesNames(executionCourse.getAssociatedCurricularCourses());
        info.setLessons(lessons);
        info.setGroupNumber(studentGroup.getGroupNumber());

        List groupAttends = studentGroup.getAttends();
        info.setGroupAttends(groupAttends);

        return info;
    }
}