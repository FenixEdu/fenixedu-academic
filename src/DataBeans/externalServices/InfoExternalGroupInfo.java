/*
 * Created on 11:36:15 AM,Mar 10, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package DataBeans.externalServices;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 11:36:15 AM, Mar 10, 2005
 */
public class InfoExternalGroupInfo
{
    private Integer number;
    private InfoExternalShiftInfo shift;
    private Collection executionCourses;  
    
    public InfoExternalGroupInfo()
    {
        this.executionCourses = new ArrayList();
    }
    
    /**
     * @return Returns the executionCourses.
     */
    public Collection getExecutionCourses()
    {
        return this.executionCourses;
    }
    /**
     * @param executionCourses The executionCourses to set.
     */
    
    public void addExecutionCourse(InfoExternalExecutionCourseInfo course)
    {
        this.executionCourses.add(course);
    }
    
    public void setExecutionCourses(Collection executionCourses)
    {
        this.executionCourses = executionCourses;
    }
    /**
     * @return Returns the number.
     */
    public Integer getNumber()
    {
        return this.number;
    }
    /**
     * @param number The number to set.
     */
    public void setNumber(Integer number)
    {
        this.number = number;
    }
    /**
     * @return Returns the shift.
     */
    public InfoExternalShiftInfo getShift()
    {
        return this.shift;
    }
    /**
     * @param shift The shift to set.
     */
    public void setShift(InfoExternalShiftInfo shift)
    {
        this.shift = shift;
    }
}
