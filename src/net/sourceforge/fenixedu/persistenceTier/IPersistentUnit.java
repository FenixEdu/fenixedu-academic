/*
 * Created on Sep 19, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IUnit;

public interface IPersistentUnit extends IPersistentObject  {

    public IUnit readByName(String unitName) throws ExcepcaoPersistencia;
}
