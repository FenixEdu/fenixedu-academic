/*
 * Created on Oct 14, 2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IExternalPerson;

/**
 * @author :
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public interface IPersistentExternalPerson extends IPersistentObject {
	public IExternalPerson readByUsername(String username) throws ExcepcaoPersistencia;
	public List readByName(String name) throws ExcepcaoPersistencia;
}