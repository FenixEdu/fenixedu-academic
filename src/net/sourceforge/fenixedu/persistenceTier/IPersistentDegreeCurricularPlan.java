package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;

public interface IPersistentDegreeCurricularPlan extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public List readByDegreeAndState(Integer degreeId, DegreeCurricularPlanState state)
            throws ExcepcaoPersistencia;
}