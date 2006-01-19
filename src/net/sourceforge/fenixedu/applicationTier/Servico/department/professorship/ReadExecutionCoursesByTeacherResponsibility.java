/*
 * Created on Aug 26, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author João Mota
 *  
 */
public class ReadExecutionCoursesByTeacherResponsibility extends Service {

    public List run(Integer teacherNumber) throws FenixServiceException, ExcepcaoPersistencia {

        final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        
        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
   
        final IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
       
        Teacher teacher = persistentTeacher.readByNumber(teacherNumber);        
        
        final List<Professorship> responsibilities = teacher.responsibleFors();
        
        if (responsibilities != null) {
            for (final Professorship professorship : responsibilities) {
                infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(professorship
                        .getExecutionCourse()));
            }
        }
        return infoExecutionCourses;
    }
}