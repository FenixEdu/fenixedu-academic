/*
 * Created on 2004/04/14
 *
 */
package ServidorAplicacao.Servico.student;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoStudentCurricularPlan;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;
/**
 * @author Luis Cruz
 *
 */
public class ReadActiveStudentCurricularPlanByDegreeType implements IService
{

	public ReadActiveStudentCurricularPlanByDegreeType()
	{
	    super();
	}

	public InfoStudentCurricularPlan run(IUserView userView, TipoCurso degreeType)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
		IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();

		IStudent student = persistentStudent.readByUsername(userView.getUtilizador());
		IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan.readActiveByStudentNumberAndDegreeType(student.getNumber(), degreeType);

		InfoStudentCurricularPlan infoStudentCurricularPlan = null;
		if (studentCurricularPlan != null)
		{
			infoStudentCurricularPlan = new InfoStudentCurricularPlan();
			if (studentCurricularPlan.getDegreeCurricularPlan() != null)
			{
				infoStudentCurricularPlan.setInfoDegreeCurricularPlan(new InfoDegreeCurricularPlan());
				infoStudentCurricularPlan.getInfoDegreeCurricularPlan().setIdInternal(studentCurricularPlan.getDegreeCurricularPlan().getIdInternal());
			}
		}

		return infoStudentCurricularPlan;
	}

}