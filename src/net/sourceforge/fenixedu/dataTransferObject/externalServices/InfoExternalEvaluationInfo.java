/*
 * Created on 4:10:39 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 4:10:39 PM, Mar 11, 2005
 */
public class InfoExternalEvaluationInfo
{

    private String grade;    
    /**
     * @return Returns the grade.
     */
    public String getGrade()
    {
        return this.grade;
    }
    /**
     * @param grade The grade to set.
     */
    public void setGrade(String grade)
    {
        this.grade = grade;
    }
    
    public static InfoExternalEvaluationInfo newFromEvaluation(IEnrolmentEvaluation evaluation)
    {
        InfoExternalEvaluationInfo info = new InfoExternalEvaluationInfo();
        info.setGrade(evaluation.getGrade());
        return info;
    }
}
