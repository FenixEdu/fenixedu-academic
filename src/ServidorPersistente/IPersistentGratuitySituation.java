/*
 * Created on 6/Jan/2004
 *
 */
package ServidorPersistente;

import java.util.List;

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
}
