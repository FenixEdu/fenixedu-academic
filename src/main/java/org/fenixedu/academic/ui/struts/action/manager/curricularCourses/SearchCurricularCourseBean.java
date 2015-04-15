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
package org.fenixedu.academic.ui.struts.action.manager.curricularCourses;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.RootCourseGroup;
import org.fenixedu.academic.util.MultiLanguageString;

public class SearchCurricularCourseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final DegreeCurricularPlan degreeCurricularPlan;
    private ExecutionYear beginExecutionYear;
    private ExecutionYear endExecutionYear;
    private String name;

    public SearchCurricularCourseBean(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public Set<Context> search() {
        Set<Context> result = new HashSet<Context>();
        RootCourseGroup root = getDegreeCurricularPlan().getRoot();
        searchRecursive(root, result);

        return result;
    }

    private void searchRecursive(final CourseGroup courseGroup, Set<Context> result) {
        Collection<Context> childContexts = courseGroup.getChildContextsSet();

        for (Context context : childContexts) {
            DegreeModule childDegreeModule = context.getChildDegreeModule();

            if (childDegreeModule.isCourseGroup()) {
                searchRecursive((CourseGroup) childDegreeModule, result);
                continue;
            }

            if (getBeginExecutionYear() != null
                    && context.getBeginExecutionPeriod().getExecutionYear().isBefore(getBeginExecutionYear())) {
                continue;
            }

            if (getEndExecutionYear() != null
                    && context.getEndExecutionPeriod().getExecutionYear().isAfter(getEndExecutionYear())) {
                continue;
            }

            MultiLanguageString nameI18N = childDegreeModule.getNameI18N();
            Collection<String> allContents = nameI18N.getAllContents();

            Pattern searchPattern = getSearchRegex();

            for (String degreeModuleName : allContents) {
                if (searchPattern.matcher(degreeModuleName).matches()) {
                    result.add(context);
                }
            }
        }
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pattern getSearchRegex() {
        String[] split = getName().split("\\s+");

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < split.length; i++) {
            String compound = split[i];
            sb.append("(.*").append(compound).append(".*)");

            if (i < split.length - 1) {
                sb.append("(\\s+)");
            }
        }

        Pattern searchPattern = Pattern.compile(sb.toString());
        return searchPattern;
    }

    public ExecutionYear getBeginExecutionYear() {
        return beginExecutionYear;
    }

    public void setBeginExecutionYear(ExecutionYear beginExecutionYear) {
        this.beginExecutionYear = beginExecutionYear;
    }

    public ExecutionYear getEndExecutionYear() {
        return endExecutionYear;
    }

    public void setEndExecutionYear(ExecutionYear endExecutionYear) {
        this.endExecutionYear = endExecutionYear;
    }
}
