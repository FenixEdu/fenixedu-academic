package net.sourceforge.fenixedu.applicationTier.Servico.departmentMember;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsPersistenceSupport;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author naat
 */
public class ReadDepartmentTeachersByDepartmentID implements IService {

    public List<InfoTeacher> run(Integer departmentID) throws ExcepcaoPersistencia,
            FenixServiceException {

        List<InfoTeacher> result = new ArrayList<InfoTeacher>();
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IDepartment department = (IDepartment) sp.getIDepartamentoPersistente().readByOID(
                Department.class, departmentID);

        List teachers = department.getTeachers();

        for (int i = 0; i < teachers.size(); i++) {

            ITeacher teacher = (ITeacher) teachers.get(i);
            result.add(InfoTeacher.newInfoFromDomain(teacher));
        }

        return result;

    }
}