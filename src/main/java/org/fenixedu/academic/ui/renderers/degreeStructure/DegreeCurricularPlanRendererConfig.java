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
package org.fenixedu.academic.ui.renderers.degreeStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.util.Pair;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeCurricularPlanRendererConfig implements Serializable {

    static private final long serialVersionUID = 1L;
    static private final String DEFAULT_DEGREE_MODULE_ID_ATTRIBUTE_NAME = "degreeModuleOid";

    private DegreeCurricularPlan degreeCurricularPlan;
    private ExecutionYear executionInterval;

    private OrganizeType organizeBy = OrganizeType.GROUPS;
    private boolean showRules = false;
    private boolean showCourses = true;

    private String degreeModuleIdAttributeName = DEFAULT_DEGREE_MODULE_ID_ATTRIBUTE_NAME;

    private boolean curricularCourseLinkable = false;
    private String viewCurricularCourseUrl = null;
    private final List<Pair<String, String>> viewCurricularCourseUrlParameters = new ArrayList<Pair<String, String>>();

    public DegreeCurricularPlanRendererConfig() {
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return this.degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public ExecutionYear getExecutionInterval() {
        return this.executionInterval;
    }

    public void setExecutionInterval(ExecutionYear executionInterval) {
        this.executionInterval = executionInterval;
    }

    public OrganizeType getOrganizeBy() {
        return organizeBy;
    }

    public void setOrganizeBy(OrganizeType organizeBy) {
        this.organizeBy = organizeBy;
    }

    public boolean organizeByGroups() {
        return getOrganizeBy() == OrganizeType.GROUPS;
    }

    public boolean organizeByYears() {
        return getOrganizeBy() == OrganizeType.YEARS;
    }

    public boolean isShowRules() {
        return showRules;
    }

    public void setShowRules(boolean showRules) {
        this.showRules = showRules;
    }

    public boolean isShowCourses() {
        return showCourses;
    }

    public void setShowCourses(boolean showCourses) {
        this.showCourses = showCourses;
    }

    private boolean hasAnyExecutionDegree() {
        return !getDegreeCurricularPlan().getExecutionDegreesSet().isEmpty();
    }

    private Collection<ExecutionDegree> getExecutionDegrees() {
        return getDegreeCurricularPlan().getExecutionDegreesSet();
    }

    private ExecutionYear getDegreeMinimumExecutionYear() {
        return getDegreeCurricularPlan().getRoot().getMinimumExecutionPeriod().getExecutionYear();
    }

    public String getDegreeModuleIdAttributeName() {
        return degreeModuleIdAttributeName;
    }

    public void setDegreeModuleIdAttributeName(String degreeModuleIdAttributeName) {
        this.degreeModuleIdAttributeName = degreeModuleIdAttributeName;
    }

    public boolean isCurricularCourseLinkable() {
        return curricularCourseLinkable;
    }

    private void setCurricularCourseLinkable(boolean value) {
        this.curricularCourseLinkable = value;
    }

    public String getViewCurricularCourseUrl() {
        return viewCurricularCourseUrl;
    }

    public void setViewCurricularCourseUrl(final String url) {
        viewCurricularCourseUrl = url;
        setCurricularCourseLinkable(true);
    }

    public List<Pair<String, String>> getViewCurricularCourseUrlParameters() {
        return viewCurricularCourseUrlParameters;
    }

    public void addViewCurricularCourseUrlParameter(final String key, final String value) {
        viewCurricularCourseUrlParameters.add(new Pair<String, String>(key, value));
        setCurricularCourseLinkable(true);
    }

    private List<ExecutionYear> getSortedExecutionYearsFromExecutionDegrees() {
        final List<ExecutionYear> result = new ArrayList<ExecutionYear>();
        for (final ExecutionDegree executionDegree : getExecutionDegrees()) {
            result.add(executionDegree.getExecutionYear());
        }

        Collections.sort(result);
        return result;
    }

    static public enum OrganizeType {
        GROUPS, YEARS;
    }

    static public class ExecutionIntervalProvider implements DataProvider {

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            final DegreeCurricularPlanRendererConfig config = (DegreeCurricularPlanRendererConfig) source;
            return (config.hasAnyExecutionDegree()) ? config.getSortedExecutionYearsFromExecutionDegrees() : Collections
                    .singletonList(config.getDegreeMinimumExecutionYear());

        }

    }

}
