/*
 * Created on 25/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Mota
 * 
 * 
 */
public class ReadTeachersByExecutionCourseProfessorship extends Service {

    public List run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException, ExcepcaoPersistencia {
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourse.getIdInternal());
        return executionCourse == null ? null : executionCourse.getProfessorships();
    }

}