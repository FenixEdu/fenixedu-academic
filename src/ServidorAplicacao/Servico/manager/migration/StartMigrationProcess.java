package ServidorAplicacao.Servico.manager.migration;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import middleware.studentMigration.enrollments.CreateAndUpdateAllStudentsPastEnrolments;
import middleware.studentMigration.enrollments.UpdateStudentEnrolments;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.ExcepcaoAutenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author David Santos
 * Jan 26, 2004
 */
public class StartMigrationProcess implements IService
{
	public StartMigrationProcess(){}

	public void run(String password, String method, String flag) throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPessoa person = persistentSuport.getIPessoaPersistente().lerPessoaPorUsername("L45438");
			persistentSuport.confirmarTransaccao();

			if (person != null && person.getPassword().equals(password))
			{
				Properties properties = new Properties();
				properties.load(StartMigrationProcess.class.getResourceAsStream("/migrationLog.properties"));
				String fileNameTemp1 = properties.getProperty("update.past.enrollments.log.file.name");
				String fileNameTemp2 = properties.getProperty("update.current.enrollments.log.file.name");
//				String fileNameTemp3 = properties.getProperty("give.all.equivalences.log.file.name");
//				String fileNameTemp4 = properties.getProperty("give.leec.equivalences.log.file.name");
				
				Date date = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(date.getTime());
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				int month = calendar.get(Calendar.MONTH);
				int year = calendar.get(Calendar.YEAR);
				String action = null;
				if (Boolean.valueOf(flag).booleanValue())
				{
					action = "recordsToAdd";
				} else
				{
					action = "recordsToRemove";
				}

				String fileName1 = fileNameTemp1 + "_" + day + "-" + month + "-" + year + "_" + action + ".txt";
				String fileName2 = fileNameTemp2 + "_" + day + "-" + month + "-" + year + "_" + action + ".txt";
//				String fileName3 = fileNameTemp3 + "_" + day + "-" + month + "-" + year + "_" + action + ".txt";
//				String fileName4 = fileNameTemp4 + "_" + day + "-" + month + "-" + year + "_" + action + ".txt";
				
				if (method.equals("pastCurriculum"))
				{
					String args[] = { flag, "true", fileName1 };
					CreateAndUpdateAllStudentsPastEnrolments.main(args);
				} else if (method.equals("thisSemester"))
				{
					String args[] = { flag, "true", fileName2 };
					UpdateStudentEnrolments.main(args);
				}
			} else
			{
				throw new ExcepcaoAutenticacao("Autenticacao incorrecta");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
	}
}