/*
 * Created on 17/Ago/2004
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;

/**
 * @author joaosa & rmalo
 */
public interface IPersistentAttendInAttendsSet extends IPersistentObject{

	public List readAll() throws ExcepcaoPersistencia;

	public void delete(IAttendInAttendsSet attendInAttendsSet) 
		throws ExcepcaoPersistencia;
		
}
