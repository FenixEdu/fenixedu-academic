/*
 * Created on 11:37:31 AM,Mar 10, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package DataBeans.externalServices;

import java.util.ArrayList;
import java.util.Collection;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import Dominio.IShift;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 11:37:31 AM, Mar 10, 2005
 */
public class InfoExternalStudentInfo
{
    /**
     * @return Returns the shifts.
     */
    public Collection getShifts()
    {
        return this.shifts;
    }
    /**
     * @param shifts The shifts to set.
     */
    public void setShifts(Collection shifts)
    {
        this.shifts = shifts;
    }
    private String name;
    private Integer number;
    private InfoExternalDegreeCurricularPlanInfo degree;
    private InfoExternalExecutionCourseInfo course;
    private Collection shifts;
    
    public InfoExternalStudentInfo()
    {
        this.shifts = new ArrayList();
    }
    
    /**
     * @return Returns the course.
     */
    public InfoExternalExecutionCourseInfo getCourse()
    {
        return this.course;
    }
    /**
     * @param course The course to set.
     */
    public void setCourse(InfoExternalExecutionCourseInfo course)
    {
        this.course = course;
    }
    /**
     * @return Returns the degree.
     */
    public InfoExternalDegreeCurricularPlanInfo getDegree()
    {
        return this.degree;
    }
    /**
     * @param degree The degree to set.
     */
    public void setDegree(InfoExternalDegreeCurricularPlanInfo degree)
    {
        this.degree = degree;
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
    public Collection getShift()
    {
        return this.shifts;
    }
    /**
     * @param shift The shift to set.
     */
    public void setShift(Collection shift)
    {
        this.shifts = shift;
    }
    
    public void addShift(InfoExternalShiftInfo shift)
    {
        this.shifts.add(shift);
    }
 }
