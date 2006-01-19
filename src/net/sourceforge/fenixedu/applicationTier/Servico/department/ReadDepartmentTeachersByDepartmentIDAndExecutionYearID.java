package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * 
 * @author naat
 * 
 */
public class ReadDepartmentTeachersByDepartmentIDAndExecutionYearID extends Service {

    public List<Teacher> run(Integer departmentID, Integer executionYearID)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentDepartment persistentDepartment = persistenceSupport.getIDepartamentoPersistente();

        Department department = (Department) persistentDepartment.readByOID(Department.class,
                departmentID);

        List<Teacher> teachersFromDepartment = new ArrayList<Teacher>();

        if (executionYearID != null) {

            IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                    .getIPersistentExecutionYear();

            ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                    ExecutionYear.class, executionYearID);

            teachersFromDepartment = department.getTeachers(executionYear.getBeginDate(), executionYear
                    .getEndDate());

        } else {
            teachersFromDepartment = department.getTeachersHistoric();
        }

        return teachersFromDepartment;
    }
}