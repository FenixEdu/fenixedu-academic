/*
 * Created on 19/Mai/2003 by jpvl
 *
 */
package ServidorPersistente;

import Dominio.ICredits;


/**
 * @author Tânia Pousão
 */
public interface IPersistentCreditsTeacher extends IPersistentObject {
	ICredits readByUnique(ICredits creditsTeacher) throws ExcepcaoPersistencia;
	void delete (ICredits creditsTeacher) throws ExcepcaoPersistencia;
}
