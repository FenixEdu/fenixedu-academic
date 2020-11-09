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
package org.fenixedu.academic.service.services.teacher;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.BibliographicReference;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;

public class ImportBibliographicReferences {

    protected void run(String executionCourseToId, ExecutionCourse executionCourseTo, ExecutionCourse executionCourseFrom,
            Shift shift) {
        if (executionCourseTo != null && executionCourseFrom != null) {
            copyBibliographicReferencesFrom(executionCourseFrom, executionCourseTo);
        }
    }

    // Service Invokers migrated from Berserk

    private static final ImportBibliographicReferences serviceInstance = new ImportBibliographicReferences();

    @Atomic
    public static void runImportBibliographicReferences(String executionCourseToId, ExecutionCourse executionCourseTo,
            ExecutionCourse executionCourseFrom, Shift shift) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseToId);
        serviceInstance.run(executionCourseToId, executionCourseTo, executionCourseFrom, shift);
    }

    public static List<BibliographicReference> copyBibliographicReferencesFrom(final ExecutionCourse executionCourseFrom,
            final ExecutionCourse executionCourseTo) {
        final List<BibliographicReference> notCopiedBibliographicReferences = new ArrayList<BibliographicReference>();

        for (final BibliographicReference bibliographicReference : executionCourseFrom
                .getAssociatedBibliographicReferencesSet()) {
            if (canAddBibliographicReference(bibliographicReference, executionCourseTo)) {
                final BibliographicReference reference = BibliographicReference.create(bibliographicReference.getTitle(),
                        bibliographicReference.getAuthors(), bibliographicReference.getReference(),
                        bibliographicReference.getYear(), bibliographicReference.getOptional());
                reference.setExecutionCourse(executionCourseTo);
            } else {
                notCopiedBibliographicReferences.add(bibliographicReference);
            }
        }

        return notCopiedBibliographicReferences;
    }

    private static boolean canAddBibliographicReference(final BibliographicReference bibliographicReferenceToAdd,
            final ExecutionCourse executionCourseTo) {
        for (final BibliographicReference bibliographicReference : executionCourseTo.getAssociatedBibliographicReferencesSet()) {
            if (bibliographicReference.getTitle().equals(bibliographicReferenceToAdd.getTitle())) {
                return false;
            }
        }
        return true;
    }

}