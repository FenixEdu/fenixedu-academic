/*
 * Created on 8/Out/2004
 *
 */
package DataBeans;

import Dominio.IStudentGroup;

/**
 * @author joaosa & rmalo
 *  
 */
public class InfoStudentGroupWithoutShift extends InfoStudentGroup {
	public void copyFromDomain(IStudentGroup studentGroup) {
		super.copyFromDomain(studentGroup);
		if(studentGroup != null) {
			setInfoAttendsSet(InfoAttendsSetWithInfoGroupProperties.newInfoFromDomain(studentGroup.getAttendsSet()));
		}
	}
	
	public static InfoStudentGroup newInfoFromDomain(IStudentGroup studentGroup) {
		InfoStudentGroupWithoutShift infoStudentGroup = null;
		if(studentGroup != null) {
			infoStudentGroup = new InfoStudentGroupWithoutShift();
			infoStudentGroup.copyFromDomain(studentGroup);
		}
		
		return infoStudentGroup;
	}
}
