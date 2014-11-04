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

public class CurricularEvaluation extends CurricularEvaluation_Base {
    public CurricularEvaluation(TeacherEvaluationProcess process) {
        super();
        setTeacherEvaluationProcess(process);
    }

    @Override
    public TeacherEvaluationType getType() {
        return TeacherEvaluationType.CURRICULAR;
    }

    @Override
    public Set<TeacherEvaluationFileType> getAutoEvaluationFileSet() {
        Set<TeacherEvaluationFileType> teacherEvaluationFileTypeSet = new HashSet<TeacherEvaluationFileType>();
        teacherEvaluationFileTypeSet.add(TeacherEvaluationFileType.AUTO_ACTIVITY_DESCRIPTION);
        teacherEvaluationFileTypeSet.add(TeacherEvaluationFileType.AUTO_CURRICULAR_EVALUATION_EXCEL);
        return teacherEvaluationFileTypeSet;
    }

    @Override
    public Set<TeacherEvaluationFileType> getEvaluationFileSet() {
        Set<TeacherEvaluationFileType> teacherEvaluationFileTypeSet = new HashSet<TeacherEvaluationFileType>();
        teacherEvaluationFileTypeSet.add(TeacherEvaluationFileType.CURRICULAR_EVALUATION_EXCEL);
        return teacherEvaluationFileTypeSet;
    }

    @Override
    public String getFilenameTypePrefix() {
        return "pc";
    }

    @Override
    public void copyAutoEvaluation() {
        CurricularEvaluation copy = new CurricularEvaluation(getTeacherEvaluationProcess());
        internalCopyAutoEvaluation(copy);
    }
}
