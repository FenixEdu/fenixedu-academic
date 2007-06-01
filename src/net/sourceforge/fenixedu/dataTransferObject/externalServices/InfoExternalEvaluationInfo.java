/*
 * Created on 4:10:39 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 4:10:39 PM, Mar 11, 2005
 */
public class InfoExternalEvaluationInfo
{

    private String gradeValue;    
    /**
     * @return Returns the grade.
     */
    public String getGradeValue()
    {
        return this.gradeValue;
    }
    /**
     * @param grade The grade to set.
     */
    public void setGradeValue(String grade)
    {
        this.gradeValue = grade;
    }
    
    public static InfoExternalEvaluationInfo newFromEvaluation(EnrolmentEvaluation evaluation)
    {
        InfoExternalEvaluationInfo info = new InfoExternalEvaluationInfo();
        info.setGradeValue(evaluation.getGradeValue());
        return info;
    }
}
