/*
 * Created on Dec 18, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author jpvl
 */
public class UpdateTeacherExecutionYearResponsabilities implements IService {

    public void run(Integer teacherId, Integer executionYearId,
            final List executionCourseResponsabilities) throws FenixServiceException,
            ExcepcaoPersistencia, DomainException {

        final ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentTeacher persistentTeacher = suportePersistente.getIPersistentTeacher();        
    
        final Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherId);
        if (teacher == null)
            throw new FenixServiceException("message.teacher-not-found");

        teacher.updateResponsabilitiesFor(executionYearId, executionCourseResponsabilities);        
    }       
}