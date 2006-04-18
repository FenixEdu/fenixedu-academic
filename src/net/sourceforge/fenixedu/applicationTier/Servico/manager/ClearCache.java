/*
 * Created on 2003/08/08
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ClearCache extends Service {

	public Boolean run() throws ExcepcaoPersistencia {
		return Boolean.TRUE;
	}

}