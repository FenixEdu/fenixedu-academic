package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.PeriodState;

public class LoggedCoordinatorCanEdit extends Service {

    public Boolean run(Integer executionDegreeCode, Integer curricularCourseCode, String username)
            throws FenixServiceException, ExcepcaoPersistencia {
        Boolean result = new Boolean(false);

        if (executionDegreeCode == null) {
            throw new FenixServiceException("nullExecutionDegreeCode");
        }
        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourseCode");
        }
        if (username == null) {
            throw new FenixServiceException("nullUsername");
        }

        final Person person = Person.readPersonByUsername(username);
        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeCode);
        ExecutionYear executionYear = executionDegree.getExecutionYear();

        CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        // if user is coordinator and is the current coordinator and
        // curricular course is not basic
        // coordinator can edit
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);
        result = Boolean.valueOf((coordinator != null)
                && executionYear.getState().equals(PeriodState.CURRENT)
                && curricularCourse.getBasic().equals(Boolean.FALSE));

        return result;
    }
    
}
