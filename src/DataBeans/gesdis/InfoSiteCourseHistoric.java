/*
 * Created on Feb 17, 2004
 *  
 */
package DataBeans.gesdis;

import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.InfoCurricularCourse;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida</a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo</a>
 *  
 */
public class InfoSiteCourseHistoric extends DataTranferObject
{
    List infoCourseHistorics;
    InfoCurricularCourse infoCurricularCourse;

    public InfoSiteCourseHistoric()
    {}

    /**
	 * @return Returns the infoCourseHistorics.
	 */
    public List getInfoCourseHistorics()
    {
        return infoCourseHistorics;
    }

    /**
	 * @param infoCourseHistorics
	 *            The infoCourseHistorics to set.
	 */
    public void setInfoCourseHistorics(List infoCourseHistorics)
    {
        this.infoCourseHistorics = infoCourseHistorics;
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

}
