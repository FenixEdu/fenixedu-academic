/*
 * Created on 18/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IStudentGroup;

/**
 * @author Tânia Pousão
 * 18/Jun/2004
 */
public class InfoStudentGroupWithAll extends InfoStudentGroup {
	public void copyFromDomain(IStudentGroup studentGroup) {
		super.copyFromDomain(studentGroup);
		if(studentGroup != null) {
			setInfoAttendsSet(InfoAttendsSetWithInfoGroupProperties.newInfoFromDomain(studentGroup.getAttendsSet()));
			setInfoShift(InfoShift.newInfoFromDomain(studentGroup.getShift()));
		}
	}
	
	public static InfoStudentGroup newInfoFromDomain(IStudentGroup studentGroup) {
		InfoStudentGroupWithAll infoStudentGroup = null;
		if(studentGroup != null) {
			infoStudentGroup = new InfoStudentGroupWithAll();
			infoStudentGroup.copyFromDomain(studentGroup);
		}
		
		return infoStudentGroup;
	}
}
