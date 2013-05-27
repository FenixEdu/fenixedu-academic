/*
 * Created on 25/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Mota
 * 
 * 
 */
public class ReadTeachersByExecutionCourseProfessorship {

    @Service
    public static List run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(infoExecutionCourse.getIdInternal());
        return executionCourse == null ? null : executionCourse.getProfessorships();
    }

}