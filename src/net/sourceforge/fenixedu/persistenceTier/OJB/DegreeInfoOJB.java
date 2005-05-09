package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeInfo;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author T�nia Pous�o Created on 30/Out/2003
 */
public class DegreeInfoOJB extends PersistentObjectOJB implements IPersistentDegreeInfo {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentDegreeInfo#readDegreeInfoByDegreeAndExecutionYear(Dominio.IDegree,
     *      Dominio.IExecutionYear)
     */
    public List readDegreeInfoByDegreeAndExecutionYear(Integer degreeId, Date beginDate, Date endDate)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeKey", degreeId);
        criteria.addBetween("lastModificationDate", beginDate, endDate);

        return queryList(DegreeInfo.class, criteria);
    }
}