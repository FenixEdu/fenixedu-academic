package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author naat
 * 
 */
public class CreateTeacherExpectationDefinitionPeriod extends Service {

    public void run(Integer departmentID, Integer executionYearID, Date startDate, Date endDate)
            throws FenixServiceException, ExcepcaoPersistencia {
        Department department = (Department) persistentObject.readByOID(Department.class,
                departmentID);
        ExecutionYear executionYear = (ExecutionYear) persistentObject.readByOID(
                ExecutionYear.class, executionYearID);

        department.createTeacherExpectationDefinitionPeriod(executionYear, startDate, endDate);
    }
}