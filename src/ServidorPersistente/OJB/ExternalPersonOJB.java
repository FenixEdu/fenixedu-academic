/*
 * Created on Oct 13, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ExternalPerson;
import Dominio.IExternalPerson;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExternalPerson;

/**
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public class ExternalPersonOJB extends ObjectFenixOJB implements IPersistentExternalPerson {

	/** Creates a new instance of ExternalPersonOJB */
	public ExternalPersonOJB() {
	}

	public IExternalPerson readByUsername(String username) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		IExternalPerson externalPerson = null;

		criteria.addEqualTo("person.username", username);
		externalPerson = (IExternalPerson) queryObject(ExternalPerson.class, criteria);

		return externalPerson;

	}

	public List readByName(String name) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		List externalPersons = new ArrayList();

		criteria.addLike("person.name", "%" + name + "%");
		externalPersons = queryList(ExternalPerson.class, criteria);

		return externalPersons;

	}

}
