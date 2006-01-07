package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.University;

/**
 * @author David Santos 28/Out/2003
 */

public interface IPersistentUniversity extends IPersistentObject {

	public University readByNameAndCode(String name, String code) throws ExcepcaoPersistencia;
}