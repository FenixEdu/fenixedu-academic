package middleware.studentMigration.enrollments;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import ServidorAplicacao.Servico.manager.migration.CreateUpdateEnrollmentsInCurrentStudentCurricularPlans;
import ServidorAplicacao.Servico.manager.migration.CreateUpdateEnrollmentsInPastStudentCurricularPlans;
import ServidorAplicacao.Servico.manager.migration.DeleteEnrollmentsInCurrentStudentCurricularPlans;
import ServidorAplicacao.Servico.manager.migration.DeleteEnrollmentsInPastStudentCurricularPlans;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author David Santos in Jan 29, 2004
 */

public class RunMigrationProcess
{
	public static void main(String args[])
	{
		ISuportePersistente persistentSuport = null;
		try
		{
			String curriculum = args[0];
			String action = args[1];
			
			persistentSuport = SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();

			if (curriculum.equals("past") && action.equals("write"))
			{
				IService service = new CreateUpdateEnrollmentsInPastStudentCurricularPlans();
				((CreateUpdateEnrollmentsInPastStudentCurricularPlans) service).run(Boolean.FALSE, null);
			} else if (curriculum.equals("current") && action.equals("write"))
			{
				IService service = new CreateUpdateEnrollmentsInCurrentStudentCurricularPlans();
				((CreateUpdateEnrollmentsInCurrentStudentCurricularPlans) service).run(Boolean.FALSE, null);
			} else if (curriculum.equals("past") && action.equals("delete"))
			{
				IService service = new DeleteEnrollmentsInPastStudentCurricularPlans();
				((CreateUpdateEnrollmentsInCurrentStudentCurricularPlans) service).run(Boolean.FALSE, null);
			} else if (curriculum.equals("current") && action.equals("delete"))
			{
				IService service = new DeleteEnrollmentsInCurrentStudentCurricularPlans();
				((CreateUpdateEnrollmentsInCurrentStudentCurricularPlans) service).run(Boolean.FALSE, null);
			}
			
			persistentSuport.confirmarTransaccao();
		} catch (Throwable e)
		{
			e.printStackTrace(System.out);
			try
			{
				persistentSuport.cancelarTransaccao();
			} catch (ExcepcaoPersistencia e1)
			{
				e1.printStackTrace(System.out);
			}
		}
	}
}