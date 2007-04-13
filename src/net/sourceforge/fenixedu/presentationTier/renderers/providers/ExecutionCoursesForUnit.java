package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
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
        CompetenceCourseGroupUnit competenceCourseGroup = bean.getSelectedCompetenceCourseGroupUnit();


        Set<ExecutionCourse> courses = new HashSet<ExecutionCourse>();
        if (unit == null) {
            // Add pre-bolonha competenceCourses
            courses.addAll(getExecutionCoursesFromCompetenceCourses(department.getCompetenceCourses()));
            unit = department.getDepartmentUnit();
        }

        // Add bolonha competenceCourses
        courses.addAll((competenceCourseGroup == null) ? getBolonhaCourses(unit)
                : getBolonhaCoursesForGivenGroup(competenceCourseGroup));

        List<ExecutionCourse> coursesToProvide = new ArrayList<ExecutionCourse>(courses);
        Collections.sort(coursesToProvide, new BeanComparator("nome"));
        return coursesToProvide;
    }

    private List<ExecutionCourse> getBolonhaCoursesForGivenGroup(CompetenceCourseGroupUnit competenceCourseGroup) {
        return getExecutionCoursesFromCompetenceCourses(competenceCourseGroup.getCompetenceCourses());
    }

    private List<ExecutionCourse> getBolonhaCourses(Unit unit) {

        List<CompetenceCourseGroupUnit> courseGroups = new ArrayList<CompetenceCourseGroupUnit>();
        List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        if (unit.isDepartmentUnit()) {
            List<ScientificAreaUnit> scientificAreaUnits = ((DepartmentUnit)unit).getScientificAreaUnits();
            for (ScientificAreaUnit areaUnit : scientificAreaUnits) {
                courseGroups.addAll(areaUnit.getCompetenceCourseGroupUnits());
            }
        } else {
            courseGroups.addAll(((ScientificAreaUnit)unit).getCompetenceCourseGroupUnits());
        }

        for (CompetenceCourseGroupUnit courseGroup : courseGroups) {
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
