/*
 * Created on 9/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.IInsuranceValue;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentInsuranceValue;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public class InsuranceValueOJB extends PersistentObjectOJB implements IPersistentInsuranceValue {

	public IInsuranceValue readByExecutionYear(Integer executionYearID) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionYear.idInternal", executionYearID);

		return (IInsuranceValue) queryObject(InsuranceValue.class, criteria);
	}

}