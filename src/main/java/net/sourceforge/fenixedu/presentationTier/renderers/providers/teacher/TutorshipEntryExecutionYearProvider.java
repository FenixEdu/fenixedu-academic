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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsByTutorBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Tutorship;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class TutorshipEntryExecutionYearProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) source;
        return getExecutionYears(bean);
    }

    public static List<ExecutionYear> getExecutionYears(StudentsPerformanceInfoBean bean) {
        Set<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);
        for (Tutorship tutor : bean.getTutorships()) {
            if (bean.getDegree().equals(tutor.getStudentCurricularPlan().getRegistration().getDegree())) {
                executionYears.add(ExecutionYear.getExecutionYearByDate(tutor.getStudentCurricularPlan().getRegistration()
                        .getStartDate()));
            }
        }
        return new ArrayList<ExecutionYear>(executionYears);
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    public static class TutorshipEntryExecutionYearProviderForSingleStudent extends TutorshipEntryExecutionYearProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) source;
            return getExecutionYears(bean);
        }

        public static List<ExecutionYear> getExecutionYears(StudentsPerformanceInfoBean bean) {
            Set<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);
            for (Tutorship tutor : bean.getTutorshipsFromStudent()) {
                executionYears.add(ExecutionYear.getExecutionYearByDate(tutor.getStudentCurricularPlan().getRegistration()
                        .getStartDate()));
            }
            return new ArrayList<ExecutionYear>(executionYears);
        }
    }

    public static class TutorshipEntryExecutionYearProviderByTeacher extends TutorshipEntryExecutionYearProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) source;
            return getExecutionYears(bean);
        }

        public static List<ExecutionYear> getExecutionYears(StudentsPerformanceInfoBean bean) {
            Set<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);
            for (Tutorship tutor : bean.getTutorships()) {
                executionYears.add(ExecutionYear.getExecutionYearByDate(tutor.getStudentCurricularPlan().getRegistration()
                        .getStartDate()));
            }
            return new ArrayList<ExecutionYear>(executionYears);
        }
    }

    public static class ActiveTutorshipEntryExecutionYearProviderByTeacher implements DataProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            StudentsByTutorBean bean = (StudentsByTutorBean) source;
            return getExecutionYears(bean);
        }

        public static List<ExecutionYear> getExecutionYears(StudentsByTutorBean bean) {
            Set<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.COMPARATOR_BY_YEAR);
            for (Tutorship tutor : bean.getTeacher().getActiveTutorships()) {
                executionYears.add(ExecutionYear.getExecutionYearByDate(tutor.getStudentCurricularPlan().getRegistration()
                        .getStartDate()));
            }
            return new ArrayList<ExecutionYear>(executionYears);
        }

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }
    }
}
