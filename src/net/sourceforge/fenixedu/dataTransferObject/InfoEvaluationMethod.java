/*
 * Created on 23/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * @author João Mota
 * 
 *  
 */
public class InfoEvaluationMethod extends InfoObject implements ISiteComponent {

    private InfoExecutionCourse infoExecutionCourse;

    private MultiLanguageString evaluationElements;

    public InfoEvaluationMethod() {
    }

    public InfoEvaluationMethod(InfoExecutionCourse infoExecutionCourse) {
        setInfoExecutionCourse(infoExecutionCourse);
    }

    public InfoEvaluationMethod(InfoExecutionCourse infoExecutionCourse, MultiLanguageString evaluationElements) {
        setInfoExecutionCourse(infoExecutionCourse);
        setEvaluationElements(evaluationElements);
    }

    public MultiLanguageString getEvaluationElements() {
        return evaluationElements;
    }

    /**
     * @return
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    public void setEvaluationElements(MultiLanguageString string) {
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

    public void copyFromDomain(EvaluationMethod evaluationMethod) {
        super.copyFromDomain(evaluationMethod);
        if (evaluationMethod != null) {
            setEvaluationElements(evaluationMethod.getEvaluationElements());
        }
    }

    public static InfoEvaluationMethod newInfoFromDomain(EvaluationMethod evaluationMethod) {
        InfoEvaluationMethod infoEvaluationMethod = null;
        if (evaluationMethod != null) {
            infoEvaluationMethod = new InfoEvaluationMethod();
            infoEvaluationMethod.copyFromDomain(evaluationMethod);
        }
        return infoEvaluationMethod;
    }
}