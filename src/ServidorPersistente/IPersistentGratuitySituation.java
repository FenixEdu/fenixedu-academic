/*
 * Created on 6/Jan/2004
 *
 */
package ServidorPersistente;

import java.util.List;

import Util.GratuitySituationType;
import Util.Specialization;

import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IGratuitySituation;
import Dominio.IGratuityValues;
import Dominio.IStudentCurricularPlan;

/**
 * @author Tânia Pousão
 *
 */
public interface IPersistentGratuitySituation extends IPersistentObject
{
	public IGratuitySituation readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(IStudentCurricularPlan studentCurricularPlan, IGratuityValues gratuityValues) throws ExcepcaoPersistencia;
	public List readGratuitySituationsByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;
	public List readGratuitySituationListByExecutionDegreeAndSpecialization(ICursoExecucao executionDegree, Specialization specialization) throws ExcepcaoPersistencia;
	public List readGratuitySituationListByExecutionDegreeAndSpecializationAndSituation(ICursoExecucao executionDegree, Specialization specialization,GratuitySituationType situation) throws ExcepcaoPersistencia;
}
