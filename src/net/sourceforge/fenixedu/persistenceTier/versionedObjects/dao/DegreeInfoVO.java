package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class DegreeInfoVO extends VersionedObjectsBase implements IPersistentDegreeInfo
{
     /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentDegreeInfo#readDegreeInfoByDegreeAndExecutionYear(Dominio.IDegree,
     *      Dominio.IExecutionYear)
     */
    public List readDegreeInfoByDegreeAndExecutionYear(Integer degreeId, final Date beginDate, final Date endDate)
            throws ExcepcaoPersistencia {
        
        final IDegree deg = (IDegree)readByOID(Degree.class,degreeId);
        List degreeInfos = deg.getDegreeInfos();
        
        degreeInfos = (List)CollectionUtils.select(degreeInfos,new Predicate ()
                {
                    public boolean evaluate(Object o) {
                        IDegreeInfo degreeInfo = (IDegreeInfo)o;
                        
                        return (beginDate.before(degreeInfo.getLastModificationDate()) &&
                                endDate.after(degreeInfo.getLastModificationDate()));
                    }});

        return degreeInfos;
    }
}
