/*
 * Created on 12/Mai/2003
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupProperties;

/**
 * @author asnr and scpo
 * @author joaosa and rmalo 28/10/2004
 *  
 */
public class GroupPropertiesOJB extends ObjectFenixOJB implements IPersistentGroupProperties
{


    public List readAll() throws ExcepcaoPersistencia
    {

        return queryList(GroupProperties.class, new Criteria());
    }

    
    public List readGroupPropertiesByName (String name) throws ExcepcaoPersistencia
    {	Criteria criteria = new Criteria();

    	criteria.addEqualTo("name", name);

        return queryList(GroupProperties.class,criteria);
    }
    
    
    
    public void delete(IGroupProperties groupProperties) throws ExcepcaoPersistencia
    {
        try
        {
            super.delete(groupProperties);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

}
