package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class DegreeCurricularPlanVO extends VersionedObjectsBase implements
        IPersistentDegreeCurricularPlan
{

    public List readAll() throws ExcepcaoPersistencia
    {
        return (List)readAll(DegreeCurricularPlan.class);
    }

    public List readByDegreeAndState(Integer degreeId, final DegreeCurricularPlanState state) throws ExcepcaoPersistencia
    {
        IDegree degree = (IDegree)readByOID(Degree.class,degreeId);
        return (List)CollectionUtils.select(degree.getDegreeCurricularPlans(),new Predicate () {
                public boolean evaluate(Object o) {
                    return ((IDegreeCurricularPlan)o).getState().equals(state);
                }
            });
    }

    public IDegreeCurricularPlan readByNameAndDegree(final String name, Integer degreeId)
        throws ExcepcaoPersistencia {

        IDegree degree = (IDegree)readByOID(Degree.class,degreeId);
        List result = (List)CollectionUtils.select(degree.getDegreeCurricularPlans(),new Predicate () {
                public boolean evaluate(Object o) {
                    return ((IDegreeCurricularPlan)o).getName().equals(name);
                }
            });
        
        return (IDegreeCurricularPlan) result.get(0);
    }

    public List readByDegreeTypeAndState(final DegreeType degreeType, final DegreeCurricularPlanState state) throws ExcepcaoPersistencia
    {
        List degreeCurricularPlans = (List)readAll(DegreeCurricularPlan.class);
        return (List)CollectionUtils.select(degreeCurricularPlans,new Predicate () {
                public boolean evaluate(Object o) {
                    IDegreeCurricularPlan dcp = (IDegreeCurricularPlan)o; 
                    return (dcp.getState().equals(state)) &&
                            (dcp.getDegree().getTipoCurso().equals(degreeType));
                }
            });
    }
}
