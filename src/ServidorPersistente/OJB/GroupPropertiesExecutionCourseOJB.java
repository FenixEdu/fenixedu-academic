/*
 * Created on 17/Ago/2004
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.GroupPropertiesExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import Dominio.IGroupPropertiesExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupPropertiesExecutionCourse;

/**
 * @author joaosa & rmalo
 */

public class GroupPropertiesExecutionCourseOJB extends ObjectFenixOJB 
				implements IPersistentGroupPropertiesExecutionCourse
				{


    public IGroupPropertiesExecutionCourse readBy(IGroupProperties groupProperties, IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyGroupProperties", groupProperties.getIdInternal());
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());

        return (IGroupPropertiesExecutionCourse) queryObject(GroupPropertiesExecutionCourse.class, criteria);

    }
    
    public IGroupPropertiesExecutionCourse readByIDs(Integer groupPropertiesID,Integer executionCourseID)
    throws ExcepcaoPersistencia
{

    Criteria criteria = new Criteria();

    criteria.addEqualTo("keyGroupProperties", groupPropertiesID);
    criteria.addEqualTo("keyExecutionCourse", executionCourseID);

    return (IGroupPropertiesExecutionCourse) queryObject(GroupPropertiesExecutionCourse.class, criteria);

}
    
    

    public List readAllByGroupProperties(IGroupProperties groupProperties) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyGroupProperties", groupProperties.getIdInternal());

        return queryList(GroupPropertiesExecutionCourse.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia
    {
        return queryList(GroupPropertiesExecutionCourse.class, new Criteria());

    }

    
    public void delete(IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) throws ExcepcaoPersistencia
    {
        try
        {
            super.delete(groupPropertiesExecutionCourse);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

   
    public List readAllByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());

        return queryList(GroupPropertiesExecutionCourse.class, criteria);
    }

    
    
    public List readByGroupPropertiesId(Integer id) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyGroupProperties", id);
        
        return queryList(GroupPropertiesExecutionCourse.class, criteria);
    }


    public List readByExecutionCourseId(Integer id) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyExecutionCourse", id);

        return queryList(GroupPropertiesExecutionCourse.class, criteria);
    }
    
}
