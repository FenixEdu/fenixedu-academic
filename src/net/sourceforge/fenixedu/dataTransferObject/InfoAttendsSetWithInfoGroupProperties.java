/*
 * Created on 30/Ago/2004
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IAttendsSet;

/**
 * @author joaosa & rmalo
 */
public class InfoAttendsSetWithInfoGroupProperties extends InfoAttendsSet{



	public void copyFromDomain(IAttendsSet attendsSet) {		
		super.copyFromDomain(attendsSet);
		 if (attendsSet != null) {
		 	setInfoGroupProperties(InfoGroupProperties.newInfoFromDomain(attendsSet.getGroupProperties()));	
		 }
	}

	public static InfoAttendsSet newInfoFromDomain(
            IAttendsSet attendsSet) {
		InfoAttendsSetWithInfoGroupProperties infoAttendsSet = null;
        if (attendsSet != null) {
            infoAttendsSet = new InfoAttendsSetWithInfoGroupProperties();
            infoAttendsSet.copyFromDomain(attendsSet);
        }

        return infoAttendsSet;
    }
}
