/*
 * Created on 6/Jan/2004
 *
 */
package ServidorPersistente;

import Dominio.IGratuitySituation;
import Dominio.IStudentCurricularPlan;

/**
 * @author Tânia Pousão
 *
 */
public interface IPersistentGratuitySituation extends IPersistentObject
{
	public IGratuitySituation readGratuitySituatuionByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;
}
