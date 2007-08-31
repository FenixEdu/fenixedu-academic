package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class CurricularCoursePathsProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Object provide(Object source, Object currentValue) {
	SelectedCurricularCourse selectedCurricularCourse = (SelectedCurricularCourse) source;
	return selectedCurricularCourse.getStudentCurricularPlan().getCurricularCoursePossibleGroups(selectedCurricularCourse.getCurricularCourse());
    }

}
