/*
 * Created on 16/Nov/2003
 *
 */
package ServidorPersistente.OJB.teacher;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ITeacher;
import Dominio.teacher.IServiceProviderRegime;
import Dominio.teacher.ServiceProviderRegime;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.teacher.IPersistentServiceProviderRegime;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ServiceProviderRegimeOJB extends PersistentObjectOJB implements
        IPersistentServiceProviderRegime {

    /**
     *  
     */
    public ServiceProviderRegimeOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentServiceProviderRegime#readByTeacher(Dominio.ITeacher)
     */
    public IServiceProviderRegime readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        return (IServiceProviderRegime) queryObject(ServiceProviderRegime.class, criteria);
    }

}