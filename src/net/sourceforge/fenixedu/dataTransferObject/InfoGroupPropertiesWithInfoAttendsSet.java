/*
 * Created on 12/Aug/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IGroupProperties;

/**
 * @author joaosa & rmalo 12/Aug/2004
 */

public class InfoGroupPropertiesWithInfoAttendsSet extends InfoGroupProperties {

	public void copyFromDomain(IGroupProperties groupProperties) {		
		super.copyFromDomain(groupProperties);
		 if (groupProperties != null) {
		setInfoAttendsSet(InfoAttendsSet.newInfoFromDomain(groupProperties.getAttendsSet()));
		}
	}

	public static InfoGroupProperties newInfoFromDomain(
            IGroupProperties groupProperties) {
        InfoGroupPropertiesWithInfoAttendsSet infoGroupProperties = null;
        if (groupProperties != null) {
            infoGroupProperties = new InfoGroupPropertiesWithInfoAttendsSet();
            infoGroupProperties.copyFromDomain(groupProperties);
        }

        return infoGroupProperties;
    }
}