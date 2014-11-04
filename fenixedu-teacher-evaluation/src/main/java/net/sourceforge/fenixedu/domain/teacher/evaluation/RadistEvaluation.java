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

import java.util.HashSet;
import java.util.Set;

public class RadistEvaluation extends RadistEvaluation_Base {
    public RadistEvaluation(TeacherEvaluationProcess process) {
        super();
        setTeacherEvaluationProcess(process);
    }

    @Override
    public TeacherEvaluationType getType() {
        return TeacherEvaluationType.RADIST;
    }

    @Override
    public Set<TeacherEvaluationFileType> getAutoEvaluationFileSet() {
        Set<TeacherEvaluationFileType> teacherEvaluationFileTypeSet = new HashSet<TeacherEvaluationFileType>();
        teacherEvaluationFileTypeSet.add(TeacherEvaluationFileType.AUTO_MULTI_CRITERIA_EXCEL);
        return teacherEvaluationFileTypeSet;
    }

    @Override
    public Set<TeacherEvaluationFileType> getEvaluationFileSet() {
        Set<TeacherEvaluationFileType> teacherEvaluationFileTypeSet = new HashSet<TeacherEvaluationFileType>();
        teacherEvaluationFileTypeSet.add(TeacherEvaluationFileType.MULTI_CRITERIA_EXCEL);
        return teacherEvaluationFileTypeSet;
    }

    @Override
    public String getFilenameTypePrefix() {
        return "mc";
    }

    @Override
    public void copyAutoEvaluation() {
        RadistEvaluation copy = new RadistEvaluation(getTeacherEvaluationProcess());
        internalCopyAutoEvaluation(copy);
    }
}
