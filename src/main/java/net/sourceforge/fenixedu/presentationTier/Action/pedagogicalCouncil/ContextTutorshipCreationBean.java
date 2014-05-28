/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * Class ContextTutorshipCreationBean.java
 * 
 * @author jaime created on Aug 4, 2010
 */

public class ContextTutorshipCreationBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int YEAR = 1;
    private static final int SEMESTER = 1;
    private ExecutionDegree degree;
    private ExecutionSemester executionSemester;
    private ExecutionCourse executionCourse;
    private Shift shift;

    /**
     * Only provides the first semester of every year
     * 
     * @author jaime
     * 
     */
    public static class ExecutionSemestersProvider implements DataProvider {
        @Override
        public Object provide(Object source, Object currentValue) {
            List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>();
            for (ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
                if (executionSemester.isFor(SEMESTER)) {
                    executionSemesters.add(executionSemester);
                }
            }
            Collections.sort(executionSemesters, new ReverseComparator());
            return executionSemesters;
        }

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }
    }

    public static class ContextDegreesProvider implements DataProvider {

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object source, Object arg1) {
            final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();
            final ContextTutorshipCreationBean bean = (ContextTutorshipCreationBean) source;
            final ExecutionSemester executionPeriod = bean.getExecutionSemester();
            if (executionPeriod != null) {
                final ExecutionYear executionYear = executionPeriod.getExecutionYear();
                for (ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
                    DegreeType degreeType = executionDegree.getDegreeType();
                    if (degreeType.compareTo(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) == 0
                            || degreeType.compareTo(DegreeType.BOLONHA_DEGREE) == 0) {
                        executionDegrees.add(executionDegree);
                    }
                }

            }
            Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
            return executionDegrees;
        }
    }

    /**
     * Gets all the courses in the first curricular year of the degree
     * 
     * @author jaime
     * 
     */
    public static class CoursesProvider implements DataProvider {
        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object source, Object current) {

            ContextTutorshipCreationBean bean = (ContextTutorshipCreationBean) source;
            ExecutionDegree executionDegree = bean.getExecutionDegree();

            ExecutionSemester executionSemester = bean.getExecutionSemester();
            Set<ExecutionCourse> courses = new HashSet<ExecutionCourse>();

            if (executionDegree != null && executionSemester != null) {
                Degree degree = executionDegree.getDegree();
                ExecutionYear executionYear = executionSemester.getExecutionYear();
                courses.addAll(getExecutionCourses(degree, executionYear, executionSemester));
            }
            return courses;
        }
    }

    public static class ShiftsProvider implements DataProvider {
        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object source, Object current) {
            ContextTutorshipCreationBean bean = (ContextTutorshipCreationBean) source;
            ExecutionCourse executionCourse = bean.getExecutionCourse();
            if (executionCourse != null) {
                Set<Shift> shifts = executionCourse.getAssociatedShifts();
                return shifts;
            } else {
                Set<Shift> shifts = new HashSet<Shift>();
                return shifts;
            }
        }
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public ExecutionDegree getExecutionDegree() {
        return degree;
    }

    public void setExecutionDegree(ExecutionDegree degree) {
        this.degree = degree;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    /**
     * Code functionality copy to fill specific purpose of getting execution
     * courses in a given Year and Semester
     * 
     * @param degree
     * @param curricularYear
     * @param executionSemester
     * @return
     */
    public static List<ExecutionCourse> getExecutionCourses(final Degree degree, final ExecutionYear execYear,
            final ExecutionSemester executionSemester) {
        final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
            for (final CurricularCourse course : degreeCurricularPlan.getCurricularCourses()) {
                for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
                    if (executionSemester == executionCourse.getExecutionPeriod()) {
                        for (final DegreeModuleScope scope : course.getDegreeModuleScopes()) {
                            String fields[] = execYear.getYear().split("/");
                            if (scope.isActiveForExecutionPeriod(executionSemester) && scope.isActiveForExecutionYear(execYear)
                                    && scope.getCurricularSemester() == executionSemester.getSemester()
                                    && scope.getCurricularYear() == YEAR) {
                                result.add(executionCourse);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
