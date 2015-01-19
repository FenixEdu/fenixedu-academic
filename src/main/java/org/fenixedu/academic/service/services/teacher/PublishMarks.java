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
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Mark;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.ExcepcaoInexistente;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.signals.Signal;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério
 */
public class PublishMarks {

    public static final String MARKS_PUBLISHED_SIGNAL = "academic.PublishMarks.marks.published";

    protected Object run(String executionCourseCode, String evaluationCode, String publishmentMessage, String announcementTitle)
            throws ExcepcaoInexistente, FenixServiceException {

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseCode);
        final Evaluation evaluation = FenixFramework.getDomainObject(evaluationCode);

        if (publishmentMessage == null || publishmentMessage.length() == 0) {
            evaluation.setPublishmentMessage(" ");
        } else {
            evaluation.setPublishmentMessage(publishmentMessage);
        }

        Signal.emit(MARKS_PUBLISHED_SIGNAL, new MarkPublishingBean(executionCourse, evaluation, announcementTitle));

        for (Mark mark : evaluation.getMarksSet()) {
            if (!mark.getMark().equals(mark.getPublishedMark())) {
                // update published mark
                mark.setPublishedMark(mark.getMark());
            }
        }

        return Boolean.TRUE;
    }

    public static class MarkPublishingBean {

        private final ExecutionCourse course;
        private final Evaluation evaluation;
        private final String title;

        public MarkPublishingBean(ExecutionCourse course, Evaluation evaluation, String title) {
            this.course = course;
            this.evaluation = evaluation;
            this.title = title;
        }

        public ExecutionCourse getCourse() {
            return course;
        }

        public Evaluation getEvaluation() {
            return evaluation;
        }

        public String getTitle() {
            return title;
        }

    }

    // Service Invokers migrated from Berserk

    private static final PublishMarks serviceInstance = new PublishMarks();

    @Atomic
    public static Object runPublishMarks(String executionCourseCode, String evaluationCode, String publishmentMessage,
            Boolean sendSMS, String announcementTitle) throws ExcepcaoInexistente, FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, evaluationCode, publishmentMessage, announcementTitle);
    }

}