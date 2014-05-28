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

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.MergeExecutionCourseDA.DegreesMergeBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object current) {

        final DegreesMergeBean degreesMergeBean = (DegreesMergeBean) source;

        List<Degree> result = Degree.readAllByDegreeCode(Degree.DEFAULT_MINISTRY_CODE);
        Collections.sort(result, Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

        return result;
    }

    @Override
    public Converter getConverter() {
        // TODO Auto-generated method stub
        return null;
    }
}
