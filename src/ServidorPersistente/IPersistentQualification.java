package ServidorPersistente;

import java.util.List;

import Dominio.IPessoa;

/**
 * @author João Simas
 * @author Nuno Barbosa
 */
public interface IPersistentQualification extends IPersistentObject
{
	public List readQualificationsByPerson(IPessoa person) throws ExcepcaoPersistencia;
}
