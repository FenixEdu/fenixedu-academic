package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IGratuity;
import net.sourceforge.fenixedu.util.State;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentGratuity extends IPersistentObject {

    /**
     * 
     * 
     * /**
     * 
     * @param studentCurricularPlanID
     * @return The History of the Grauity for This Student CurricularPlan
     * @throws ExcepcaoPersistencia
     */
    public List readByStudentCurricularPlanID(Integer studentCurricularPlanID)
            throws ExcepcaoPersistencia;

    /**
     * 
     * @param studentCurricularPlanID
     * @param state
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public IGratuity readByStudentCurricularPlanIDAndState(Integer studentCurricularPlanID, State state)
            throws ExcepcaoPersistencia;

}