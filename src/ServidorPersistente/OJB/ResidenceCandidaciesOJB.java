/*
 * Created on Aug 4, 2004
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IStudent;
import Dominio.student.ResidenceCandidacies;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentResidenceCandidacies;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ResidenceCandidaciesOJB extends PersistentObjectOJB implements
        IPersistentResidenceCandidacies {

    public ResidenceCandidaciesOJB() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentResidenceCandidacies#readResidenceCandidaciesByStudent()
     */
    public List readResidenceCandidaciesByStudent(IStudent student) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", student.getIdInternal());
        return (List) queryList(ResidenceCandidacies.class, criteria);
    }

}