package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author naat
 */
public class ReadDepartmentTeachersByDepartmentID extends Service {

    public List<InfoTeacher> run(Integer departmentID) throws ExcepcaoPersistencia,
            FenixServiceException {

        List<InfoTeacher> result = new ArrayList<InfoTeacher>();
        Department department = rootDomainObject.readDepartmentByOID( departmentID);

        List teachers = department.getAllCurrentTeachers();

        for (int i = 0; i < teachers.size(); i++) {

            Teacher teacher = (Teacher) teachers.get(i);
            result.add(InfoTeacher.newInfoFromDomain(teacher));
        }

        return result;

    }
}