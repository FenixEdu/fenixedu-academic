package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Luis Cruz
 */

public class ReadRoles extends Service {

	/**
	 * Executes the service. Returns the current infodegree.
	 * @throws ExcepcaoPersistencia 
	 */
	public List run() throws FenixServiceException, ExcepcaoPersistencia {
		List result = null;

		result = (List) CollectionUtils.collect(persistentObject.readAll(Role.class), new Transformer() {
			public Object transform(Object arg0) {
				return InfoRole.newInfoFromDomain((Role) arg0);
			}
		});

		return result;
	}
}