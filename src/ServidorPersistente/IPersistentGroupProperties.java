/*
 * Created on 12/Mai/2003
 *
 */
package ServidorPersistente;
import java.util.List;

import Dominio.IGroupProperties;

/**
 * @author asnr and scpo
 *
 */
public interface IPersistentGroupProperties extends IPersistentObject{
    public List readAll() throws ExcepcaoPersistencia;

    public List readGroupPropertiesByName (String name) throws ExcepcaoPersistencia;

    public void delete(IGroupProperties groupProperties) throws ExcepcaoPersistencia;

}
