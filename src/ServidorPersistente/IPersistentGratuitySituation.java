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
import Dominio.IStudentCurricularPlan;

/**
 * @author Tânia Pousão
 *
 */
public interface IPersistentGratuitySituation extends IPersistentObject
{
	public IGratuitySituation readGratuitySituatuionByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;
	public List readGratuitySituationsByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;
	public List readGratuitySituationListByExecutionDegreeAndSpecialization(ICursoExecucao executionDegree, Specialization specialization) throws ExcepcaoPersistencia;
	public List readGratuitySituationListByExecutionDegreeAndSpecializationAndSituation(ICursoExecucao executionDegree, Specialization specialization,GratuitySituationType situation) throws ExcepcaoPersistencia;
}
