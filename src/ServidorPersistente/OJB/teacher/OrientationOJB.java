/*
 * Created on 21/Nov/2003
 *  
 */
package ServidorPersistente.OJB.teacher;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ITeacher;
import Dominio.teacher.IOrientation;
import Dominio.teacher.Orientation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.teacher.IPersistentOrientation;
import Util.OrientationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class OrientationOJB extends ObjectFenixOJB implements IPersistentOrientation
{

    /**
	 *  
	 */
    public OrientationOJB()
    {
        super();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.IPersistentOrientation#readByTeacher(Dominio.ITeacher)
	 */
    public IOrientation readByTeacherAndOrientationType(ITeacher teacher, OrientationType orientationType) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        criteria.addEqualTo("orientationType", new Integer(orientationType.getValue()));
        return (IOrientation) queryObject(Orientation.class, criteria);
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.teacher.IPersistentOrientation#readAllByTeacher(Util.OrientationType)
     */
    public List readAllByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        return queryList(Orientation.class, criteria);
    }

}
