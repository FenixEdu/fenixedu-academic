/*
 * Created on 2:31:25 PM,Mar 10, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import net.sourceforge.fenixedu.domain.IExecutionCourse;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 2:31:25 PM, Mar 10, 2005
 */
public class InfoExternalExecutionCourseInfo
{
    private String name;
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
    
    public static InfoExternalExecutionCourseInfo newFromExecutionCourse(IExecutionCourse executionCourse)
    {
       InfoExternalExecutionCourseInfo info = new InfoExternalExecutionCourseInfo();
       info.setName(executionCourse.getNome());
       
       return info;
    }
}
