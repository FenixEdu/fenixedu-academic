package net.sourceforge.fenixedu.domain;

/**
 * @author Tânia Pousão
 *  
 */
public class EvaluationExecutionCourse extends DomainObject implements IEvalutionExecutionCourse {

    private IEvaluation evaluation;

    private IExecutionCourse executionCourse;

    private Integer keyEvaluation;

    private Integer keyExecutionCourse;

    public EvaluationExecutionCourse() {
    }

    public EvaluationExecutionCourse(IEvaluation evaluation, IExecutionCourse executionCourse) {
        this.setEvaluation(evaluation);
        this.setExecutionCourse(executionCourse);
    }

    public boolean equals(Object obj) {
        if (obj instanceof EvaluationExecutionCourse) {
            EvaluationExecutionCourse evaluationObj = (EvaluationExecutionCourse) obj;
            return this.getEvaluation().equals(evaluationObj.getEvaluation())
                    && this.getExecutionCourse().equals(evaluationObj.getExecutionCourse());
        }

        return false;
    }

    public String toString() {
        return "[EVALUATION_EXECUTIONCOURSE:" + " evaluation= '" + this.getEvaluation() + "'"
                + " execution_course= '" + this.getExecutionCourse() + "'" + "]";
    }

    /**
     * @return
     */
    public IEvaluation getEvaluation() {
        return evaluation;
    }

    /**
     * @return
     */
    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    /**
     * @return
     */
    public Integer getKeyEvaluation() {
        return keyEvaluation;
    }

    /**
     * @return
     */
    public Integer getKeyExecutionCourse() {
        return keyExecutionCourse;
    }

    /**
     * @param evaluation
     */
    public void setEvaluation(IEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    /**
     * @param execucao
     */
    public void setExecutionCourse(IExecutionCourse execucao) {
        executionCourse = execucao;
    }

    /**
     * @param integer
     */
    public void setKeyEvaluation(Integer integer) {
        keyEvaluation = integer;
    }

    /**
     * @param integer
     */
    public void setKeyExecutionCourse(Integer integer) {
        keyExecutionCourse = integer;
    }

}