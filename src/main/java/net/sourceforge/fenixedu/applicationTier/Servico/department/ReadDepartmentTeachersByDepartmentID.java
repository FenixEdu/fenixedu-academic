package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author naat
 */
public class ReadDepartmentTeachersByDepartmentID extends FenixService {

    public List<InfoTeacher> run(Integer departmentID) throws FenixServiceException {

        List<InfoTeacher> result = new ArrayList<InfoTeacher>();
        Department department = rootDomainObject.readDepartmentByOID(departmentID);

        List teachers = department.getAllCurrentTeachers();

        for (int i = 0; i < teachers.size(); i++) {

            Teacher teacher = (Teacher) teachers.get(i);
            result.add(InfoTeacher.newInfoFromDomain(teacher));
        }

        return result;

    }

    // Service Invokers migrated from Berserk

    private static final ReadDepartmentTeachersByDepartmentID serviceInstance = new ReadDepartmentTeachersByDepartmentID();

    @Service
    public static List<InfoTeacher> runReadDepartmentTeachersByDepartmentID(Integer departmentID) throws FenixServiceException,
            NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(departmentID);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(departmentID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}