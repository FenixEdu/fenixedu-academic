/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation -
 * Code and Comments
 */
package Dominio.gesdis;

import java.util.Date;

import Dominio.DomainObject;
import Dominio.ICurricularCourse;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class StudentCourseReport extends DomainObject implements IStudentCourseReport
{

    private String studentReport;
    private String strongPoints;
    private String weakPoints;
    private Integer keyCurricularCourse;
    private ICurricularCourse curricularCourse;
    private Date lastModificationDate;

    /**
	 *  
	 */
    public StudentCourseReport()
    {
        super();
    }

    /**
	 * @param idInternal
	 */
    public StudentCourseReport(Integer idInternal)
    {
        super(idInternal);
    }

    /**
	 * @return Returns the curricularCourse.
	 */
    public ICurricularCourse getCurricularCourse()
    {
        return curricularCourse;
    }

    /**
	 * @param curricularCourse
	 *            The curricularCourse to set.
	 */
    public void setCurricularCourse(ICurricularCourse curricularCourse)
    {
        this.curricularCourse = curricularCourse;
    }

    /**
	 * @return Returns the keyCurricularCourse.
	 */
    public Integer getKeyCurricularCourse()
    {
        return keyCurricularCourse;
    }

    /**
	 * @param keyCurricularCourse
	 *            The keyCurricularCourse to set.
	 */
    public void setKeyCurricularCourse(Integer keyCurricularCourse)
    {
        this.keyCurricularCourse = keyCurricularCourse;
    }

    /**
	 * @return Returns the lastModificationDate.
	 */
    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }

    /**
	 * @param lastModificationDate
	 *            The lastModificationDate to set.
	 */
    public void setLastModificationDate(Date lastModificationDate)
    {
        this.lastModificationDate = lastModificationDate;
    }

    /**
	 * @return Returns the strongPoints.
	 */
    public String getStrongPoints()
    {
        return strongPoints;
    }

    /**
	 * @param strongPoints
	 *            The strongPoints to set.
	 */
    public void setStrongPoints(String strongPoints)
    {
        this.strongPoints = strongPoints;
    }

    /**
	 * @return Returns the studentReport.
	 */
    public String getStudentReport()
    {
        return studentReport;
    }

    /**
	 * @param studentReport
	 *            The studentReport to set.
	 */
    public void setStudentReport(String studentReport)
    {
        this.studentReport = studentReport;
    }

    /**
	 * @return Returns the weakPoints.
	 */
    public String getWeakPoints()
    {
        return weakPoints;
    }

    /**
	 * @param weakPoints
	 *            The weakPoints to set.
	 */
    public void setWeakPoints(String weakPoints)
    {
        this.weakPoints = weakPoints;
    }

}
