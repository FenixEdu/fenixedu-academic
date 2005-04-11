/*
 * Created on 08/Mar/2005
 *
 */
package net.sourceforge.fenixedu.domain;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;
import net.sourceforge.fenixedu.util.TipoAula;

/**
 * @author joaosa & rmalo
 *
 */
public class GroupProperties extends GroupProperties_Base {
	
	
    private EnrolmentGroupPolicyType enrolmentPolicy;
	
    private TipoAula shiftType;

    private Calendar enrolmentBeginDay;

    private Calendar enrolmentEndDay;

	
    /**
     * Construtor
     */
    public GroupProperties() {
    }

    /**
     * Construtor
     */
    public GroupProperties(Integer idInternal) {
        setIdInternal(idInternal);
    }
	
	/** 
	 * Construtor
	 */
	public GroupProperties(IAttendsSet attendsSet,String name) {
			super.setAttendsSet(attendsSet);
			super.setName(name);
	}
	
	/** 
	 * Construtor
	 */
	public GroupProperties(Integer maximumCapacity,Integer minimumCapacity,
							Integer idealCapacity,EnrolmentGroupPolicyType enrolmentPolicy,
	 						Integer groupMaximumNumber,String name,IAttendsSet attendsSet,
							TipoAula shiftType, Calendar enrolmentBeginDay,Calendar enrolmentEndDay,String projectDescription) {
	    super.setMaximumCapacity(maximumCapacity);
	    super.setMinimumCapacity(minimumCapacity);
	    super.setIdealCapacity(idealCapacity);
	    setEnrolmentPolicy(enrolmentPolicy);
	    super.setGroupMaximumNumber(groupMaximumNumber);
	    super.setName(name);
	    super.setAttendsSet(attendsSet);
	    setShiftType(shiftType);
	    setEnrolmentBeginDay(enrolmentBeginDay);
	    setEnrolmentEndDay(enrolmentEndDay);
	    super.setProjectDescription(projectDescription);
		
		}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof IGroupProperties) {
			result =(getAttendsSet().equals(((IGroupProperties) arg0).getAttendsSet()))&&
					(getName().equals(((IGroupProperties) arg0).getName()));
		} 
		return result;		
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[GROUP";
		result += ", maximumCapacity=" + getMaximumCapacity();
		result += ", minimumCapacity=" + getMinimumCapacity();
		result += ", idealCapacity=" + getIdealCapacity();
		result += ", enrolmentPolicy=" + getEnrolmentPolicy();
		result += ", groupMaximumNumber=" + getGroupMaximumNumber();
		result += ", name=" + getName();
		result += ", attendsSet=" + getAttendsSet();
		result += ", shiftType=" + getShiftType();
		//result += ", enrolmentBeginDay=" + getEnrolmentBeginDay();
		//result += ", enrolmentEndDay=" + getEnrolmentEndDay();
		result += ", projectDescription=" + getProjectDescription();
		result += "]";
		return result;
	}
		
	/**
	* @return EnrolmentPolicy
	*/
	public EnrolmentGroupPolicyType getEnrolmentPolicy() {
		return enrolmentPolicy;
	}
			
	
	/**
	* @return Tipo Lesson
	**/
	public TipoAula getShiftType() {
		return shiftType;
	}
	
	/**
	* @return Calendar
	**/
	public Calendar getEnrolmentBeginDay() {
		return enrolmentBeginDay;
	}

	/**
	* @return Calendar
	**/
	public Calendar getEnrolmentEndDay() {
		return enrolmentEndDay;
	}
	
	/**
	* @return List
	**/
	public List getExecutionCourses() {
		List executionCourses = new ArrayList();
		Iterator iterGroupPropertiesExecutionCourse = 
			getGroupPropertiesExecutionCourse().iterator();
		IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = null;
		while(iterGroupPropertiesExecutionCourse.hasNext()){
			groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse)iterGroupPropertiesExecutionCourse.next();
			if(groupPropertiesExecutionCourse.getProposalState().getState().intValue()==1
					|| groupPropertiesExecutionCourse.getProposalState().getState().intValue()==2){
			executionCourses.add(groupPropertiesExecutionCourse.getExecutionCourse());
			}
		}
		return executionCourses;
	}
	
		
	/**
	* Sets the enrolmentPolicy.
	* @param enrolmentPolicy The enrolmentPolicy to set
	*/
	public void setEnrolmentPolicy(EnrolmentGroupPolicyType enrolmentPolicy) {
		this.enrolmentPolicy=enrolmentPolicy;
	}

	/**
	* Sets the shiftType.
	* @param shiftType The shiftType to set
	*/
	public void setShiftType(TipoAula shiftType) {
		this.shiftType=shiftType;
	}

	/**
	* Sets the enrolmentBeginDay.
	* @param enrolmentBeginDay The enrolmentBeginDay to set
	*/
	public void setEnrolmentBeginDay(Calendar enrolmentBeginDay) {
		this.enrolmentBeginDay=enrolmentBeginDay;
	}

	/**
	* Sets the enrolmentEndDay.
	* @param enrolmentEndDay The enrolmentEndDay to set
	*/
	public void setEnrolmentEndDay(Calendar enrolmentEndDay) {
		this.enrolmentEndDay=enrolmentEndDay;
	}

	
	public void addGroupPropertiesExecutionCourse(IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse){
		
		if(super.getGroupPropertiesExecutionCourse()==null){
			super.setGroupPropertiesExecutionCourse(new ArrayList());
			super.getGroupPropertiesExecutionCourse().add(groupPropertiesExecutionCourse);
		}
		else{
			super.getGroupPropertiesExecutionCourse().add(groupPropertiesExecutionCourse);		
		}
		
	}
	
	
	public void removeGroupPropertiesExecutionCourse(IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse){
		super.getGroupPropertiesExecutionCourse().remove(groupPropertiesExecutionCourse);
	}

}
