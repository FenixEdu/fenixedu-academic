package ServidorPersistente.grant;
/**
 *
 * @author  Barbosa
 * @author  Pica
 */
import DataBeans.InfoPerson;
import Dominio.grant.owner.IGrantOwner;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public interface IPersistentGrantOwner extends IPersistentObject{
	
	public IGrantOwner readGrantOwnerByNumber(Integer grantOwnerNumber) throws ExcepcaoPersistencia;
	public IGrantOwner readGrantOwnerByPerson(InfoPerson infoPerson) throws ExcepcaoPersistencia;
	public Integer readMaxGrantOwnerNumber() throws ExcepcaoPersistencia;
}