/*
 * Created on 7/Nov/2003
 *  
 */
package DataBeans.gesdis;

import java.util.Date;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoObject;
import Dominio.gesdis.ICourseReport;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoCourseReport extends InfoObject
{

    private String report;
    private Date lastModificationDate;
    private InfoExecutionCourse infoExecutionCourse;

    public InfoCourseReport()
    {}

    public String getReport()
    {
        return report;
    }

    public InfoExecutionCourse getInfoExecutionCourse()
    {
        return infoExecutionCourse;
    }

    public void setReport(String report)
    {
        this.report = report;
    }

    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse)
    {
        this.infoExecutionCourse = infoExecutionCourse;
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

    public void copyFromDomain(ICourseReport courseReport) {
        super.copyFromDomain(courseReport);
        if(courseReport != null) {
            setLastModificationDate(courseReport.getLastModificationDate());
            setReport(courseReport.getReport());
        }
    }
    
    public static InfoCourseReport newInfoFromDomain(ICourseReport courseReport) {
        InfoCourseReport infoCourseReport = null;
        if(courseReport != null) {
            infoCourseReport = new InfoCourseReport();
            infoCourseReport.copyFromDomain(courseReport);
        }
        return infoCourseReport;
    }
}
