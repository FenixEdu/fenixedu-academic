package net.sourceforge.fenixedu.domain;

/**
 * @author Fernanda Quitério created on 16/06/2004
 */
public class WrittenEvaluationCurricularCourseScope extends WrittenEvaluationCurricularCourseScope_Base {

    public String toString() {
        return "[WRITTEN EVALUATION CURRICULAR COURSE SCOPE:" + " WrittenEvaluation= '"
                + this.getWrittenEvaluation() + "'\n" + " CurricularCourseScope= '"
                + this.getCurricularCourseScope() + "'\n" + "";
    }

    public boolean equals(Object obj) {
        if (obj instanceof IWrittenEvaluationCurricularCourseScope) {
            final IWrittenEvaluationCurricularCourseScope writtenEvaluationCurricularCourseScope = (IWrittenEvaluationCurricularCourseScope) obj;
            return this.getIdInternal().equals(writtenEvaluationCurricularCourseScope.getIdInternal());
        }
        return false;
    }

}
