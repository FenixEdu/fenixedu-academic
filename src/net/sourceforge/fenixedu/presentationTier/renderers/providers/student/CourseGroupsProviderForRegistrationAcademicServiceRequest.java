package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CourseGroupsProviderForRegistrationAcademicServiceRequest implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final RegistrationSelectExecutionYearBean bean = ((RegistrationSelectExecutionYearBean) source);
	final DegreeCurricularPlan degreeCurricularPlan = bean.getRegistration().getLastDegreeCurricularPlan();
	final Set<CourseGroup> courseGroups = degreeCurricularPlan.getAllCoursesGroups();
	courseGroups.removeAll(degreeCurricularPlan.getRoot().getCycleCourseGroups());
	courseGroups.remove(degreeCurricularPlan.getRoot());
	return courseGroups;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
