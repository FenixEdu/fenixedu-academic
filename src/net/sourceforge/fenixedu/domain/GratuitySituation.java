/*
 * Created on 5/Jan/2004
 *
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.gratuity.ExemptionGratuityType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author Tânia Pousão
 */
public class GratuitySituation extends GratuitySituation_Base {
	private ExemptionGratuityType exemptionType;
        
	/**
	 * @return Returns the exemptionType.
	 */
	public ExemptionGratuityType getExemptionType() {
		return exemptionType;
	}

	/**
	 * @param exemptionType
	 *            The exemptionType to set.
	 */
	public void setExemptionType(ExemptionGratuityType exemptionType) {
		this.exemptionType = exemptionType;
	}

}
