/*
 * Created on 11/Out/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IStudentGroup;

/**
 * @author joasa & rmalo
 *
 */
public class InfoStudentGroupWithInfoShift extends InfoStudentGroup {
	public void copyFromDomain(IStudentGroup studentGroup) {
		super.copyFromDomain(studentGroup);
		if(studentGroup != null) {
			setInfoShift(InfoShift.newInfoFromDomain(studentGroup.getShift()));
		}
	}
	
	public static InfoStudentGroup newInfoFromDomain(IStudentGroup studentGroup) {
		InfoStudentGroupWithInfoShift infoStudentGroup = null;
		if(studentGroup != null) {
			infoStudentGroup = new InfoStudentGroupWithInfoShift();
			infoStudentGroup.copyFromDomain(studentGroup);
		}
		
		return infoStudentGroup;
	}
}
