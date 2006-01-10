/*
 * Created on 9/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentInsuranceValue;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public class InsuranceValueVO extends VersionedObjectsBase implements IPersistentInsuranceValue {

	public InsuranceValue readByExecutionYear(Integer executionYearID) throws ExcepcaoPersistencia {

		ExecutionYear executionYear = (ExecutionYear) readByOID(ExecutionYear.class, executionYearID);

		if (executionYear == null) {
			return null;
		}

		return executionYear.getInsuranceValue();
	}

}