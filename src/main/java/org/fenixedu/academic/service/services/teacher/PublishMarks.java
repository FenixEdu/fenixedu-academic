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

import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.Mark;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.ExcepcaoInexistente;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.signals.DomainObjectEvent;
import org.fenixedu.bennu.signals.Signal;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério
 */
public class PublishMarks {

    public static final String MARKS_PUBLISHED_SIGNAL = "academic.PublishMarks.marks.published";

    protected Object run(String evaluationCode, String publishmentMessage, String announcementTitle) throws ExcepcaoInexistente,
            FenixServiceException {

        final Evaluation evaluation = FenixFramework.getDomainObject(evaluationCode);

        if (publishmentMessage == null || publishmentMessage.length() == 0) {
            evaluation.setPublishmentMessage(" ");
        } else {
            evaluation.setPublishmentMessage(publishmentMessage);
        }

        Signal.emit(MARKS_PUBLISHED_SIGNAL, new DomainObjectEvent<Evaluation>(evaluation));

        for (Mark mark : evaluation.getMarksSet()) {
            if (!mark.getMark().equals(mark.getPublishedMark())) {
                // update published mark
                mark.setPublishedMark(mark.getMark());
            }
        }

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final PublishMarks serviceInstance = new PublishMarks();

    @Atomic
    public static Object runPublishMarks(String executionCourseCode, String evaluationCode, String publishmentMessage,
            Boolean sendSMS, String announcementTitle) throws ExcepcaoInexistente, FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(evaluationCode, publishmentMessage, announcementTitle);
    }

}