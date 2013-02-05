package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Tânia Pousão Create on 3/Dez/2003
 */
public class CourseOfTheExpectedDegree extends FenixService {

    @Service
    public static Boolean run(Integer curricularCourseCode, String degreeCode) throws FenixServiceException {
        return Boolean.valueOf(CurricularCourseDegree(curricularCourseCode, degreeCode)
                && CurricularCourseNotBasic(curricularCourseCode));
    }

    private static boolean CurricularCourseDegree(Integer curricularCourseCode, String degreeCode) {
        CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);
        Degree degree = curricularCourse.getDegreeCurricularPlan().getDegree();
        return degree.getSigla().equals(degreeCode);
    }

    private static boolean CurricularCourseNotBasic(Integer curricularCourseCode) {
        CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);
        return curricularCourse.getBasic() == Boolean.FALSE;
    }

}