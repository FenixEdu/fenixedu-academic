/*
 * Created on 9/Mai/2003
 *
 */
package Dominio;

import java.util.Calendar;
import java.util.List;

import Util.EnrolmentGroupPolicyType;
import Util.TipoAula;

/**
 * @author asnr and scpo
 *
 */
public interface IGroupProperties extends IDomainObject{

    public Integer getMaximumCapacity();

    public Integer getMinimumCapacity();

    public Integer getIdealCapacity();

    public EnrolmentGroupPolicyType getEnrolmentPolicy();

    public Integer getGroupMaximumNumber();

    public String getName();

    public String getProjectDescription();

    public TipoAula getShiftType();

    public Calendar getEnrolmentBeginDay();

    public Calendar getEnrolmentEndDay();

    public List getGroupPropertiesExecutionCourse();

    public List getExecutionCourses();

    public IAttendsSet getAttendsSet();

    public void setMaximumCapacity(Integer maximumCapacity);

    public void setMinimumCapacity(Integer minimumCapacity);

    public void setIdealCapacity(Integer idealCapacity);

    public void setEnrolmentPolicy(EnrolmentGroupPolicyType enrolmentPolicy);

    public void setGroupMaximumNumber(Integer groupMaximumNumber);

    public void setName(String name);

    public void setProjectDescription(String projectDescription);

    public void setShiftType(TipoAula shiftType);

    public void setEnrolmentBeginDay(Calendar enrolmentBeginDay);

    public void setEnrolmentEndDay(Calendar enrolmentEndDay);

    public void setGroupPropertiesExecutionCourse(List groupPropertiesExecutionCourses);

    public void setAttendsSet(IAttendsSet attendsSet);

    public void addGroupPropertiesExecutionCourse(IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse);

    public void removeGroupPropertiesExecutionCourse(IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse);
}
		
