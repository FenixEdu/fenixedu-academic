/*
 * Created on 6/Mai/2003
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
 * @author asnr and scpo
 *
 */
public class GroupProperties extends DomainObject implements IGroupProperties{
	
	
    private Integer maximumCapacity;

    private Integer minimumCapacity;

    private Integer idealCapacity;

    private EnrolmentGroupPolicyType enrolmentPolicy;

    private Integer groupMaximumNumber;

    private String name;
    
	private Integer keyAttendsSet;
	
	private IAttendsSet attendsSet;
	
    private TipoAula shiftType;

    private Calendar enrolmentBeginDay;

    private Calendar enrolmentEndDay;

    private String projectDescription;
    
	private List groupPropertiesExecutionCourse = null;
	
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
			this.attendsSet=attendsSet;
			this.name = name;
	}
	
	/** 
	 * Construtor
	 */
	public GroupProperties(Integer maximumCapacity,Integer minimumCapacity,
							Integer idealCapacity,EnrolmentGroupPolicyType enrolmentPolicy,
	 						Integer groupMaximumNumber,String name,IAttendsSet attendsSet,
							TipoAula shiftType, Calendar enrolmentBeginDay,Calendar enrolmentEndDay,String projectDescription) {
		this.maximumCapacity=maximumCapacity;
		this.minimumCapacity=minimumCapacity;
		this.idealCapacity=idealCapacity;
		this.enrolmentPolicy= enrolmentPolicy;
		this.groupMaximumNumber=groupMaximumNumber;
		this.name = name;
		this.attendsSet=attendsSet;
		this.shiftType = shiftType;
		this.enrolmentBeginDay = enrolmentBeginDay;
		this.enrolmentEndDay = enrolmentEndDay;
		this.projectDescription = projectDescription;
		
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
	 * @return Integer
	 */
	public Integer getKeyAttendsSet() {
		return keyAttendsSet;
	}
	
	/**
	 * @return Integer
	 */
	public Integer getMaximumCapacity() {
		return maximumCapacity;
	}

	/**
	 * @return Integer
	 */
	public Integer getMinimumCapacity() {
		return minimumCapacity;
	}
	
	/**
	 * @return Integer
	 */
	public Integer getIdealCapacity() {
		return idealCapacity;
	}
	
	/**
	* @return EnrolmentPolicy
	*/
	public EnrolmentGroupPolicyType getEnrolmentPolicy() {
		return enrolmentPolicy;
	}
			
	/**
	 * @return Integer
	 */
	public Integer getGroupMaximumNumber() {
		return groupMaximumNumber;
	}

	/**
	* @return String
	*/
	public String getName() {
		return name;
	}

	/**
	* @return String
	*/
	public String getProjectDescription() {
		return projectDescription;
	}

	/**
	 * @return ConjuntoDisciplinasAlunos
	 */
	public IAttendsSet getAttendsSet() {
		return attendsSet;
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
	
	public List getGroupPropertiesExecutionCourse(){
		return groupPropertiesExecutionCourse;
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
	* Sets the keyAttendsSet.
	* @param keyAttendsSet
	*/
	public void setKeyAttendsSet(Integer keyAttendsSet) {
		this.keyAttendsSet=keyAttendsSet;
	}
	
	/**
	* Sets the maximumCapacity.
	* @param maximumCapacity The maximumCapacity to set
	*/
	public void setMaximumCapacity(Integer maximumCapacity) {
		this.maximumCapacity=maximumCapacity;
	}

	/**
	* Sets the minimumCapacity.
	* @param minimumCapacity The minimumCapacity to set
	*/
	public void setMinimumCapacity(Integer minimumCapacity) {
		this.minimumCapacity=minimumCapacity;
	}
	
	/**
	* Sets the idealCapacity.
	* @param idealCapacity The idealCapacity to set
	*/
	public void setIdealCapacity(Integer idealCapacity) {
		this.idealCapacity=idealCapacity;
	}
	
	/**
	* Sets the enrolmentPolicy.
	* @param enrolmentPolicy The enrolmentPolicy to set
	*/
	public void setEnrolmentPolicy(EnrolmentGroupPolicyType enrolmentPolicy) {
		this.enrolmentPolicy=enrolmentPolicy;
	}		
	/**
	* Sets the groupMaximumNumber.
	* @param groupMaximumNumber The groupMaximumNumber to set
	*/
	public void setGroupMaximumNumber(Integer groupMaximumNumber) {
		this.groupMaximumNumber=groupMaximumNumber;
	}	
	/**
	* Sets the name.
	* @param name The name to set
	*/
	public void setName(String name) {
		this.name=name;
	}
	
	/**
	* Sets the projectDescription.
	* @param projectDescription The projectDescription to set
	*/
	public void setProjectDescription(String projectDescription) {
		this.projectDescription=projectDescription;
	}
	
	/**
	* Sets the attendsSet.
	* @param attendsSet The attendsSet to set
	*/
	public void setAttendsSet(IAttendsSet attendsSet) {
		this.attendsSet=attendsSet;
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

	
	public void setGroupPropertiesExecutionCourse(List groupPropertiesExecutionCourse){
		this.groupPropertiesExecutionCourse=groupPropertiesExecutionCourse;
	}

	
	public void addGroupPropertiesExecutionCourse(IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse){
		
		if(this.groupPropertiesExecutionCourse==null){
			this.groupPropertiesExecutionCourse=new ArrayList();
			this.groupPropertiesExecutionCourse.add(groupPropertiesExecutionCourse);
		}
		else{
			this.groupPropertiesExecutionCourse.add(groupPropertiesExecutionCourse);		
		}
		
	}
	
	
	public void removeGroupPropertiesExecutionCourse(IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse){
		this.groupPropertiesExecutionCourse.remove(groupPropertiesExecutionCourse);
	}

}
