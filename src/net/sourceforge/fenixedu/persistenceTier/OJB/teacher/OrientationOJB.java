/*
 * Created on 21/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.teacher;

import net.sourceforge.fenixedu.domain.teacher.IOrientation;
import net.sourceforge.fenixedu.domain.teacher.Orientation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.util.OrientationType;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class OrientationOJB extends PersistentObjectOJB implements IPersistentOrientation {

    /**
     *  
     */
    public OrientationOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentOrientation#readByTeacher(Dominio.ITeacher)
     */
    public IOrientation readByTeacherIdAndOrientationType(Integer teacherId, OrientationType orientationType)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacherId);
        criteria.addEqualTo("orientationType", new Integer(orientationType.getValue()));
        return (IOrientation) queryObject(Orientation.class, criteria);
    }

}