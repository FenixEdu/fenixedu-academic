/*
 * Created on 2003/08/08
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ReadNumberCachedItems extends Service {

	public Integer run() throws FenixServiceException, ExcepcaoPersistencia {
		Integer numberCachedObjects = null;

		numberCachedObjects = persistentSupport.getNumberCachedItems();

		return numberCachedObjects;
	}

}