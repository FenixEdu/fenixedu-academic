package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Luis Cruz
 */

public class ReadRolesByUser extends Service {

	/**
	 * Executes the service. Returns the current infodegree.
	 * @throws ExcepcaoPersistencia 
	 */
	public List run(String username) throws FenixServiceException, ExcepcaoPersistencia {
		List result = null;

		Person person = Person.readPersonByUsername(username);
		if (person == null) {
			throw new FenixServiceException("error.noUsername");
		}

		result = (List) CollectionUtils.collect(person.getPersonRoles(), new Transformer() {
			public Object transform(Object arg0) {
				return InfoRole.newInfoFromDomain((Role) arg0);
			}
		});

		return result;
	}
}