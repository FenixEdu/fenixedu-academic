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
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 10/Set/2003, 20:47:24
 *  
 */
public class GetProjectGroupAttendantsByExecutionCourseIDANDStudentUsername implements IServico {
    private static GetProjectGroupAttendantsByExecutionCourseIDANDStudentUsername service = new GetProjectGroupAttendantsByExecutionCourseIDANDStudentUsername();

    /**
     * The singleton access method of this class.
     */
    public static GetProjectGroupAttendantsByExecutionCourseIDANDStudentUsername getService() {
        return service;
    }

    /**
     * The actor of this class.
     */
    private GetProjectGroupAttendantsByExecutionCourseIDANDStudentUsername() {
    }

    /**
     * Returns The Service Name
     */
    public final String getNome() {
        return "publico.GetProjectGroupAttendantsByExecutionCourseIDANDStudentUsername";
    }

    public StudentGroupAttendacyInformation run(Integer executionCourseID, String username)
            throws BDException {
        try {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IFrequentaPersistente persistentAttendacy = persistenceSupport.getIFrequentaPersistente();
            IStudent student = persistenceSupport.getIPersistentStudent().readByUsername(username);
            IPersistentStudentGroupAttend persistentStudentGroupAttend = persistenceSupport
                    .getIPersistentStudentGroupAttend();
            //
            IPersistentExecutionCourse persistentExecutionCourse = persistenceSupport
                    .getIPersistentExecutionCourse();
            //
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseID);
            //
            //
            IAttends attendacy = persistentAttendacy.readByAlunoAndDisciplinaExecucao(student,
                    executionCourse);
            if (attendacy == null)
                return null; // the student is not enrolled on this course
            IStudentGroupAttend groupAttend = persistentStudentGroupAttend.readBy(attendacy);
            StudentGroupAttendacyInformation info = new StudentGroupAttendacyInformation();
            if (groupAttend == null)
                return null; // the student has not a group, at least at this
            // course
            info.setShiftName(groupAttend.getStudentGroup().getShift().getNome());
            List lessons = groupAttend.getStudentGroup().getShift().getAssociatedLessons();
            info.setDegreesNames(executionCourse.getAssociatedCurricularCourses());
            info.setLessons(lessons);
            info.setGroupNumber(groupAttend.getStudentGroup().getGroupNumber());
            List groupAttends = persistentStudentGroupAttend.readByStudentGroupId(groupAttend
                    .getKeyStudentGroup());
            info.setGroupAttends(groupAttends);

            return info;
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException("Got an error while trying to get info about a student's work group",
                    ex);
        }
    }
}