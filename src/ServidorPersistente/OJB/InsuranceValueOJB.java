/*
 * Created on 9/Jan/2004
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionYear;
import Dominio.IInsuranceValue;
import Dominio.InsuranceValue;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentInsuranceValue;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class InsuranceValueOJB extends PersistentObjectOJB implements IPersistentInsuranceValue {

    public IInsuranceValue readByExecutionYear(IExecutionYear executionYear) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.idInternal", executionYear.getIdInternal());

        return (IInsuranceValue) queryObject(InsuranceValue.class, criteria);
    }

}