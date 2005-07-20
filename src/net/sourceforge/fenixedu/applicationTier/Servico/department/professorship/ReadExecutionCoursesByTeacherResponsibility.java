/*
 * Created on Aug 26, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 *  
 */
public class ReadExecutionCoursesByTeacherResponsibility implements IService {

    public List run(Integer teacherNumber) throws FenixServiceException, ExcepcaoPersistencia {

        final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        
        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
   
        final IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
       
        ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);        
        
        final List<IProfessorship> responsibilities = teacher.responsibleFors();
        
        if (responsibilities != null) {
            for (final IProfessorship professorship : responsibilities) {
                infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(professorship
                        .getExecutionCourse()));
            }
        }
        return infoExecutionCourses;
    }
}