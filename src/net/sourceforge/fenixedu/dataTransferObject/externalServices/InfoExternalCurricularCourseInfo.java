/*
 * Created on 4:02:48 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * 
 * Created at 4:02:48 PM, Mar 11, 2005
 */
public class InfoExternalCurricularCourseInfo
{

    private String name;
    private String code;
    private String ECTSCredits;
    private String credits;
    private String weigth;
    private String curricularYear;


    public String getCredits()
    {
        return this.credits;
    }


    public void setCredits(String credits)
    {
        this.credits = credits;
    }

    public String getECTSCredits()
    {
        return this.ECTSCredits;
    }

    public void setECTSCredits(String credits)
    {
        ECTSCredits = credits;
    }

    /**
     * @return Returns the code.
     */
    public String getCode()
    {
        return this.code;
    }

    /**
     * @param code
     *            The code to set.
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    static public InfoExternalCurricularCourseInfo newFromDomain(ICurricularCourse course)
    {
        Integer year = null;
        for (Iterator iter = course.getScopes().iterator(); iter.hasNext();)
        {
            ICurricularCourseScope element = (ICurricularCourseScope) iter.next();
            int currentYear = element.getCurricularSemester().getCurricularYear().getYear().intValue();
            if (year == null || currentYear < year.intValue())
                year = currentYear;
        }

        InfoExternalCurricularCourseInfo info = new InfoExternalCurricularCourseInfo();

        if (year != null)
        {
            info.setCurricularYear(new Integer(year).toString());
        }
        info.setCode(course.getCode());
        info.setName(course.getName());
        if (course.getWeigth() != null)
        {
            info.setWeigth(course.getWeigth().toString());
        }
        if (course.getEctsCredits() != null)
        {
            info.setECTSCredits(course.getEctsCredits().toString());
        }
        if (course.getCredits() != null)
        {
            info.setCredits(course.getCredits().toString());
        }

        return info;
    }


    public String getWeigth()
    {
        return this.weigth;
    }


    public void setWeigth(String weigth)
    {
        this.weigth = weigth;
    }


    public String getCurricularYear()
    {
        return this.curricularYear;
    }


    public void setCurricularYear(String curricularYear)
    {
        this.curricularYear = curricularYear;
    }
}
