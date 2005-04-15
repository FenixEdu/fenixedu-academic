/*
 * Created on 23/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author João Mota
 * 
 *  
 */
public class EvaluationMethod extends EvaluationMethod_Base {

    public EvaluationMethod() {
    }

    public EvaluationMethod(IExecutionCourse executionCourse) {
        setExecutionCourse(executionCourse);
    }

    public EvaluationMethod(IExecutionCourse executionCourse, String evaluationElements) {
        setExecutionCourse(executionCourse);
        setEvaluationElements(evaluationElements);
    }

    public EvaluationMethod(IExecutionCourse executionCourse, String evaluationElements,
            String evaluationElementsEn) {
        setExecutionCourse(executionCourse);
        setEvaluationElements(evaluationElements);
        setEvaluationElementsEn(evaluationElementsEn);
    }

    public String toString() {
        String result = "[EvaluationMethod";
        result += ", codInt=" + getIdInternal();
        result += ", evaluationElements =" + getEvaluationElements();
        result += ", executionCourse =" + getExecutionCourse();
        result += "]";
        return result;
    }

    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof IEvaluationMethod) {
            result = getExecutionCourse().equals(((IEvaluationMethod) arg0).getExecutionCourse());
        }
        return result;
    }
}