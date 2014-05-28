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
package net.sourceforge.fenixedu.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public abstract class DegreeModuleScope {

    private static final String KEY_SEPARATOR = ":";
    public static final Comparator<DegreeModuleScope> COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME =
            new Comparator<DegreeModuleScope>() {

                @Override
                public int compare(DegreeModuleScope o1, DegreeModuleScope o2) {
                    final int cy = o1.getCurricularYear().compareTo(o2.getCurricularYear());
                    if (cy != 0) {
                        return cy;
                    }
                    final int cs = o1.getCurricularSemester().compareTo(o2.getCurricularSemester());
                    if (cs != 0) {
                        return cs;
                    }
                    final int cn =
                            Collator.getInstance()
                                    .compare(o1.getCurricularCourse().getName(), o2.getCurricularCourse().getName());
                    if (cn != 0) {
                        return cn;
                    }
                    return o1.getExternalId().compareTo(o2.getExternalId());
                }

            };

    public static final Comparator<DegreeModuleScope> COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME_AND_BRANCH =
            new Comparator<DegreeModuleScope>() {

                @Override
                public int compare(DegreeModuleScope o1, DegreeModuleScope o2) {
                    final int cy = o1.getCurricularYear().compareTo(o2.getCurricularYear());
                    if (cy != 0) {
                        return cy;
                    }
                    final int cs = o1.getCurricularSemester().compareTo(o2.getCurricularSemester());
                    if (cs != 0) {
                        return cs;
                    }
                    final int cn =
                            Collator.getInstance()
                                    .compare(o1.getCurricularCourse().getName(), o2.getCurricularCourse().getName());
                    if (cn != 0) {
                        return cn;
                    }
                    final int cb = Collator.getInstance().compare(o1.getBranch(), o2.getBranch());
                    if (cb != 0) {
                        return cb;
                    }
                    final int cc = o1.getCurricularCourse().getExternalId().compareTo(o2.getCurricularCourse().getExternalId());
                    if (cc == 0) {
                        return cc;
                    }
                    return o1.getExternalId().compareTo(o2.getExternalId());
                }

            };

    public static final Comparator<DegreeModuleScope> COMPARATOR_BY_NAME = new Comparator<DegreeModuleScope>() {

        @Override
        public int compare(DegreeModuleScope o1, DegreeModuleScope o2) {
            final int c = o1.getCurricularCourse().getName().compareTo(o2.getCurricularCourse().getName());
            return c == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : c;
        }

    };

    public abstract String getClassName();

    public abstract String getExternalId();

    public abstract Integer getCurricularSemester();

    public abstract Integer getCurricularYear();

    public abstract String getBranch();

    public abstract String getAnotation();

    public abstract CurricularCourse getCurricularCourse();

    @Deprecated
    public abstract boolean isActiveForExecutionPeriod(ExecutionSemester executionSemester);

    public abstract boolean isActiveForAcademicInterval(AcademicInterval academicInterval);

    public static List<DegreeModuleScope> getDegreeModuleScopes(WrittenEvaluation writtenEvaluation) {
        return getDegreeModuleScopes(writtenEvaluation.getAssociatedCurricularCourseScope(),
                writtenEvaluation.getAssociatedContexts());
    }

    public static List<DegreeModuleScope> getDegreeModuleScopes(CurricularCourse curricularCourse) {
        return getDegreeModuleScopes(curricularCourse.getScopes(), curricularCourse.getParentContexts());
    }

    private static List<DegreeModuleScope> getDegreeModuleScopes(Collection<CurricularCourseScope> curricularCourseScopes,
            Collection<Context> contexts) {
        List<DegreeModuleScope> degreeModuleScopes = new ArrayList<DegreeModuleScope>();
        for (CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
            degreeModuleScopes.add(curricularCourseScope.getDegreeModuleScopeCurricularCourseScope());
        }
        for (Context context : contexts) {
            degreeModuleScopes.add(context.getDegreeModuleScopeContext());
        }
        return degreeModuleScopes;
    }

    public boolean isActiveForExecutionYear(ExecutionYear executionYear) {
        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            if (isActiveForExecutionPeriod(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    public boolean isActive() {
        return isActiveForExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
    }

    public boolean isActive(int year, int semester) {
        return getCurricularYear().intValue() == year && getCurricularSemester().intValue() == semester;
    }

    public boolean isFirstSemester() {
        return (this.getCurricularSemester().intValue() == 1);
    }

    public boolean isSecondSemester() {
        return (this.getCurricularSemester().intValue() == 2);
    }

    public String getKey() {
        return getExternalId() + KEY_SEPARATOR + getClassName();
    }

    public static String getKey(String externalId, String className) {
        return externalId + KEY_SEPARATOR + className;
    }

    public static DegreeModuleScope getDegreeModuleScopeByKey(String key) {
        String[] split = key.split(KEY_SEPARATOR);
        if (split.length == 2) {
            String externalId = split[0];
            String className = split[1];
            try {
                Class clazz = Class.forName(className);
                DomainObject domainObject = FenixFramework.getDomainObject(externalId);
                if (domainObject != null && domainObject instanceof CurricularCourseScope) {
                    return ((CurricularCourseScope) domainObject).getDegreeModuleScopeCurricularCourseScope();
                }
                if (domainObject != null && domainObject instanceof Context) {
                    return ((Context) domainObject).getDegreeModuleScopeContext();
                }
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        return null;
    }

    public LabelFormatter getDescription() {
        return new LabelFormatter(getCurricularYear().toString()).appendLabel("º ")
                .appendLabel("label.curricular.year", LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ")
                .appendLabel(getCurricularSemester().toString()).appendLabel("º ")
                .appendLabel("label.semester.short", LabelFormatter.APPLICATION_RESOURCES);

    }
}
