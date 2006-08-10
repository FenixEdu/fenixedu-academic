package net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys;

import net.sourceforge.fenixedu.dataTransferObject.InfoFinalResult;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.EvaluationType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public interface IDegreeCurricularPlanStrategy {

    /**
     * Gets the Degree Curricular Plan
     * 
     * @return the DegreeCurricular Plan
     */
    public DegreeCurricularPlan getDegreeCurricularPlan();

    /**
     * Checks if the mark is Valid for this Degree Curricular Plan
     * 
     * @param The
     *            String with the mark to test
     */
    public boolean checkMark(String mark);

    public boolean checkMark(String mark, EvaluationType et);

    /**
     * Calculate's the Registration's regular average
     * 
     * @param The
     *            student's Curricular Plan
     * @param The
     *            list of the students enrolment
     * @return The Registration's Average
     */
    public Double calculateStudentRegularAverage(StudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia;

    /**
     * Calculate's the Registration's weighted average
     * 
     * @param The
     *            student's Curricular Plan
     * @param The
     *            list of the students enrolment
     * @return The Registration's Average
     */
    public Double calculateStudentWeightedAverage(StudentCurricularPlan studentCurricularPlan)
            throws ExcepcaoPersistencia;

    /**
     * 
     * @param studentCurricularPlan
     * @param infoFinalResult
     * @throws ExcepcaoPersistencia
     */
    public void calculateStudentAverage(StudentCurricularPlan studentCurricularPlan,
            InfoFinalResult infoFinalResult) throws ExcepcaoPersistencia;

}