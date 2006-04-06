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
        Department department = rootDomainObject.readDepartmentByOID(
                departmentID);
        ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);

        department.createTeacherExpectationDefinitionPeriod(executionYear, startDate, endDate);
    }
}