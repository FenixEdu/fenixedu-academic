/*
 * Created on 22/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;


/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InfoStudentGroup {
	
		private Integer groupNumber;
		private InfoShift infoShift;
		private InfoGroupProperties infoGroupProperties;
		
		/** 
		 * Construtor
		 */
		public InfoStudentGroup() {}
	
		/** 
		 * Construtor
		 */
		public InfoStudentGroup(Integer groupNumber,InfoGroupProperties infoGroupProperties,InfoShift infoShift) {
			
			this.groupNumber = groupNumber;
			this.infoGroupProperties = infoGroupProperties;
			this.infoShift = infoShift;
		}
	
	
	
		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object arg0) {
			boolean result = false;
			if (arg0 instanceof InfoStudentGroup) {
				result = (getInfoGroupProperties().equals(((InfoStudentGroup) arg0).getInfoGroupProperties()))&&
						 (getGroupNumber().equals(((InfoStudentGroup) arg0).getGroupNumber()));
			} 
			return result;		
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			String result = "[INFO_STUDENT_GROUP";
			result += ", groupNumber=" + getGroupNumber();
			result += ", infoGroupProperties=" + getInfoGroupProperties();
			result += "]";
			return result;
		}


		/**
		 * @return Integer
		 */
		public Integer getGroupNumber() {
			return groupNumber;
		}

		/**
		 * @return InfoGroupProperties
		 */
		public InfoGroupProperties getInfoGroupProperties() {
			return infoGroupProperties;
		}

		/**
		 * @return InfoTurno
		 */
		public InfoShift getInfoShift() {
			return infoShift;
		}

	
		
		/**
		 * Sets the groupNumber.
		 * @param groupNumber The groupNumber to set
		 */
		public void setGroupNumber(Integer groupNumber) {
			this.groupNumber = groupNumber;
		}

		/**
		 * Sets the infoGroupProperties.
		 * @param infoGroupProperties The infoGroupProperties to set
		 */
		public void setInfoGroupProperties(InfoGroupProperties infoGroupProperties) {
			this.infoGroupProperties = infoGroupProperties;
		}

		/**
		* Sets the infoShift.
		* @param infoShift The infoShift to set
		*/
		public void setInfoShift(InfoShift infoShift) {
			this.infoShift = infoShift;
		}
}
