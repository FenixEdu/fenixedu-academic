package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;

/**
 * @author naat
 */
public class InsertTeacherPersonalExpectation extends Service {

    public void run(InfoTeacherPersonalExpectation infoTeacherPersonalExpectation, Integer teacherID,
            Integer executionYearID) throws ExcepcaoPersistencia, FenixServiceException {

        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
        IPersistentExecutionYear persistentExecutionYear = persistentSupport
                .getIPersistentExecutionYear();

        Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherID);
        ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executionYearID);
        
        teacher.createTeacherPersonalExpectation(infoTeacherPersonalExpectation, executionYear);

    }
}