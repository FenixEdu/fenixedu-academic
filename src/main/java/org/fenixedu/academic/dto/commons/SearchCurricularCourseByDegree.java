/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.dto.academicAdministration.DegreeByExecutionYearBean;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixframework.FenixFramework;

/**
 * This class provides the degrees for the StandaloneIndividualCandidacy
 */
public class SearchCurricularCourseByDegree implements Serializable {

    private ExecutionSemester executionSemester;
    private DegreeByExecutionYearBean degreeBean;
    private DegreeCurricularPlan degreeCurricularPlan;
    private CurricularCourseByExecutionSemesterBean curricularCourseBean;

    public SearchCurricularCourseByDegree(final ExecutionSemester executionSemester) {
        setExecutionSemester(executionSemester);
    }

    public ExecutionSemester getExecutionSemester() {
        return this.executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public DegreeByExecutionYearBean getDegreeBean() {
        return degreeBean;
    }

    public void setDegreeBean(DegreeByExecutionYearBean degreeBean) {
        this.degreeBean = degreeBean;
        setDegreeCurricularPlan(null);
        setCurricularCourseBean(null);
    }

    public boolean hasDegreeBean() {
        return getDegreeBean() != null;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return this.degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        if (hasDegreeBean() && getDegreeBean().getDegree().getDegreeCurricularPlansSet().contains(degreeCurricularPlan)) {
            this.degreeCurricularPlan = degreeCurricularPlan;
        } else {
            this.degreeCurricularPlan = null;
        }
        setCurricularCourseBean(null);
    }

    public void removeDegreeCurricularPlan() {
        this.degreeCurricularPlan = null;
    }

    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    public CurricularCourseByExecutionSemesterBean getCurricularCourseBean() {
        return curricularCourseBean;
    }

    public void setCurricularCourseBean(CurricularCourseByExecutionSemesterBean curricularCourseBean) {
        this.curricularCourseBean = curricularCourseBean;
    }

    public void removeCurricularCourseBean() {
        this.curricularCourseBean = null;
    }

    public boolean hasCurricularCourseBean() {
        return this.curricularCourseBean != null;
    }

    static public class SearchCurricularCourseBolonhaDegreesProvider implements DataProvider {

        @Override
        public Object provide(Object source, Object currentValue) {

            Set<AcademicProgram> programs = AcademicAccessRule
                    .getProgramsAccessibleToFunction(AcademicOperationType.MANAGE_INDIVIDUAL_CANDIDACIES, Authenticate.getUser())
                    .collect(Collectors.toSet());

            final SearchCurricularCourseByDegree bean = (SearchCurricularCourseByDegree) source;
            final List<DegreeByExecutionYearBean> result = new ArrayList<DegreeByExecutionYearBean>();

            for (final Degree degree : Degree.readBolonhaDegrees()) {
                if (programs.contains(degree)) {
                    result.add(new DegreeByExecutionYearBean(degree, bean.getExecutionSemester().getExecutionYear()));
                }
            }

            Collections.sort(result);
            return result;
        }

        @Override
        public Converter getConverter() {
            return new BiDirectionalConverter() {

                @Override
                public Object convert(Class type, Object value) {
                    String key = (String) value;
                    if (key == null || key.isEmpty()) {
                        return null;
                    }
                    final String[] values = key.split(":");
                    final Degree degree = FenixFramework.getDomainObject(values[0]);
                    final ExecutionYear year = FenixFramework.getDomainObject(values[1]);
                    return new DegreeByExecutionYearBean(degree, year);
                }

                @Override
                public String deserialize(Object object) {
                    return (object == null) ? "" : ((DegreeByExecutionYearBean) object).getKey();
                }
            };
        }
    }

    static public class SearchCurricularCourseDegreeCurricularPlansProvider implements DataProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            final SearchCurricularCourseByDegree bean = (SearchCurricularCourseByDegree) source;

            if (bean.hasDegreeBean()) {
                final List<DegreeCurricularPlan> result = getDegreeCurricularPlans(bean);
                Collections.sort(result, DegreeCurricularPlan.COMPARATOR_BY_NAME);
                return result;

            } else {
                return Collections.<DegreeCurricularPlan> emptyList();
            }
        }

        private List<DegreeCurricularPlan> getDegreeCurricularPlans(final SearchCurricularCourseByDegree bean) {
            return new ArrayList<DegreeCurricularPlan>(bean.getDegreeBean().getDegreeCurricularPlans());
        }

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }
    }

}
