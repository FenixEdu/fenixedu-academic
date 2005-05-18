/*
 * Created on 6/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.gratuity.GratuitySituationType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;

/**
 * @author Tânia Pousão
 *  
 */
public interface IPersistentGratuitySituation extends IPersistentObject {

    public IGratuitySituation readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
            IStudentCurricularPlan studentCurricularPlan, IGratuityValues gratuityValues)
            throws ExcepcaoPersistencia;

    public List readGratuitySituatuionListByStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;

    public List readGratuitySituationsByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
            throws ExcepcaoPersistencia;

    public List readGratuitySituationListByExecutionDegreeAndSpecialization(
            IExecutionDegree executionDegree, Specialization specialization) throws ExcepcaoPersistencia;

    public List readGratuitySituationListByExecutionDegreeAndSpecializationAndSituation(
            IExecutionDegree executionDegree, Specialization specialization,
            GratuitySituationType situation) throws ExcepcaoPersistencia;

    public IGratuitySituation readGratuitySituationByExecutionDegreeAndStudent(
            IExecutionDegree executionDegree, IStudent student) throws ExcepcaoPersistencia;

    public IGratuitySituation readByStudentCurricularPlanAndGratuityValuesAndGratuitySituationType(
            IStudentCurricularPlan studentCurricularPlan, IGratuityValues gratuityValues,
            GratuitySituationType gratuitySituationType) throws ExcepcaoPersistencia;
}