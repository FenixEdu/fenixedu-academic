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
/*
 * Created on Jul 26, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.applicationTier.utils;

import java.util.List;

import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

public class ExecutionCourseUtils {

    public static List<BibliographicReference> copyBibliographicReference(final ExecutionCourse executionCourseFrom,
            ExecutionCourse executionCourseTo) {
        return executionCourseTo.copyBibliographicReferencesFrom(executionCourseFrom);
    }

    public static void copyEvaluationMethod(final ExecutionCourse executionCourseFrom, ExecutionCourse executionCourseTo) {
        executionCourseTo.copyEvaluationMethodFrom(executionCourseFrom);
    }

}
