/*
 * Created on Jul 31, 2004
 */


package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;



/**
 * @author joaosa-rmalo
 */



public interface IPersistentAttendsSet extends IPersistentObject {
	
	public List readAll()  throws ExcepcaoPersistencia;
	public List readAttendsSetByName(String attendsSetName)  throws ExcepcaoPersistencia;
	
}
