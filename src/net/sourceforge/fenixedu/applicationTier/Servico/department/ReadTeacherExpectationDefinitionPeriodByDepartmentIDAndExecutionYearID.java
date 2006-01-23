package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.TeacherExpectationDefinitionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author naat
 * 
 */
public class ReadTeacherExpectationDefinitionPeriodByDepartmentIDAndExecutionYearID extends Service {

    public TeacherExpectationDefinitionPeriod run(Integer departmentID, Integer executionYearID)
            throws FenixServiceException, ExcepcaoPersistencia {
        Department department = (Department) persistentObject.readByOID(Department.class,
                departmentID);
        ExecutionYear executionYear = (ExecutionYear) persistentObject.readByOID(
                ExecutionYear.class, executionYearID);

        return department.readTeacherExpectationDefinitionPeriodByExecutionYear(executionYear);
    }
}