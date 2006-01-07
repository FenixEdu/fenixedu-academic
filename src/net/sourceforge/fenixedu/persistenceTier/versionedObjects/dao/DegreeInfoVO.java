package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
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
     * @see ServidorPersistente.IPersistentDegreeInfo#readDegreeInfoByDegreeAndExecutionYear(Dominio.Degree,
     *      Dominio.ExecutionYear)
     */
    public List readDegreeInfoByDegreeAndExecutionYear(Integer degreeId, final Date beginDate, final Date endDate)
            throws ExcepcaoPersistencia {
        
        final Degree deg = (Degree)readByOID(Degree.class,degreeId);
        List degreeInfos = deg.getDegreeInfos();
        
        degreeInfos = (List)CollectionUtils.select(degreeInfos,new Predicate ()
                {
                    public boolean evaluate(Object o) {
                        DegreeInfo degreeInfo = (DegreeInfo)o;
                        
                        return (beginDate.before(degreeInfo.getLastModificationDate()) &&
                                endDate.after(degreeInfo.getLastModificationDate()));
                    }});

        return degreeInfos;
    }
    public List readDegreeByType(final DegreeType degreeType)
    	throws ExcepcaoPersistencia {
    	  final List degrees = (List) readAll(Degree.class);
    	  List degreeInfos = (List)CollectionUtils.select(degrees,new Predicate ()
                  {
                      public boolean evaluate(Object o) {
                          Degree degree = (Degree)o;
                          
                          return (degree.getTipoCurso().equals(degreeType.getName()));
                      }
                  }
    	  );

          return degreeInfos;
    }
}
