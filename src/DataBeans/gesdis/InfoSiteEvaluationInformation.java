/*
 * Created on Feb 12, 2004
 *
 */
package DataBeans.gesdis;

import DataBeans.InfoCurricularCourse;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida</a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo</a>
 *
 */
public class InfoSiteEvaluationInformation
{
    private InfoCurricularCourse infoCurricularCourse;
    private Integer enrolled;
    private Integer evaluated;
    private Integer approved;
    
    /**
     * 
     */
    public InfoSiteEvaluationInformation()
    {
        super();
    }

    /**
     * @return Returns the approved.
     */
    public Integer getApproved()
    {
        return approved;
    }

    /**
     * @param approved The approved to set.
     */
    public void setApproved(Integer approved)
    {
        this.approved = approved;
    }

    /**
     * @return Returns the enrolled.
     */
    public Integer getEnrolled()
    {
        return enrolled;
    }

    /**
     * @param enrolled The enrolled to set.
     */
    public void setEnrolled(Integer enrolled)
    {
        this.enrolled = enrolled;
    }

    /**
     * @return Returns the evaluated.
     */
    public Integer getEvaluated()
    {
        return evaluated;
    }

    /**
     * @param evaluated The evaluated to set.
     */
    public void setEvaluated(Integer evaluated)
    {
        this.evaluated = evaluated;
    }

    /**
     * @return Returns the infoCurricularCourse.
     */
    public InfoCurricularCourse getInfoCurricularCourse()
    {
        return infoCurricularCourse;
    }

    /**
     * @param infoCurricularCourse The infoCurricularCourse to set.
     */
    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse)
    {
        this.infoCurricularCourse = infoCurricularCourse;
    }

}
