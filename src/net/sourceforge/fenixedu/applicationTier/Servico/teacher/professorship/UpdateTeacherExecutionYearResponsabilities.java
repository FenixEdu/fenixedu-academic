/*
 * Created on Dec 18, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class UpdateTeacherExecutionYearResponsabilities implements IService {

    public Boolean run(Integer teacherId, Integer executionYearId,
            final List executionCourseResponsabilities) throws FenixServiceException,
            ExcepcaoPersistencia, DomainException {

        ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentTeacher persistentTeacher = suportePersistente.getIPersistentTeacher();        
        IPersistentExecutionCourse executionCourseDAO = suportePersistente
                .getIPersistentExecutionCourse();

        ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherId);
        if (teacher == null)
            throw new FenixServiceException();

        teacher.updateResponsabilitiesFor(executionYearId, executionCourseResponsabilities);
                        
        return Boolean.TRUE;        
    }       
}