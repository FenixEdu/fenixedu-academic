/*
 * Created on 15/Nov/2003
 *
 */
package ServidorPersistente.OJB.teacher;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ITeacher;
import Dominio.teacher.ExternalActivity;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.teacher.IPersistentExternalActivity;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ExternalActivityOJB extends PersistentObjectOJB implements IPersistentExternalActivity {

    /**
     *  
     */
    public ExternalActivityOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentExternalActivity#readAllByTeacher(Dominio.ITeacher)
     */
    public List readAllByTeacher(ITeacher teacher) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        List externalActivities = queryList(ExternalActivity.class, criteria);

        return externalActivities;
    }

}