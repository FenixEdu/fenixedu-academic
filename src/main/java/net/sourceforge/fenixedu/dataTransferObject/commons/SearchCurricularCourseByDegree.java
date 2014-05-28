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
package net.sourceforge.fenixedu.dataTransferObject.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.academicAdministration.DegreeByExecutionYearBean;
import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
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

            Set<AcademicProgram> programs =
                    AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_INDIVIDUAL_CANDIDACIES);

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

    static public class SearchCurricularCourseCurricularCoursesProvider implements DataProvider {

        @Override
        public Object provide(Object source, Object currentValue) {
            final SearchCurricularCourseByDegree bean = (SearchCurricularCourseByDegree) source;
            if (bean.hasDegreeBean() && bean.hasDegreeCurricularPlan()) {
                return getSortedCurricularCourses(bean);
            }
            return Collections.<DegreeCurricularPlan> emptyList();
        }

        private List<CurricularCourseByExecutionSemesterBean> getSortedCurricularCourses(final SearchCurricularCourseByDegree bean) {
            final DegreeCurricularPlan dcp = bean.getDegreeCurricularPlan();
            final List<CurricularCourseByExecutionSemesterBean> result = new ArrayList<CurricularCourseByExecutionSemesterBean>();

            for (final DegreeModule degreeModule : dcp.getDcpDegreeModules(CurricularCourse.class, bean.getExecutionSemester())) {
                result.add(new CurricularCourseByExecutionSemesterBean((CurricularCourse) degreeModule, bean
                        .getExecutionSemester()));
            }

            Collections.sort(result);
            return result;
        }

        @Override
        public Converter getConverter() {
            return new BiDirectionalConverter() {

                @Override
                public Object convert(Class type, Object value) {
                    return CurricularCourseByExecutionSemesterBean.buildFrom((String) value);
                }

                @Override
                public String deserialize(Object object) {
                    return (object == null) ? "" : ((CurricularCourseByExecutionSemesterBean) object).getKey();
                }
            };
        }
    }
}
