package ServidorPersistente.grant;
/**
 *
 * @author  Barbosa
 * @author  Pica
 */
import java.util.List;

import Dominio.grant.contract.IGrantContract;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public interface IPersistentGrantOrientationTeacher extends IPersistentObject
{
    public List readActualGrantOrientationTeacherByContract(IGrantContract contract,Integer idInternal)
        throws ExcepcaoPersistencia;
}