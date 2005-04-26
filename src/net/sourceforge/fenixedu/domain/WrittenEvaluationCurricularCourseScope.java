package net.sourceforge.fenixedu.domain;

/**
 * @author Fernanda Quitério created on 16/06/2004
 */
public class WrittenEvaluationCurricularCourseScope extends WrittenEvaluationCurricularCourseScope_Base {

    public WrittenEvaluationCurricularCourseScope() {
    }

    public boolean equals(Object obj) {
        return ((obj instanceof WrittenEvaluationCurricularCourseScope)
                && (((WrittenEvaluationCurricularCourseScope) obj).getCurricularCourseScope()
                        .equals(getCurricularCourseScope())) && (((WrittenEvaluationCurricularCourseScope) obj)
                .getWrittenEvaluation().equals(getWrittenEvaluation())));
    }

    public String toString() {
        return "[WRITTEN EVALUATION CURRICULAR COURSE SCOPE:" + " WrittenEvaluation= '"
                + this.getWrittenEvaluation() + "'\n" + " CurricularCourseScope= '"
                + this.getCurricularCourseScope() + "'\n" + "";
    }

}