/*
 * Created on 18/Dez/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.ScientificArea;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentScientificArea extends IPersistentObject {
	public ScientificArea readByName(String name) throws ExcepcaoPersistencia;
}