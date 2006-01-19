package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author naat
 */
public class ReadDepartmentTeachersByDepartmentID implements IService {

    public List<InfoTeacher> run(Integer departmentID) throws ExcepcaoPersistencia,
            FenixServiceException {

        List<InfoTeacher> result = new ArrayList<InfoTeacher>();
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Department department = (Department) sp.getIDepartamentoPersistente().readByOID(
                Department.class, departmentID);

        List teachers = department.getCurrentTeachers();

        for (int i = 0; i < teachers.size(); i++) {

            Teacher teacher = (Teacher) teachers.get(i);
            result.add(InfoTeacher.newInfoFromDomain(teacher));
        }

        return result;

    }
}