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

/**
 * @author Fernanda Quitério
 */
public class PublishMarks {

    public static final String MARKS_PUBLISHED_SIGNAL = "academic.PublishMarks.marks.published";

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

}