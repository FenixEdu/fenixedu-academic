/*
 * Created on 9/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

import Util.EnrolmentPolicyType;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IGroupProperties extends IDomainObject{
	 
	
		public Integer getMaximumCapacity();
		public Integer getMinimumCapacity() ;
		public Integer getIdealCapacity();
		public EnrolmentPolicyType getEnrolmentPolicy();
		public Integer getGroupMaximumNumber();
		public String getProjectName();
		public IDisciplinaExecucao getExecutionCourse();

		public void setMaximumCapacity(Integer maximumCapacity);
		public void setMinimumCapacity(Integer minimumCapacity);
		public void setIdealCapacity(Integer idealCapacity);
		public void setEnrolmentPolicy(EnrolmentPolicyType enrolmentPolicy);
		public void setGroupMaximumNumber(Integer groupMaximumNumber);
		public void setProjectName(String projectName);
		public void setExecutionCourse(IDisciplinaExecucao executionCourse);	

}
