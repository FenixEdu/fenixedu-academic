package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IInsuranceValue;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public interface IPersistentInsuranceValue extends IPersistentObject {

	public IInsuranceValue readByExecutionYear(Integer executionYearID) throws ExcepcaoPersistencia;

}