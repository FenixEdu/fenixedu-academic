package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author naat
 */
public class ReadFinalDegreeWorksByTeacherIDAndExecutionYearID extends Service {

    public List<Proposal> run(Integer teacherID, Integer executionYearID) throws ExcepcaoPersistencia,
            FenixServiceException {
        Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
        ExecutionYear executionYear = (ExecutionYear) persistentObject.readByOID(
                ExecutionYear.class, executionYearID);

        ExecutionYear previousExecutionYear = executionYear.getPreviousExecutionYear();

        List<Proposal> finalDegreeWorks = teacher
                .getFinalDegreeWorksByExecutionYear(previousExecutionYear);

        return finalDegreeWorks;

    }
}