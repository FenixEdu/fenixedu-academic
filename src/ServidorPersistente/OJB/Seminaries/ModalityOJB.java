/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.OJB.Seminaries;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Seminaries.IModality;
import Dominio.Seminaries.Modality;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.Seminaries.*;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 * 
 */
public class ModalityOJB extends ObjectFenixOJB implements IPersistentSeminaryModality
{
    public IModality readByName(String name) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        return (IModality) super.queryObject(Modality.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return super.queryList(Modality.class, criteria);
    }

    public void delete(IModality modality) throws ExcepcaoPersistencia
    {
        super.deleteByOID(Modality.class, modality.getIdInternal());
    }
}
