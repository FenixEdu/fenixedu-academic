/*
 * Created on 12/Mai/2003
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.GroupProperties;
import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupProperties;

/**
 * @author asnr and scpo
 *  
 */
public class GroupPropertiesOJB extends PersistentObjectOJB implements IPersistentGroupProperties {

    public List readAllGroupPropertiesByExecutionCourse(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        List temp = queryList(GroupProperties.class, criteria);
        return temp;
    }

    public List readAllGroupPropertiesByExecutionCourseID(Integer id) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", id);
        return queryList(GroupProperties.class, criteria);
    }

    public IGroupProperties readGroupPropertiesByExecutionCourseAndName(
            IExecutionCourse executionCourse, String name) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        criteria.addEqualTo("name", name);
        return (IGroupProperties) queryObject(GroupProperties.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {

        return queryList(GroupProperties.class, new Criteria());
    }

    public void delete(IGroupProperties groupProperties) throws ExcepcaoPersistencia {
        try {
            super.delete(groupProperties);
        } catch (ExcepcaoPersistencia ex) {
            throw ex;
        }
    }

}