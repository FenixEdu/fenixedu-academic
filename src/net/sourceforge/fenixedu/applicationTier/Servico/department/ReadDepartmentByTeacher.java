/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author jpvl
 */
public class ReadDepartmentByTeacher implements IService {

    public InfoDepartment run(InfoTeacher infoTeacher) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();       
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        
        Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, infoTeacher.getIdInternal());
        
        Department department = teacher.getCurrentWorkingDepartment();
        return InfoDepartment.newInfoFromDomain(department);
    }
}