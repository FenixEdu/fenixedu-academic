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

public class NoEvaluation extends NoEvaluation_Base {
    public NoEvaluation(TeacherEvaluationProcess process) {
        super();
        setTeacherEvaluationProcess(process);
    }

    @Override
    public TeacherEvaluationType getType() {
        return TeacherEvaluationType.NO_EVALUATION;
    }

    @Override
    public Set<TeacherEvaluationFileType> getAutoEvaluationFileSet() {
        return new HashSet<TeacherEvaluationFileType>();
    }

    @Override
    public Set<TeacherEvaluationFileType> getEvaluationFileSet() {
        return new HashSet<TeacherEvaluationFileType>();
    }

    @Override
    protected void internalLickingBusiness() {
        super.internalLickingBusiness();
        super.lickEvaluationStamp();
        setEvaluationMark(TeacherEvaluationMark.GOOD);
    }

    @Override
    public String getFilenameTypePrefix() {
        return "";
    }

    @Override
    public void copyAutoEvaluation() {
        NoEvaluation copy = new NoEvaluation(getTeacherEvaluationProcess());
        internalCopyAutoEvaluation(copy);
    }
}
