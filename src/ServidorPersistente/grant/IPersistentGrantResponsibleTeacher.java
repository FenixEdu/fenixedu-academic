package ServidorPersistente.grant;
/**
 *
 * @author  Barbosa
 * @author  Pica
 */
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantResponsibleTeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public interface IPersistentGrantResponsibleTeacher extends IPersistentObject
{
    public IGrantResponsibleTeacher readActualGrantResponsibleTeacherByContract(IGrantContract contract,Integer idInternal)
        throws ExcepcaoPersistencia;
}