package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

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
}
