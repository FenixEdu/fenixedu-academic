/*
 * Created on 4:02:48 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import net.sourceforge.fenixedu.domain.ICurricularCourse;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 4:02:48 PM, Mar 11, 2005
 */
public class InfoExternalCurricularCourseInfo
{
    private String name;
    private String code;
    /**
     * @return Returns the code.
     */
    public String getCode()
    {
        return this.code;
    }
    /**
     * @param code The code to set.
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
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    static public InfoExternalCurricularCourseInfo newFromDomain(ICurricularCourse course)
    {
        InfoExternalCurricularCourseInfo info = new InfoExternalCurricularCourseInfo();
        info.setCode(course.getCode());
        info.setName(course.getName());
        
        return info;
    }
}
