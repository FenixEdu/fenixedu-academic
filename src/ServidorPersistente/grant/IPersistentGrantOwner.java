package ServidorPersistente.grant;
/**
 *
 * @author  Barbosa
 * @author  Pica
 */
import Dominio.grant.owner.IGrantOwner;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public interface IPersistentGrantOwner extends IPersistentObject{
	
	public IGrantOwner readGrantOwnerByNumber(Integer grantOwnerNumber) throws ExcepcaoPersistencia;
	public IGrantOwner readGrantOwnerByPerson(Integer personIdInternal) throws ExcepcaoPersistencia;
	public Integer readMaxGrantOwnerNumber() throws ExcepcaoPersistencia;
}