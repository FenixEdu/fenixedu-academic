package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

public interface IPersistentDegreeCurricularPlan extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public List readByDegreeAndState(Integer degreeId, DegreeCurricularPlanState state, CurricularStage curricularStage)
            throws ExcepcaoPersistencia;
    
    public IDegreeCurricularPlan readByNameAndDegree(String name, Integer degreeId, CurricularStage curricularStage)
        throws ExcepcaoPersistencia;
    
    public List readByDegreeTypeAndState(DegreeType degreeType, DegreeCurricularPlanState state)
        throws ExcepcaoPersistencia;
}