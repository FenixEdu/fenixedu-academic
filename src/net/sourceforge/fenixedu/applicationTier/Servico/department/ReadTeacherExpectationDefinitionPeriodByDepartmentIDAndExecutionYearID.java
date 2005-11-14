package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacherExpectationDefinitionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author naat
 * 
 */
public class ReadTeacherExpectationDefinitionPeriodByDepartmentIDAndExecutionYearID implements IService {

    public ReadTeacherExpectationDefinitionPeriodByDepartmentIDAndExecutionYearID() {
    }

    public ITeacherExpectationDefinitionPeriod run(Integer departmentID, Integer executionYearID)
            throws FenixServiceException {

        try {
            ISuportePersistente persistenceSupport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();
            IPersistentDepartment persistentDepartment = persistenceSupport
                    .getIDepartamentoPersistente();
            IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                    .getIPersistentExecutionYear();

            IDepartment department = (IDepartment) persistentDepartment.readByOID(Department.class,
                    departmentID);
            IExecutionYear executionYear = (IExecutionYear) persistentExecutionYear.readByOID(
                    ExecutionYear.class, executionYearID);

            return department.readTeacherExpectationDefinitionPeriodByExecutionYear(executionYear);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}