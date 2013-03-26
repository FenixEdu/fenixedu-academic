package net.sourceforge.fenixedu.presentationTier.Action.teacher.evaluation;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.teacher.evaluation.CurricularEvaluation;
import net.sourceforge.fenixedu.domain.teacher.evaluation.NoEvaluation;
import net.sourceforge.fenixedu.domain.teacher.evaluation.RadistEvaluation;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluation;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationType;
import pt.ist.fenixWebFramework.services.Service;

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

    @Service
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
