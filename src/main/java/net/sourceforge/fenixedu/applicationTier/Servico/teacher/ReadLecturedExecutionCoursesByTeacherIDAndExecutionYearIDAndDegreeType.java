package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author naat
 */
public class ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType {

    public List<ExecutionCourse> run(String teacherID, String executionYearID, DegreeType degreeType)
            throws FenixServiceException {

        Teacher teacher = FenixFramework.getDomainObject(teacherID);

        List<ExecutionCourse> lecturedExecutionCourses;

        if (executionYearID == null) {
            lecturedExecutionCourses = teacher.getAllLecturedExecutionCourses();

        } else {
            ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);
            lecturedExecutionCourses = teacher.getLecturedExecutionCoursesByExecutionYear(executionYear);
        }

        List<ExecutionCourse> result;

        if (degreeType == DegreeType.DEGREE) {
            result = filterExecutionCourses(lecturedExecutionCourses, false);
        } else {
            // master degree
            result = filterExecutionCourses(lecturedExecutionCourses, true);
        }

        return result;

    }

    private List<ExecutionCourse> filterExecutionCourses(List<ExecutionCourse> executionCourses, boolean masterDegreeOnly) {
        List<ExecutionCourse> masterDegreeExecutionCourses = new ArrayList<ExecutionCourse>();

        for (ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.isMasterDegreeDFAOrDEAOnly() == masterDegreeOnly) {
                masterDegreeExecutionCourses.add(executionCourse);
            }
        }

        return masterDegreeExecutionCourses;
    }

    // Service Invokers migrated from Berserk

    private static final ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType serviceInstance =
            new ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType();

    @Atomic
    public static List<ExecutionCourse> runReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType(
            String teacherID, String executionYearID, DegreeType degreeType) throws FenixServiceException, NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(teacherID, executionYearID, degreeType);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(teacherID, executionYearID, degreeType);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}