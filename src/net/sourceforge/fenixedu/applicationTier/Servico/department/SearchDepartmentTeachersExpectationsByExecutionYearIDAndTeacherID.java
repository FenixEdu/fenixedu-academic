package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * 
 * @author naat
 * 
 */
public class SearchDepartmentTeachersExpectationsByExecutionYearIDAndTeacherID implements IService {

    public List<TeacherPersonalExpectation> run(Integer departmentID, Integer executionYearID,
            Integer teacherID) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentDepartment persistentDepartment = persistenceSupport.getIDepartamentoPersistente();
        IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                .getIPersistentExecutionYear();
        IPersistentTeacher persistentTeacher = persistenceSupport.getIPersistentTeacher();

        Department department = (Department) persistentDepartment.readByOID(Department.class,
                departmentID);

        List<TeacherPersonalExpectation> result = new ArrayList<TeacherPersonalExpectation>();

        ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executionYearID);

        if (teacherID != null) {
            Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherID);
            TeacherPersonalExpectation teacherPersonalExpectation = teacher
                    .getTeacherPersonalExpectationByExecutionYear(executionYear);

            if (teacherPersonalExpectation != null) {
                result.add(teacherPersonalExpectation);
            }

        } else {
            result = department.getTeachersPersonalExpectationsByExecutionYear(executionYear);
        }

        return result;
    }
}