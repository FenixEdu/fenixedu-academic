/*
 * Created on 17/Fev/2004
 *  
 */
package DataBeans.gesdis;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoCourseHistoric extends InfoObject
{

    private Integer enrolled;
    private Integer evaluated;
    private Integer approved;
    private String curricularYear;
    private Integer semester;
    private InfoCurricularCourse infoCurricularCourse;

    public InfoCourseHistoric()
    {}
    
    /**
	 * @return Returns the approved.
	 */
    public Integer getApproved()
    {
        return approved;
    }

    /**
	 * @param approved
	 *            The approved to set.
	 */
    public void setApproved(Integer approved)
    {
        this.approved = approved;
    }

    /**
	 * @return Returns the curricularYear.
	 */
    public String getCurricularYear()
    {
        return curricularYear;
    }

    /**
	 * @param curricularYear
	 *            The curricularYear to set.
	 */
    public void setCurricularYear(String curricularYear)
    {
        this.curricularYear = curricularYear;
    }

    /**
	 * @return Returns the enrolled.
	 */
    public Integer getEnrolled()
    {
        return enrolled;
    }

    /**
	 * @param enrolled
	 *            The enrolled to set.
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
	 * @param evaluated
	 *            The evaluated to set.
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
	 * @param infoCurricularCourse
	 *            The infoCurricularCourse to set.
	 */
    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse)
    {
        this.infoCurricularCourse = infoCurricularCourse;
    }

    /**
	 * @return Returns the semester.
	 */
    public Integer getSemester()
    {
        return semester;
    }

    /**
	 * @param semester
	 *            The semester to set.
	 */
    public void setSemester(Integer semester)
    {
        this.semester = semester;
    }
}