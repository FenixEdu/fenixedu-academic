package net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys;

import java.util.Date;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public interface IMasterDegreeCurricularPlanStrategy extends IDegreeCurricularPlanStrategy {

    /**
     * Checks if the Master Degree Student has finished his scholar part. <br/>
     * All his credits are added and compared to the ones required by his Degree
     * Curricular Plan.
     * 
     * @param The
     *            Student's Curricular Plan
     * @return A boolean indicating if he has finished it or not.
     */
    public boolean checkEndOfScholarship(StudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia;

    /**
     * 
     * @param studentCurricularPlan
     * @return The Date of the student's end of his scholar part.
     * @throws ExcepcaoPersistencia
     */
    public Date dateOfEndOfScholarship(StudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia;

}