package ServidorPersistente.grant;
/**
 *
 * @author  Barbosa
 * @author  Pica
 */
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantOrientationTeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public interface IPersistentGrantOrientationTeacher extends IPersistentObject
{
    public IGrantOrientationTeacher readActualGrantOrientationTeacherByContract(IGrantContract contract,Integer idInternal)
        throws ExcepcaoPersistencia;
}