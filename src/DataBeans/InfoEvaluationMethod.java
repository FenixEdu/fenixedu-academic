/*
 * Created on 23/Abr/2003
 *
 * 
 */
package DataBeans;

import Dominio.IEvaluationMethod;

/**
 * @author João Mota
 * 
 *  
 */
public class InfoEvaluationMethod extends InfoObject implements ISiteComponent {

    private InfoExecutionCourse infoExecutionCourse;

    private String evaluationElements;

    private String evaluationElementsEn;

    public InfoEvaluationMethod() {
    }

    public InfoEvaluationMethod(InfoExecutionCourse infoExecutionCourse) {
        setInfoExecutionCourse(infoExecutionCourse);
    }

    public InfoEvaluationMethod(InfoExecutionCourse infoExecutionCourse,
            String evaluationElements, String evaluationElementsEn) {
        setInfoExecutionCourse(infoExecutionCourse);
        setEvaluationElements(evaluationElements);
        setEvaluationElementsEn(evaluationElementsEn);
    }

    /**
     * @return
     */
    public String getEvaluationElements() {
        return evaluationElements;
    }

    /**
     * @return
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    /**
     * @param string
     */
    public void setEvaluationElements(String string) {
        evaluationElements = string;
    }

    /**
     * @param course
     */
    public void setInfoExecutionCourse(InfoExecutionCourse course) {
        infoExecutionCourse = course;
    }

    public String toString() {
        String result = "[EvaluationMethod";

        result += ", evaluationElements =" + getEvaluationElements();
        result += ", evaluationElementsEn =" + getEvaluationElementsEn();
        result += ", executionCourse =" + getInfoExecutionCourse();
        result += "]";
        return result;
    }

    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof InfoEvaluationMethod) {
            result = getInfoExecutionCourse().equals(
                    ((InfoEvaluationMethod) arg0).getInfoExecutionCourse());
        }
        return result;
    }

    /**
     * @return
     */
    public String getEvaluationElementsEn() {
        return evaluationElementsEn;
    }

    /**
     * @param string
     */
    public void setEvaluationElementsEn(String string) {
        evaluationElementsEn = string;
    }

    public void copyFromDomain(IEvaluationMethod evaluationMethod) {
        super.copyFromDomain(evaluationMethod);
        if (evaluationMethod != null) {
            setEvaluationElements(evaluationMethod.getEvaluationElements());
            setEvaluationElementsEn(evaluationMethod.getEvaluationElementsEn());
        }
    }

    public static InfoEvaluationMethod newInfoFromDomain(
            IEvaluationMethod evaluationMethod) {
        InfoEvaluationMethod infoEvaluationMethod = null;
        if (evaluationMethod != null) {
            infoEvaluationMethod = new InfoEvaluationMethod();
            infoEvaluationMethod.copyFromDomain(evaluationMethod);
        }
        return infoEvaluationMethod;
    }
}