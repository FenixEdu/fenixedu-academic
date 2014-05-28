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
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditExercise {

    protected Boolean run(ExecutionCourse executionCourseId, String metadataId, String author, String description,
            String difficulty, Calendar learningTime, String level, String mainSubject, String secondarySubject)
            throws FenixServiceException {
        Metadata metadata = FenixFramework.getDomainObject(metadataId);
        if (metadata == null) {
            throw new InvalidArgumentsServiceException();
        }

        if (author != null) {
            metadata.setAuthor(author);
        }
        if (!difficulty.equals("-1")) {
            metadata.setDifficulty(difficulty);
        }
        metadata.setDescription(description);
        metadata.setLearningTime(learningTime);
        metadata.setLevel(level);
        metadata.setMainSubject(mainSubject);
        metadata.setSecondarySubject(secondarySubject);

        return true;
    }

    // Service Invokers migrated from Berserk

    private static final EditExercise serviceInstance = new EditExercise();

    @Atomic
    public static Boolean runEditExercise(ExecutionCourse executionCourseId, String metadataId, String author,
            String description, String difficulty, Calendar learningTime, String level, String mainSubject,
            String secondarySubject) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, metadataId, author, description, difficulty, learningTime, level,
                mainSubject, secondarySubject);
    }

}