package ServidorPersistente.grant;
/**
 *
 * @author  Barbosa
 * @author  Pica
 */
import java.util.List;

import Dominio.grant.contract.IGrantType;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public interface IPersistentGrantType extends IPersistentObject
{
    public IGrantType readGrantTypeBySigla(String sigla) throws ExcepcaoPersistencia;
    public List readAll() throws ExcepcaoPersistencia;

}