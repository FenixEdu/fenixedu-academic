/*
 * Created on 6/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

/**
 * @author Tânia Pousão
 *  
 */
public interface IPersistentPaymentPhase extends IPersistentObject {
	public void deletePaymentPhasesOfThisGratuity(Integer gratuityValuesID)
			throws ExcepcaoPersistencia;

	/* delete - not used anywhere */
	public List readByGratuityValues(Integer gratuityValuesID)
			throws ExcepcaoPersistencia;
}