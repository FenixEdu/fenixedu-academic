package ServidorPersistente.grant;

import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Pica
 * @author Barbosa
 */
public interface IPersistentGrantSubsidy extends IPersistentObject
{
	public List readAllSubsidiesByGrantContract(Integer idContract) throws ExcepcaoPersistencia;
}
