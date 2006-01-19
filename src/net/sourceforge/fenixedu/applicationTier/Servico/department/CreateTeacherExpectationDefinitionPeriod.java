package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * 
 * @author naat
 * 
 */
public class CreateTeacherExpectationDefinitionPeriod implements IService {

    public void run(Integer departmentID, Integer executionYearID, Date startDate, Date endDate)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentDepartment persistentDepartment = persistenceSupport.getIDepartamentoPersistente();
        IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                .getIPersistentExecutionYear();

        Department department = (Department) persistentDepartment.readByOID(Department.class,
                departmentID);
        ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executionYearID);

        department.createTeacherExpectationDefinitionPeriod(executionYear, startDate, endDate);
    }
}