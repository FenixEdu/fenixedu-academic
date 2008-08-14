package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;

/**
 * @author Tânia Pousão Create on 3/Dez/2003
 */
public class CourseOfTheExpectedDegree extends Service {

    public Boolean run(Integer curricularCourseCode, String degreeCode) throws FenixServiceException {
	return Boolean.valueOf(CurricularCourseDegree(curricularCourseCode, degreeCode)
		&& CurricularCourseNotBasic(curricularCourseCode));
    }

    private boolean CurricularCourseDegree(Integer curricularCourseCode, String degreeCode) {
	CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);
	Degree degree = curricularCourse.getDegreeCurricularPlan().getDegree();
	return degree.getSigla().equals(degreeCode);
    }

    private boolean CurricularCourseNotBasic(Integer curricularCourseCode) {
	CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);
	return curricularCourse.getBasic() == Boolean.FALSE;
    }

}