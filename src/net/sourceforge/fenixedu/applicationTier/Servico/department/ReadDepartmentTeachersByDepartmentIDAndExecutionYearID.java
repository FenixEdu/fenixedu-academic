package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacher;
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
public class ReadDepartmentTeachersByDepartmentIDAndExecutionYearID implements IService {

    public List<ITeacher> run(Integer departmentID, Integer executionYearID)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentDepartment persistentDepartment = persistenceSupport.getIDepartamentoPersistente();

        IDepartment department = (IDepartment) persistentDepartment.readByOID(Department.class,
                departmentID);

        List<ITeacher> teachersFromDepartment = new ArrayList<ITeacher>();

        if (executionYearID != null) {

            IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                    .getIPersistentExecutionYear();

            IExecutionYear executionYear = (IExecutionYear) persistentExecutionYear.readByOID(
                    ExecutionYear.class, executionYearID);

            teachersFromDepartment = department.getTeachers(executionYear.getBeginDate(), executionYear
                    .getEndDate());

        } else {
            teachersFromDepartment = department.getTeachersHistoric();
        }

        return teachersFromDepartment;
    }
}