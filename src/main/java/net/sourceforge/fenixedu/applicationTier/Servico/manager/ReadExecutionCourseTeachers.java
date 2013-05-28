/*
 * Created on 16/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author lmac1
 */
public class ReadExecutionCourseTeachers {

    /**
     * Executes the service. Returns the current collection of infoTeachers.
     * 
     * @throws ExcepcaoPersistencia
     */

    protected List run(Integer executionCourseId) throws FenixServiceException {

        List professorShips = null;
        ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);
        professorShips = executionCourse.getProfessorships();

        if (professorShips == null || professorShips.isEmpty()) {
            return null;
        }

        List infoTeachers = new ArrayList();
        Iterator iter = professorShips.iterator();
        Teacher teacher = null;

        while (iter.hasNext()) {
            teacher = ((Professorship) iter.next()).getTeacher();
            infoTeachers.add(InfoTeacher.newInfoFromDomain(teacher));
        }

        return infoTeachers;
    }

    // Service Invokers migrated from Berserk

    private static final ReadExecutionCourseTeachers serviceInstance = new ReadExecutionCourseTeachers();

    @Service
    public static List runReadExecutionCourseTeachers(Integer executionCourseId) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(executionCourseId);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                return serviceInstance.run(executionCourseId);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}