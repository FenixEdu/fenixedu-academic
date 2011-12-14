package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments.SpecialSeasonStatusTrackerBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CompetenceCoursesForDepartmentProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	SpecialSeasonStatusTrackerBean bean = ((SpecialSeasonStatusTrackerBean)  source);
	final List<CompetenceCourse> courses = new ArrayList<CompetenceCourse>();
	if(bean != null && bean.getDepartment() != null) {
	    courses.addAll(bean.getDepartment().getBolonhaCompetenceCourses());
	    Collections.sort(courses, CompetenceCourse.COMPETENCE_COURSE_COMPARATOR_BY_NAME);
	}
	return courses;
    }

}
