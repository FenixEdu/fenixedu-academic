/*
 * Created on 15/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;

import org.apache.ojb.broker.query.Criteria;

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