/*
 * Created on 6/Jan/2004
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IGratuitySituation;
import Dominio.IGratuityValues;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Util.GratuitySituationType;
import Util.Specialization;

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
            ICursoExecucao executionDegree, Specialization specialization) throws ExcepcaoPersistencia;

    public List readGratuitySituationListByExecutionDegreeAndSpecializationAndSituation(
            ICursoExecucao executionDegree, Specialization specialization,
            GratuitySituationType situation) throws ExcepcaoPersistencia;

    public IGratuitySituation readGratuitySituationByExecutionDegreeAndStudent(
            ICursoExecucao executionDegree, IStudent student) throws ExcepcaoPersistencia;

    public IGratuitySituation readByStudentCurricularPlanAndGratuityValuesAndGratuitySituationType(
            IStudentCurricularPlan studentCurricularPlan, IGratuityValues gratuityValues,
            GratuitySituationType gratuitySituationType) throws ExcepcaoPersistencia;
}