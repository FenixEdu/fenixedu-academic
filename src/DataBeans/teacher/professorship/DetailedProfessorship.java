/*
 * Created on Dec 12, 2003 by jpvl
 *  
 */
package DataBeans.teacher.professorship;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegree;
import DataBeans.InfoProfessorship;

/**
 * @author jpvl
 */
public class DetailedProfessorship
{
    private InfoProfessorship infoProfessorship;
    private List executionCourseCurricularCoursesList;
    /**
	 * @return Returns the executionCourseCurricularCoursesList.
	 */
    public List getExecutionCourseCurricularCoursesList()
    {
        return this.executionCourseCurricularCoursesList;
    }
    /**
	 * @param executionCourseCurricularCoursesList
	 *                   The executionCourseCurricularCoursesList to set.
	 */
    public void setExecutionCourseCurricularCoursesList(List executionCourseCurricularCoursesList)
    {
        this.executionCourseCurricularCoursesList = executionCourseCurricularCoursesList;
    }
    /**
	 * @return Returns the infoProfessorship.
	 */
    public InfoProfessorship getInfoProfessorship()
    {
        return this.infoProfessorship;
    }
    /**
	 * @param infoProfessorship
	 *                   The infoProfessorship to set.
	 */
    public void setInfoProfessorship(InfoProfessorship infoProfessorship)
    {
        this.infoProfessorship = infoProfessorship;
    }

    public List getInfoDegreeList()
    {

        List infoDegreeList = new ArrayList();
        Iterator iter = executionCourseCurricularCoursesList.iterator();
        while (iter.hasNext())
        {
            InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
            InfoDegree infoDegree = infoCurricularCourse.getInfoDegreeCurricularPlan().getInfoDegree();
            if (!infoDegreeList.contains(infoDegree))
            {
                infoDegreeList.add(infoDegree);
            }
        }
        return infoDegreeList;
    }
}