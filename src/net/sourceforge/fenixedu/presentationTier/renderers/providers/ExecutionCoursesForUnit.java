package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilancyCourseGroupBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class ExecutionCoursesForUnit implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        VigilancyCourseGroupBean bean = (VigilancyCourseGroupBean) source;

        Unit unit = bean.getSelectedUnit();
        Department department = bean.getSelectedDepartment();
        Unit competenceCourseGroup = bean.getSelectedCompetenceCourseGroupUnit();

        ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
        List<ExecutionCourse> courses = new ArrayList<ExecutionCourse>();
        if (unit == null) {
            // Add pre-bolonha competenceCourses
            courses.addAll(getExecutionCoursesFromCompetenceCourses(department.getCompetenceCourses()));
            unit = department.getDepartmentUnit();
        }

        // Add bolonha competenceCourses
        courses.addAll((competenceCourseGroup == null) ? getBolonhaCourses(unit)
                : getBolonhaCoursesForGivenGroup(competenceCourseGroup));

        Collections.sort(courses, new BeanComparator("nome"));
        return courses;
    }

    private List<ExecutionCourse> getBolonhaCoursesForGivenGroup(Unit competenceCourseGroup) {
        return getExecutionCoursesFromCompetenceCourses(competenceCourseGroup.getCompetenceCourses());
    }

    private List<ExecutionCourse> getBolonhaCourses(Unit unit) {

        List<Unit> courseGroups = new ArrayList<Unit>();
        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        if (unit.isDepartmentUnit()) {
            List<Unit> scientificAreaUnits = unit.getScientificAreaUnits();
            for (Unit areaUnit : scientificAreaUnits) {
                courseGroups.addAll(areaUnit.getCompetenceCourseGroupUnits());
            }
        } else {
            courseGroups.addAll(unit.getCompetenceCourseGroupUnits());
        }

        for (Unit courseGroup : courseGroups) {
            executionCourses.addAll(getBolonhaCoursesForGivenGroup(courseGroup));
        }

        return executionCourses;
    }

    private List<ExecutionCourse> getExecutionCoursesFromCompetenceCourses(
            List<CompetenceCourse> competenceCourses) {
        List<ExecutionCourse> courses = new ArrayList<ExecutionCourse>();
        ExecutionPeriod period = ExecutionPeriod.readActualExecutionPeriod();
        for (CompetenceCourse course : competenceCourses) {
            courses.addAll(course.getExecutionCoursesByExecutionPeriod(period));
        }
        return courses;
    }

    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}
