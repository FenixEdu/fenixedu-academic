/*
 * Created on 12/Jul/2003 by jpvl
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Funcionario;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;

/**
 * @author jpvl
 */
public class EmployeeOJB
	extends ObjectFenixOJB
	implements IPersistentEmployee {

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentEmployee#readByNumber(java.lang.Integer)
	 */
	public Funcionario readByNumber(Integer number) throws ExcepcaoPersistencia {
		// TODO Auto-generated method stub
		Criteria criteria = new Criteria ();
		criteria.addEqualTo("numeroMecanografico", number);
		return (Funcionario) queryObject(Funcionario.class, criteria);
	}

}
