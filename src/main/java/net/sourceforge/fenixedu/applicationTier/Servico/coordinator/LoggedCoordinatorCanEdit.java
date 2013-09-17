package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LoggedCoordinatorCanEdit {

    @Atomic
    public static Boolean run(String executionDegreeCode, String curricularCourseCode, String username)
            throws FenixServiceException {
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
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeCode);
        ExecutionYear executionYear = executionDegree.getExecutionYear();

        CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        // if user is coordinator and is the current coordinator and
        // curricular course is not basic
        // coordinator can edit
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);
        result =
                Boolean.valueOf((coordinator != null) && executionYear.isCurrent()
                        && curricularCourse.getBasic().equals(Boolean.FALSE));

        return result;
    }

}