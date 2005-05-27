package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IUniversity;

/**
 * @author David Santos 28/Out/2003
 */

public interface IPersistentUniversity extends IPersistentObject {

	public IUniversity readByNameAndCode(String name, String code) throws ExcepcaoPersistencia;
}