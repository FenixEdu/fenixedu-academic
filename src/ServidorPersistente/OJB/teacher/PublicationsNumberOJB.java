/*
 * Created on 21/Nov/2003
 *  
 */
package ServidorPersistente.OJB.teacher;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import DataBeans.teacher.IPublicationsNumber;
import Dominio.ITeacher;
import Dominio.teacher.PublicationsNumber;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.teacher.IPersistentPublicationsNumber;
import Util.PublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class PublicationsNumberOJB extends ObjectFenixOJB implements IPersistentPublicationsNumber
{

    /**
	 *  
	 */
    public PublicationsNumberOJB()
    {
        super();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.IPersistentPublicationsNumber#readByTeacherAndPublicationType(Dominio.ITeacher,
	 *      Util.PublicationType)
	 */
    public IPublicationsNumber readByTeacherAndPublicationType(
        ITeacher teacher,
        PublicationType publicationType)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        criteria.addEqualTo("publicationType", new Integer(publicationType.getValue()));
        return (IPublicationsNumber) queryObject(PublicationsNumber.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.IPersistentPublicationsNumber#readAllByTeacher(Dominio.ITeacher)
	 */
    public List readAllByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        return queryList(PublicationsNumber.class, criteria);
    }

}
