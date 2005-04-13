package net.sourceforge.fenixedu.domain;

/**
 * @author Fernanda Quitério created on 16/06/2004
 */
public class WrittenEvaluationCurricularCourseScope extends WrittenEvaluationCurricularCourseScope_Base {

    private IWrittenEvaluation writtenEvaluation;

    private ICurricularCourseScope curricularCourseScope;

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

    /**
     * @return
     */
    public IWrittenEvaluation getWrittenEvaluation() {
        return writtenEvaluation;
    }

    /**
     * @return
     */
    public ICurricularCourseScope getCurricularCourseScope() {
        return curricularCourseScope;
    }

    /**
     * @param writtenEvaluation
     */
    public void setWrittenEvaluation(IWrittenEvaluation writtenEvaluation) {
        this.writtenEvaluation = writtenEvaluation;
    }

    /**
     * @param curricularCourseScope
     */
    public void setCurricularCourseScope(ICurricularCourseScope curricularCourseScope) {
        this.curricularCourseScope = curricularCourseScope;
    }

}