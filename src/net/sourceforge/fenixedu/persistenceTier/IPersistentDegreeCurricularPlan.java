package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

public interface IPersistentDegreeCurricularPlan extends IPersistentObject {

    public List<DegreeCurricularPlan> readByCurricularStage(CurricularStage curricularStage) throws ExcepcaoPersistencia;

    public List<DegreeCurricularPlan> readFromNewDegreeStructure() throws ExcepcaoPersistencia;
    
    public List readByDegreeAndState(Integer degreeId, DegreeCurricularPlanState state, CurricularStage curricularStage)
            throws ExcepcaoPersistencia;
    
    public DegreeCurricularPlan readByNameAndDegree(String name, Integer degreeId, CurricularStage curricularStage)
        throws ExcepcaoPersistencia;
    
    public List readByDegreeTypeAndState(DegreeType degreeType, DegreeCurricularPlanState state)
        throws ExcepcaoPersistencia;
}