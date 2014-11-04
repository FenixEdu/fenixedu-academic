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
package org.fenixedu.academic.domain.teacher.evaluation;

import java.io.Serializable;
import java.util.Comparator;

import org.joda.time.DateTime;

public class TeacherEvaluationFileBean implements Serializable {
    public static final Comparator<TeacherEvaluationFileBean> COMPARATOR_BY_TYPE = new Comparator<TeacherEvaluationFileBean>() {
        @Override
        public int compare(TeacherEvaluationFileBean b1, TeacherEvaluationFileBean b2) {
            if (b1.getTeacherEvaluationFileType().compareTo(b2.getTeacherEvaluationFileType()) != 0) {
                return b1.getTeacherEvaluationFileType().compareTo(b2.getTeacherEvaluationFileType());
            } else if (b1.getTeacherEvaluationFile() != null && b2.getTeacherEvaluationFile() != null) {
                return b1.getTeacherEvaluationFile().getExternalId().compareTo(b2.getTeacherEvaluationFile().getExternalId());
            } else if (b1.getTeacherEvaluationFile() != null && b2.getTeacherEvaluationFile() == null) {
                return 1;
            } else {
                return -1;
            }
        }
    };

    private TeacherEvaluationFileType teacherEvaluationFileType;
    private TeacherEvaluationFile teacherEvaluationFile;
    private final TeacherEvaluation teacherEvaluation;

    public TeacherEvaluationFileBean(TeacherEvaluation teacherEvaluation, TeacherEvaluationFileType teacherEvaluationFileType) {
        this.teacherEvaluationFileType = teacherEvaluationFileType;
        this.teacherEvaluation = teacherEvaluation;
        for (TeacherEvaluationFile teacherEvaluationFile : teacherEvaluation.getTeacherEvaluationFileSet()) {
            if (teacherEvaluationFile.getTeacherEvaluationFileType().equals(getTeacherEvaluationFileType())
                    && (this.teacherEvaluationFile == null || this.teacherEvaluationFile.getUploadTime().isBefore(
                            teacherEvaluationFile.getUploadTime()))) {
                this.teacherEvaluationFile = teacherEvaluationFile;
            }
        }
    }

    public TeacherEvaluationFileType getTeacherEvaluationFileType() {
        return teacherEvaluationFileType;
    }

    public void setTeacherEvaluationFileType(TeacherEvaluationFileType teacherEvaluationFileType) {
        this.teacherEvaluationFileType = teacherEvaluationFileType;
    }

    public TeacherEvaluationFile getTeacherEvaluationFile() {
        return teacherEvaluationFile;
    }

    public void setTeacherEvaluationFile(TeacherEvaluationFile teacherEvaluationFile) {
        this.teacherEvaluationFile = teacherEvaluationFile;
    }

    public DateTime getTeacherEvaluationFileUploadDate() {
        return hasTeacherEvaluationFile() ? teacherEvaluationFile.getUploadTime() : null;
    }

    public boolean hasTeacherEvaluationFile() {
        return teacherEvaluationFile != null;
    }

    public boolean getCanUploadAutoEvaluationFile() {
        return teacherEvaluationFileType.isAutoEvaluationFile()
                && teacherEvaluation.getTeacherEvaluationProcess().isInAutoEvaluation();
    }

    public boolean getCanUploadEvaluationFile() {
        return !teacherEvaluationFileType.isAutoEvaluationFile()
                && teacherEvaluation.getTeacherEvaluationProcess().isInEvaluation();
    }

}
