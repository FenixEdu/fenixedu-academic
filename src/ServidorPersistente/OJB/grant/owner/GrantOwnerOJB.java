package ServidorPersistente.OJB.grant.owner;

import org.apache.ojb.broker.query.Criteria;

import Dominio.grant.owner.GrantOwner;
import Dominio.grant.owner.IGrantOwner;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.grant.IPersistentGrantOwner;
/**
 *
 * @author  Barbosa
 * @author  Pica
 */

public class GrantOwnerOJB
	extends ServidorPersistente.OJB.ObjectFenixOJB
	implements IPersistentGrantOwner {

	public GrantOwnerOJB() {
	}

	public IGrantOwner readGrantOwnerByNumber(Integer grantOwnerNumber)
		throws ExcepcaoPersistencia {
		IGrantOwner grantOwner = null;

		Criteria criteria = new Criteria();
		criteria.addEqualTo("number", grantOwnerNumber);
		grantOwner = (IGrantOwner) queryObject(GrantOwner.class, criteria);
		return grantOwner;
	}

	public IGrantOwner readGrantOwnerByPerson(Integer personIdInternal)
		throws ExcepcaoPersistencia {
		IGrantOwner grantOwner = null;
		Criteria criteria = new Criteria();
		criteria.addEqualTo("key_person", personIdInternal);
		grantOwner = (IGrantOwner) queryObject(GrantOwner.class, criteria);
		return grantOwner;
	}

	public Integer readMaxGrantOwnerNumber() throws ExcepcaoPersistencia{
		IGrantOwner grantOwner = new GrantOwner();
		Integer maxGrantOwnerNumber = new Integer(0);

		Criteria criteria = new Criteria();
		criteria.addOrderBy("number", false);
		grantOwner = (IGrantOwner) queryObject(GrantOwner.class, criteria);
		maxGrantOwnerNumber = grantOwner.getNumber();
		return maxGrantOwnerNumber;
	}
}
