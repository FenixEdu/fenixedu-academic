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

public interface IPersistentGrantResponsibleTeacher extends IPersistentObject
{
    public List readActualGrantResponsibleTeacherByContract(IGrantContract contract,Integer idInternal)
        throws ExcepcaoPersistencia;
}