package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class DegreeCurricularPlanVO extends VersionedObjectsBase implements
        IPersistentDegreeCurricularPlan {

    public List readByCurricularStage(CurricularStage curricularStage) throws ExcepcaoPersistencia {
        List<IDegreeCurricularPlan> result = new ArrayList<IDegreeCurricularPlan>();
        for (IDegreeCurricularPlan dcp : (List<IDegreeCurricularPlan>) readAll(DegreeCurricularPlan.class)) {
            if (dcp.getCurricularStage().equals(curricularStage)) {
                result.add(dcp);
            }
        }
        return result;
    }

    public List readFromNewDegreeStructure() throws ExcepcaoPersistencia {
        List<IDegreeCurricularPlan> result = new ArrayList<IDegreeCurricularPlan>();
        for (IDegreeCurricularPlan dcp : (List<IDegreeCurricularPlan>) readAll(DegreeCurricularPlan.class)) {
            if (!dcp.getCurricularStage().equals(CurricularStage.OLD)) {
                result.add(dcp);
            }
        }
        return result;
    }
    
    public List readByDegreeAndState(Integer degreeId, final DegreeCurricularPlanState state, final CurricularStage curricularStage)
            throws ExcepcaoPersistencia {
        IDegree degree = (IDegree) readByOID(Degree.class, degreeId);
        return (List) CollectionUtils.select(degree.getDegreeCurricularPlans(), new Predicate() {
            public boolean evaluate(Object o) {
                IDegreeCurricularPlan dcp = (IDegreeCurricularPlan) o;
                return dcp.getState().equals(state) && dcp.getCurricularStage().equals(curricularStage);
            }
        });
    }

    public IDegreeCurricularPlan readByNameAndDegree(final String name, Integer degreeId, final CurricularStage curricularStage)
        throws ExcepcaoPersistencia {

        IDegree degree = (IDegree)readByOID(Degree.class,degreeId);
        List result = (List)CollectionUtils.select(degree.getDegreeCurricularPlans(),new Predicate () {
                public boolean evaluate(Object o) {
                    IDegreeCurricularPlan dcp = (IDegreeCurricularPlan) o;
                    return dcp.getName().equals(name) && dcp.getCurricularStage().equals(curricularStage);
                }
            });
        
        return (IDegreeCurricularPlan) result.get(0);
    }
    
    public List readByDegreeTypeAndState(final DegreeType degreeType, final DegreeCurricularPlanState state) throws ExcepcaoPersistencia
    {
        List degreeCurricularPlans = (List) readByCurricularStage(CurricularStage.OLD);
        return (List)CollectionUtils.select(degreeCurricularPlans,new Predicate () {
                public boolean evaluate(Object o) {
                    IDegreeCurricularPlan dcp = (IDegreeCurricularPlan)o; 
                    return (dcp.getState().equals(state)) &&
                            (dcp.getDegree().getTipoCurso().equals(degreeType));
                }
            });
    }

}
