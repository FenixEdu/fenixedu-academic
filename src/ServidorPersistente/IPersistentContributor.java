/*
 * Created on 21/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente;

import Dominio.IContributor;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentContributor extends IPersistentObject {
    
		public IContributor readByContributorNumber(Integer contributorNumber) throws ExcepcaoPersistencia; 
		public void write(IContributor contributor) throws ExcepcaoPersistencia, ExistingPersistentException;
}
