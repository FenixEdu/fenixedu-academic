/*
 * Created on 2003/08/08
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ReadNumberCachedItems extends Service {

	public Integer run() throws FenixServiceException, ExcepcaoPersistencia {
		Integer numberCachedObjects = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		numberCachedObjects = sp.getNumberCachedItems();

		return numberCachedObjects;
	}

}