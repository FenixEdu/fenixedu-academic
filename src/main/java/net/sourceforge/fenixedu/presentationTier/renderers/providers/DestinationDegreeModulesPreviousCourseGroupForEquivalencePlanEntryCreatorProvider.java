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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry.EquivalencePlanEntryCreator;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixframework.FenixFramework;

public class DestinationDegreeModulesPreviousCourseGroupForEquivalencePlanEntryCreatorProvider implements DataProvider {

    private static class CourseGroupPair extends GenericPair<String, String> {

        public CourseGroupPair(CourseGroup courseGroup, String path) {
            super(courseGroup.getExternalId(), path);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof CourseGroup) {
                return getLeft().equals(((CourseGroup) obj).getExternalId());
            }

            return false;
        }

    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final List<GenericPair<String, String>> result = new ArrayList<GenericPair<String, String>>();
        for (final List<DegreeModule> degreeModules : getDegreeCurricularPlan(source).getDcpDegreeModulesIncludingFullPath(
                CourseGroup.class, null)) {
            result.add(new CourseGroupPair((CourseGroup) degreeModules.get(degreeModules.size() - 1), buildPath(degreeModules)));

        }

        return result;

    }

    protected DegreeCurricularPlan getDegreeCurricularPlan(final Object source) {

        final EquivalencePlanEntryCreator equivalencePlanEntryCreator = (EquivalencePlanEntryCreator) source;
        final DegreeCurricularPlanEquivalencePlan equivalencePlan =
                (DegreeCurricularPlanEquivalencePlan) equivalencePlanEntryCreator.getEquivalencePlan();

        return equivalencePlan.getDegreeCurricularPlan();

    }

    private String buildPath(final List<DegreeModule> degreeModules) {
        final StringBuilder result = new StringBuilder();

        final Iterator<DegreeModule> iterator = degreeModules.iterator();
        while (iterator.hasNext()) {
            final DegreeModule degreeModule = iterator.next();
            result.append(degreeModule.getName());
            if (iterator.hasNext()) {
                result.append(" > ");
            }
        }

        return result.toString();

    }

    @Override
    public Converter getConverter() {
        return new BiDirectionalConverter() {
            @Override
            public Object convert(Class type, Object value) {
                if (!StringUtils.isEmpty((String) value)) {
                    return FenixFramework.getDomainObject((String) value);
                }

                return null;
            }

            @Override
            public String deserialize(Object object) {
                if (object == null) {
                    return "";
                }

                final CourseGroupPair option = (CourseGroupPair) object;

                return option.getLeft();

            }
        };
    }
}
