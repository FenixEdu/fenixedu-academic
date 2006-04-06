package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author naat
 */
public class ReadTeacherPersonalExpectationByTeacherIDAndExecutionYearID extends Service {

    public InfoTeacherPersonalExpectation run(Integer teacherID,
            Integer executionYearID) throws ExcepcaoPersistencia, FenixServiceException {
        Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
        ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);

        TeacherPersonalExpectation teacherPersonalExpectation = teacher
                .getTeacherPersonalExpectationByExecutionYear(executionYear);

        return InfoTeacherPersonalExpectation
                .newInfoFromDomain(teacherPersonalExpectation);

    }
}