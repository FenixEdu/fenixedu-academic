/*
 * Created on 22/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;

import java.util.Calendar;

import Util.EnrolmentGroupPolicyType;
import Util.TipoAula;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InfoGroupProperties extends InfoObject{
		
		private Integer maximumCapacity;
		private Integer minimumCapacity;
		private Integer idealCapacity;
		private EnrolmentGroupPolicyType enrolmentPolicy;
		private Integer groupMaximumNumber;
		private String name;
		private InfoExecutionCourse infoExecutionCourse;
		private TipoAula shiftType;
		private Calendar enrolmentBeginDay;
		private Calendar enrolmentEndDay;

	
		/** 
		 * Construtor
		 */
		public InfoGroupProperties() {}
	
		/** 
		 * Construtor
		 */
		public InfoGroupProperties(InfoExecutionCourse infoExecutionCourse) {
				this.infoExecutionCourse=infoExecutionCourse;
		}
	
		/** 
		 * Construtor
		 */
		public InfoGroupProperties(Integer maximumCapacity,Integer minimumCapacity,
								Integer idealCapacity,EnrolmentGroupPolicyType enrolmentPolicy,
								Integer groupMaximumNumber,InfoExecutionCourse infoExecutionCourse,String name,
								TipoAula shiftType, Calendar enrolmentBeginDay,Calendar enrolmentEndDay) {
			this.maximumCapacity=maximumCapacity;
			this.minimumCapacity=minimumCapacity;
			this.idealCapacity=idealCapacity;
			this.enrolmentPolicy=enrolmentPolicy;
			this.groupMaximumNumber=groupMaximumNumber;
			this.infoExecutionCourse=infoExecutionCourse;
			this.name = name;
			this.shiftType = shiftType;
			this.enrolmentBeginDay = enrolmentBeginDay;
			this.enrolmentEndDay = enrolmentEndDay;
		
		}
	
		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object arg0) {
			boolean result = false;
			if (arg0 instanceof InfoGroupProperties) {
				result = (getInfoExecutionCourse().equals(((InfoGroupProperties) arg0).getInfoExecutionCourse()))&&
				(getName().equals(((InfoGroupProperties) arg0).getName()));
			} 
			return result;		
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			String result = "[INFO_STUDENT_GROUP";
			result += ", maximumCapacity=" + getMaximumCapacity();
			result += ", minimumCapacity=" + getMinimumCapacity();
			result += ", idealCapacity=" + getIdealCapacity();
			result += ", enrolmentPolicy=" + getEnrolmentPolicy();
			result += ", groupMaximumNumber=" + getGroupMaximumNumber();
			result += ", name=" + getName();
			result += ", infoExecutionCourse=" + getInfoExecutionCourse();
			result += ", shiftType=" + getShiftType();
			result += ", enrolmentBeginDay=" + getEnrolmentBeginDay();
			result += ", enrolmentEndDay=" + getEnrolmentEndDay();
			result += "]";
			return result;
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
		 * @return InfoExecutionCourse
		 */
		public InfoExecutionCourse getInfoExecutionCourse() {
			return infoExecutionCourse;
		}
	
	    /**
		* @return Tipo Aula
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
		* Sets the projectName.
		* @param projectName The projectName to set
		*/
		public void setName(String name) {
			this.name=name;
		}

		/**
		* Sets the infoExecutionCourse.
		* @param infoExecutionCourse The infoExecutionCourse to set
		*/
		public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
			this.infoExecutionCourse=infoExecutionCourse;
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
}
