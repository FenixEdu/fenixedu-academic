/*
 * Created on 12/Mai/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;

import org.apache.ojb.broker.query.Criteria;

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
