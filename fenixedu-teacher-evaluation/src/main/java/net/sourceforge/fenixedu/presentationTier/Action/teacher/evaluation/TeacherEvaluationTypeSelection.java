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
package org.fenixedu.academic.ui.struts.action.teacher.evaluation;

import java.io.Serializable;

import org.fenixedu.academic.domain.teacher.evaluation.CurricularEvaluation;
import org.fenixedu.academic.domain.teacher.evaluation.NoEvaluation;
import org.fenixedu.academic.domain.teacher.evaluation.RadistEvaluation;
import org.fenixedu.academic.domain.teacher.evaluation.TeacherEvaluation;
import org.fenixedu.academic.domain.teacher.evaluation.TeacherEvaluationProcess;
import org.fenixedu.academic.domain.teacher.evaluation.TeacherEvaluationType;
import pt.ist.fenixframework.Atomic;

public class TeacherEvaluationTypeSelection implements Serializable {
    private final TeacherEvaluationProcess process;

    private TeacherEvaluationType type;

    public TeacherEvaluationTypeSelection(TeacherEvaluationProcess process) {
        this.process = process;
        TeacherEvaluation current = process.getCurrentTeacherEvaluation();
        if (current != null) {
            this.type = current.getType();
        }
    }

    public TeacherEvaluationProcess getProcess() {
        return process;
    }

    public void setType(TeacherEvaluationType type) {
        this.type = type;
    }

    public TeacherEvaluationType getType() {
        return type;
    }

    @Atomic
    public void createEvaluation() {
        switch (type) {
        case NO_EVALUATION:
            new NoEvaluation(process);
            break;
        case RADIST:
            new RadistEvaluation(process);
            break;
        case CURRICULAR:
            new CurricularEvaluation(process);
            break;
        }
    }
}
