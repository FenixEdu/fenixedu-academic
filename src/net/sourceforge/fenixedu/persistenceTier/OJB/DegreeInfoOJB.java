package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeInfo;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Tânia Pousão Created on 30/Out/2003
 */
public class DegreeInfoOJB extends PersistentObjectOJB implements IPersistentDegreeInfo {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentDegreeInfo#readDegreeInfoByDegree(Dominio.IDegree)
     */
    public List readDegreeInfoByDegree(IDegree degree) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeKey", degree.getIdInternal());

        return queryList(DegreeInfo.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentDegreeInfo#readDegreeInfoByDegreeAndExecutionYear(Dominio.IDegree,
     *      Dominio.IExecutionYear)
     */
    public List readDegreeInfoByDegreeAndExecutionYear(IDegree degree, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeKey", degree.getIdInternal());
        criteria.addBetween("lastModificationDate", executionYear.getBeginDate(), executionYear
                .getEndDate());

        return queryList(DegreeInfo.class, criteria);
    }
}